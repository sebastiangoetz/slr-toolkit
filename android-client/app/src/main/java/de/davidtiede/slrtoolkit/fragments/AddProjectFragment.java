package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.worker.CloneWorker;

public class AddProjectFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_add_project, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_add_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkRequest cloneWorkRequest =
                        new OneTimeWorkRequest.Builder(CloneWorker.class)
                                .build();
                WorkManager
                        .getInstance(requireActivity().getApplicationContext())
                        .enqueue(cloneWorkRequest);

                NavHostFragment.findNavController(AddProjectFragment.this)
                        .navigate(R.id.action_AddProjectFragment_to_ProjectsFragment);
            }
        });
    }
}