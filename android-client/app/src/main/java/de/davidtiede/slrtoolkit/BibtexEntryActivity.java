package de.davidtiede.slrtoolkit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.jbibtex.BibTeXEntry;

public class BibtexEntryActivity extends AppCompatActivity {
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibtex_entry);
        Bundle extra = getIntent().getBundleExtra("extra");
        BibTeXEntry entry = (BibTeXEntry) extra.getSerializable("bibtexEntry");
        titleTextView = findViewById(R.id.bibtex_entry_title);
        titleTextView.setText(entry.getField(BibTeXEntry.KEY_TITLE).toUserString());
    }
}