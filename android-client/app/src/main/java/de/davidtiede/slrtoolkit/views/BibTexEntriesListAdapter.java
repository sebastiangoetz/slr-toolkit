package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

public class BibTexEntriesListAdapter extends ListAdapter<Entry, BibTexEntriesListAdapter.BibTexEntriesViewHolder> {
    private final RecyclerViewClickListener listener;
    private RecyclerView recyclerView;
    private final int repoId;

    public BibTexEntriesListAdapter(@NonNull DiffUtil.ItemCallback<Entry> diffCallback, RecyclerViewClickListener listener, int repoId) {
        super(diffCallback);
        this.listener = listener;
        this.repoId = repoId;
    }
    @NonNull
    @NotNull
    @Override
    public BibTexEntriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return BibTexEntriesViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BibTexEntriesViewHolder bibTexEntriesViewHolder, int i) {
        Entry current = getItem(i);
        bibTexEntriesViewHolder.bind(current, listener);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public Context getContext() {
        return recyclerView.getContext();
    }

    public void deleteItem(int i) {
        Entry deletedEntry = getItem(i);
        ProjectViewModel projectViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(ProjectViewModel.class);
        projectViewModel.delete(deletedEntry, repoId);
        notifyItemRemoved(i);
    }

    public Entry getItemAtPosition(int position) {
        if(position > getCurrentList().size()) return null;
        return getItem(position);
    }

    public static class BibTexEntriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView bibtexItemView;
        private final TextView bibtexDescriptionItemView;
        private BibTexEntriesListAdapter.RecyclerViewClickListener listener;

        public BibTexEntriesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            bibtexItemView = itemView.findViewById(R.id.textview_recyclerview);
            bibtexDescriptionItemView = itemView.findViewById(R.id.description_textview_recyclerview);
            itemView.setOnClickListener(this);
        }

        public static BibTexEntriesViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bibtex_recyclerview_item, parent, false);
            return new BibTexEntriesViewHolder(view);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

        public void bind(Entry entry, RecyclerViewClickListener listener) {
            bibtexItemView.setText(entry.getTitle());
            String description = entry.getYear();
            if(!entry.getYear().equals("")) {
                description = entry.getYear() + ", ";
            }
            description += entry.getAuthor();
            bibtexDescriptionItemView.setText(description);
            this.listener = listener;
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public static class EntryDiff extends DiffUtil.ItemCallback<Entry> {
        @Override
        public boolean areItemsTheSame(@NonNull Entry oldItem, @NonNull Entry newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Entry oldItem, @NonNull Entry newItem) {
            return false;
        }
    }
}
