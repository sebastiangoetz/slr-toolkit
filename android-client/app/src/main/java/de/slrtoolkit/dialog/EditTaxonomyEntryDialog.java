package de.slrtoolkit.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import de.slrtoolkit.R;

public class EditTaxonomyEntryDialog extends DialogFragment {
    private String origName;

    public EditTaxonomyEntryDialog(String origName) {
        this.origName = origName;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_edit_taxonomy_entry, null))
                .setMessage("Edit Taxonomy Entry")
                .setPositiveButton("Edit", null)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Delete", null);
        return builder.create(); //TODO find out how to send a value to the dialog
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText t = view.findViewById(R.id.edittext_taxonomy_entry_name);
        t.setText(origName);
    }
}
