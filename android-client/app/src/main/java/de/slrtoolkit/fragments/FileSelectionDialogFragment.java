package de.slrtoolkit.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Repo;
import de.slrtoolkit.viewmodels.AnalyzeViewModel;
import de.slrtoolkit.viewmodels.RepoViewModel;

public class FileSelectionDialogFragment extends DialogFragment {
    public static String TAG = "FileSelectionDialog";
   // private RecyclerView recyclerView;
    private Button addDefaultFileButton;
    private Button addFileButton;
    private RepoViewModel repoViewModel;

    private Button sample_bib_btn;
    private Button sample_slrproj_btn;
    private Button sample_taxonomy_btn;
    private CardView button_create_project;
    private static String currentType;

    private static boolean isSlrChoosen = false, isBibChoosen = false, isTaxonomyChoosen = false;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

//        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            Intent data = result.getData();
//                            Uri selectedDoc = data.getData();
//                            repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
//                            Repo repo = repoViewModel.getCurrentRepo();
//                            copyDocumentFile(getContext(), selectedDoc, repo);
//
//                        }
//                    }
//                });


        Dialog dialog = new Dialog(this.getContext());
//        dialog.setContentView(R.layout.add_file_dialog);
//        addFileButton = dialog.findViewById(R.id.button_add_file);
//        addDefaultFileButton = dialog.findViewById(R.id.button_add_default_file);
//        sample_slrproj_btn = dialog.findViewById(R.id.button_choose_slrproject);
//        sample_bib_btn = dialog.findViewById(R.id.button_choose_bib);
//        sample_taxonomy_btn = dialog.findViewById(R.id.button_choose_taxonomy);
//        button_create_project = dialog.findViewById(R.id.button_create_project);
//
//
//        addFileButton.setOnClickListener(view2 -> {
//            dismiss();
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.setType("*/*");
//            currentType = ".slrproject";
//            resultLauncher.launch(intent);
//        });
//        addDefaultFileButton.setOnClickListener(view2 -> {
//            dismiss();
//            Uri selectedDoc = Uri.fromFile(new File(getContext().getFilesDir() + File.separator + "temp.slrproject"));
//            repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
//            Repo repo = repoViewModel.getCurrentRepo();
//            copyDocumentFile(getContext(), selectedDoc, repo);
//        });

        return dialog;
    }

//    public void copyDocumentFile(Context context, Uri documentUri, Repo repo) {
//        ContentResolver contentResolver = context.getContentResolver();
//        String fileName = getFileNameFromUri(contentResolver, documentUri);
//        if (fileName == null) {
//            Log.e("MediaStoreCopyHelper", "Failed to retrieve file name.");
//            return;
//        }
//        try (InputStream inputStream = contentResolver.openInputStream(documentUri)) {
//
//            File internalFile = new File(context.getFilesDir() + File.separator + repo.getLocal_path(), fileName);
//            try (OutputStream outputStream = new FileOutputStream(internalFile)) {
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (Objects.equals(currentType, ".slrproject")) {
//            isSlrChoosen = true;
//            sample_slrproj_btn.setEnabled(false);
//            sample_slrproj_btn.setOnClickListener(null);
//        } else if (Objects.equals(currentType, ".bib")) {
//            isBibChoosen = true;
//            sample_bib_btn.setEnabled(false);
//            sample_bib_btn.setOnClickListener(null);
//        } else if (Objects.equals(currentType, ".taxonomy")) {
//            isTaxonomyChoosen = true;
//            sample_taxonomy_btn.setEnabled(false);
//            sample_taxonomy_btn.setOnClickListener(null);
//        }
//
//        if (isSlrChoosen && isBibChoosen && isTaxonomyChoosen) {
//            button_create_project.setEnabled(true);
//            button_create_project.setOnClickListener(cardview_create_project ->
//                    NavHostFragment.findNavController(FileSelectionDialogFragment.this).navigate(R.id.action_CreateProjectFragment_to_AddProject2Fragment));
//        }
//    }
//
//    private String getFileNameFromUri(ContentResolver contentResolver, Uri uri) {
//        String fileName = null;
//        try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
//            if (cursor != null && cursor.moveToFirst()) {
//                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                if (nameIndex != -1) {
//                    fileName = cursor.getString(nameIndex);
//                    if (!fileName.contains(currentType)) {
//                        return null;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return fileName;
//    }



}
