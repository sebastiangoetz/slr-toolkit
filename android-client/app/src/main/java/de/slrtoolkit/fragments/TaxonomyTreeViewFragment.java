package de.slrtoolkit.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.dialog.AddTaxonomyEntryDialog;
import de.slrtoolkit.repositories.TaxonomyRepository;
import de.slrtoolkit.util.TaxonomyTreeNode;
import de.slrtoolkit.util.TaxonomyUtil;
import de.slrtoolkit.viewmodels.RepoViewModel;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;
import de.slrtoolkit.views.TaxonomyTreeViewHolder;

public class TaxonomyTreeViewFragment extends Fragment {
    TaxonomiesViewModel taxonomiesViewModel;
    RepoViewModel repoViewModel;
    TaxonomyRepository taxonomyRepository;
    private TreeViewAdapter treeViewAdapter;
    private List<Taxonomy> taxonomiesList;

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

        taxonomiesViewModel = new ViewModelProvider(requireActivity()).get(TaxonomiesViewModel.class);
        taxonomyRepository = new TaxonomyRepository(requireActivity().getApplication());
        repoViewModel = new RepoViewModel(requireActivity().getApplication());

        RecyclerView rv = view.findViewById(R.id.taxonomyRecyclerview);

        TreeViewHolderFactory factory = (v, layout) -> new TaxonomyTreeViewHolder(v);
        treeViewAdapter = new TreeViewAdapter(factory);
        rv.setAdapter(treeViewAdapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        treeViewAdapter.setTreeNodeLongClickListener((treeNode, view1) -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Taxonomy Entry")
                    .setMessage("Do you really want to delete '"+treeNode.getValue().toString()+"' ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            remove(treeNode);
                            try {
                                Repo repo = repoViewModel.getRepoDirectly(taxonomiesViewModel.getCurrentRepoId());
                                taxonomyRepository.updateFile(repo.getLocal_path(), requireActivity().getApplication(), taxonomiesList);
                            } catch (ExecutionException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        private void remove(TreeNode node) {
                            if(!node.getChildren().isEmpty()) {
                                for(TreeNode n : node.getChildren()) {
                                    remove(n);
                                }
                            }
                            taxonomyRepository.remove(taxonomiesViewModel.getCurrentRepoId(), ((TaxonomyTreeNode) node.getValue()).getId());
                            taxonomiesList.removeIf(taxonomy -> taxonomy.getTaxonomyId() == ((TaxonomyTreeNode)node.getValue()).getId());
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null).show();
            return true;
        });

        taxonomiesViewModel.getAllTaxonomiesForRepo(taxonomiesViewModel.getCurrentRepoId()).observe(getViewLifecycleOwner(), this::onLoaded);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_taxonomy);
        fab.setOnClickListener(v -> {
                AddTaxonomyEntryDialog dialog = new AddTaxonomyEntryDialog(taxonomiesList);
                dialog.show(getChildFragmentManager(), AddTaxonomyEntryDialog.class.getName());
        });
    }

    private void onLoaded(List<Taxonomy> taxonomies) {
        this.taxonomiesList = taxonomies;
        List<TreeNode> rootTaxonomies = new TaxonomyUtil().taxonomiesToTreeNodes(taxonomies);
        treeViewAdapter.updateTreeNodes(rootTaxonomies);
    }

}
