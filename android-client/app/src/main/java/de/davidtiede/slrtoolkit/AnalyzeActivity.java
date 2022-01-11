package de.davidtiede.slrtoolkit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.slider.LabelFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.viewmodels.AnalyzeViewModel;

public class AnalyzeActivity extends AppCompatActivity {
    int repoId;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AnalyzeViewModel analyzeViewModel = new ViewModelProvider(this).get(AnalyzeViewModel.class);
        setContentView(R.layout.activity_analyze);
        barChart = findViewById(R.id.chart);

        repoId = 1;

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
        List<BarEntry> entries = new ArrayList<BarEntry>();

        /*for (int i = 0; i < 20; i++) {
            BarEntry entry = new BarEntry(i, (i*3) - 2);
            entries.add(entry);

        }*/
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