package de.slrtoolkit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.slrtoolkit.R;
import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.viewmodels.ProjectViewModel;
import de.slrtoolkit.views.BibTexEntriesListAdapter;
import de.slrtoolkit.views.SwipeToDeleteCallbackBibTexEntries;

/**
 * A simple {@link Fragment} subclass.
 */
public class BibtexEntriesListFragment extends Fragment {

    BibTexEntriesListAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noEntriesTextView;
    private BibTexEntriesListAdapter.RecyclerViewClickListener listener;
    private int repoId;
    private ProjectViewModel projectViewModel;

    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                this::onActivityResult);
        return inflater.inflate(R.layout.fragment_bibtex_entries_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.bibTexEntriesRecyclerView);
        noEntriesTextView = view.findViewById(R.id.textview_no_entries);

        view.findViewById(R.id.btn_add_bibtex_entry).setOnClickListener(btn -> {
            AlertDialog importBibtexDialog = new MaterialAlertDialogBuilder(requireActivity())
                    .setView(R.layout.dialog_add_bibtex)
                    .setNegativeButton(R.string.close, (dialogInterface, i) -> dialogInterface.dismiss()).create();
            importBibtexDialog.show();
            //TODO check if pasted bibtex is parsable and disable import button if that is not the case
            importBibtexDialog.findViewById(R.id.button_dialog_import_bibtex).setOnClickListener(view1 -> {
                importBibtexDialog.dismiss();
                EditText txt = importBibtexDialog.findViewById(R.id.dialog_import_bibtex_text);
                String bibtex = txt.getText().toString();
                Log.e("de.slrtoolkit", "onViewCreated: "+bibtex);
                projectViewModel.addBibEntry(bibtex,repoId);
            });
            Button btnImportFromFile = importBibtexDialog.findViewById(R.id.button_import_from_file);
            if(btnImportFromFile != null) {
                btnImportFromFile.setOnClickListener(view1 -> {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    resultLauncher.launch(intent);
                });
            }
        });

        setOnClickListener();

        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);

        repoId = projectViewModel.getCurrentRepoId();

        adapter = new BibTexEntriesListAdapter(new BibTexEntriesListAdapter.EntryDiff(), listener, repoId);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallbackBibTexEntries(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        this.setEntries();
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            BibEntry clickedBibEntry = adapter.getItemAtPosition(position);

            if (clickedBibEntry == null) return;

            projectViewModel.setCurrentBibEntryIdForCard(clickedBibEntry.getEntryId());
            int indexOfEntryInOriginalList = projectViewModel.getCurrentBibEntriesInList().indexOf(clickedBibEntry);
            projectViewModel.setCurrentBibEntryInListCount(indexOfEntryInOriginalList);
            NavHostFragment.findNavController(BibtexEntriesListFragment.this)
                    .navigate(R.id.action_bibtexEntriesListFragment_to_bibtexEntryDetailFragment);
        };
    }

    private void setEntries() {
        projectViewModel.getEntriesForRepo(repoId).observe(getViewLifecycleOwner(), this::onLoaded);
    }

    private void onLoaded(List<BibEntry> list) {
        projectViewModel.setCurrentBibEntriesInList(list);
        if (list.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
            noEntriesTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noEntriesTextView.setVisibility(View.INVISIBLE);
            adapter.submitList(list);
        }
    }

    private void filterList(String searchTerm) {
        List<BibEntry> filteredEntries = new ArrayList<>();
        for (BibEntry e : projectViewModel.getCurrentBibEntriesInList()) {
            if (e.getTitle().toLowerCase(Locale.ROOT).contains(searchTerm.toLowerCase(Locale.ROOT))) {
                filteredEntries.add(e);
            }
        }
        adapter.submitList(filteredEntries);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_entries_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
    }

    private void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Uri selectedDoc = data.getData();
                if (selectedDoc != null) {
                    try (InputStream is = requireContext().getContentResolver().openInputStream(selectedDoc)) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        StringBuilder entries = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            entries.append(line).append(System.lineSeparator());
                        }
                        br.close();
                        projectViewModel.addBibEntry(entries.toString(), repoId);
                    } catch (IOException exception) {
                        Log.e(this.getClass().getName(), "Can't open bibtex file.", exception);
                    }
                }
            }
        }
    }
}