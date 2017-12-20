package com.example.milab.androidlabs;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private boolean devicePhone;

    public MessageFragment() {
        //Empty constructor - initiated as a phone
        devicePhone = true;
    }

    @SuppressLint("ValidFragment")
    public MessageFragment(ChatWindow chatWindow) {
        devicePhone = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        final Bundle args = this.getArguments();
        TextView messageText =(TextView) view.findViewById(R.id.MessageHere);
        TextView messageID = (TextView) view.findViewById(R.id.textMessageID);
        // Setting boxes with texts from bundles
        messageText.setText(args.getString("iMessage"));
        final Long lineID = args.getLong("lineID");
        final int lineViewID = args.getInt("lineViewID");
        messageID.setText(Long.toString(lineID));

        Button deleteButton = (Button)view.findViewById(R.id.buttonDeleteThisMessage);
        //Delete button handler
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (devicePhone) {
                    //Working with a phone
                    Intent intent = new Intent();
                    intent.putExtra("data", args);
                    getActivity().setResult(737, intent);
                    getActivity().finish();
                } else{
                    //If device is a tablet
                    ((ChatWindow)getActivity()).eraseMessage(lineViewID, lineID);
                }
            }
        });

        return view;
    }

}
