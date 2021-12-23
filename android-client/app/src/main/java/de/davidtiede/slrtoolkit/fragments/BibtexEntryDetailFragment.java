package de.davidtiede.slrtoolkit.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.davidtiede.slrtoolkit.ClassificationActivity;
import de.davidtiede.slrtoolkit.ProjectActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.TaxonomiesActivity;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;
import de.davidtiede.slrtoolkit.viewmodels.TaxonomiesViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BibtexEntryDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "repoId";
    private static final String ARG_PARAM2 = "entryId";
    private TextView titleTextView;
    private TextView authorsTextView;
    private TextView yearTextView;
    private TextView monthTextView;
    private TextView abstractTextView;
    private TextView doiTextView;
    private TextView keywordsTextView;
    private Button classifyButton;
    private int entryId;
    private int repoId;

    public BibtexEntryDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param repoId Parameter 1.
     * @param entryId Parameter 2.
     * @return A new instance of fragment TaxonomyEntriesFragment.
     */
    public static BibtexEntryDetailFragment newInstance(int repoId, int entryId) {
        BibtexEntryDetailFragment fragment = new BibtexEntryDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, repoId);
        args.putInt(ARG_PARAM2, entryId);
        fragment.setArguments(args);
        return fragment;
    }

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
        authorsTextView = view.findViewById(R.id.bibtex_entry_authors);
        yearTextView = view.findViewById(R.id.bibtex_entry_year);
        monthTextView = view.findViewById(R.id.bibtex_entry_month);
        abstractTextView = view.findViewById(R.id.bibtex_entry_abstract);
        doiTextView = view.findViewById(R.id.bibtex_entry_doi);
        keywordsTextView = view.findViewById(R.id.bibtex_entry_keywords);
        classifyButton = view.findViewById(R.id.classify_entry_button);

        if(getActivity() instanceof ProjectActivity) {
            ProjectViewModel projectViewModel = new ViewModelProvider(getActivity()).get(ProjectViewModel.class);
            entryId = projectViewModel.getCurrentEntryIdForCard();
            repoId = projectViewModel.getCurrentRepoId();
            projectViewModel.getEntryById(entryId).observe(getViewLifecycleOwner(), this::setEntryInformation);
        } else if(getActivity() instanceof TaxonomiesActivity){
            if (getArguments() != null) {
                repoId = getArguments().getInt(ARG_PARAM1);
                entryId = getArguments().getInt(ARG_PARAM2);
            }
            TaxonomiesViewModel taxonomiesViewModel = new ViewModelProvider(getActivity()).get(TaxonomiesViewModel.class);
            taxonomiesViewModel.getEntryById(entryId).observe(getViewLifecycleOwner(), this::setEntryInformation);
        }

        setOnClickListener();
    }

    public void setEntryInformation(Entry entry) {
        titleTextView.setText(entry.getTitle());
        authorsTextView.setText(entry.getAuthor());
        yearTextView.setText(entry.getYear());
        monthTextView.setText(entry.getMonth());
        abstractTextView.setText(entry.getAbstractText());
        doiTextView.setText(entry.getDoi());
        keywordsTextView.setText(entry.getKeywords());
    }

    public void setOnClickListener() {
        classifyButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ClassificationActivity.class);
            intent.putExtra("repo", repoId);
            intent.putExtra("entry", entryId);
            startActivity(intent);
        });
    }
}