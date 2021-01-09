package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.viewmodels.RepoViewModel;
import de.davidtiede.slrtoolkit.views.RepoListAdapter;

public class ProjectsFragment extends Fragment {

    private RepoViewModel repoViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        final RepoListAdapter adapter = new RepoListAdapter(new RepoListAdapter.RepoDiff());
        recyclerView.setAdapter(adapter);

        repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);

        repoViewModel.getAllRepos().observe(getViewLifecycleOwner(), repos -> {
            adapter.submitList(repos);
        });


        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProjectsFragment.this)
                        .navigate(R.id.action_ProjectsFragment_to_AddProjectFragment);
            }
        });
    }
}