package de.slrtoolkit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amrdeveloper.treeview.TreeNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.util.TaxonomyTreeNode;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;
import de.slrtoolkit.views.BibTexEntriesListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaxonomyEntriesListFragment extends Fragment {
    private static TaxonomiesViewModel taxonomiesViewModel;
    int repoId;
    int currentTaxonomyId;
    private BibTexEntriesListAdapter bibTexEntriesListAdapter;
    private BibTexEntriesListAdapter.RecyclerViewClickListener listener;
    private TextView noTaxonomyEntriesTextview;
    private TextView taxonomiesBreadCrumbTextview;
    private List<TreeNode> taxonomyTree;

    public void setTaxonomyTree(List<TreeNode> taxonomyTree) {
        this.taxonomyTree = taxonomyTree;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_taxonomy_entries_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setOnClickListener();
        taxonomiesViewModel = new ViewModelProvider(requireActivity()).get(TaxonomiesViewModel.class);
        repoId = taxonomiesViewModel.getCurrentRepoId();
        currentTaxonomyId = taxonomiesViewModel.getCurrentTaxonomyId();
        RecyclerView taxonomyEntriesRecyclerView = view.findViewById(R.id.taxonomyEntriesRecyclerview);
        noTaxonomyEntriesTextview = view.findViewById(R.id.textview_no_taxonomy_entries);
        taxonomiesBreadCrumbTextview = view.findViewById(R.id.textview_entries_taxonomies_breadcrumb);
        taxonomyEntriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        bibTexEntriesListAdapter = new BibTexEntriesListAdapter(new BibTexEntriesListAdapter.EntryDiff(), listener, repoId);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(taxonomyEntriesRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        taxonomyEntriesRecyclerView.addItemDecoration(dividerItemDecoration);
        taxonomyEntriesRecyclerView.setAdapter(bibTexEntriesListAdapter);

        List<Integer> currentTaxonomyAndChildrenIDs = new ArrayList<>();
        currentTaxonomyAndChildrenIDs.add(currentTaxonomyId);
        currentTaxonomyAndChildrenIDs.addAll(getAllChildrenIDs(currentTaxonomyId));

        taxonomiesViewModel.getTaxonomyWithEntries(currentTaxonomyAndChildrenIDs).observe(getViewLifecycleOwner(), this::onLoaded);
    }

    private Collection<Integer> getAllChildrenIDs(int taxId) {
        List<Integer> ret = new ArrayList<>();
        for(TreeNode n : taxonomyTree) {
            TaxonomyTreeNode ttn = (TaxonomyTreeNode)n.getValue();
            if(ttn.getId() == taxId) {
                for(TreeNode c : n.getChildren()) {
                    int childId = ((TaxonomyTreeNode)c.getValue()).getId();
                    ret.add(childId);
                    ret.addAll(getAllChildrenIDs(childId));
                }
            }
        }
        return ret;
    }

    public void setHeader(Taxonomy taxonomy) {
        if (taxonomy != null) {
            String title = getResources().getString(R.string.entries_for_taxonomy) + " " + taxonomy.getName();
            taxonomiesBreadCrumbTextview.setText(title);
        }
    }

    public void onLoaded(List<BibEntry> entries) {
        try {
            setHeader(taxonomiesViewModel.getTaxonomyById(currentTaxonomyId));
        } catch (ExecutionException | InterruptedException e) {
            Log.e(this.getClass().getName(), "Couldn't load current taxonomy.", e);
        }
        taxonomiesViewModel.setCurrentEntriesInList(entries);
        if (entries.isEmpty()) {
            noTaxonomyEntriesTextview.setVisibility(View.VISIBLE);
        } else {
            noTaxonomyEntriesTextview.setVisibility(View.INVISIBLE);
            bibTexEntriesListAdapter.submitList(entries);
        }
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            BibEntry clickedBibEntry = bibTexEntriesListAdapter.getItemAtPosition(position);
            if (clickedBibEntry == null) return;

            taxonomiesViewModel.setCurrentEntryIdForCard(clickedBibEntry.getEntryId());
            int indexOfEntryInOriginalList = taxonomiesViewModel.getCurrentEntriesInList().indexOf(clickedBibEntry);
            taxonomiesViewModel.setCurrentEntryInListCount(indexOfEntryInOriginalList);
            Fragment entryFragment = new BibtexEntriesDetailViewPagerFragment();
            FragmentTransaction ft = TaxonomyEntriesListFragment.this.getParentFragmentManager().beginTransaction();
            ft.replace(R.id.entries_by_taxonomies_fragment_container_view, entryFragment);
            ft.addToBackStack(null);
            ft.commit();
        };
    }
}