package de.slrtoolkit.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.slrtoolkit.database.AppDatabase;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.database.TaxonomyDao;
import de.slrtoolkit.util.FileUtil;
import de.slrtoolkit.util.TaxonomyParser;
import de.slrtoolkit.util.TaxonomyParserNode;

public class TaxonomyRepository {
    private final TaxonomyDao taxonomyDao;
    private final FileUtil fileUtil;

    private static class TaxonomyEntryTreeNode {
        private final Taxonomy t;
        private final List<TaxonomyEntryTreeNode> children;
        public TaxonomyEntryTreeNode(Taxonomy t) {
            this.t = t;
            children = new ArrayList<>();
        }
        public TaxonomyEntryTreeNode addChild(Taxonomy t) {
            TaxonomyEntryTreeNode tetn = new TaxonomyEntryTreeNode(t);
            children.add(tetn);
            return tetn;
        }
        public Taxonomy getTaxonomy() {
            return t;
        }
        public List<TaxonomyEntryTreeNode> getChildren() {
            return children;
        }
    }
    public TaxonomyRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taxonomyDao = db.taxonomyDao();
        this.fileUtil = new FileUtil();
    }

    private List<TaxonomyEntryTreeNode> transformListToTree(List<Taxonomy> allTaxonomies) {
        List<TaxonomyEntryTreeNode> rootTaxonomies = new ArrayList<>();
        for(Taxonomy tax : allTaxonomies) {
            if(tax.getParentId() == 0) {
                TaxonomyEntryTreeNode tetn = new TaxonomyEntryTreeNode(tax);
                addChildrenToTree(tetn, allTaxonomies);
                rootTaxonomies.add(tetn);
            }
        }
        return rootTaxonomies;
    }

    private void addChildrenToTree(TaxonomyEntryTreeNode node, List<Taxonomy> allTaxonomies) {
        for(Taxonomy t : allTaxonomies) {
            if(t.getParentId() != 0 && t.getParentId() == node.getTaxonomy().getTaxonomyId()) {
                TaxonomyEntryTreeNode tetn = node.addChild(t);
                addChildrenToTree(tetn, allTaxonomies);
            }
        }
    }

    public void updateFile(String path, Application application, List<Taxonomy> allTaxonomies) {
        List<TaxonomyEntryTreeNode> rootTaxonomies = transformListToTree(allTaxonomies);
        File file = fileUtil.accessFiles(path, application, ".taxonomy");
        try(FileWriter fw = new FileWriter(file, false)) {
            StringBuilder sb = new StringBuilder();
            buildStringRepresentation(0, rootTaxonomies, sb);
            fw.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void buildStringRepresentation(int level, List<TaxonomyEntryTreeNode> roots, StringBuilder sb) {
        for(TaxonomyEntryTreeNode t : roots) {
            addIndent(level, sb);
            sb.append(t.getTaxonomy().getName());
            if(!t.getChildren().isEmpty()) {
                sb.append(" {\n");
                buildStringRepresentation(level + 1, t.getChildren(), sb);
                sb.append("\n");
                addIndent(level, sb);
                sb.append("}\n");
            } else {
                sb.append(",\n");
            }
        }
        if(sb.lastIndexOf(",") == sb.length()-2)
            sb.delete(sb.length() - 2, sb.length());
    }

    private static void addIndent(int level, StringBuilder sb) {
        for(int i = 0; i < level; i++) {
            sb.append("  ");
        }
    }

    public long insert(Taxonomy taxonomy) {
        Callable<Long> insertCallable = () -> taxonomyDao.insert(taxonomy);
        long id = 0;

        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(insertCallable);
        try {
            id = future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TaxonomyRepository.class.getName(), "insert: failed", e);
        }
        return id;
    }

    public void insertAll(List<Taxonomy> taxonomies) {
        AppDatabase.databaseWriteExecutor.execute(() -> taxonomyDao.insertAll(taxonomies));
    }

    public void initializeTaxonomy(int repoId, String path, Application application) {
        try {
            String taxonomyString = fileUtil.readContentFromFile(path, application);
            TaxonomyParser parser = new TaxonomyParser();
            List<TaxonomyParserNode> parserNodes = parser.parse(taxonomyString);
            addTaxonomyEntries(parserNodes, repoId, 0);
        } catch (FileNotFoundException e) {
            Log.e(TaxonomyRepository.class.getName(), "initializeTaxonomy: failed", e);
        }
    }

    private void addTaxonomyEntries(List<TaxonomyParserNode> nodes, int repoId, int parent) {
        for (TaxonomyParserNode node : nodes) {
            //if node is root node
            if (node.getParent() == null || parent != 0) {
                Taxonomy taxonomyNode = new Taxonomy();
                taxonomyNode.setName(node.getName());
                taxonomyNode.setRepoId(repoId);
                taxonomyNode.setPath(node.getPath());
                if (parent != 0) {
                    taxonomyNode.setParentId(parent);
                }
                if (!node.getChildren().isEmpty()) {
                    taxonomyNode.setHasChildren(true);
                    //insert current node
                    int parentId = (int) insert(taxonomyNode);
                    List<Taxonomy> nodesWithoutChildren = new ArrayList<>();
                    List<TaxonomyParserNode> nodesWithChildren = new ArrayList<>();
                    for (TaxonomyParserNode childNode : node.getChildren()) {
                        if (childNode.getChildren().isEmpty()) {
                            Taxonomy childTaxonomy = new Taxonomy();
                            childTaxonomy.setName(childNode.getName());
                            childTaxonomy.setPath(childNode.getPath());
                            childTaxonomy.setRepoId(repoId);
                            childTaxonomy.setParentId(parentId);
                            nodesWithoutChildren.add(childTaxonomy);
                        } else {
                            nodesWithChildren.add(childNode);
                        }
                    }
                    //insert child nodes without childnodes directly
                    insertAll(nodesWithoutChildren);
                    //recursively do the same thing for the child nodes
                    addTaxonomyEntries(nodesWithChildren, repoId, parentId);
                } else {
                    taxonomyNode.setHasChildren(false);
                    insert(taxonomyNode);
                }
            }
        }
    }

    public LiveData<List<Taxonomy>> getChildTaxonomies(int repoId, int parentId) {
        return taxonomyDao.getChildTaxonomies(repoId, parentId);
    }

    public LiveData<List<Taxonomy>> getAllTaxonomiesForRepo(int repoId) {
        return taxonomyDao.getAllTaxonomiesForRepo(repoId);
    }

    public Taxonomy getTaxonomyByRepoAndPathDirectly(int repoId, String path) throws ExecutionException, InterruptedException {
        Callable<Taxonomy> getCallable = () -> taxonomyDao.getTaxonomyByRepoAndPathDirectly(repoId, path);
        Future<Taxonomy> future = Executors.newSingleThreadExecutor().submit(getCallable);
        return future.get();
    }
}
