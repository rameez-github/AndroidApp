package com.example.androidapp;

import java.util.ArrayList;

import com.developer.adapter.ChatAdapter;
import com.developer.model.ChatModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentChats extends Fragment implements OnItemClickListener{
	
	ArrayList<ChatModel> list;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_chats,
		        container, false);
		
		ListView listview = (ListView) view.findViewById(R.id.list_view);
		listview.setOnItemClickListener(this);
		
		addChatList ();
		
		listview.setAdapter(new ChatAdapter(getActivity(), list));
		
		return view;
	}
	
	private void addChatList () {
		list = new ArrayList<ChatModel>();
		
		list.add(new ChatModel("Bobby", "you reckon?", "1:40 AM", R.drawable.pic1, R.drawable.online));
		list.add(new ChatModel("Dave Hartmann", "Word bro, we got to setup up the whole stuff by tonight ...", "2:50", R.drawable.pic2, R.drawable.offline));
		list.add(new ChatModel("Oliver Jackson", "I'll think about decorations for the party", "4:8 AM", R.drawable.pic3, R.drawable.online));
		list.add(new ChatModel("Ganesh Jones", "Man, no one invited me to valentine's day. What should I do?", "11:25 AM", R.drawable.pic4, R.drawable.online));
		list.add(new ChatModel("Mimi Thomson", "We are at the surf festival on bells beach. Hang with us?", "12:55 AM", R.drawable.pic5, R.drawable.offline));
		list.add(new ChatModel("Kalyan Parvati", "Brooklyn was awesome. My neighbours too", "6:51 AM", R.drawable.pic6, R.drawable.load));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_home, new FragmentChatDetails());
		//////ft.addToBackStack(null);
		ft.commit();
	}
}
