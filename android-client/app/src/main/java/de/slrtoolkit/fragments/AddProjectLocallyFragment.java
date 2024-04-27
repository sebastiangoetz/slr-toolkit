package de.slrtoolkit.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.slrtoolkit.MainActivity;
import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.viewmodels.RepoViewModel;

public class AddProjectLocallyFragment extends Fragment {
    private RepoViewModel repoViewModel;
    private Button bibDefaultBtn, bibChooseBtn;
    private Button slrDefaultBtn, slrChooseBtn;
    private Button taxonomyDefaultBtn, taxonomyChooseBtn;
    // private TextInputEditText project_text;
    private CardView button_create_project;

    private static String currentType;
    private static boolean isSlrChoosen = false, isBibChoosen = false, isTaxonomyChoosen = false;

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null) {
                            Uri selectedDoc = data.getData();
                            repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
                            Repo repo = repoViewModel.getCurrentRepo();
                            copyDocumentFile(requireContext(), selectedDoc, repo);
                        }
                    }
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton floatingActionButton = ((MainActivity) requireActivity()).getFloatingActionButton();
        floatingActionButton.setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.fragment_add_project_locally, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        slrChooseBtn = view.findViewById(R.id.button_choose_slr);
        bibChooseBtn = view.findViewById(R.id.button_choose_bib);
        taxonomyChooseBtn = view.findViewById(R.id.button_choose_tax);

        slrDefaultBtn = view.findViewById(R.id.button_default_slr);
        bibDefaultBtn = view.findViewById(R.id.button_default_bib);
        taxonomyDefaultBtn = view.findViewById(R.id.button_default_tax);

        button_create_project = view.findViewById(R.id.button_create_project);

        button_create_project.setEnabled(false);
        button_create_project.setAlpha(0.3f);

        slrDefaultBtn.setOnClickListener(view1 -> {
            currentType = ".slrproject";
            repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
            Repo repo = repoViewModel.getCurrentRepo();
            addDefaultFile(repo.getLocal_path(), currentType, """
                    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                    <slrProjectMetainformation>
                        <title></title>
                        <keywords></keywords>
                        <projectAbstract></projectAbstract>
                        <taxonomyDescription></taxonomyDescription>
                        <authorsList>
                            <email></email>
                            <name></name>
                            <organisation></organisation>
                        </authorsList>
                    </slrProjectMetainformation>
                    """);
        });

        slrChooseBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            currentType = ".slrproject";
            resultLauncher.launch(intent);
        });

        bibChooseBtn.setOnClickListener(view1 ->
        {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            currentType = ".bib";
            resultLauncher.launch(intent);
        });
        bibDefaultBtn.setOnClickListener(view1 -> {
            currentType = ".bib";
            repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
            Repo repo = repoViewModel.getCurrentRepo();
            addDefaultFile(repo.getLocal_path(), currentType, "");
        });

        taxonomyChooseBtn.setOnClickListener(view1 ->
        {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            currentType = ".taxonomy";
            resultLauncher.launch(intent);
        });
        taxonomyDefaultBtn.setOnClickListener(view1 -> {
            currentType = ".taxonomy";
            repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
            Repo repo = repoViewModel.getCurrentRepo();
            addDefaultFile(repo.getLocal_path(), currentType, """
                    Venue ,
                    Venue Type\s
                    """);
        });

        button_create_project.setOnClickListener(null);
        Repo repo = new Repo("", "", "", "", "");
        repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);

        int id = (int) repoViewModel.insert(repo);
        try {
            repo = repoViewModel.getRepoDirectly(id);
        } catch (ExecutionException |
                 InterruptedException e) {
            Log.e("slr-toolkit", "onViewCreated: ", e);
        }
        repoViewModel.setCurrentRepo(repo);
        repo.setLocal_path("repo_" + repo.getId());
        repoViewModel.update(repo);

        File repoFolder = new File(requireActivity().getFilesDir(), repo.getLocal_path());
        if (!repoFolder.exists()) {
            if(!repoFolder.mkdir()) {
                Log.e(AddProjectLocallyFragment.class.getName(), "onViewCreated: could not create repo dir", null);
            }
        }
    }

    private void addDefaultFile(String repoFolder, String fileType, String body) {

        File dir = new File(requireContext().getFilesDir(), repoFolder);
        if (!dir.exists()) {
            if(!dir.mkdir()) {
                Log.e("slr-toolkit", "addDefaultFile: couldn't create directory", null);
            }
        }
        try {
            File gpxfile = new File(dir, "temp" + fileType);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(body);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            Log.e("slr-toolkit", "addDefaultFile: ", e);
        }

        if (Objects.equals(currentType, ".slrproject")) {
            isSlrChoosen = true;
            slrDefaultBtn.setEnabled(false);
            slrChooseBtn.setEnabled(false);
            slrDefaultBtn.setOnClickListener(null);
            slrChooseBtn.setOnClickListener(null);
        } else if (Objects.equals(currentType, ".bib")) {
            isBibChoosen = true;
            bibDefaultBtn.setEnabled(false);
            bibDefaultBtn.setOnClickListener(null);
            bibChooseBtn.setEnabled(false);
            bibChooseBtn.setOnClickListener(null);
        } else if (Objects.equals(currentType, ".taxonomy")) {
            isTaxonomyChoosen = true;
            taxonomyDefaultBtn.setEnabled(false);
            taxonomyDefaultBtn.setOnClickListener(null);
            taxonomyChooseBtn.setEnabled(false);
            taxonomyChooseBtn.setOnClickListener(null);
        }

        if (isSlrChoosen && isBibChoosen && isTaxonomyChoosen) {
            button_create_project.setAlpha(1.0f);
            button_create_project.setEnabled(true);
            button_create_project.setOnClickListener(cardview_create_project ->
                    NavHostFragment.findNavController(AddProjectLocallyFragment.this).navigate(R.id.action_CreateProjectLocallyFragment_to_AddProjectSetNameFragment));
        }
    }

    public void copyDocumentFile(Context context, Uri documentUri, Repo repo) {
        ContentResolver contentResolver = context.getContentResolver();
        String fileName = getFileNameFromUri(contentResolver, documentUri);
        if (fileName == null) {
            Log.e("MediaStoreCopyHelper", "Failed to retrieve file name.");
            return;
        }
        try (InputStream inputStream = contentResolver.openInputStream(documentUri)) {
            if(inputStream != null) {
                File internalFile = new File(context.getFilesDir() + File.separator + repo.getLocal_path(), fileName);
                try (OutputStream outputStream = new FileOutputStream(internalFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    Log.e("slr-toolkit", "copyDocumentFile: ", e);
                }
            }
        } catch (IOException e) {
            Log.e("slr-toolkit", "copyDocumentFile: ", e);
        }
        if (Objects.equals(currentType, ".slrproject")) {
            isSlrChoosen = true;
            slrDefaultBtn.setEnabled(false);
            slrChooseBtn.setEnabled(false);
            slrDefaultBtn.setOnClickListener(null);
            slrChooseBtn.setOnClickListener(null);
        } else if (Objects.equals(currentType, ".bib")) {
            isBibChoosen = true;
            bibDefaultBtn.setEnabled(false);
            bibDefaultBtn.setOnClickListener(null);
            bibChooseBtn.setEnabled(false);
            bibChooseBtn.setOnClickListener(null);
        } else if (Objects.equals(currentType, ".taxonomy")) {
            isTaxonomyChoosen = true;
            taxonomyDefaultBtn.setEnabled(false);
            taxonomyDefaultBtn.setOnClickListener(null);
            taxonomyChooseBtn.setEnabled(false);
            taxonomyChooseBtn.setOnClickListener(null);
        }

        if (isSlrChoosen && isBibChoosen && isTaxonomyChoosen) {
            button_create_project.setEnabled(true);
            button_create_project.setOnClickListener(cardview_create_project ->
                    NavHostFragment.findNavController(AddProjectLocallyFragment.this).navigate(R.id.action_CreateProjectLocallyFragment_to_AddProjectSetNameFragment));
        }
    }

    private String getFileNameFromUri(ContentResolver contentResolver, Uri uri) {
        String fileName = null;
        try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                    if (!fileName.contains(currentType)) {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("slr-toolkit", "getFileNameFromUri: ", e);
        }
        return fileName;
    }


}
