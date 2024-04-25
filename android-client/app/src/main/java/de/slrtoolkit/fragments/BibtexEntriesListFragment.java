package de.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Entry;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
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
                //TODO add bibtex entry to the project
            });
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
            Entry clickedEntry = adapter.getItemAtPosition(position);

            if (clickedEntry == null) return;

            projectViewModel.setCurrentEntryIdForCard(clickedEntry.getEntryId());
            int indexOfEntryInOriginalList = projectViewModel.getCurrentEntriesInList().indexOf(clickedEntry);
            projectViewModel.setCurrentEntryInListCount(indexOfEntryInOriginalList);
            NavHostFragment.findNavController(BibtexEntriesListFragment.this)
                    .navigate(R.id.action_bibtexEntriesListFragment_to_bibtexEntryDetailFragment);
        };
    }

    private void setEntries() {
        projectViewModel.getEntriesForRepo(repoId).observe(getViewLifecycleOwner(), this::onLoaded);
    }

    private void onLoaded(List<Entry> list) {
        projectViewModel.setCurrentEntriesInList(list);
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
        List<Entry> filteredEntries = new ArrayList<>();
        for (Entry e : projectViewModel.getCurrentEntriesInList()) {
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
}