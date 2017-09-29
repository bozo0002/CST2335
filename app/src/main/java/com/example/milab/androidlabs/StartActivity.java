package com.example.milab.androidlabs;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import static android.R.attr.data;
import static android.R.attr.duration;
import static android.widget.Toast.LENGTH_SHORT;

public class StartActivity extends Activity {
    protected static final String StartActivity = "StartActivity";
    protected  final int requesCode = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(StartActivity, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(i, 10);// 1== return value
            }
        });
    }
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requesCode == 10) {
             Log.i(StartActivity,"Returned to StartActivity.onActivityResult");
            if (resultCode == Activity.RESULT_OK) {
                String messagePassed = data.getStringExtra("Response");
                String text = "ListItemsActivity passed: My information to share"+ messagePassed;
                Toast toast = Toast.makeText(StartActivity.this , text, LENGTH_SHORT); //this is the ListActivity
                toast.show(); //display your message box

            }
        }
    }


        // protected void onCreate(Bundle savedInstanceState);

    protected void onStart() {
        super.onStart();
        Log.i(StartActivity, "In onStart()");
    }

    protected void onResume() {
        super.onResume();
        Log.i(StartActivity, "In onResume()");
    }

    protected void onPause(){
        super.onPause();
        Log.i(StartActivity, "In onPause()");
    }

    protected void onStop(){
        super.onStop();
        Log.i(StartActivity, "In onStop()");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(StartActivity, "In onDestroy()");
    }

}
