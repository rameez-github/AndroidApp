package com.example.androidapp;

import com.developer.adapter.SettingsAdapter;
import com.developer.utils.Const;
import com.developer.utils.SafeJSONArray;
import com.developer.utils.SafeJSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentSettings extends Fragment{

	private SafeJSONArray list;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_settings,
		        container, false);
		ListView listview = (ListView) view.findViewById(R.id.list_view);
		
		addChatList ();
		
		listview.setAdapter(new SettingsAdapter(getActivity(), list));
		
		return view;
	}
	
	private void addChatList () {
		list = new SafeJSONArray();
		
		SafeJSONObject profile = new SafeJSONObject();
		profile.putString("user_name", "Gunez");
		profile.putString("status", "online");
		profile.putString("item_type", Const.TYPE_PROFILE);
		list.addJSONObject(profile);
		
		SafeJSONObject header1 = new SafeJSONObject();
		header1.putString("header", "YOUR PHONE NUMBER");
		header1.putString("item_type", Const.TYPE_HEADER);
		list.addJSONObject(header1);

		SafeJSONObject text1 = new SafeJSONObject();
		text1.putString("text", "+61 481");
		text1.putString("item_type", Const.TYPE_TEXT_1);
		list.addJSONObject(text1);
	}
}
