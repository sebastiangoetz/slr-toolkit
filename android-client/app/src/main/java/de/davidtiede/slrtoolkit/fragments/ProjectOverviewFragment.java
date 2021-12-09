package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.davidtiede.slrtoolkit.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectOverviewFragment} factory method to
 * create an instance of this fragment.
 */
public class ProjectOverviewFragment extends Fragment {

    private Button allEntryButton;
    private Button filterButton;
    private Button taxonomyButton;
    private Button classifyButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_overview, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        allEntryButton = view.findViewById(R.id.button_all_entries);
        filterButton = view.findViewById(R.id.button_filter);
        taxonomyButton = view.findViewById(R.id.button_entries_by_taxonomy);
        classifyButton = view.findViewById(R.id.button_classify);

        allEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProjectOverviewFragment.this)
                        .navigate(R.id.action_projectOverviewFragment_to_bibtexEntriesListFragment);
            }
        });

        filterButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProjectOverviewFragment.this)
                    .navigate(R.id.action_projectOverviewFragment_to_filterFragment);
        });

        taxonomyButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProjectOverviewFragment.this)
                    .navigate(R.id.action_projectOverviewFragment_to_taxonomyListFragment);
        });

        classifyButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(ProjectOverviewFragment.this)
                    .navigate(R.id.action_projectOverviewFragment_to_entriesToClassifyViewPagerFragment);
        });

        /*final Observer entryAmountObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer amount) {
                allEntryButton.setText("All Entries (" + amount.toString() + ")");
            }
        };
        projectViewModel.getEntryAmount(id).observe(this, entryAmountObserver);

        final Observer openEntryAmountObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer amount) {
                filterButton.setText("Filter (" + amount.toString() + ")");
            }
        };
        projectViewModel.getOpenEntryAmount(id).observe(this, openEntryAmountObserver); */


        /*
        */
    }
}