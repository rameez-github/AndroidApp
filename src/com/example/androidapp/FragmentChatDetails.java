package com.example.androidapp;

import java.util.ArrayList;

import com.developer.adapter.MessageAdapter;
import com.developer.model.Message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FragmentChatDetails  extends Fragment{
	

	private ArrayList<Message> messages;
	private MessageAdapter adapter;
	private ListView listview;
	private EditText text;
	private Button send;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_chat_details,
		        container, false);

		text = (EditText) view.findViewById(R.id.text);
		send = (Button) view.findViewById(R.id.btn_send);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String newMessage = text.getText().toString().trim(); 
				if(newMessage.length() > 0)
				{
					text.setText("");
					addNewMessage(new Message(newMessage, "--:-- AM", true));
					//new SendMessage().execute();
				}
			}
		});
		messages = new ArrayList<Message>();

		messages.add(new Message("How about dinner", "2:19 PM", R.drawable.pic7, false));
		messages.add(new Message("Ok. See you at 7 o'clock!", "2:23 PM", true));
		messages.add(new Message("Wassup??", "2:31 PM", R.drawable.pic8, false));
		messages.add(new Message("nothing much, working on speech bubbles.", "2:33 PM", true));
		messages.add(new Message("you say!", "2:47 PM", true));
		messages.add(new Message("oh thats great. how are you showing them", "2:50 PM", R.drawable.pic7, false));
		

		adapter = new MessageAdapter(getActivity(), messages);
		setListAdapter(view, adapter);
		addNewMessage(new Message("mmm, well, using 9 patches png to show them.", "2:59 PM", true));
		
		return view;
	}

	private void setListAdapter(View view, MessageAdapter adapter2) {
		// TODO Auto-generated method stub
		listview = (ListView) view.findViewById(R.id.list);
		listview.setAdapter(adapter2);
	}
	
	private void addNewMessage(Message m)
	{
		messages.add(m);
		adapter.notifyDataSetChanged();
		listview.setSelection(messages.size()-1);
	}
}
