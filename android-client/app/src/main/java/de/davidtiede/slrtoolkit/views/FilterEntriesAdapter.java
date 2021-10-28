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
import java.util.List;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;

public class FilterEntriesAdapter extends ArrayAdapter<Entry> {
    private Context context;
    private ArrayList<Entry> entries;

    public FilterEntriesAdapter(@NonNull Context context, @NonNull ArrayList<Entry> entries) {
        super(context, 0, entries);
        this.context = context;
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

        TextView titleTextView = listItem.findViewById(R.id.entry_title);
        titleTextView.setText(currentEntry.getTitle());

        return listItem;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    public void setEntries(ArrayList<Entry> entries) {
        System.out.println("in clear entries");
        this.entries.clear();
        System.out.println("Cleared");
        this.entries.addAll(entries);
        System.out.println("set new entries");
        notifyDataSetChanged();
    }

    public void removeFirstObject() {
        this.entries.remove(0);
        notifyDataSetChanged();
    }
}
