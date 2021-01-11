package de.davidtiede.slrtoolkit.views;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import de.davidtiede.slrtoolkit.database.Repo;

public class RepoListAdapter extends ListAdapter<Repo, RepoViewHolder> {

    public RepoListAdapter(@NonNull DiffUtil.ItemCallback<Repo> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return RepoViewHolder.create(viewGroup);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        Repo current = getItem(position);
        holder.bind(current.getName());
    }

    public static class RepoDiff extends DiffUtil.ItemCallback<Repo> {

        @Override
        public boolean areItemsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
