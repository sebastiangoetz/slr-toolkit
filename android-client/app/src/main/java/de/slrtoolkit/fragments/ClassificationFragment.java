package de.slrtoolkit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.treeview.TreeNode;
import com.amrdeveloper.treeview.TreeViewAdapter;
import com.amrdeveloper.treeview.TreeViewHolderFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.util.TaxonomyTreeNode;
import de.slrtoolkit.viewmodels.ClassificationViewModel;
import de.slrtoolkit.views.TaxonomyTreeViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassificationFragment extends Fragment {

    private static final String ARG_PARAM1 = "currentTaxonomy";
    private int entryId;
    private TreeViewAdapter treeViewAdapter;
    private ClassificationViewModel classificationViewModel;

    private List<Taxonomy> taxonomiesList;
    private Set<Integer> selectedTaxonomies;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentTaxonomy Parameter 1.
     * @return A new instance of fragment ClassificationFragment.
     */
    public static ClassificationFragment newInstance(int currentTaxonomy) {
        ClassificationFragment fragment = new ClassificationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, currentTaxonomy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedTaxonomies = new HashSet<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classification, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        classificationViewModel = new ViewModelProvider(requireActivity()).get(ClassificationViewModel.class);
        int repoId = classificationViewModel.getCurrentRepoId();
        entryId = classificationViewModel.getCurrentEntryId();

        RecyclerView rv = view.findViewById(R.id.taxonomy_classification_recyclerview);
        TreeViewHolderFactory factory = (v, layout) -> new TaxonomyTreeViewHolder(v);
        treeViewAdapter = new TreeViewAdapter(factory);
        rv.setAdapter(treeViewAdapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        try {
            classificationViewModel.getAllTaxonomiesForRepo(repoId).observe(getViewLifecycleOwner(), this::setTree);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(ClassificationFragment.class.getName(), "could not select taxonomies", e);
        }
        setOnLongClickListener();
    }

    private void setTree(List<Taxonomy> taxonomies) {
        this.taxonomiesList = taxonomies;
        List<TreeNode> rootTaxonomies = new ArrayList<>();
        selectedTaxonomies = classificationViewModel.getSelectedTaxonomies();
        for(Taxonomy root : taxonomies) {
            if(root.getParentId() == 0) {
                TaxonomyTreeNode n = new TaxonomyTreeNode(root.getTaxonomyId(), root.getName()+" "+selectedDecoration(root.getTaxonomyId()));
                TreeNode rootNode;
                if(selectedTaxonomies.contains(root.getTaxonomyId()))
                    rootNode = new TreeNode(n, R.layout.item_taxonomy_entry_selected);
                else
                    rootNode = new TreeNode(n, R.layout.item_taxonomy_entry);
                addChildrenToRoot(rootNode, root.getTaxonomyId(), taxonomies);
                rootTaxonomies.add(rootNode);
            }
        }
        treeViewAdapter.updateTreeNodes(rootTaxonomies);
        treeViewAdapter.expandAll();
    }

    private String selectedDecoration(int taxId) {
        String selected = "(-)";
        if(selectedTaxonomies.contains(taxId)) {
            selected = "(+)";
        }
        return selected;
    }

    /**Adds all taxonomy entries from the third parameter to the root node, if this node is their parent
     *
     * @param root the TreeNode to which children shall be added
     * @param rootId the id of the root
     * @param taxonomies list of taxonomy entries which potentially are children of root
     */
    private void addChildrenToRoot(TreeNode root, int rootId, List<Taxonomy> taxonomies) {
        for (Taxonomy tax : taxonomies) {
            if (tax.getParentId() == rootId) {
                TaxonomyTreeNode n = new TaxonomyTreeNode(tax.getTaxonomyId(), tax.getName()+" "+selectedDecoration(tax.getTaxonomyId()));
                TreeNode child;
                if(selectedTaxonomies.contains(tax.getTaxonomyId()))
                    child = new TreeNode(n, R.layout.item_taxonomy_entry_selected);
                else
                    child = new TreeNode(n, R.layout.item_taxonomy_entry);
                addChildrenToRoot(child, tax.getTaxonomyId(), taxonomies);
                root.addChild(child);
            }
        }
    }

    private void setOnLongClickListener() {
        treeViewAdapter.setTreeNodeLongClickListener((treeNode, view) -> {
            TaxonomyTreeNode clickedTaxonomy = (TaxonomyTreeNode) treeNode.getValue();
            boolean entryContainsTaxonomy = false;
            Set<Integer> selectedTaxonomyIds = classificationViewModel.getSelectedTaxonomies();
            if(selectedTaxonomyIds == null) selectedTaxonomyIds = new HashSet<>();
            for (int taxId : selectedTaxonomyIds) {
                if (taxId == clickedTaxonomy.getId()) {
                    entryContainsTaxonomy = true;
                    break;
                }
            }
            if (entryContainsTaxonomy) {
                classificationViewModel.removeTaxonomyFromClassification(clickedTaxonomy, entryId);
            } else {
                classificationViewModel.addTaxonomyToClassification(clickedTaxonomy, entryId);
            }
            setTree(taxonomiesList);
            return entryContainsTaxonomy;
        });
    }
}