package com.example.androidapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentContactRequest extends Fragment{

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_contact_request,
		        container, false);
		
		return view;
	}
}
