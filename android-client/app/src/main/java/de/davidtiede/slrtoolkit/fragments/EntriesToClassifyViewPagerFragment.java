package de.davidtiede.slrtoolkit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.davidtiede.slrtoolkit.ClassifyActivity;
import de.davidtiede.slrtoolkit.R;
import de.davidtiede.slrtoolkit.database.Entry;
import de.davidtiede.slrtoolkit.viewmodels.BibtexEntriesViewModel;
import de.davidtiede.slrtoolkit.views.EntrySlidePagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntriesToClassifyViewPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntriesToClassifyViewPagerFragment extends Fragment {

    ViewPager2 viewPager;
    BibtexEntriesViewModel bibtexEntriesViewModel;
    private FragmentStateAdapter pagerAdapter;
    private static final String ARG_PARAM1 = "repoId";
    int repoId;

    public EntriesToClassifyViewPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param repoId Parameter 1.
     * @return A new instance of fragment EntriesToClassifyViewPagerFragment.
     */
    public static EntriesToClassifyViewPagerFragment newInstance(int repoId) {
        EntriesToClassifyViewPagerFragment fragment = new EntriesToClassifyViewPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, repoId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            repoId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entries_to_classify_view_pager, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        bibtexEntriesViewModel = new ViewModelProvider(this).get(BibtexEntriesViewModel.class);


        viewPager = view.findViewById(R.id.classify_entries_viewpager);
        pagerAdapter = new EntrySlidePagerAdapter(EntriesToClassifyViewPagerFragment.this.getActivity(), new ArrayList<Entry>());
        viewPager.setAdapter(pagerAdapter);

        bibtexEntriesViewModel.getEntriesForRepo(repoId).observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                pagerAdapter = new EntrySlidePagerAdapter(EntriesToClassifyViewPagerFragment.this.getActivity(), entries);
                viewPager.setAdapter(pagerAdapter);
            }
        });
    }
}