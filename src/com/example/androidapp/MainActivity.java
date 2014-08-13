package com.example.androidapp;


import com.developer.adapter.MenuAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity implements View.OnClickListener, 
	OnItemClickListener, PanelSlideListener{

	@SuppressWarnings("unused")
	private Fragment childFragment;
	private ListView menu_list_view;
	public ImprovedSlidingPaneLayout mLayout;
	private boolean isPanelClosed = true;;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mLayout = (ImprovedSlidingPaneLayout) findViewById(R.id.sliding_panel_layout);
		mLayout.setPanelSlideListener(this);
		
		// set menu options on the left side menu in home screen
		MenuAdapter adapter = new MenuAdapter (this);
        menu_list_view = (ListView) findViewById(R.id.menu_list);
		menu_list_view.setAdapter(adapter);
		menu_list_view.setOnItemClickListener(this);
		
		//findViewById(R.id.refresh_button).setOnClickListener(this);
		findViewById(R.id.menu_button).setOnClickListener(this);

		getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_home, childFragment = new FragmentHome()).commitAllowingStateLoss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.menu_button){
			mLayout.openPane();
		}
	}
	

	private void changeFragment (Fragment newFragment){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_home, newFragment);
		ft.commit();
		childFragment = newFragment;
	}


	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long arg3) {
		// TODO Auto-generated method stub
		
		if (isPanelClosed) // do nothing
			return;
		
		switch (position){
		case 0:
			changeFragment (new FragmentHome());
			mLayout.closePane();
			break;
		case 1:
			changeFragment (new FragmentChats());
			mLayout.closePane();
			break;
		case 2:
			changeFragment (new FragmentContact ());
			mLayout.closePane();
			break;
		case 3:
			changeFragment (new FragmentShakeToMake());
			mLayout.closePane();
			break;
		case 4:
			changeFragment (new FragmentSettings());
			mLayout.closePane();
			break;
		}
	}

	@Override
	public void onPanelClosed(View arg0) {
		// TODO Auto-generated method stub
		this.isPanelClosed = true;
	}

	@Override
	public void onPanelOpened(View arg0) {
		// TODO Auto-generated method stub
		this.isPanelClosed = false;
	}

	@Override
	public void onPanelSlide(View arg0, float arg1) { /* TODO Auto-generated method stub */ }
}
