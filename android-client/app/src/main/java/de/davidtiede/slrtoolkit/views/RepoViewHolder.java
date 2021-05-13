package de.davidtiede.slrtoolkit.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.davidtiede.slrtoolkit.R;

class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView projectItemView;
    private RepoListAdapter.RecyclerViewClickListener listener;

    private RepoViewHolder(View itemView) {
        super(itemView);
        projectItemView = itemView.findViewById(R.id.textview_recyclerview);
        itemView.setOnClickListener((View.OnClickListener) this);
    }

    public void bind(String text, RepoListAdapter.RecyclerViewClickListener listener) {
        projectItemView.setText(text);
        this.listener = listener;
    }

    static RepoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition());
    }
}
