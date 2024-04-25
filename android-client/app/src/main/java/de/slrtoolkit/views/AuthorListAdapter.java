package de.slrtoolkit.views;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

import de.slrtoolkit.database.Author;
import de.slrtoolkit.repositories.AuthorRepository;
import de.slrtoolkit.util.DoubleClickListener;
import de.slrtoolkit.util.FileUtil;
import de.slrtoolkit.util.SlrprojectParser;
import de.slrtoolkit.viewmodels.RepoViewModel;

public class AuthorListAdapter extends ListAdapter<Author, AuthorViewHolder> {
    private RecyclerView recyclerView;

    private final Application application;

    private final AuthorRepository authorRepository;

    public AuthorListAdapter(Application application, AuthorRepository authorRepository, @NonNull DiffUtil.ItemCallback<Author> diffCallback) {
        super(diffCallback);
        this.application = application;
        this.authorRepository = authorRepository;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return AuthorViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author current = getItem(position);
        holder.bind(current.getName(), current.getAffilation(), current.getEmail());
        RepoViewModel repoViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(RepoViewModel.class);

        holder.itemView.setOnTouchListener(new DoubleClickListener(recyclerView.getContext(), v -> showDialog(repoViewModel, current)));
    }

    public Context getContext() {
        return recyclerView.getContext();
    }

    private void showDialog(RepoViewModel repoViewModel, Author current) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Author?")
                .setPositiveButton("OK", (dialog, which) -> {
                    FileUtil fileUtil = new FileUtil();
                    File file = fileUtil.accessFiles(repoViewModel.getCurrentRepo().getLocal_path(), application, ".slrproject");
                    SlrprojectParser slrprojectParser = new SlrprojectParser();
                    slrprojectParser.deleteAuthorList(String.valueOf(file), current.getEmail());

                    authorRepository.deleteAsync(current, () -> {

                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public static class AuhtorsDiff extends DiffUtil.ItemCallback<Author> {

        @Override
        public boolean areItemsTheSame(@NonNull Author oldItem, @NonNull Author newItem) {
            return oldItem.getAuthorId() == newItem.getAuthorId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Author oldItem, @NonNull Author newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
