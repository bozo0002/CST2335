package com.example.milab.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends Activity {
    protected static final String ListItemsActivity = "StartActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton iButton;
    Switch aSwitch;
    boolean isChecked = false;
   // Context context = getApplicationContext();
    CheckBox cBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ListItemsActivity, "In onCreate()");
        setContentView(R.layout.activity_list_items);

        iButton = (ImageButton) findViewById(R.id.imageButton);
        iButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //  dispatchTakePictureIntent();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });
        aSwitch = (Switch) findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    CharSequence text = "Switch is Off";
                    int duration = Toast.LENGTH_LONG; //= Toast.LENGTH_LONG if Off

                    Toast toast = Toast.makeText(ListItemsActivity.this, text, duration); //this is the ListActivity
                    toast.show(); //display your message box
                } else {
                    CharSequence text = "Switch is On";// "Switch is Off"
                    int duration = Toast.LENGTH_SHORT; //= Toast.LENGTH_LONG if Off

                    Toast toast = Toast.makeText(ListItemsActivity.this, text, duration); //this is the ListActivity
                    toast.show(); //display your message box
                }
            }
        });
       final AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
        cBox = (CheckBox) findViewById(R.id.checkBox2);
        cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    builder.setMessage(R.string.dialog_message);
                    builder.setTitle(R.string.dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent resultIntent = new Intent(  );
                            resultIntent.putExtra("Response", "Here is my response");
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    })
                    .show();
                }
            }
        });
    }


    protected void onStart() {
        super.onStart();
        Log.i(ListItemsActivity, "In onStart()");
    }

    protected void onResume() {
        super.onResume();
        Log.i(ListItemsActivity, "In onResume()");
    }

    protected void onPause(){
        super.onPause();
        Log.i(ListItemsActivity, "In onPause()");
    }

    protected void onStop(){
        super.onStop();
        Log.i(ListItemsActivity, "In onStop()");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(ListItemsActivity, "In onDestroy()");
    }

    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iButton.setImageBitmap(imageBitmap);
        }
    }

}
