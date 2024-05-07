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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.amrdeveloper.treeview.TreeNode;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.database.Taxonomy;
import de.slrtoolkit.repositories.TaxonomyRepository;
import de.slrtoolkit.util.TaxonomyTreeNode;
import de.slrtoolkit.viewmodels.RepoViewModel;
import de.slrtoolkit.viewmodels.TaxonomiesViewModel;

public class AddTaxonomyEntryDialog extends DialogFragment {
    private TextInputEditText edittextTaxonomyEntryName;
    private TaxonomiesViewModel taxonomiesViewModel;
    private TaxonomyRepository taxonomyRepository;
    private RepoViewModel repoViewModel;

    private final List<TreeNode> rootTaxonomies;
    private String selectedTaxonomy;

    public AddTaxonomyEntryDialog(List<TreeNode> rootTaxonomies) {
        this.rootTaxonomies = rootTaxonomies;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        taxonomiesViewModel = new ViewModelProvider(requireActivity()).get(TaxonomiesViewModel.class);
        taxonomyRepository = new TaxonomyRepository(requireActivity().getApplication());
        repoViewModel = new RepoViewModel(requireActivity().getApplication());

        List<String> taxonomies = new ArrayList<>();
        for(TreeNode n : rootTaxonomies) {
            taxonomies.add(n.getValue().toString());
            addChildren(taxonomies, n, "");
        }

        View view1 = getLayoutInflater().inflate(R.layout.dialog_add_taxonomy_entry, null);

        Spinner spinnerParentEntries = view1.findViewById(R.id.spinner_parentEntry);

        ArrayAdapter<String> parentEntriesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, taxonomies);
        spinnerParentEntries.setAdapter(parentEntriesAdapter);
        parentEntriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.e(AddTaxonomyEntryDialog.class.getName(), "onCreateDialog: test");
        spinnerParentEntries.setEnabled(true);
        spinnerParentEntries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTaxonomy = adapterView.getSelectedItem().toString();
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
            int parentId = getIdForString(rootTaxonomies, selectedTaxonomy);
            t.setParentId(parentId);
            Taxonomy parent = new Taxonomy();
            parent.setName(selectedTaxonomy);
            parent.setRepoId(taxonomiesViewModel.getCurrentRepoId());

            try {
                Repo repo = repoViewModel.getRepoDirectly(taxonomiesViewModel.getCurrentRepoId());
                taxonomyRepository.addToFile(repo.getLocal_path(), requireActivity().getApplication(), t, rootTaxonomies);
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

    private int getIdForString(List<TreeNode> nodes, String selectedTaxonomy) {
        for(TreeNode n : nodes) {
            TaxonomyTreeNode t = (TaxonomyTreeNode)n.getValue();
            if(t.getName().equals(selectedTaxonomy)) {
                return t.getId();
            } else if(selectedTaxonomy.contains((t.getName()))) {
                int id = getIdForString(n.getChildren(), selectedTaxonomy);
                if(id != -1)  {
                    return id;
                }
            }
        }
        return -1;
    }

    private void addChildren(List<String> taxonomies, TreeNode n, String path) {
        for(TreeNode child : n.getChildren()) {
            taxonomies.add(child.getValue().toString());
            addChildren(taxonomies, child, path+"#"+n.getValue().toString());
        }
    }
}
