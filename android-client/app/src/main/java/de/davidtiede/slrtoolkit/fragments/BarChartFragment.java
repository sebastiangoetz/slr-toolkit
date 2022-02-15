package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        int repoId = analyzeViewModel.getCurrentRepoId();
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

    /*private void setBarChart(Map<Taxonomy, Integer> taxonomyWithNumEntries) {
        List<BarEntry> entries = new ArrayList<>();

        int count = 0;
        //ArrayList<String> xAxisLabels = new ArrayList<>();
        String xAxisLabels[] = new String[taxonomyWithNumEntries.keySet().size()];
        int num = 0;
        for(Taxonomy taxonomy : taxonomyWithNumEntries.keySet()) {
            xAxisLabels[num] = taxonomy.getName();
            num++;
        }
        for(Taxonomy taxonomy: taxonomyWithNumEntries.keySet()) {
            int number = taxonomyWithNumEntries.get(taxonomy);
            BarEntry entry = new BarEntry(count, number, xAxisLabels);
            entries.add(entry);
            //xAxisLabels.add(taxonomy.getName());
            System.out.println("number and name");
            System.out.println(number);
            System.out.println(taxonomy.getName());
            count++;
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelCount(10, true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setLabelRotationAngle(-45);
        xAxis.setCenterAxisLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setGranularity(1f);
        //xAxis.setGranularityEnabled(true);

        BarDataSet dataSet = new BarDataSet(entries, "Taxonomies"); // add entries to dataset
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.setVisibleXRangeMaximum(10);
        barChart.invalidate(); // refresh
    }*/

    /*private void setBarChart(Map<Taxonomy, Integer> taxonomyWithNumEntries) {
        int[] numArr = {1, 2, 3, 4, 2, 2};
        List<BarEntry> entries = new ArrayList<BarEntry>();
        int index = 1;
        for (int num : numArr) {
            entries.add(new BarEntry(index, num));
            index ++;
        }
        BarDataSet dataSet = new BarDataSet(entries, "Numbers");
        BarData data = new BarData(dataSet);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        barChart.setData(data);
        barChart.invalidate();
    }*/

    private void setBarChart(Map<Taxonomy, Integer> taxonomyWithNumEntries) {
        List<BarEntry> entries = new ArrayList<BarEntry>();
        int index = 1;
        for(Taxonomy taxonomy : taxonomyWithNumEntries.keySet()) {
            if(index > 7) {
                break;
            }
            BarEntry barEntry = new BarEntry(index, taxonomyWithNumEntries.get(taxonomy), taxonomy.getName());
            entries.add(barEntry);
            index++;
        }
        BarDataSet dataSet = new BarDataSet(entries, "Numbers");
        BarData data = new BarData(dataSet);

        ValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart, entries);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setLabelRotationAngle(45);
        xAxis.setValueFormatter(xAxisFormatter);

        barChart.setData(data);
        barChart.invalidate();
    }

    public class DayAxisValueFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;
        List<BarEntry> entries;
        public DayAxisValueFormatter(BarLineChartBase<?> chart, List<BarEntry> entries) {
            this.chart = chart;
            this.entries = entries;
        }
        @Override
        public String getFormattedValue(float value) {
            String title = (String) entries.get(Math.round(value)-1).getData();
            if(title.length() < 15) {
                return title;
            }
            return title.substring(0, 20) + "...";
        }
    }
}