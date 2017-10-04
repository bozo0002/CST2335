package com.example.milab.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String tipedMessage = message1.getText().toString();
                if (!tipedMessage.isEmpty()){
                chatMessage.add(tipedMessage);
                ////System.out.println(tipedMessage);
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
}