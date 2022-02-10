package de.davidtiede.slrtoolkit.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.viewmodels.AnalyzeViewModel;
import de.davidtiede.slrtoolkit.viewmodels.ProjectViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarChartFragment extends Fragment {
    BarChart barChart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bar_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        AnalyzeViewModel analyzeViewModel = new ViewModelProvider(requireActivity()).get(AnalyzeViewModel.class);
        int repoId = 1;
        barChart = view.findViewById(R.id.barchart);

        try{
            Map<Taxonomy, Integer> taxonomyWithNumEntries = analyzeViewModel.getNumberOfEntriesForTaxonomy(repoId);
            setBarChart(taxonomyWithNumEntries);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
    }

    private void setBarChart(Map<Taxonomy, Integer> taxonomyWithNumEntries) {
        List<BarEntry> entries = new ArrayList<>();

        int count = 0;
        ArrayList<String> xAxisLabels = new ArrayList<>();
        for(Taxonomy taxonomy: taxonomyWithNumEntries.keySet()) {
            int number = taxonomyWithNumEntries.get(taxonomy);
            BarEntry entry = new BarEntry(count, number, taxonomy.getName());
            entries.add(entry);
            xAxisLabels.add(taxonomy.getName());
            count++;
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(xAxisLabels.size(), true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setLabelRotationAngle(-45);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        BarDataSet dataSet = new BarDataSet(entries, "Taxonomies"); // add entries to dataset
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); // refresh
    }
}