package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;
import de.davidtiede.slrtoolkit.views.FilterEntriesAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {
    private FilterEntriesAdapter arrayAdapter;
    private ArrayList<Entry> entries;
    SwipeFlingAdapterView flingAdapterView;
    private Button keepButton;
    private Button discardButton;
    private TextView noEntriesToFilterTextview;
    private int repoId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ProjectViewModel projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);

        repoId = projectViewModel.getCurrentRepoId();

        noEntriesToFilterTextview = view.findViewById(R.id.textview_no_entries_to_filter);
        flingAdapterView = view.findViewById(R.id.swipe_entries);
        entries = new ArrayList<>();

        arrayAdapter = new FilterEntriesAdapter(requireContext(), entries);
        flingAdapterView.setAdapter(arrayAdapter);

        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                arrayAdapter.removeFirstObject();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Entry entry = (Entry) o;
                projectViewModel.delete(entry, repoId);
            }

            @Override
            public void onRightCardExit(Object o) {
                Entry entry = (Entry) o;
                entry.setStatus(Entry.Status.KEEP);
                projectViewModel.updateEntry(entry);
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

        int id = projectViewModel.getCurrentRepoId();

        final Observer<List<Entry>> openEntriesObserver = data -> {
            entries = (ArrayList<Entry>) data;
            if (entries.size() == 0) {
                noEntriesToFilterTextview.setVisibility(View.VISIBLE);
                flingAdapterView.setVisibility(View.INVISIBLE);
                keepButton.setVisibility(View.INVISIBLE);
                discardButton.setVisibility(View.INVISIBLE);
            } else {
                noEntriesToFilterTextview.setVisibility(View.INVISIBLE);
                keepButton.setVisibility(View.VISIBLE);
                discardButton.setVisibility(View.VISIBLE);
                ArrayList<Entry> newItems = new ArrayList<>(data);
                arrayAdapter.setEntries(newItems);
            }
        };

        projectViewModel.getOpenEntriesForRepo(id).observe(getViewLifecycleOwner(), openEntriesObserver);

        keepButton = view.findViewById(R.id.keep_button);
        discardButton = view.findViewById(R.id.discard_button);

        keepButton.setOnClickListener(this::selectRight);

        discardButton.setOnClickListener(this::selectLeft);
    }

    private void selectRight(View v) {
        flingAdapterView.getTopCardListener().selectRight();
    }

    private void selectLeft(View v) {
        flingAdapterView.getTopCardListener().selectLeft();
    }
}