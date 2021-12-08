package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.BibtexEntriesViewModel;
import de.davidtiede.slrtoolkit.views.BibTexEntriesListAdapter;
import de.davidtiede.slrtoolkit.views.SwipeToDeleteCallbackBibTexEntries;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BibtexEntriesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BibtexEntriesListFragment extends Fragment {

    private RecyclerView recyclerView;
    private BibTexEntriesListAdapter.RecyclerViewClickListener listener;
    private BibtexEntriesViewModel bibtexEntriesViewModel;
    BibTexEntriesListAdapter adapter;
    private static final String ARG_PARAM1 = "repoId";
    private int repoId;

    public BibtexEntriesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param repoId Parameter 1.
     * @return A new instance of fragment BibtexEntriesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BibtexEntriesListFragment newInstance(int repoId) {
        BibtexEntriesListFragment fragment = new BibtexEntriesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, repoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            repoId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bibtex_entries_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.bibTexEntriesRecyclerView);

        setOnClickListener();

        bibtexEntriesViewModel = new ViewModelProvider(this).get(BibtexEntriesViewModel.class);

        adapter = new BibTexEntriesListAdapter(new BibTexEntriesListAdapter.EntryDiff(), listener, repoId);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallbackBibTexEntries(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        bibtexEntriesViewModel.getEntriesForRepo(repoId).observe(getViewLifecycleOwner(), this::onLoaded);

    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            Entry clickedEntry = adapter.getItemAtPosition(position);
            Fragment bibtexEntryDetailFragment = BibtexEntryDetailFragment.newInstance(clickedEntry.getId());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.bibtexEntriesFragment, bibtexEntryDetailFragment);
            ft.addToBackStack(null);
            ft.commit();
        };
    }

    private void onLoaded(List<Entry> list){
        if (list.size() == 0) {
            //TODO: show that there are no entries yet
        }
        else {
            adapter.submitList(list);
        }
    }
}