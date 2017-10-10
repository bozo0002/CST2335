package com.example.milab.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ChatWindow extends Activity {
    protected Button send;
    protected EditText message1;
    protected ListView lv;
    protected ArrayList<String> chatMessage = new ArrayList<>();;
    protected  SQLiteDatabase dbase;// = new ChatDatabaseHelper(getApplicationContext()).getReadableDatabase();
    protected Cursor cursor;
    //SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);



        send = (Button) findViewById(R.id.button4);
        message1 = (EditText) findViewById(R.id.editText4);

        lv = (ListView) findViewById(R.id.chatView);

        //in this case, “this” is the ChatWindow, which is-A Context object
       final ChatAdapter messageAdapter = new ChatAdapter(this);
        lv.setAdapter(messageAdapter);

        dbase = new ChatDatabaseHelper(getApplicationContext()).getReadableDatabase();
        //String[] projection = {dbase.KEY_ID, dbase.KEY_MESSAGE};
        cursor = dbase.rawQuery("select * from " + ChatDatabaseHelper.TABLE_NAME, null);
        cursor .moveToFirst();
        while(!cursor.isAfterLast() ){
            chatMessage.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }
        cursor.moveToFirst();
        while(!cursor.isAfterLast() ) {
            Log.i("ChatWindow", "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }
        Log.i("ChatWindow", "Cursor’s  column count =" + cursor.getColumnCount() );
        for (int i = 0; i < cursor.getColumnCount(); i++ ){
            Log.i("ChatWindow", cursor.getColumnName(i));
        }
        cursor.close();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipedMessage = message1.getText().toString();
                if (!tipedMessage.isEmpty()){
                chatMessage.add(tipedMessage);
                    ContentValues values = new ContentValues();
                    values.put(ChatDatabaseHelper.KEY_MESSAGE, tipedMessage);
                    // Insert the new row, returning the primary key value of the new row
                    dbase.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
                    message1.setText("");

                }
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
            }


        });


    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return chatMessage.size();
        }

        public String getItem(int position) {
            return chatMessage.get(position);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }else{
                result = inflater.inflate(R.layout.chat_row_outgoing, null);}
            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;


        }


}
    protected void onDestroy(){
        super.onDestroy();
        dbase.close();
        Log.i("ChatWindow", "In onDestroy()");

    }
}