package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BibtexEntryDetailFragment extends Fragment {

    private TextView titleTextView;
    private ProjectViewModel projectViewModel;
    private int entryId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bibtex_entry_detail, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        titleTextView = view.findViewById(R.id.bibtex_entry_title);
        projectViewModel = new ViewModelProvider(getActivity()).get(ProjectViewModel.class);

        entryId = projectViewModel.getCurrentEntryIdForCard();

        projectViewModel.getEntryById(entryId).observe(getViewLifecycleOwner(), entry -> {
            titleTextView.setText(entry.getTitle());
        });
    }
}