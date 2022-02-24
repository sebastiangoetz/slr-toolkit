package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.TaxonomyWithEntries;
import de.davidtiede.slrtoolkit.viewmodels.AnalyzeViewModel;
import de.davidtiede.slrtoolkit.views.SpinnerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartSelectionFragment extends Fragment {
    AnalyzeViewModel analyzeViewModel;
    int repoId;
    String selectedChart;
    TaxonomyWithEntries selectedTaxonomy1;
    TaxonomyWithEntries selectedTaxonomy2;

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
        analyzeViewModel = new ViewModelProvider(requireActivity()).get(AnalyzeViewModel.class);
        repoId = analyzeViewModel.getCurrentRepoId();
        Button analyzeButton = view.findViewById(R.id.analyze_button);
        Spinner chartSpinner = view.findViewById(R.id.chart_selection_spinner);
        Spinner taxonomySpinner1 = view.findViewById(R.id.taxonomy_selection_spinner1);
        Spinner taxonomySpinner2 = view.findViewById(R.id.taxonomy_selection_spinner2);
        //set Spinner for chart selection
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this.getContext(), R.array.chart_array, android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(R.layout.spinner_item);
        chartSpinner.setAdapter(staticAdapter);

        chartSpinner.getSelectedItem().toString();

        //set Spinner for taxonomy Selection
        try {
            List<TaxonomyWithEntries> taxonomyWithEntriesList = analyzeViewModel.getTaxonomiesWithLeafChildTaxonomies(repoId);
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), taxonomyWithEntriesList);
            taxonomySpinner1.setAdapter(spinnerAdapter);
            taxonomySpinner2.setAdapter(spinnerAdapter);
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        chartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedChart = chartSpinner.getSelectedItem().toString();
                System.out.println(selectedChart);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedChart = null;
            }
        });

        taxonomySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTaxonomy1 = (TaxonomyWithEntries) taxonomySpinner1.getSelectedItem();
                System.out.println(selectedTaxonomy1.taxonomy.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedTaxonomy1 = null;
            }
        });

        taxonomySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTaxonomy2 = (TaxonomyWithEntries) taxonomySpinner1.getSelectedItem();
                System.out.println(selectedTaxonomy1.taxonomy.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedTaxonomy1 = null;
            }
        });

        analyzeButton.setOnClickListener(v -> {
            System.out.println("Currently selected");
            System.out.println(selectedChart);
            if(selectedChart.equals("Bubblechart")) {
                Fragment bubblechartFragment = new BubbleChartFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.analyze_fragment_container_view, bubblechartFragment);
                ft.addToBackStack(null);
                ft.commit();
            } else {
                Fragment barchartFragment = new BarChartFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.analyze_fragment_container_view, barchartFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}