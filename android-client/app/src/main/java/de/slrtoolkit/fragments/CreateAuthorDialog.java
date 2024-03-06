package de.slrtoolkit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Author;
import de.slrtoolkit.repositories.AuthorRepository;
import de.slrtoolkit.viewmodels.RepoViewModel;

public class CreateAuthorDialog extends DialogFragment {
    public static String TAG = "CreateAuthorDialog";
    private Button createButton;
    private TextInputEditText editAuthorName;
    private TextInputEditText editAuthorAffilation;
    private TextInputEditText editAuthorEmail;
    private RepoViewModel repoViewModel;
    private AuthorRepository authorRepository;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
        authorRepository = new AuthorRepository(getActivity().getApplication());

        View view1 = getLayoutInflater().inflate(R.layout.create_author_dialog, null);
        editAuthorName = view1.findViewById(R.id.editname_author);
        editAuthorAffilation = view1.findViewById(R.id.editaffilation_author);
        editAuthorEmail = view1.findViewById(R.id.editemail_author);
        createButton = view1.findViewById(R.id.button_create_author);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Author author = new Author(editAuthorName.getText().toString(), editAuthorAffilation.getText().toString(), editAuthorEmail.getText().toString());
                author.setRepoId(repoViewModel.getCurrentRepo().getId());
                authorRepository.insert(author);
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        return builder
                .setMessage("Create new author")
                .setView(view1)
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create();
    }
}
