package de.slrtoolkit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import de.slrtoolkit.MainActivity;
import de.slrtoolkit.ProjectActivity;
import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.viewmodels.RepoViewModel;
import de.slrtoolkit.views.RepoListAdapter;
import de.slrtoolkit.views.SwipeToDeleteCallback;

public class ProjectsFragment extends Fragment {
    private TextView textview_no_project;
    private RepoListAdapter repoListAdapter;
    private RepoListAdapter.RecyclerViewClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton floatingActionButton = ((MainActivity) requireActivity()).getFloatingActionButton();
        floatingActionButton.setVisibility(View.VISIBLE);

        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setOnClickListener();
        textview_no_project = view.findViewById(R.id.textview_no_project);
        repoListAdapter = new RepoListAdapter(new RepoListAdapter.RepoDiff(), listener);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(repoListAdapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(repoListAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        RepoViewModel repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
        repoViewModel.getAllRepos().observe(getViewLifecycleOwner(), this::onLoaded);
    }

    private void setOnClickListener() {
        listener = (v, position) -> {
            Intent intent = new Intent(getActivity(), ProjectActivity.class);
            Repo clickedRepo = repoListAdapter.getItemAtPosition(position);
            intent.putExtra("repo", clickedRepo.getId());
            startActivity(intent);
        };
    }

    private void onLoaded(List<Repo> list) {
        if (list.isEmpty()) {
            textview_no_project.setVisibility(View.VISIBLE);
        } else {
            textview_no_project.setVisibility(View.INVISIBLE);
            repoListAdapter.submitList(list);
        }
    }
}