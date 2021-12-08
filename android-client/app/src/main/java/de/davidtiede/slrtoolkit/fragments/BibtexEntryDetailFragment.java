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
import de.davidtiede.slrtoolkit.viewmodels.BibtexEntryViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BibtexEntryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BibtexEntryDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "repoId";
    private static final String ARG_PARAM2 = "entryId";
    private TextView titleTextView;
    private BibtexEntryViewModel bibtexEntryViewModel;
    private int entryId;

    public BibtexEntryDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param repoId Parameter 1.
     * @return A new instance of fragment BibtexEntryDetailFragment.
     */
    public static BibtexEntryDetailFragment newInstance(int entryId) {
        BibtexEntryDetailFragment fragment = new BibtexEntryDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, entryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entryId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bibtex_entry_detail, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        titleTextView = view.findViewById(R.id.bibtex_entry_title);
        bibtexEntryViewModel = new ViewModelProvider(this).get(BibtexEntryViewModel.class);

        bibtexEntryViewModel.getEntryById(entryId).observe(getViewLifecycleOwner(), entry -> {
            titleTextView.setText(entry.getTitle());
        });
    }
}