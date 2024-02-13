package de.slrtoolkit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

import de.slrtoolkit.R;
import de.slrtoolkit.fragments.CreateProject1Fragment;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab_add_project);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.choose_option_dialog_layout, null);
                Button button_create_project_with_git = view1.findViewById(R.id.button_create_project_with_git);
                Button button_create_project_manually = view1.findViewById(R.id.button_create_project_manualy);

                AlertDialog alertDialog = new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Choose option to add a new project")
                        .setView(view1)
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                alertDialog.show();

                button_create_project_with_git.setOnClickListener(view2 -> {
                    alertDialog.dismiss();
                    NavHostFragment.findNavController(
                                    Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)))
                            .navigate(R.id.action_ProjectsFragment_to_AddProjectFragment);
                });
                button_create_project_manually.setOnClickListener(view2 -> {
                    alertDialog.dismiss();
                    NavHostFragment.findNavController(
                                    Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)))
                            .navigate((R.id.actionProjectsFragment_to_CreateProjectFragment));
                });
            }
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
        return fab;
    }
}