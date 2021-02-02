package de.davidtiede.slrtoolkit.views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import de.davidtiede.slrtoolkit.R;

public class ProgressButtonCloneProject {

    private final View view;
    private final Context context;
    private final CardView cardView;
    private final ProgressBar progressBar;
    private final TextView textView;

    public ProgressButtonCloneProject(Context context, View view) {
        this.context = context;
        this.view = view;
        cardView = view.findViewById(R.id.button_clone_project);
        progressBar = view.findViewById(R.id.progressbar_clone_project);
        textView = view.findViewById(R.id.textview_clone_project);
    }

    public void onLoading() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setText(view.getResources().getString(R.string.progressbutton_onloading));
    }

    public void onSucceeded() {
        cardView.setBackgroundColor(Color.GREEN);
        progressBar.setVisibility(View.GONE);
        textView.setText(view.getResources().getString(R.string.progressbutton_succeeded));
    }

    public void onFailed() {
        cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.design_default_color_error));
        progressBar.setVisibility(View.GONE);
        textView.setText(view.getResources().getString(R.string.progressbutton_onfailed));
    }
}
