package de.slrtoolkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton btnAddProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnAddProject = findViewById(R.id.fab_add_project);

        btnAddProject.setOnClickListener(view -> {
            View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_create_project_choose_option, null);
            Button buttonAddProjectFromGit = view1.findViewById(R.id.button_add_project_from_git);
            Button buttonCreateProjectLocally = view1.findViewById(R.id.button_create_project_locally);

            AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("Choose option to add a new project")
                    .setView(view1)
                .setNegativeButton("Close", (dialogInterface, i) -> dialogInterface.dismiss()).create();
            alertDialog.show();

            buttonAddProjectFromGit.setOnClickListener(view2 -> {
                alertDialog.dismiss();
                NavHostFragment.findNavController(
                                Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)))
                        .navigate(R.id.action_ProjectsFragment_to_AddProjectFromGitFragment);
            });
            buttonCreateProjectLocally.setOnClickListener(view2 -> {
                alertDialog.dismiss();
                NavHostFragment.findNavController(
                                Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)))
                        .navigate((R.id.actionProjectsFragment_to_CreateLocalProjectFragment));
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public FloatingActionButton getFloatingActionButton() {
        return btnAddProject;
    }
}