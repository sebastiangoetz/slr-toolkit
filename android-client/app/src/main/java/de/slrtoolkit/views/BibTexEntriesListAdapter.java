package de.slrtoolkit.views;

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

import de.slrtoolkit.R;
import de.slrtoolkit.database.BibEntry;
import de.slrtoolkit.viewmodels.ProjectViewModel;

public class BibTexEntriesListAdapter extends ListAdapter<BibEntry, BibTexEntriesListAdapter.BibTexEntriesViewHolder> {
    private final RecyclerViewClickListener listener;
    private final int repoId;
    private RecyclerView recyclerView;

    public BibTexEntriesListAdapter(@NonNull DiffUtil.ItemCallback<BibEntry> diffCallback, RecyclerViewClickListener listener, int repoId) {
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
        BibEntry current = getItem(i);
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
        BibEntry deletedBibEntry = getItem(i);
        ProjectViewModel projectViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(ProjectViewModel.class);
        projectViewModel.delete(deletedBibEntry, repoId);
        notifyItemRemoved(i);
    }

    public BibEntry getItemAtPosition(int position) {
        if (position > getCurrentList().size()) return null;
        return getItem(position);
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public static class BibTexEntriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
                    .inflate(R.layout.item_bibtex, parent, false);
            return new BibTexEntriesViewHolder(view);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

        public void bind(BibEntry bibEntry, RecyclerViewClickListener listener) {
            bibtexItemView.setText(bibEntry.getTitle());
            String description = bibEntry.getYear();
            if (!bibEntry.getYear().isEmpty()) {
                description = bibEntry.getYear() + ", ";
            }
            description += bibEntry.getAuthor();
            bibtexDescriptionItemView.setText(description);
            this.listener = listener;
        }
    }

    public static class EntryDiff extends DiffUtil.ItemCallback<BibEntry> {
        @Override
        public boolean areItemsTheSame(@NonNull BibEntry oldItem, @NonNull BibEntry newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull BibEntry oldItem, @NonNull BibEntry newItem) {
            return false;
        }
    }
}
