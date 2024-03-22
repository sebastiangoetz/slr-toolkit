package de.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.treeview.TreeNode;
import com.amrdeveloper.treeview.TreeViewAdapter;
import com.amrdeveloper.treeview.TreeViewHolderFactory;

import java.util.ArrayList;
import java.util.List;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;
import de.slrtoolkit.views.TaxonomyTreeViewHolder;

public class TaxonomyTreeViewFragment extends Fragment {
    private TreeViewAdapter treeViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_taxonomy_treeview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TaxonomiesViewModel taxonomiesViewModel = new ViewModelProvider(requireActivity()).get(TaxonomiesViewModel.class);

        RecyclerView rv = view.findViewById(R.id.taxonomyRecyclerview);

        TreeViewHolderFactory factory = (v, layout) -> new TaxonomyTreeViewHolder(v);
        treeViewAdapter = new TreeViewAdapter(factory);
        rv.setAdapter(treeViewAdapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        taxonomiesViewModel.getAllTaxonomiesForRepo(taxonomiesViewModel.getCurrentRepoId()).observe(getViewLifecycleOwner(), this::onLoaded);
    }

    private void onLoaded(List<Taxonomy> taxonomies) {
        List<TreeNode> rootTaxonomies = new ArrayList<>();
        for(Taxonomy root : taxonomies) {
            if(root.getParentId() == 0) {
                TaxonomyTreeNode n = new TaxonomyTreeNode(root.getTaxonomyId(), root.getName());
                TreeNode rootNode = new TreeNode(n, R.layout.taxonomy_entry_item);
                addChildren(rootNode, root.getTaxonomyId(), taxonomies);
                rootTaxonomies.add(rootNode);
            }
        }

        treeViewAdapter.updateTreeNodes(rootTaxonomies);
    }

    private void addChildren(TreeNode root, int rootId, List<Taxonomy> taxonomies) {
        for(Taxonomy tax : taxonomies) {
            if(tax.getParentId() == rootId) {
                TaxonomyTreeNode n = new TaxonomyTreeNode(tax.getTaxonomyId(), tax.getName());
                TreeNode child = new TreeNode(n, R.layout.taxonomy_entry_item);
                addChildren(child, tax.getTaxonomyId(), taxonomies);
                root.addChild(child);
            }
        }
    }

    private static class TaxonomyTreeNode {
        private int id;
        private String name;
        public TaxonomyTreeNode(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }
}
