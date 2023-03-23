package de.davidtiede.slrtoolkit.fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.davidtiede.slrtoolkit.MainActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.viewmodels.RepoViewModel;

public class AddProject2Fragment extends Fragment {

    private RepoViewModel repoViewModel;
    private TextInputEditText edittext_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton floatingActionButton = ((MainActivity) requireActivity()).getFloatingActionButton();
        floatingActionButton.setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.fragment_add_project_2, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edittext_name = view.findViewById(R.id.edittext_name);
        view.findViewById(R.id.button_add_project).setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(edittext_name.getText())) {
                Toast.makeText(requireActivity().getApplicationContext(),
                        getString(R.string.toast_empty_name),  Toast.LENGTH_SHORT).show();
                return;
            }

            repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
            Repo repo = repoViewModel.getCurrentRepo();
            repo.setName(Objects.requireNonNull(edittext_name.getText()).toString());
            repoViewModel.update(repo);

            repoViewModel.initializeDataForRepo(repo.getId(), repo.getLocal_path());


            NavHostFragment.findNavController(AddProject2Fragment.this)
                    .navigate(R.id.action_AddProject2Fragment_to_ProjectsFragment);
        });
    }
}