package de.davidtiede.slrtoolkit.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.viewmodels.AnalyzeViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BubbleChartFragment extends Fragment {
    BubbleChart bubbleChart;
    BubbleData bubbleData;
    BubbleDataSet bubbleDataSet;
    List bubbleEntries = new ArrayList();

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        bubbleChart = view.findViewById(R.id.bubblechart);
        AnalyzeViewModel analyzeViewModel = new ViewModelProvider(requireActivity()).get(AnalyzeViewModel.class);
        int repoId = analyzeViewModel.getCurrentRepoId();
        try {
            List<TaxonomyWithEntries> parentTaxonomies = analyzeViewModel.getTaxonomiesWithLeafChildTaxonomies(repoId);
            int parentTaxonomyId1 = parentTaxonomies.get(0).taxonomy.getTaxonomyId();
            int parentTaxonomyId2 = parentTaxonomies.get(1).taxonomy.getTaxonomyId();
            List<TaxonomyWithEntries> childTaxonomies1 = analyzeViewModel.getChildTaxonomiesForTaxonomyId(repoId, parentTaxonomyId1);
            List<TaxonomyWithEntries> childTaxonomies2 = analyzeViewModel.getChildTaxonomiesForTaxonomyId(repoId, parentTaxonomyId2);
            setBubbleChart(childTaxonomies1, childTaxonomies2);
        } catch (ExecutionException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setBubbleChart(List<TaxonomyWithEntries> taxonomies1, List<TaxonomyWithEntries> taxonomies2) {
        getData(taxonomies1, taxonomies2);
        bubbleDataSet = new BubbleDataSet(bubbleEntries, "");
        bubbleData = new BubbleData(bubbleDataSet);
        bubbleChart.setData(bubbleData);
        bubbleDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        bubbleDataSet.setValueTextColor(Color.BLACK);
        bubbleDataSet.setValueTextSize(18f);
        ValueFormatter xAxisFormatter = new AxisValueFormatter(taxonomies1);
        ValueFormatter yAxisFormatter = new AxisValueFormatter(taxonomies2);
        XAxis xAxis = bubbleChart.getXAxis();
        YAxis yAxis = bubbleChart.getAxisLeft();
        yAxis.setGranularity(1f);
        xAxis.setGranularity(1f);
        yAxis.setValueFormatter(yAxisFormatter);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(taxonomies1.size());
        xAxis.setValueFormatter(xAxisFormatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getData(List<TaxonomyWithEntries> taxonomyWithEntries1, List<TaxonomyWithEntries> taxonomyWithEntries2) {
        int xCount = 0;
        for(TaxonomyWithEntries t1: taxonomyWithEntries1) {
            int yCount = 0;
            xCount++;
            for(TaxonomyWithEntries t2: taxonomyWithEntries2) {
                yCount++;
                List<Entry> t1Entries = t1.entries;
                List<Entry> t2Entries = t2.entries;
                List<Integer> t1EntryIds = t1Entries.stream().map(Entry::getId).collect(Collectors.toList());
                List<Integer> t2EntryIds = t2Entries.stream().map(Entry::getId).collect(Collectors.toList());
                t1EntryIds.retainAll(t2EntryIds);
                bubbleEntries.add(new BubbleEntry(xCount, yCount, t1EntryIds.size()));
            }
        }
    }

    public static class AxisValueFormatter extends ValueFormatter {
        List<TaxonomyWithEntries> taxonomyWithEntries;
        public AxisValueFormatter(List<TaxonomyWithEntries> taxonomyWithEntries) {
            this.taxonomyWithEntries = taxonomyWithEntries;
        }
        @Override
        public String getFormattedValue(float value) {
            int index = Math.round(value);
            if(index == 0) {
                return "";
            }
            String title = taxonomyWithEntries.get(index -1).taxonomy.getName();
            if(title.length() > 20) {
                return title.substring(0, 20);
            }
            return title;
        }
    }

}