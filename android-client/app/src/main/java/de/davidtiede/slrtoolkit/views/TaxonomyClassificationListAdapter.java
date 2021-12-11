package de.davidtiede.slrtoolkit.views;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;

public class TaxonomyClassificationListAdapter extends ListAdapter<TaxonomyWithEntries, TaxonomyClassificationListAdapter.TaxonomyClassificationViewHolder> {
    private TaxonomyClassificationListAdapter.RecyclerViewClickListener listener;
    private RecyclerView recyclerView;
    private int entryId;

    public TaxonomyClassificationListAdapter(@NonNull DiffUtil.ItemCallback<TaxonomyWithEntries> diffCallback, TaxonomyClassificationListAdapter.RecyclerViewClickListener listener, int entryId) {
        super(diffCallback);
        this.listener = listener;
        this.entryId = entryId;
    }

    @NonNull
    @Override
    public TaxonomyClassificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TaxonomyClassificationViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TaxonomyClassificationViewHolder holder, int position) {
        TaxonomyWithEntries current = getItem(position);
        holder.bind(current, listener, entryId);
    }

    public TaxonomyWithEntries getItemAtPosition(int position) {
        TaxonomyWithEntries taxonomy = getItem(position);
        return taxonomy;
    }

    public static class TaxonomyClassificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView taxonomyItemView;
        private TaxonomyClassificationListAdapter.RecyclerViewClickListener listener;
        private int entryId;

        public TaxonomyClassificationViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            taxonomyItemView = itemView.findViewById(R.id.textview_recyclerview);
            itemView.setOnClickListener(this);
        }

        public static TaxonomyClassificationListAdapter.TaxonomyClassificationViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_item, parent, false);
            return new TaxonomyClassificationListAdapter.TaxonomyClassificationViewHolder(view);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

        public void bind(TaxonomyWithEntries taxonomy, TaxonomyClassificationListAdapter.RecyclerViewClickListener listener, int entryId) {
            //maybe set background color here?
            //loop through entries, set current entry in construtor, check equality
            System.out.println("Binding");
            System.out.println(entryId);
            boolean taxonomyInEntry = false;
            for(Entry entry: taxonomy.entries) {
                System.out.println(entry.getTitle());
                System.out.println(entry.getId());
                if(entry.getId() == entryId) taxonomyInEntry = true;
            }
            if(taxonomyInEntry) {
                taxonomyItemView.setBackgroundColor(Color.BLUE);
            }
            taxonomyItemView.setText(taxonomy.taxonomy.getName());
            this.listener = listener;
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public static class TaxonomyDiff extends DiffUtil.ItemCallback<TaxonomyWithEntries> {
        @Override
        public boolean areItemsTheSame(@NonNull TaxonomyWithEntries oldItem, @NonNull TaxonomyWithEntries newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull TaxonomyWithEntries oldItem, @NonNull TaxonomyWithEntries newItem) {
            return false;
        }
    }
}
