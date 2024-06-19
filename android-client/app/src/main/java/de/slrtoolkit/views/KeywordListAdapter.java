package de.slrtoolkit.views;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
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
import de.slrtoolkit.util.DoubleClickListener;
import de.slrtoolkit.util.FileUtil;
import de.slrtoolkit.util.SlrprojectParser;
import de.slrtoolkit.viewmodels.RepoViewModel;

public class KeywordListAdapter extends ListAdapter<Keyword, KeywordViewHolder> {
    private RecyclerView recyclerView;
    private final KeywordRepository keywordRepository;

    private final Application application;


    public KeywordListAdapter(Application application, FragmentManager fragmentManager, KeywordRepository keywordRepository, @NonNull DiffUtil.ItemCallback<Keyword> diffCallback) {
        super(diffCallback);
        this.keywordRepository = keywordRepository;
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

        holder.itemView.setOnTouchListener(new DoubleClickListener(recyclerView.getContext(), v -> showDialog(repoViewModel, current)));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public Context getContext() {
        return recyclerView.getContext();
    }

    private void showDialog(RepoViewModel repoViewModel, Keyword current) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Keyword?")
                .setPositiveButton("OK", (dialog, which) -> {
                    FileUtil fileUtil = new FileUtil();
                    File file = fileUtil.accessFiles(repoViewModel.getCurrentRepo().getLocal_path(), application, ".slrproject");
                    SlrprojectParser slrprojectParser = new SlrprojectParser();
                    slrprojectParser.deleteKeyword(String.valueOf(file), current.getName());

                    keywordRepository.deleteAsync(current, () -> {

                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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
