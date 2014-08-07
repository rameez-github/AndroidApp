package com.example.androidapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity implements View.OnClickListener, 
	OnItemClickListener{

	private Fragment childFragment;
	private MainActivity mActivity;
	private ListView menu_list_view;
	private ImprovedSlidingPaneLayout mLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mLayout = (ImprovedSlidingPaneLayout) findViewById(R.id.sliding_panel_layout);
		
		
		// set menu options on the left side menu in home screen
		MenuAdapter adapter = new MenuAdapter (this);
        menu_list_view = (ListView) findViewById(R.id.menu_list);
		menu_list_view.setAdapter(adapter);
		menu_list_view.setOnItemClickListener(this);
		
		// set listener for refresh and three bar buttons/icons... 
		//findViewById(R.id.refresh_button).setOnClickListener(this);
		findViewById(R.id.menu_button).setOnClickListener(this);
		

		getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_home, childFragment = new Screen1()).commitAllowingStateLoss();
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
		switch (position){
		case 0:
			changeFragment (new Screen1());
			mLayout.closePane();
			break;
		case 3:
			changeFragment (new Screen2());
			mLayout.closePane();
			break;
		case 4:
			changeFragment (new FragmentSettings());
			mLayout.closePane();
			break;
		}
	}
}
