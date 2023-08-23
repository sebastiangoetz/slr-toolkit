package de.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.viewmodels.ProjectViewModel;

public class ViewProjectMetadataFragment extends Fragment {
    private EditText textName;
    private EditText textAbstract;
    private ProjectViewModel projectViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_project_metadata, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);

        textName = view.findViewById(R.id.edittext_name);
        final Observer<Repo> repoTitleObserver = repo -> textName.setText(repo.getName());
        projectViewModel.getRepoById(projectViewModel.getCurrentRepoId()).observe(getViewLifecycleOwner(), repoTitleObserver);

        textAbstract = view.findViewById(R.id.edittext_abstract);
        final Observer<Repo> repoAbstractObserver = repo -> textAbstract.setText(repo.getTextAbstract());
        projectViewModel.getRepoById(projectViewModel.getCurrentRepoId()).observe(getViewLifecycleOwner(), repoAbstractObserver);

    }
}
