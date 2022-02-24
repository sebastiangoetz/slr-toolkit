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
        Button bubblechartButton = view.findViewById(R.id.bubblechart_button);
        Button barchartButton = view.findViewById(R.id.barchart_button);

        //Spinner for chart selection
        Spinner chartSpinner = view.findViewById(R.id.chart_selection_spinner);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this.getContext(), R.array.chart_array, android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(R.layout.spinner_item);
        chartSpinner.setAdapter(staticAdapter);

        chartSpinner.getSelectedItem().toString();

        //Spinner for taxonomy Selection
        Spinner taxonomySpinner = view.findViewById(R.id.taxonomy_selection_spinner);
        try {
            List<TaxonomyWithEntries> taxonomyWithEntriesList = analyzeViewModel.getTaxonomiesWithLeafChildTaxonomies(repoId);
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), taxonomyWithEntriesList);
            taxonomySpinner.setAdapter(spinnerAdapter);
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        barchartButton.setOnClickListener(v -> {
            TaxonomyWithEntries selectedTaxonomy = (TaxonomyWithEntries) taxonomySpinner.getSelectedItem();
            String name = selectedTaxonomy.taxonomy.getName();
            System.out.println(name);
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