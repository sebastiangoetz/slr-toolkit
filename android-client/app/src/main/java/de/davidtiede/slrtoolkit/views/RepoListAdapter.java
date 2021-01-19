package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.viewmodels.RepoViewModel;

public class RepoListAdapter extends ListAdapter<Repo, RepoViewHolder> {

    private Context context;

    public RepoListAdapter(@NonNull DiffUtil.ItemCallback<Repo> diffCallback) {
        super(diffCallback);
    }

    @NonNull @Override
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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    public Context getContext() {
        return context;
    }

    public void deleteItem(int position) {
        Repo recentlyDeletedItem = getItem(position);
        int recentlyDeletedItemPosition = position;
        RepoViewModel repoViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(RepoViewModel.class);
        repoViewModel.delete(recentlyDeletedItem);
        notifyItemRemoved(position);
        //showUndoSnackbar();
    }
}
