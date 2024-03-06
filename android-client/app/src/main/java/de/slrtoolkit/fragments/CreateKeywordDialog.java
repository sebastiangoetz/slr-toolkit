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
import de.slrtoolkit.database.Keyword;
import de.slrtoolkit.repositories.KeywordRepository;
import de.slrtoolkit.viewmodels.RepoViewModel;

public class CreateKeywordDialog extends DialogFragment {
    public static String TAG = "CreateKeywordDialog";
    private Button createButton;
    private TextInputEditText editKeywordName;
    private RepoViewModel repoViewModel;
    private KeywordRepository keywordRepository;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        repoViewModel = new ViewModelProvider(requireActivity()).get(RepoViewModel.class);
        keywordRepository = new KeywordRepository(getActivity().getApplication());

        View view1 = getLayoutInflater().inflate(R.layout.create_keyword_dialog, null);
        editKeywordName = view1.findViewById(R.id.editkeyword_name);
        createButton= view1.findViewById(R.id.button_create_keyword);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyword keyword = new Keyword(editKeywordName.getText().toString());
                keyword.setRepoId(repoViewModel.getCurrentRepo().getId());
                keywordRepository.insert(keyword);
                dismiss();
            }
        });
        AlertDialog.Builder builder= new AlertDialog.Builder(requireContext());
        return builder
                .setMessage("Create new keyword")
                .setView(view1)
                .setNegativeButton("Cancel", (dialog, which)->{
                    dialog.dismiss();
                })
                .create();
    }
}
