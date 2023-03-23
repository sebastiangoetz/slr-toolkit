package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;

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
        if(listItem == null) {
            listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent, false);
        }

        Entry currentEntry = entries.get(position);

        TextView titleTextView = listItem.findViewById(R.id.filter_entry_title);
        TextView authorTextView = listItem.findViewById(R.id.filter_entry_authors);
        TextView doiTextView = listItem.findViewById(R.id.filter_entry_doi);
        TextView keywordsTextView = listItem.findViewById(R.id.filter_entry_keywords);
        TextView yearTextView = listItem.findViewById(R.id.filter_entry_year);
        TextView monthTextView = listItem.findViewById(R.id.filter_entry_month);
        titleTextView.setText(currentEntry.getTitle());
        authorTextView.setText(currentEntry.getAuthor());
        doiTextView.setText(currentEntry.getDoi());
        keywordsTextView.setText(currentEntry.getKeywords());
        yearTextView.setText(currentEntry.getYear());
        monthTextView.setText(currentEntry.getMonth());

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
