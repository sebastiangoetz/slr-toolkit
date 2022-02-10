package de.davidtiede.slrtoolkit.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import de.davidtiede.slrtoolkit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BubbleChartFragment extends Fragment {
    BubbleChart bubbleChart;
    BubbleData bubbleData;
    BubbleDataSet bubbleDataSet;
    ArrayList bubbleEntries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bubble_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        bubbleChart = view.findViewById(R.id.bubblechart);
        setBubbleChart();
    }

    private void setBubbleChart() {
        getEntries();
        bubbleDataSet = new BubbleDataSet(bubbleEntries, "");
        bubbleData = new BubbleData(bubbleDataSet);
        bubbleChart.setData(bubbleData);
        bubbleDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        bubbleDataSet.setValueTextColor(Color.BLACK);
        bubbleDataSet.setValueTextSize(18f);
    }

    private void getEntries() {
        bubbleEntries = new ArrayList<>();
        bubbleEntries.add(new BubbleEntry(0, 1,0.21f));
        bubbleEntries.add(new BubbleEntry(1, 2,0.12f));
        bubbleEntries.add(new BubbleEntry(2, 3,0.20f));
        bubbleEntries.add(new BubbleEntry(2,4, 0.52f));
        bubbleEntries.add(new BubbleEntry(3, 5,0.29f));
        bubbleEntries.add(new BubbleEntry(4, 6,0.62f));
    }
}