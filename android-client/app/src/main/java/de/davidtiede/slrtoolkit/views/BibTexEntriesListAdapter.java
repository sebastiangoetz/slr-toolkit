package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXObject;
import org.jbibtex.Key;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.util.BibTexParser;

public class BibTexEntriesListAdapter extends RecyclerView.Adapter<BibTexEntriesListAdapter.BibTexEntriesViewHolder> {
    public Map<Key, BibTeXEntry> bibtexEntries;
    public Context context;
    private RecyclerViewClickListener listener;
    private RecyclerView recyclerView;

    public BibTexEntriesListAdapter(Context context, RecyclerViewClickListener listener, Map<Key, BibTeXEntry> bibtexEntries) {
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
        Map.Entry<Key, BibTeXEntry> mapEntry = (Map.Entry<Key, BibTeXEntry>) bibtexEntries.entrySet().toArray()[i];
        BibTeXEntry entry = mapEntry.getValue();
        String title = entry.getField(BibTeXEntry.KEY_TITLE).toUserString();
        bibTexEntriesViewHolder.bind(title, listener);
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
        Map.Entry<Key, BibTeXEntry> mapEntry = (Map.Entry<Key, BibTeXEntry>) bibtexEntries.entrySet().toArray()[i];
        BibTeXObject deleteObject = (BibTeXObject) mapEntry.getValue();
        BibTexParser parser = BibTexParser.getBibTexParser();
        parser.removeObject(deleteObject);
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
