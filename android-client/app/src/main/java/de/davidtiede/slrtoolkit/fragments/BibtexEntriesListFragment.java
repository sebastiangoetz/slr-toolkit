package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;
import de.davidtiede.slrtoolkit.views.BibTexEntriesListAdapter;
import de.davidtiede.slrtoolkit.views.SwipeToDeleteCallbackBibTexEntries;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BibtexEntriesListFragment} factory method to
 * create an instance of this fragment.
 */
public class BibtexEntriesListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noEntriesTextView;
    private BibTexEntriesListAdapter.RecyclerViewClickListener listener;
    BibTexEntriesListAdapter adapter;
    private int repoId;
    private ProjectViewModel projectViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bibtex_entries_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.bibTexEntriesRecyclerView);
        noEntriesTextView = view.findViewById(R.id.textview_no_entries);

        setOnClickListener();

        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);

        repoId = projectViewModel.getCurrentRepoId();

        adapter = new BibTexEntriesListAdapter(new BibTexEntriesListAdapter.EntryDiff(), listener, repoId);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallbackBibTexEntries(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        projectViewModel.getEntriesForRepo(repoId).observe(getViewLifecycleOwner(), this::onLoaded);

    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            Entry clickedEntry = adapter.getItemAtPosition(position);
            /*Fragment bibtexEntryDetailFragment = BibtexEntryDetailFragment.newInstance(clickedEntry.getId());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.bibtexEntriesFragment, bibtexEntryDetailFragment);
            ft.addToBackStack(null);
            ft.commit();*/
            projectViewModel.setCurrentEntryIdForCard(clickedEntry.getId());
            NavHostFragment.findNavController(BibtexEntriesListFragment.this)
                    .navigate(R.id.action_bibtexEntriesListFragment_to_bibtexEntryDetailFragment);
        };
    }

    private void onLoaded(List<Entry> list){
        if (list.size() == 0) {
            System.out.println("No entries");
            noEntriesTextView.setVisibility(View.VISIBLE);
        }
        else {
            noEntriesTextView.setVisibility(View.INVISIBLE);
            adapter.submitList(list);
        }
    }
}