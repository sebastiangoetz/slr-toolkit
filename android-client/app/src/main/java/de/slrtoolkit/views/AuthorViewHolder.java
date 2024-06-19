package de.slrtoolkit.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.slrtoolkit.R;

public class AuthorViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameView;
    private final TextView affilationView;
    private final TextView emailView;
    public AuthorViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.author_item_name);
        affilationView = itemView.findViewById(R.id.author_item_affiliation);
        emailView = itemView.findViewById(R.id.author_item_email);
    }

    static AuthorViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view);
    }

    public void bind(String name, String affiliation, String email) {
        nameView.setText(name);
        affilationView.setText(affiliation);
        emailView.setText(email);
    }
}
