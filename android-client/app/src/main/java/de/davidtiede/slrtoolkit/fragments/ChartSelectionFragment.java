package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.davidtiede.slrtoolkit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartSelectionFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Button bubblechartButton = view.findViewById(R.id.bubblechart_button);
        Button barchartButton = view.findViewById(R.id.barchart_button);

        barchartButton.setOnClickListener(v -> {
            Fragment barchartFragment = new BarChartFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.analyze_fragment_container_view, barchartFragment);
            ft.addToBackStack(null);
            ft.commit();
        });

        bubblechartButton.setOnClickListener(v -> {
            Fragment bubblechartFragment = new BubbleChartFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.analyze_fragment_container_view, bubblechartFragment);
            ft.addToBackStack(null);
            ft.commit();
        });
    }
}