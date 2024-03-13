package de.slrtoolkit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Author;
import de.slrtoolkit.database.Keyword;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.repositories.AuthorRepository;
import de.slrtoolkit.repositories.KeywordRepository;
import de.slrtoolkit.repositories.OnDeleteCompleteListener;
import de.slrtoolkit.util.FileUtil;
import de.slrtoolkit.util.SlrprojectParser;
import de.slrtoolkit.viewmodels.ProjectViewModel;
import de.slrtoolkit.viewmodels.RepoViewModel;
import de.slrtoolkit.views.AuthorListAdapter;
import de.slrtoolkit.views.KeywordListAdapter;

public class ViewProjectMetadataFragment extends Fragment {
    private EditText textName;
    private EditText textAbstract;
    private ProjectViewModel projectViewModel;
    private RepoViewModel repoViewModel;
    private KeywordRepository keywordRepository;
    private AuthorRepository authorRepository;
    private KeywordListAdapter keywordListAdapter;
    private AuthorListAdapter authorListAdapter;
    private Button updateMetadata;
    private FloatingActionButton plusKeyword;
    private FloatingActionButton plusAuthor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keywordRepository = new KeywordRepository(getActivity().getApplication());
        authorRepository = new AuthorRepository(getActivity().getApplication());
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
        try {
            repoViewModel.setCurrentRepo(repoViewModel.getRepoDirectly(projectViewModel.getCurrentRepoId()));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Repo currentRepository = repoViewModel.getCurrentRepo();
        updateMetadata = view.findViewById(R.id.button_edit_metadata);
        plusKeyword = view.findViewById(R.id.plus_keyword);
        plusAuthor = view.findViewById(R.id.plus_author);

        keywordListAdapter = new KeywordListAdapter(getActivity().getApplication(),getActivity().getSupportFragmentManager(), keywordRepository, new KeywordListAdapter.KeywordsDiff());
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

        plusKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateKeywordDialog dialog = new CreateKeywordDialog();
                dialog.show(getChildFragmentManager(), CreateKeywordDialog.TAG);

                List<Keyword> keywords = keywordListAdapter.getCurrentList();
                if (!(keywords.isEmpty()))
                    if (keywords.get(0).getName().equals("")) {
                        keywordRepository.deleteAsync(keywords.get(0), new OnDeleteCompleteListener() {
                            @Override
                            public void onDeleteComplete() {

                            }
                        });
                    }
            }
        });

        plusAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAuthorDialog dialog = new CreateAuthorDialog();
                dialog.show(getChildFragmentManager(), CreateAuthorDialog.TAG);
                List<Author> authors = authorListAdapter.getCurrentList();
                if (!(authors.isEmpty()))
                    if (authors.get(0).getName().equals("")) {
                        authorRepository.deleteAsync(authors.get(0), new OnDeleteCompleteListener() {
                            @Override
                            public void onDeleteComplete() {

                            }
                        });
                    }
            }
        });

        updateMetadata.setOnClickListener(view1 -> {
            currentRepository.setName(textName.getText().toString());
            currentRepository.setTextAbstract(textAbstract.getText().toString());
            repoViewModel.update(currentRepository);

            SlrprojectParser slrprojectParser = new SlrprojectParser();
            FileUtil fileUtil = new FileUtil();

            File file = fileUtil.accessFiles(currentRepository.getLocal_path(), getActivity().getApplication(), ".slrproject");

            slrprojectParser.parseSlr(String.valueOf(file), currentRepository.getName(), currentRepository.getTextAbstract());


            NavHostFragment.findNavController(
                            Objects.requireNonNull(ViewProjectMetadataFragment.this))
                    .navigate(R.id.action_editProjectMetadata_to_projectOverview);
        });
    }

    private void onKeywordsLoaded(List<Keyword> keywordList) {
        keywordListAdapter.submitList(keywordList);
    }

    private void onAuthorsLoaded(List<Author> authorList) {
        authorListAdapter.submitList(authorList);
    }


}
