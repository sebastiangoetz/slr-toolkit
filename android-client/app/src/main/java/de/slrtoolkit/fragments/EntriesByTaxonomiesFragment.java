package de.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.treeview.TreeNode;
import com.amrdeveloper.treeview.TreeViewAdapter;
import com.amrdeveloper.treeview.TreeViewHolderFactory;

import java.util.List;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.util.TaxonomyTreeNode;
import de.slrtoolkit.util.TaxonomyUtil;
import de.slrtoolkit.viewmodels.ProjectViewModel;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;
import de.slrtoolkit.views.TaxonomyTreeViewHolder;

/**This Fragment shows the Taxonomy and allows the user to select a class for which the bib entries shall be shown. */
public class EntriesByTaxonomiesFragment extends Fragment {

    private static final String ARG_PARAM1 = "currentTaxonomyId";
    ConstraintLayout constraintLayout;
    private TaxonomiesViewModel taxonomiesViewModel;
    private TreeViewAdapter treeViewAdapter;
    private TextView taxonomiesBreadCrumbTextview;
    private TextView noTaxonomiesTextview;
    private int currentTaxonomyId;
    private int repoId;

    public EntriesByTaxonomiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param currentItemId Parameter 1.
     * @return A new instance of fragment TaxonomyListFragment.
     */
    public static EntriesByTaxonomiesFragment newInstance(int currentItemId) {
        EntriesByTaxonomiesFragment fragment = new EntriesByTaxonomiesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, currentItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentTaxonomyId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entries_by_taxonomies_overview, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        taxonomiesViewModel = new ViewModelProvider(requireActivity()).get(TaxonomiesViewModel.class);
        repoId = taxonomiesViewModel.getCurrentRepoId();
        RecyclerView rv = view.findViewById(R.id.taxonomyRecyclerview);
        noTaxonomiesTextview = view.findViewById(R.id.textview_no_taxonomies);
        taxonomiesBreadCrumbTextview = view.findViewById(R.id.textview_taxonomies_breadcrumb);
        constraintLayout = view.findViewById(R.id.taxonomy_list_constraint_layout);

        TreeViewHolderFactory factory = (v, layout) -> new TaxonomyTreeViewHolder(v);
        treeViewAdapter = new TreeViewAdapter(factory);
        rv.setAdapter(treeViewAdapter);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        treeViewAdapter.setTreeNodeClickListener((treeNode, view1) -> {
            TaxonomyTreeNode n = (TaxonomyTreeNode) treeNode.getValue();

            taxonomiesViewModel.setCurrentTaxonomyId(n.getId());
            Fragment entriesFragment = new TaxonomyEntriesListFragment();
            FragmentTransaction ft = this.getParentFragmentManager().beginTransaction();
            ft.replace(R.id.entries_by_taxonomies_fragment_container_view, entriesFragment);
            ft.addToBackStack(null);
            ft.commit();
        });

        taxonomiesViewModel.getAllTaxonomiesForRepo(repoId).observe(getViewLifecycleOwner(), this::onLoaded);
        setHeader();
    }

    public void setHeader() {
        if (currentTaxonomyId > 0) {
            taxonomiesViewModel.getTaxonomyWithEntries(repoId, currentTaxonomyId).observe(getViewLifecycleOwner(), t -> {
                String path = t.taxonomy.getPath();
                if (path.length() > 1) {
                    path = path.replaceAll("#", " > ");
                    if (path.charAt(1) == '>') {
                        path = path.replaceFirst(" > ", "");
                    }
                    taxonomiesBreadCrumbTextview.setText(path);
                }
            });
        } else {
            taxonomiesBreadCrumbTextview.setVisibility(View.INVISIBLE);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.taxonomyRecyclerview, ConstraintSet.TOP, 0, ConstraintSet.TOP, 0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    private void onLoaded(List<Taxonomy> taxonomies) {
        List<TreeNode> rootTaxonomies = new TaxonomyUtil().taxonomiesToTreeNodes(taxonomies, true, new ViewModelProvider(requireActivity()).get(ProjectViewModel.class));
        if(!rootTaxonomies.isEmpty()) {
            noTaxonomiesTextview.setVisibility(View.INVISIBLE);
        } else {
            noTaxonomiesTextview.setVisibility(View.VISIBLE);
        }
        treeViewAdapter.updateTreeNodes(rootTaxonomies);
        treeViewAdapter.expandAll();
    }
}