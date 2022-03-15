package de.davidtiede.slrtoolkit.fragments;

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
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Taxonomy;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.viewmodels.AnalyzeViewModel;

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
        barChart = view.findViewById(R.id.barchart);
        int repoId = analyzeViewModel.getCurrentRepoId();
        int currentParentTaxonomyId = analyzeViewModel.getParentTaxonomyToDisplayChildrenFor1();
        List<TaxonomyWithEntries> childTaxonomiesToDisplay = analyzeViewModel.getChildTaxonomiesToDisplay1();
        try {
            if(childTaxonomiesToDisplay == null || childTaxonomiesToDisplay.size() == 0) {
                List<TaxonomyWithEntries> parentTaxonomies = analyzeViewModel.getChildTaxonomiesForTaxonomyId(repoId, currentParentTaxonomyId);
                Map<Taxonomy, Integer> taxonomyWithNumEntries = analyzeViewModel.getNumberOfEntriesForChildrenOfTaxonomy(repoId, parentTaxonomies);
                setBarChart(taxonomyWithNumEntries);
            } else {
                Map<Taxonomy, Integer> taxonomyWithNumEntries = analyzeViewModel.getNumberOfEntriesForChildrenOfTaxonomy(repoId, childTaxonomiesToDisplay);
                setBarChart(taxonomyWithNumEntries);
            }
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace();
        }

    }

    private void setBarChart(Map<Taxonomy, Integer> taxonomyWithNumEntries) {
        List<BarEntry> entries = new ArrayList<>();
        int index = 1;
        for(Taxonomy taxonomy : taxonomyWithNumEntries.keySet()) {
            BarEntry barEntry = new BarEntry(index, taxonomyWithNumEntries.get(taxonomy), taxonomy.getName());
            entries.add(barEntry);
            index++;
        }
        BarDataSet dataSet = new BarDataSet(entries, "Amount of entries for taxonomy");
        BarData data = new BarData(dataSet);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(entries);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setLabelRotationAngle(45);
        xAxis.setValueFormatter(xAxisFormatter);
        barChart.setData(data);
        barChart.setVisibleXRangeMaximum(7);
        barChart.invalidate();
    }

    public static class DayAxisValueFormatter extends ValueFormatter {
        List<BarEntry> entries;
        public DayAxisValueFormatter(List<BarEntry> entries) {
            this.entries = entries;
        }
        @Override
        public String getFormattedValue(float value) {
            String title = (String) entries.get(Math.round(value)-1).getData();
            if(title.length() < 20) {
                return title;
            }
            return title.substring(0, 20) + "...";
        }
    }
}