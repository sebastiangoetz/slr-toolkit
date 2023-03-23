package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    List<TaxonomyWithEntries> taxonomyWithEntries;
    LayoutInflater inflater;

    public SpinnerAdapter(Context context, List<TaxonomyWithEntries> taxonomyWithEntries) {
        this.context = context;
        this.taxonomyWithEntries = taxonomyWithEntries;
        this.inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return taxonomyWithEntries.size();
    }

    @Override
    public TaxonomyWithEntries getItem(int i) {
        return taxonomyWithEntries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return taxonomyWithEntries.get(i).taxonomy.getTaxonomyId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_item, null);
        TextView textView = view.findViewById(R.id.spinner_text_view);
        String text = getItem(i).taxonomy.getName();
        textView.setText(text);
        return view;
    }
}
