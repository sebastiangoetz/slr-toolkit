package de.slrtoolkit.views;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

import de.slrtoolkit.database.Keyword;
import de.slrtoolkit.repositories.KeywordRepository;
import de.slrtoolkit.repositories.OnDeleteCompleteListener;
import de.slrtoolkit.util.DoubleClickListener;
import de.slrtoolkit.util.FileUtil;
import de.slrtoolkit.util.SlrprojectParser;
import de.slrtoolkit.viewmodels.RepoViewModel;

public class KeywordListAdapter extends ListAdapter<Keyword, KeywordViewHolder> {
    private RecyclerView recyclerView;
    private KeywordRepository keywordRepository;
    private FragmentManager fragmentManager;

    private Application application;


    public KeywordListAdapter(Application application, FragmentManager fragmentManager, KeywordRepository keywordRepository, @NonNull DiffUtil.ItemCallback<Keyword> diffCallback) {
        super(diffCallback);
        this.keywordRepository = keywordRepository;
        this.fragmentManager = fragmentManager;
        this.application = application;
    }

    @NonNull
    @Override
    public KeywordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return KeywordViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull KeywordViewHolder holder, int position) {
        Keyword current = getItem(position);
        holder.bind(current.getName());
        RepoViewModel repoViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(RepoViewModel.class);

        holder.itemView.setOnTouchListener(new DoubleClickListener(recyclerView.getContext(), new DoubleClickListener.OnDoubleClickListener() {

            @Override
            public void onDoubleClick(View v) {


                FileUtil fileUtil= new FileUtil();
                File file = fileUtil.accessFiles(repoViewModel.getCurrentRepo().getLocal_path(), application,".slrproject");
                SlrprojectParser slrprojectParser = new SlrprojectParser();
                slrprojectParser.deleteKeyword(String.valueOf(file), current.getName());

                keywordRepository.deleteAsync(current, new OnDeleteCompleteListener() {
                    @Override
                    public void onDeleteComplete() {

                    }
                });
            }
        }));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }
    public Context getContext() {
        return recyclerView.getContext();
    }

    public static class KeywordsDiff extends DiffUtil.ItemCallback<Keyword> {

        @Override
        public boolean areItemsTheSame(@NonNull Keyword oldItem, @NonNull Keyword newItem) {
            return oldItem.getKeywordId() == newItem.getKeywordId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Keyword oldItem, @NonNull Keyword newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
