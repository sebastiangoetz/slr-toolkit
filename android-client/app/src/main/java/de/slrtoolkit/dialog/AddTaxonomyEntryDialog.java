package de.slrtoolkit.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Keyword;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.repositories.KeywordRepository;
import de.slrtoolkit.repositories.RepoRepository;
import de.slrtoolkit.repositories.TaxonomyRepository;
import de.slrtoolkit.util.FileUtil;
import de.slrtoolkit.util.SlrprojectParser;
import de.slrtoolkit.util.TaxonomyParser;
import de.slrtoolkit.viewmodels.RepoViewModel;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;

public class AddTaxonomyEntryDialog extends DialogFragment {
    private TextInputEditText edittextTaxonomyEntryName;
    private TaxonomiesViewModel taxonomiesViewModel;
    private TaxonomyRepository taxonomyRepository;
    private RepoViewModel repoViewModel;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        taxonomiesViewModel = new ViewModelProvider(requireActivity()).get(TaxonomiesViewModel.class);
        taxonomyRepository = new TaxonomyRepository(requireActivity().getApplication());
        repoViewModel = new RepoViewModel(requireActivity().getApplication());

        View view1 = getLayoutInflater().inflate(R.layout.dialog_add_taxonomy_entry, null);

        Spinner spinnerParentEntries = view1.findViewById(R.id.spinner_parentEntry);

        List<Taxonomy> taxonomies = new ArrayList<>();
        taxonomiesViewModel.getAllTaxonomiesForRepo(taxonomiesViewModel.getCurrentRepoId()).observe(this, taxonomies::addAll);
        ArrayAdapter<Taxonomy> parentEntriesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, taxonomies);
        spinnerParentEntries.setAdapter(parentEntriesAdapter);
        parentEntriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.e(AddTaxonomyEntryDialog.class.getName(), "onCreateDialog: test");
        spinnerParentEntries.setEnabled(true);
        spinnerParentEntries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(adapterView.getContext(),adapterView.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edittextTaxonomyEntryName = view1.findViewById(R.id.edittext_taxonomy_entry_name);
        Button createButton = view1.findViewById(R.id.button_add_taxonomy_entry);
        createButton.setOnClickListener(view -> {
            Taxonomy t = new Taxonomy();
            t.setName(Objects.requireNonNull(edittextTaxonomyEntryName.getText()).toString());
            t.setRepoId(taxonomiesViewModel.getCurrentRepoId());

            try {
                Repo repo = repoViewModel.getRepoDirectly(taxonomiesViewModel.getCurrentRepoId());
                taxonomyRepository.addToFile(repo.getLocal_path(), requireActivity().getApplication(), t, null);
                taxonomyRepository.insert(t);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            dismiss();
        });
        AlertDialog.Builder builder= new AlertDialog.Builder(requireContext());
        return builder
                .setMessage(R.string.fab_add_taxonomy)
                .setView(view1)
                .setNegativeButton(R.string.cancel, (dialog, which)-> dialog.dismiss())
                .create();
    }
}
