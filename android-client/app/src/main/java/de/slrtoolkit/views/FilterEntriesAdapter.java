package de.slrtoolkit.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.slrtoolkit.R;
import de.slrtoolkit.database.Entry;

public class FilterEntriesAdapter extends ArrayAdapter<Entry> {
    private final ArrayList<Entry> entries;

    public FilterEntriesAdapter(@NonNull Context context, @NonNull ArrayList<Entry> entries) {
        super(context, 0, entries);
        this.entries = entries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View converView, @NonNull ViewGroup parent) {
        View listItem = converView;
        if (listItem == null) {
            listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_bibtex_entry_detail, parent, false);
        }

        Entry currentEntry = entries.get(position);

        TextView titleTextView = listItem.findViewById(R.id.bibtex_entry_title);
        TextView authorTextView = listItem.findViewById(R.id.bibtex_entry_authors);
        TextView doiTextView = listItem.findViewById(R.id.bibtex_entry_doi);
        TextView keywordsTextView = listItem.findViewById(R.id.bibtex_entry_keywords);
        TextView yearTextView = listItem.findViewById(R.id.bibtex_entry_year);
        TextView monthTextView = listItem.findViewById(R.id.bibtex_entry_month);
        TextView abstractTextView = listItem.findViewById(R.id.bibtex_entry_abstract);
        TextView publishedTextView = listItem.findViewById(R.id.bibtex_entry_published);
        titleTextView.setText(currentEntry.getTitle());
        authorTextView.setText(currentEntry.getAuthor());
        doiTextView.setText(currentEntry.getDoi());
        keywordsTextView.setText(currentEntry.getKeywords());
        yearTextView.setText(currentEntry.getYear());
        monthTextView.setText(currentEntry.getMonth());
        abstractTextView.setText(currentEntry.getAbstractText());
        if(currentEntry.getJournal() != null && !currentEntry.getJournal().isEmpty())
            publishedTextView.setText(currentEntry.getJournal());
        else
            publishedTextView.setText(currentEntry.getBooktitle());

        return listItem;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    public void setEntries(ArrayList<Entry> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
        notifyDataSetChanged();
    }

    public void removeFirstObject() {
        this.entries.remove(0);
        notifyDataSetChanged();
    }
}
