package de.slrtoolkit.views;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import de.slrtoolkit.database.Author;
import de.slrtoolkit.database.Keyword;

public class AuthorListAdapter extends ListAdapter<Author, AuthorViewHolder> {
    private RecyclerView recyclerView;

    public AuthorListAdapter(@NonNull DiffUtil.ItemCallback<Author> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return AuthorViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author current = getItem(position);
        holder.bind(current.getName(), current.getAffilation(), current.getEmail());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public static class AuhtorsDiff extends DiffUtil.ItemCallback<Author> {

        @Override
        public boolean areItemsTheSame(@NonNull Author oldItem, @NonNull Author newItem) {
            return oldItem.getAuthorId() == newItem.getAuthorId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Author oldItem, @NonNull Author newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
