package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Taxonomy;

public class TaxonomyListAdapter extends ListAdapter<Taxonomy, TaxonomyListAdapter.TaxonomyViewHolder> {
    private final TaxonomyListAdapter.RecyclerViewClickListener listener;
    private RecyclerView recyclerView;

    public TaxonomyListAdapter(@NonNull DiffUtil.ItemCallback<Taxonomy> diffCallback, TaxonomyListAdapter.RecyclerViewClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaxonomyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TaxonomyViewHolder.create(parent);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public Context getContext() {
        return recyclerView.getContext();
    }

    @Override
    public void onBindViewHolder(@NonNull TaxonomyViewHolder holder, int position) {
        Taxonomy current = getItem(position);
        holder.bind(current, listener, getContext());
    }

    public Taxonomy getItemAtPosition(int position) {
        return getItem(position);
    }

    public static class TaxonomyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView taxonomyItemView;
        private final ImageView taxonomyArrowItemView;
        private TaxonomyListAdapter.RecyclerViewClickListener listener;

        public TaxonomyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            taxonomyItemView = itemView.findViewById(R.id.taxonomy_textview_recyclerview);
            taxonomyArrowItemView = itemView.findViewById(R.id.taxonomy_arrow_textview_recyclerview);
            itemView.setOnClickListener(this);
        }

        public static TaxonomyListAdapter.TaxonomyViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.taxonomy_classification_recyclerview_item, parent, false);
            return new TaxonomyListAdapter.TaxonomyViewHolder(view);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

        public void bind(Taxonomy taxonomy, TaxonomyListAdapter.RecyclerViewClickListener listener, Context context) {
            taxonomyItemView.setText(taxonomy.getName());
            if(!taxonomy.isHasChildren()) {
                Drawable arrow = ContextCompat.getDrawable(context, R.drawable.arrow);
                taxonomyArrowItemView.setImageDrawable(arrow);
            }
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
