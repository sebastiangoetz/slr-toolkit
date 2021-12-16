package de.davidtiede.slrtoolkit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.davidtiede.slrtoolkit.ClassificationActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BibtexEntryDetailFragment extends Fragment {

    private TextView titleTextView;
    private Button classifyButton;
    private ProjectViewModel projectViewModel;
    private int entryId;
    private int repoId;


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
        classifyButton = view.findViewById(R.id.classify_entry_button);
        projectViewModel = new ViewModelProvider(getActivity()).get(ProjectViewModel.class);

        entryId = projectViewModel.getCurrentEntryIdForCard();
        repoId = projectViewModel.getCurrentRepoId();

        projectViewModel.getEntryById(entryId).observe(getViewLifecycleOwner(), entry -> {
            titleTextView.setText(entry.getTitle());
        });

        classifyButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ClassificationActivity.class);
            intent.putExtra("repo", repoId);
            intent.putExtra("entry", entryId);
            startActivity(intent);
            ((Activity) getActivity()).overridePendingTransition(0, 0);
        });
    }
}