package de.davidtiede.slrtoolkit.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private TextView titleTextView;
    private TextView authorsTextView;
    private TextView yearTextView;
    private TextView monthTextView;
    private TextView abstractTextView;
    private TextView doiTextView;
    private TextView keywordsTextView;
    private int entryId;
    private int repoId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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

        if(getActivity() instanceof ProjectActivity) {
            ProjectViewModel projectViewModel = new ViewModelProvider(getActivity()).get(ProjectViewModel.class);
            entryId = projectViewModel.getCurrentEntryIdForCard();
            repoId = projectViewModel.getCurrentRepoId();
            projectViewModel.getEntryById(entryId).observe(getViewLifecycleOwner(), entry -> {
                setEntryInformation(entry);
            });


        } else if(getActivity() instanceof TaxonomiesActivity){
            TaxonomiesViewModel taxonomiesViewModel = new ViewModelProvider(getActivity()).get(TaxonomiesViewModel.class);
            repoId = taxonomiesViewModel.getCurrentRepoId();
            entryId = taxonomiesViewModel.getCurrentEntryIdForCard();
            taxonomiesViewModel.getEntryById(entryId).observe(getViewLifecycleOwner(), entry -> {
                setEntryInformation(entry);
            });
        }

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

    private void deleteEntry() {
        if(getActivity() instanceof ProjectActivity) {
            ProjectViewModel projectViewModel = new ViewModelProvider(getActivity()).get(ProjectViewModel.class);
            entryId = projectViewModel.getCurrentEntryIdForCard();
            repoId = projectViewModel.getCurrentRepoId();
            projectViewModel.deleteById(entryId, repoId);
            getActivity().onBackPressed();

        } else if(getActivity() instanceof TaxonomiesActivity){
            TaxonomiesViewModel taxonomiesViewModel = new ViewModelProvider(getActivity()).get(TaxonomiesViewModel.class);
            repoId = taxonomiesViewModel.getCurrentRepoId();
            entryId = taxonomiesViewModel.getCurrentEntryIdForCard();
            taxonomiesViewModel.deleteEntryById(entryId, repoId);
            getActivity().onBackPressed();
        }
    }

    private void classifyEntry() {
        Intent intent = new Intent(getActivity(), ClassificationActivity.class);
        intent.putExtra("repo", repoId);
        intent.putExtra("entry", entryId);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_entry_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_delete: {
                System.out.println("Delete");
                deleteEntry();
                break;
            }
            case R.id.action_classify: {
                System.out.println("classify");
                classifyEntry();
            }
        }
        return false;
    }
}