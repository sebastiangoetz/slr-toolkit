package de.slrtoolkit.views;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import de.slrtoolkit.database.Keyword;
import de.slrtoolkit.database.Repo;

public class KeywordListAdapter extends ListAdapter<Keyword, KeywordViewHolder> {
    private RecyclerView recyclerView;

    public KeywordListAdapter(@NonNull DiffUtil.ItemCallback<Keyword> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public KeywordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return KeywordViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull KeywordViewHolder holder, int position) {
        Keyword current = getItem(position);
        holder.bind(current.getName());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public static class KeywordsDiff extends DiffUtil.ItemCallback<Keyword> {

        @Override
        public boolean areItemsTheSame(@NonNull Keyword oldItem, @NonNull Keyword newItem) {
            return oldItem.getKeywordId() == newItem.getKeywordId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Keyword oldItem, @NonNull Keyword newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
