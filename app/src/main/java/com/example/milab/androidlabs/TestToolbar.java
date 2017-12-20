package com.example.milab.androidlabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {
    FloatingActionButton fab;
    String messageForOne = "You selected item 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        fab= findViewById(R.id.floatingActionButton);
        setSupportActionBar(myToolbar);

        //Floating Button Handler
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"This is snackbar" ,Snackbar.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_1:
                Log.d("Toolbar", "Option 1 selected");
                Snackbar.make(findViewById(R.id.testToolBarLayout),messageForOne ,Snackbar.LENGTH_LONG).show();
                break;
            // action with ID action_settings was selected
            case R.id.action_2:
                AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
                builder.setTitle(R.string.ddialog_title);
                builder.setPositiveButton(R.string.ook, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                       finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        // User cancelled the dialog
                       // dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                        dialog.show();
                break;
            case R.id.action_3:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(TestToolbar.this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_signin, null);
                final EditText temp = view.findViewById(R.id.username);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder1.setView(view)
                        // Add action buttons
                        .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                                messageForOne = temp.getText().toString();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                break;
            case R.id.action_4:
                Toast.makeText(getApplicationContext(), "Version 1.0, Mila-Stana Bozovic", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }
}
