package de.slrtoolkit.fragments;

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

import java.io.File;

import de.slrtoolkit.MainActivity;
import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.util.FileUtil;
import de.slrtoolkit.util.SlrprojectParser;
import de.slrtoolkit.viewmodels.RepoViewModel;

public class AddProjectSetNameFragment extends Fragment {

    private RepoViewModel repoViewModel;
    private TextInputEditText edittext_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton floatingActionButton = ((MainActivity) requireActivity()).getFloatingActionButton();
        floatingActionButton.setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.fragment_add_project_set_name, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeFromFiles();

        edittext_name = view.findViewById(R.id.edittext_name);
        edittext_name.setText(repoViewModel.getCurrentRepo().getName());

        view.findViewById(R.id.button_add_project).setOnClickListener(view1 -> {
            if (TextUtils.isEmpty(edittext_name.getText())) {
                Toast.makeText(requireActivity().getApplicationContext(),
                        getString(R.string.toast_empty_name), Toast.LENGTH_SHORT).show();
                return;
            }

            repoViewModel.getCurrentRepo().setName(edittext_name.getText().toString());
            repoViewModel.update(repoViewModel.getCurrentRepo());

            Repo currentRepo = repoViewModel.getCurrentRepo();

            SlrprojectParser slrprojectParser = new SlrprojectParser();
            FileUtil fileUtil = new FileUtil();
            File file = fileUtil.accessFiles(repoViewModel.getCurrentRepo().getLocal_path(), getActivity().getApplication(), ".slrproject");
            slrprojectParser.parseSlr(String.valueOf(file), currentRepo.getName(), currentRepo.getTextAbstract());


            NavHostFragment.findNavController(AddProjectSetNameFragment.this)
                    .navigate(R.id.action_AddProjectSetNameFragment_to_ProjectsFragment);
        });
    }

    private void initializeFromFiles() {
        repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
        Repo repo = repoViewModel.getCurrentRepo();
        repoViewModel.initializeDataForRepo(repo.getId(), repo.getLocal_path());
    }
}