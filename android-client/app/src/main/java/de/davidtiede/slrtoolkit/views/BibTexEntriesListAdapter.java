package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jbibtex.BibTeXEntry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.davidtiede.slrtoolkit.R;

public class BibTexEntriesListAdapter extends RecyclerView.Adapter<BibTexEntriesListAdapter.BibTexEntriesViewHolder> {
    public ArrayList<BibTeXEntry> bibtexEntries;
    public Context context;
    private RecyclerViewClickListener listener;

    public BibTexEntriesListAdapter(Context context, RecyclerViewClickListener listener, ArrayList<BibTeXEntry> bibtexEntries) {
        this.bibtexEntries = bibtexEntries;
        this.context = context;
        this.listener = listener;
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
        String title = bibtexEntries.get(i).getField(BibTeXEntry.KEY_TITLE).toUserString();
        bibTexEntriesViewHolder.bind(title, listener);
    }

    @Override
    public int getItemCount() {
        return bibtexEntries.size();
    }

    public class BibTexEntriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView bibtexItemView;
        private BibTexEntriesListAdapter.RecyclerViewClickListener listener;

        public BibTexEntriesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            bibtexItemView = itemView.findViewById(R.id.textview_recyclerview);
            itemView.setOnClickListener(this);
        }

        public void bind(String text, BibTexEntriesListAdapter.RecyclerViewClickListener listener) {
            bibtexItemView.setText(text);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
