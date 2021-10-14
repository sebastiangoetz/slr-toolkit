package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.davidtiede.slrtoolkit.R;

public class BibTexEntriesListAdapter extends RecyclerView.Adapter<BibTexEntriesListAdapter.BibTexEntriesViewHolder> {
    ArrayList<String> titles;
    Context context;

    public BibTexEntriesListAdapter(Context context, ArrayList<String> titles) {
        this.titles = titles;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public BibTexEntriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context)
                    .inflate(R.layout.bibtex_recyclerview_item, parent, false);
        return new BibTexEntriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BibTexEntriesViewHolder bibTexEntriesViewHolder, int i) {
        bibTexEntriesViewHolder.bibtexItemView.setText(titles.get(i));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class BibTexEntriesViewHolder extends RecyclerView.ViewHolder{
        TextView bibtexItemView;

        public BibTexEntriesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            bibtexItemView = itemView.findViewById(R.id.textview_recyclerview);
        }
    }
}
