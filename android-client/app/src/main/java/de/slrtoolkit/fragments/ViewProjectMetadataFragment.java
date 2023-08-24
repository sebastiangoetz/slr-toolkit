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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Author;
import de.slrtoolkit.database.Keyword;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.viewmodels.ProjectViewModel;
import de.slrtoolkit.viewmodels.RepoViewModel;
import de.slrtoolkit.views.AuthorListAdapter;
import de.slrtoolkit.views.KeywordListAdapter;

public class ViewProjectMetadataFragment extends Fragment {
    private EditText textName;
    private EditText textAbstract;
    private ProjectViewModel projectViewModel;
    private RepoViewModel repoViewModel;
    private KeywordListAdapter keywordListAdapter;
    private AuthorListAdapter authorListAdapter;
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
        super.onViewCreated(view, savedInstanceState);

        projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);
        repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);

        keywordListAdapter = new KeywordListAdapter(new KeywordListAdapter.KeywordsDiff());
        RecyclerView keywordsRecycler = view.findViewById(R.id.list_keywords);
        LinearLayoutManager lm = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        keywordsRecycler.setLayoutManager(lm);
        keywordsRecycler.setAdapter(keywordListAdapter);

        authorListAdapter = new AuthorListAdapter(new AuthorListAdapter.AuhtorsDiff());
        RecyclerView authorsRecycler = view.findViewById(R.id.list_authors);
        LinearLayoutManager lm2 = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        authorsRecycler.setLayoutManager(lm2);
        authorsRecycler.setAdapter(authorListAdapter);

        textName = view.findViewById(R.id.edittext_name);
        final Observer<Repo> repoTitleObserver = repo -> textName.setText(repo.getName());
        projectViewModel.getRepoById(projectViewModel.getCurrentRepoId()).observe(getViewLifecycleOwner(), repoTitleObserver);

        textAbstract = view.findViewById(R.id.edittext_abstract);
        final Observer<Repo> repoAbstractObserver = repo -> textAbstract.setText(repo.getTextAbstract());
        projectViewModel.getRepoById(projectViewModel.getCurrentRepoId()).observe(getViewLifecycleOwner(), repoAbstractObserver);

        projectViewModel.getKeywordsForCurrentProject().observe(getViewLifecycleOwner(), this::onKeywordsLoaded);
        projectViewModel.getAuthorsForCurrentProject().observe(getViewLifecycleOwner(), this::onAuthorsLoaded);
    }

    private void onKeywordsLoaded(List<Keyword> keywordList) {
        keywordListAdapter.submitList(keywordList);
    }

    private void onAuthorsLoaded(List<Author> authorList) {
        authorListAdapter.submitList(authorList);
    }
}
