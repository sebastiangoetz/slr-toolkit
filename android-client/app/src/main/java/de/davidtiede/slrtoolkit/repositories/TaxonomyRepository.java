package de.davidtiede.slrtoolkit.repositories;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.davidtiede.slrtoolkit.database.AppDatabase;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyDao;
import de.davidtiede.slrtoolkit.util.FileUtil;
import de.davidtiede.slrtoolkit.util.TaxonomyParser;
import de.davidtiede.slrtoolkit.util.TaxonomyParserNode;

public class TaxonomyRepository {
    private TaxonomyDao taxonomyDao;
    private Application application;
    private FileUtil fileUtil;


    public TaxonomyRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taxonomyDao = db.taxonomyDao();
        this.application  = application;
        this.fileUtil = new FileUtil();
    }

    public long insert(Taxonomy taxonomy) {
        Callable<Long> insertCallable = () -> taxonomyDao.insert(taxonomy);
        long id = 0;

        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(insertCallable);
        try {
            id = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void insertAll(List<Taxonomy> taxonomies) {
        AppDatabase.databaseWriteExecutor.execute(() -> taxonomyDao.insertAll(taxonomies));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initializeTaxonomy(int repoId, String path, Application application) {
        try {
            String taxonomyString = fileUtil.readContentFromFile(path, application);
            TaxonomyParser parser = new TaxonomyParser();
            List<TaxonomyParserNode> parserNodes = parser.parse(taxonomyString);
            addTaxonomyEntries(parserNodes, repoId, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addTaxonomyEntries(List<TaxonomyParserNode> nodes, int repoId, int parent) {
        for(TaxonomyParserNode node: nodes) {
            //if node is root node
            if(node.getParent() == null || parent != 0) {
                Taxonomy taxonomyNode = new Taxonomy();
                taxonomyNode.setName(node.getName());
                taxonomyNode.setRepoId(repoId);
                if (parent != 0) {
                    taxonomyNode.setParentId(parent);
                }
                //insert current node
                int parentId = (int) insert(taxonomyNode);
                if(node.getChildren().size() > 0) {
                    List<Taxonomy> nodesWithoutChildren = new ArrayList<>();
                    List<TaxonomyParserNode> nodesWithChildren = new ArrayList<>();
                    for(TaxonomyParserNode childNode : node.getChildren()) {
                        if(childNode.getChildren().size() == 0) {
                            Taxonomy childTaxonomy = new Taxonomy();
                            childTaxonomy.setName(childNode.getName());
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
                }
            }
        }
    }

    public LiveData<List<Taxonomy>> getChildTaxonomies(int repoId, int parentId) {
        return taxonomyDao.getChildTaxonomies(repoId, parentId);
    }
}
