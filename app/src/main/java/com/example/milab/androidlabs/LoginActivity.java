package com.example.milab.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {
    protected static final String LoginActivity = "StartActivity";
    protected static final String login ="DefaultEmail";
    protected String defaultEmail = "email@domain.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i(LoginActivity, "In onCreate()");
        setContentView(R.layout.activity_login);

        final SharedPreferences prefs = getSharedPreferences(login, Context.MODE_PRIVATE);
        final EditText tv = (EditText) findViewById(R.id.editText);

        String email = prefs.getString("DefaultEmail", defaultEmail );
        tv.setText(email);

        final Button myButton = (Button)findViewById(R.id.button2);
        // Reading from SharedPreferences
        prefs.getString("DefaultEmail", defaultEmail);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tipedEmail = tv.getEditableText().toString();

                // Writing data to SharedPreferences
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("DefaultEmail",tipedEmail);
                edit.commit();

                Intent i = new Intent(LoginActivity.this, StartActivity.class);
                startActivity( i);
            }

        });

    }



    protected void onStart() {
        super.onStart();
        Log.i(LoginActivity, "In onStart()");
    }

    protected void onResume() {
        super.onResume();
        Log.i(LoginActivity, "In onResume()");
    }

    protected void onPause(){
        super.onPause();
        Log.i(LoginActivity, "In onPause()");
    }

    protected void onStop(){
        super.onStop();
        Log.i(LoginActivity, "In onStop()");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(LoginActivity, "In onDestroy()");
    }


}
