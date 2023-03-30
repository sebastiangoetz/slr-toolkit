package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;

public class TaxonomyClassificationListAdapter extends ListAdapter<TaxonomyWithEntries, TaxonomyClassificationListAdapter.TaxonomyClassificationViewHolder> {
    private final TaxonomyClassificationListAdapter.RecyclerViewClickListener listener;
    List<Integer> currentTaxonomyIds;
    boolean showArrows;
    private RecyclerView recyclerView;

    public TaxonomyClassificationListAdapter(@NonNull DiffUtil.ItemCallback<TaxonomyWithEntries> diffCallback, TaxonomyClassificationListAdapter.RecyclerViewClickListener listener, boolean showArrows) {
        super(diffCallback);
        this.listener = listener;
        this.currentTaxonomyIds = new ArrayList<>();
        this.showArrows = showArrows;
    }

    public void setCurrentTaxonomyIds(List<Integer> currentTaxonomyIds) {
        this.currentTaxonomyIds = currentTaxonomyIds;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public Context getContext() {
        return recyclerView.getContext();
    }

    @NonNull
    @Override
    public TaxonomyClassificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TaxonomyClassificationViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TaxonomyClassificationViewHolder holder, int position) {
        TaxonomyWithEntries current = getItem(position);
        holder.bind(current, listener, currentTaxonomyIds, getContext(), showArrows);
    }

    public TaxonomyWithEntries getItemAtPosition(int position) {
        return getItem(position);
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public static class TaxonomyClassificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView taxonomyItemView;
        private final ImageView taxonomyArrowItemView;
        private final ConstraintLayout constraintLayout;
        private TaxonomyClassificationListAdapter.RecyclerViewClickListener listener;

        public TaxonomyClassificationViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            taxonomyItemView = itemView.findViewById(R.id.taxonomy_textview_recyclerview);
            taxonomyArrowItemView = itemView.findViewById(R.id.taxonomy_arrow_textview_recyclerview);
            constraintLayout = itemView.findViewById(R.id.taxonomy_constraint_layout);
            itemView.setOnClickListener(this);
        }

        public static TaxonomyClassificationListAdapter.TaxonomyClassificationViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.taxonomy_classification_recyclerview_item, parent, false);
            return new TaxonomyClassificationListAdapter.TaxonomyClassificationViewHolder(view);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }

        public void bind(TaxonomyWithEntries taxonomy, TaxonomyClassificationListAdapter.RecyclerViewClickListener listener, List<Integer> selectedTaxonomyIds, Context context, boolean showArrows) {
            boolean taxonomyInEntry = false;
            for (int taxonomyId : selectedTaxonomyIds) {
                if (taxonomyId == taxonomy.taxonomy.getTaxonomyId()) {
                    taxonomyInEntry = true;
                    break;
                }
            }
            if (taxonomyInEntry) {
                constraintLayout.setBackgroundColor(Color.LTGRAY);
            } else {
                constraintLayout.setBackgroundColor(Color.WHITE);
            }
            if (taxonomy.taxonomy.isHasChildren() && showArrows) {
                Drawable arrow = ContextCompat.getDrawable(context, R.drawable.arrow);
                taxonomyArrowItemView.setImageDrawable(arrow);
            }
            taxonomyItemView.setText(taxonomy.taxonomy.getName());
            this.listener = listener;
        }
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
