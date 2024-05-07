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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.dialog.AddTaxonomyEntryDialog;
import de.slrtoolkit.util.TaxonomyTreeNode;
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

        FloatingActionButton fab = view.findViewById(R.id.fab_add_taxonomy);
        fab.setOnClickListener(v -> {
                AddTaxonomyEntryDialog dialog = new AddTaxonomyEntryDialog(treeViewAdapter.getTreeNodes());
                dialog.show(getChildFragmentManager(), AddTaxonomyEntryDialog.class.getName());
        });
    }

    private void onLoaded(List<Taxonomy> taxonomies) {
        List<TreeNode> rootTaxonomies = new ArrayList<>();
        for(Taxonomy root : taxonomies) {
            if(root.getParentId() == 0) {
                TaxonomyTreeNode n = new TaxonomyTreeNode(root.getTaxonomyId(), root.getName());
                TreeNode rootNode = new TreeNode(n, R.layout.item_taxonomy_entry);
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
                TreeNode child = new TreeNode(n, R.layout.item_taxonomy_entry);
                addChildren(child, tax.getTaxonomyId(), taxonomies);
                root.addChild(child);
            }
        }
    }

}
