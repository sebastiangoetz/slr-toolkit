package de.slrtoolkit.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.amrdeveloper.treeview.TreeNode;

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
import de.slrtoolkit.util.TaxonomyTreeNode;

public class TaxonomyRepository {
    private final TaxonomyDao taxonomyDao;
    private final FileUtil fileUtil;
    public TaxonomyRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taxonomyDao = db.taxonomyDao();
        this.fileUtil = new FileUtil();
    }

    public void addToFile(String path, Application application, Taxonomy t, List<TreeNode> roots) {
            TaxonomyTreeNode ttt = new TaxonomyTreeNode(t.getTaxonomyId(), t.getName());
            TreeNode newTT = new TreeNode(ttt, 0);
            if(t.getParentId() == 0) {
                roots.add(newTT);
            } else {
                addChild(roots, newTT, t);
            }
            File file = fileUtil.accessFiles(path, application, ".taxonomy");
            try(FileWriter fw = new FileWriter(file, false)) {
                //we need the full taxonomy to insert and then pretty print
                StringBuilder sb = new StringBuilder();
                buildStringRepresentation(0, roots, sb);
                fw.write(sb.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    private static void buildStringRepresentation(int level, List<TreeNode> roots, StringBuilder sb) {
        for(TreeNode n : roots) {
            addIndent(level, sb);
            sb.append(n.getValue().toString());
            if(!n.getChildren().isEmpty()) {
                addIndent(level, sb);
                sb.append(" {\n");
                addIndent(level, sb);
                buildStringRepresentation(level + 1, n.getChildren(), sb);
                addIndent(level, sb);
                sb.append("\n}\n");
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

    private void addChild(List<TreeNode> parents, TreeNode newTT, Taxonomy t) {
        for(TreeNode parent : parents) {
            boolean done = false;
            if(((TaxonomyTreeNode)parent.getValue()).getId() == t.getParentId()) {
                parent.addChild(newTT);
                done = true;
            }
            if(!done) {
                addChild(parent.getChildren(), newTT, t);
            }
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
