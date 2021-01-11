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

import java.util.List;

import de.davidtiede.slrtoolkit.MainActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.viewmodels.RepoViewModel;
import de.davidtiede.slrtoolkit.views.RepoListAdapter;

public class ProjectsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textview_no_project;
    private RepoListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton floatingActionButton = ((MainActivity) requireActivity()).getFloatingActionButton();
        floatingActionButton.setVisibility(View.VISIBLE);

        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textview_no_project = view.findViewById(R.id.textview_no_project);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        adapter = new RepoListAdapter(new RepoListAdapter.RepoDiff());
        recyclerView.setAdapter(adapter);

        RepoViewModel repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
        repoViewModel.getAllRepos().observe(getViewLifecycleOwner(), this::onLoaded);
    }

    public void onLoaded(List<Repo> list){
        if (list.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            textview_no_project.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            textview_no_project.setVisibility(View.INVISIBLE);
            adapter.submitList(list);
        }
    }
}