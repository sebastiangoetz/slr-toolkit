package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.davidtiede.slrtoolkit.MainActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.viewmodels.RepoViewModel;
import de.davidtiede.slrtoolkit.views.RepoListAdapter;

public class ProjectsFragment extends Fragment {

    private RepoViewModel repoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton floatingActionButton = ((MainActivity) requireActivity()).getFloatingActionButton();
        floatingActionButton.setVisibility(View.VISIBLE);

        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textview_no_project = view.findViewById(R.id.textview_no_project);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        final RepoListAdapter adapter = new RepoListAdapter(new RepoListAdapter.RepoDiff());

        if (adapter.getItemCount() == 1) { // TODO: Fix: getItemCount always 0
            recyclerView.setVisibility(View.INVISIBLE);
            textview_no_project.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            textview_no_project.setVisibility(View.INVISIBLE);
        }
        recyclerView.setAdapter(adapter);
        repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
        repoViewModel.getAllRepos().observe(getViewLifecycleOwner(), adapter::submitList);
    }
}