package de.davidtiede.slrtoolkit.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.davidtiede.slrtoolkit.R;

class RepoViewHolder extends RecyclerView.ViewHolder {
    private final TextView projectItemView;

    private RepoViewHolder(View itemView) {
        super(itemView);
        projectItemView = itemView.findViewById(R.id.textView);
    }

    public void bind(String text) {
        projectItemView.setText(text);
    }

    static RepoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new RepoViewHolder(view);
    }
}
