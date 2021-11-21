package de.davidtiede.slrtoolkit.views;

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

public class TaxonomyListAdapter extends ListAdapter<Taxonomy, TaxonomyListAdapter.TaxonomyViewHolder> {
    private TaxonomyListAdapter.RecyclerViewClickListener listener;
    private RecyclerView recyclerView;
    private int repoId;

    public TaxonomyListAdapter(@NonNull DiffUtil.ItemCallback<Taxonomy> diffCallback, TaxonomyListAdapter.RecyclerViewClickListener listener, int repoId) {
        super(diffCallback);
        this.listener = listener;
        this.repoId = repoId;
    }

    @NonNull
    @Override
    public TaxonomyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TaxonomyViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TaxonomyViewHolder holder, int position) {
        Taxonomy current = getItem(position);
        holder.bind(current, listener);
    }

    public static class TaxonomyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView taxonomyItemView;
        private TaxonomyListAdapter.RecyclerViewClickListener listener;

        public TaxonomyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            taxonomyItemView = itemView.findViewById(R.id.textview_recyclerview);
            itemView.setOnClickListener(this);
        }

        public static TaxonomyListAdapter.TaxonomyViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_item, parent, false);
            return new TaxonomyListAdapter.TaxonomyViewHolder(view);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

        public void bind(Taxonomy taxonomy, TaxonomyListAdapter.RecyclerViewClickListener listener) {
            taxonomyItemView.setText(taxonomy.getName());
            this.listener = listener;
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public static class TaxonomyDiff extends DiffUtil.ItemCallback<Taxonomy> {
        @Override
        public boolean areItemsTheSame(@NonNull Taxonomy oldItem, @NonNull Taxonomy newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Taxonomy oldItem, @NonNull Taxonomy newItem) {
            return false;
        }
    }
}
