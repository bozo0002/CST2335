package com.example.milab.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//import static com.example.milab.androidlabs.WeatherForecast.ACTIVITY_NAME;

public class ChatWindow extends FragmentActivity {
    protected Button send;
    protected EditText message1;
    protected ListView lv;
    protected ArrayList<String> chatMessage = new ArrayList<>();
    protected   boolean fl;
    protected FrameLayout frame;
    protected  ChatDatabaseHelper mydbase;
    protected  SQLiteDatabase dbase;// = new ChatDatabaseHelper(getApplicationContext()).getReadableDatabase();
    protected Cursor cursor;
    protected ChatAdapter messageAdapter;

    //SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

         frame=(FrameLayout)findViewById(R.id.frame);
        fl = frame!=null;

        send = (Button) findViewById(R.id.button4);
        message1 = (EditText) findViewById(R.id.editText4);

        lv = (ListView) findViewById(R.id.chatView);

        //in this case, “this” is the ChatWindow, which is-A Context object
       messageAdapter = new ChatAdapter(this);
        lv.setAdapter(messageAdapter);

         mydbase = new ChatDatabaseHelper(getApplicationContext()) ;
         dbase = mydbase.getReadableDatabase();
         //mydbase.onUpgrade (dbase,1,2);


        //String[] projection = {dbase.KEY_ID, dbase.KEY_MESSAGE};
        cursor = dbase.rawQuery("select * from " + ChatDatabaseHelper.TABLE_NAME, null);
        cursor .moveToFirst();
        while(!cursor.isAfterLast() ){
            chatMessage.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i("ChatWindow", "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }
        /** cursor.moveToFirst();
        while(!cursor.isAfterLast() ) {
            Log.i("ChatWindow", "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }*/
        Log.i("ChatWindow", "Cursor’s  column count =" + cursor.getColumnCount() );
        for (int i = 0; i < cursor.getColumnCount(); i++ ){
            Log.i("ChatWindow", cursor.getColumnName(i));
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipedMessage = message1.getText().toString();
                if (!tipedMessage.isEmpty()){
                chatMessage.add(tipedMessage);
                    //Add text to database also
                    // Create a new map of values, where column names are the key
                    ContentValues values = new ContentValues();
                    values.put(ChatDatabaseHelper.KEY_MESSAGE, tipedMessage);
                    // Insert the new row, returning the primary key value of the new row
                    dbase.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
                    message1.setText("");

                }
                //Update cursor with new data
                cursor = dbase.rawQuery("SELECT * FROM "+ChatDatabaseHelper.TABLE_NAME, null);
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
            }


        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long itemID;
                String itemMessage;
                Log.i("ChatWindow", "I=="+position+" l=="+id);
                itemID = messageAdapter.getItemId(position);
                itemMessage = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                Log.i("ChatWindow", "ID=="+itemID+" ItemMessage=="+itemMessage);
                //Checking for phone
                Bundle args = new Bundle();
                args.putInt("lineViewID",position);
                args.putLong("lineID", itemID);
                args.putString("iMessage",itemMessage);

                if (!fl){
                    Log.i("ChatWindow", "==Processing Fragments for a Phone");
                    // Create fragment and give it an argument for the selected article
//                    MessageFragment newFragment = new MessageFragment();
                    Intent intent = new Intent(getApplicationContext(), MessageDetails.class);
                    intent.putExtra("data", args);
                    startActivityForResult(intent, 730);
                }
                // Checking for Tablet
                else {
                    Log.i("ChatWindow", "==Processing Fragments for a Tablet");
                    MessageFragment newFragment = new MessageFragment(ChatWindow.this);
                    newFragment.setArguments(args);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack so the user can navigate back
                    transaction.replace(R.id.frame, newFragment);
////        transaction.addToBackStack(null);
//        // Commit the transaction
                    transaction.commit();
                }
            }
        });
    }
    //On result return handler
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 730) {
            Log.i("ChatWindow", "==Returned to ChatWindow.onActivityResult");
            if (resultCode==737){
                Bundle args =  data.getBundleExtra("data");

                Long databaseKeyID = args.getLong("lineID");
                int lineViewID = args.getInt("lineViewID");
                Log.i("ChatWindow", "==Database deletion. lineID="+databaseKeyID+", lineViewID="+lineViewID);
                eraseMessage(lineViewID, databaseKeyID);
            }
        }
    }
    //Erasing message
    protected void eraseMessage(int lineViewID, Long lineID){
        // Deleting from Arraylist
        chatMessage.remove(lineViewID);
        // Deleting row from database
        dbase.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + "=" + lineID, null);
        //Update Cursor
        cursor = dbase.rawQuery("SELECT * FROM "+ChatDatabaseHelper.TABLE_NAME, null);
        messageAdapter.notifyDataSetChanged();
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
        @Override
        public long getItemId(int position){
            cursor.moveToPosition(position);
            String x;
            x =cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
            return Long.parseLong(x);
        }

}
    protected void onDestroy(){
        super.onDestroy();
        dbase.close();
        cursor.close();
        Log.i("ChatWindow", "In onDestroy()");

    }


}