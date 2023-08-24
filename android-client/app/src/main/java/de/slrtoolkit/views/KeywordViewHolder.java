package de.slrtoolkit.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.slrtoolkit.R;

public class KeywordViewHolder extends RecyclerView.ViewHolder {
    private final TextView keywordNameView;
    public KeywordViewHolder(@NonNull View itemView) {
        super(itemView);
        keywordNameView = itemView.findViewById(R.id.keyword_item_name);
    }

    static KeywordViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.keyword_item, parent, false);
        return new KeywordViewHolder(view);
    }

    public void bind(String text) {
        keywordNameView.setText(text);
    }
}
