package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.davidtiede.slrtoolkit.MainActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.viewmodels.RepoViewModel;
import de.davidtiede.slrtoolkit.worker.CloneWorker;

public class AddProjectFragment extends Fragment {

    private RepoViewModel repoViewModel;
    private TextInputEditText edittext_url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton floatingActionButton = ((MainActivity) requireActivity()).getFloatingActionButton();
        floatingActionButton.setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.fragment_add_project, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edittext_url = view.findViewById(R.id.edittext_url);
        view.findViewById(R.id.button_add_project).setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(edittext_url.getText())) {
                Toast.makeText(requireActivity().getApplicationContext(),
                        getString(R.string.toast_empty_url),  Toast.LENGTH_SHORT).show();
                return;
            }
            Repo repo = new Repo(Objects.requireNonNull(edittext_url.getText()).toString());
            repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);

            repoViewModel.insert(repo);

            WorkRequest cloneWorkRequest =
                    new OneTimeWorkRequest.Builder(CloneWorker.class)
                            .build();
            WorkManager
                    .getInstance(requireActivity().getApplicationContext())
                    .enqueue(cloneWorkRequest);

            NavHostFragment.findNavController(AddProjectFragment.this)
                    .navigate(R.id.action_AddProjectFragment_to_ProjectsFragment);
        });
    }
}