package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Repo;
import de.davidtiede.slrtoolkit.viewmodels.RepoViewModel;

public class RepoListAdapter extends ListAdapter<Repo, RepoViewHolder> {

    private RecyclerView recyclerView;
    private RecyclerViewClickListener listener;

    public RepoListAdapter(@NonNull DiffUtil.ItemCallback<Repo> diffCallback, RecyclerViewClickListener listener) {

        super(diffCallback);
        this.listener = listener;
    }

    @NonNull @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return RepoViewHolder.create(viewGroup);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        Repo current = getItem(position);
        holder.bind(current.getName(), listener);
    }

    public static class RepoDiff extends DiffUtil.ItemCallback<Repo> {

        @Override
        public boolean areItemsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public Context getContext() {
        return recyclerView.getContext();
    }

    public void deleteItem(int position) {
        Repo recentlyDeletedRepo = getItem(position);
        RepoViewModel repoViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(RepoViewModel.class);
        repoViewModel.delete(recentlyDeletedRepo);
        notifyItemRemoved(position);
        //notifyItemRangeRemoved(position, 1);
        //notifyDataSetChanged();

        Snackbar snackbar = Snackbar.make(recyclerView, R.string.snackbar_undo, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snackbar_undo, v -> undoDelete(recentlyDeletedRepo, position));
        snackbar.show();
    }

    private void undoDelete(Repo recentlyDeletedRepo, int position) {
        RepoViewModel repoViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(RepoViewModel.class);
        repoViewModel.insert(recentlyDeletedRepo);
        notifyItemInserted(position);
    }

    public Repo getItemAtPosition(int position) {
        Repo repo = getItem(position);
        return repo;
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
