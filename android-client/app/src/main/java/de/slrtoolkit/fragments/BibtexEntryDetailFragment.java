package de.slrtoolkit.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.slrtoolkit.ProjectActivity;
import de.slrtoolkit.R;
import de.slrtoolkit.TaxonomiesActivity;
import de.slrtoolkit.database.Entry;
import de.slrtoolkit.viewmodels.ProjectViewModel;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BibtexEntryDetailFragment extends Fragment {

    private TextView titleTextView;
    private TextView authorsTextView;
    private TextView yearTextView;
    private TextView monthTextView;
    private TextView abstractTextView;
    private TextView doiTextView;
    private TextView keywordsTextView;
    private TextView publishedTextView;

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
        publishedTextView = view.findViewById(R.id.bibtex_entry_published);

        if (getActivity() instanceof ProjectActivity) {
            ProjectViewModel projectViewModel = new ViewModelProvider(getActivity()).get(ProjectViewModel.class);
            int entryId = projectViewModel.getCurrentEntryIdForCard();
            projectViewModel.getEntryById(entryId).observe(getViewLifecycleOwner(), this::setEntryInformation);


        } else if (getActivity() instanceof TaxonomiesActivity) {
            TaxonomiesViewModel taxonomiesViewModel = new ViewModelProvider(getActivity()).get(TaxonomiesViewModel.class);
            int entryId = taxonomiesViewModel.getCurrentEntryIdForCard();
            taxonomiesViewModel.getEntryById(entryId).observe(getViewLifecycleOwner(), this::setEntryInformation);
        }

        doiTextView.setOnClickListener(view1 -> {
            Intent openDoiIntent = new Intent(Intent.ACTION_VIEW);
            openDoiIntent.setData(Uri.parse("https://doi.org/"+doiTextView.getText()));
            startActivity(openDoiIntent);
        });

    }

    public void setEntryInformation(Entry entry) {
        if (entry == null) {
            return;
        }
        titleTextView.setText(entry.getTitle());
        authorsTextView.setText(entry.getAuthor());
        yearTextView.setText(entry.getYear());
        monthTextView.setText(entry.getMonth());
        abstractTextView.setText(entry.getAbstractText());
        doiTextView.setText(entry.getDoi());
        keywordsTextView.setText(entry.getKeywords());
        if(entry.getJournal() == null || entry.getJournal().trim().isEmpty()) {
            publishedTextView.setText(entry.getBooktitle());
        } else {
            publishedTextView.setText(entry.getJournal());
        }
    }
}