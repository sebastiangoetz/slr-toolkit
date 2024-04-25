package de.slrtoolkit.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

import de.slrtoolkit.R;
import de.slrtoolkit.database.Entry;
import de.slrtoolkit.database.TaxonomyWithEntries;
import de.slrtoolkit.viewmodels.AnalyzeViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BubbleChartFragment extends Fragment {
    BubbleChart bubbleChart;
    BubbleData bubbleData;
    BubbleDataSet bubbleDataSet;
    TextView noBubbleEntriesTextView;

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
        noBubbleEntriesTextView = view.findViewById(R.id.textview_no_bubblechart);
        AnalyzeViewModel analyzeViewModel = new ViewModelProvider(requireActivity()).get(AnalyzeViewModel.class);
        int repoId = analyzeViewModel.getCurrentRepoId();
        try {
            int parentTaxonomyId1 = analyzeViewModel.getParentTaxonomyToDisplayChildrenFor1();
            int parentTaxonomyId2 = analyzeViewModel.getParentTaxonomyToDisplayChildrenFor2();
            List<TaxonomyWithEntries> childTaxonomies1;
            List<TaxonomyWithEntries> childTaxonomies2;

            if (analyzeViewModel.getChildTaxonomiesToDisplay1() == null || analyzeViewModel.getChildTaxonomiesToDisplay1().isEmpty()) {
                childTaxonomies1 = analyzeViewModel.getChildTaxonomiesForTaxonomyId(repoId, parentTaxonomyId1);
            } else {
                childTaxonomies1 = analyzeViewModel.getChildTaxonomiesToDisplay1();
            }

            if (analyzeViewModel.getChildTaxonomiesToDisplay2() == null || analyzeViewModel.getChildTaxonomiesToDisplay2().isEmpty()) {
                childTaxonomies2 = analyzeViewModel.getChildTaxonomiesForTaxonomyId(repoId, parentTaxonomyId2);
            } else {
                childTaxonomies2 = analyzeViewModel.getChildTaxonomiesToDisplay2();
            }
            childTaxonomies1 = analyzeViewModel.getTaxonomiesWithAggregatedChildren(repoId, childTaxonomies1);
            childTaxonomies2 = analyzeViewModel.getTaxonomiesWithAggregatedChildren(repoId, childTaxonomies2);
            setBubbleChart(childTaxonomies1, childTaxonomies2);
        } catch (ExecutionException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setBubbleChart(List<TaxonomyWithEntries> taxonomies1, List<TaxonomyWithEntries> taxonomies2) {
        List<BubbleEntry> bubbleEntries = getData(taxonomies1, taxonomies2);
        if (!bubbleEntries.isEmpty()) {
            noBubbleEntriesTextView.setVisibility(View.INVISIBLE);
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
        } else {
            bubbleChart.setVisibility(View.INVISIBLE);
            noBubbleEntriesTextView.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<BubbleEntry> getData(List<TaxonomyWithEntries> taxonomyWithEntries1, List<TaxonomyWithEntries> taxonomyWithEntries2) {
        List<BubbleEntry> bubbleEntries = new ArrayList<>();
        int xCount = 0;
        for (TaxonomyWithEntries t1 : taxonomyWithEntries1) {
            int yCount = 0;
            for (TaxonomyWithEntries t2 : taxonomyWithEntries2) {
                List<Entry> t1Entries = t1.entries;
                System.out.println("t1 entries!");
                System.out.println("name: " + t1.taxonomy.getName());
                for (Entry entry : t1Entries) {
                    System.out.println(entry.getTitle());
                }
                List<Entry> t2Entries = t2.entries;
                System.out.println("t2 entries!");
                System.out.println("name: " + t2.taxonomy.getName());
                for (Entry entry : t2Entries) {
                    System.out.println(entry.getTitle());
                }
                List<Integer> t1EntryIds = t1Entries.stream().map(Entry::getEntryId).collect(Collectors.toList());
                List<Integer> t2EntryIds = t2Entries.stream().map(Entry::getEntryId).collect(Collectors.toList());
                System.out.println(t1EntryIds);
                System.out.println(t2EntryIds);
                t1EntryIds.retainAll(t2EntryIds);
                System.out.println(t1EntryIds);
                System.out.println("Adding!");
                System.out.println(t1EntryIds.size());
                if (!t1EntryIds.isEmpty()) {
                    BubbleEntry bubbleEntry = new BubbleEntry(xCount, yCount, t1EntryIds.size());
                    bubbleEntries.add(bubbleEntry);
                }
                yCount++;
            }
            xCount++;
        }
        System.out.println("what is the size?");
        System.out.println(bubbleEntries.size());
        return bubbleEntries;
    }

    public static class AxisValueFormatter extends ValueFormatter {
        final List<TaxonomyWithEntries> taxonomyWithEntries;

        public AxisValueFormatter(List<TaxonomyWithEntries> taxonomyWithEntries) {
            this.taxonomyWithEntries = taxonomyWithEntries;
        }

        @Override
        public String getFormattedValue(float value) {
            int index = Math.round(value);
            if (taxonomyWithEntries.size() > index && index >= 0) {
                String title = taxonomyWithEntries.get(index).taxonomy.getName();
                if (title.length() > 20) {
                    return title.substring(0, 20);
                }
                return title;
            }
            return "";
        }
    }

}