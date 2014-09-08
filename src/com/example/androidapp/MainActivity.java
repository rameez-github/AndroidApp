package com.example.androidapp;


import java.util.Date;
import java.util.HashMap;

import com.developer.adapter.MenuAdapter;
import com.developer.adapter.MyInfoWindowAdapter;
import com.developer.album.ActivityAlbumList;
import com.developer.model.EventInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity implements View.OnClickListener, 
	OnItemClickListener, PanelSlideListener{
	
	@SuppressWarnings("unused")
	private Fragment childFragment;
	private ListView menu_list_view;
	public ImprovedSlidingPaneLayout mLayout;
	private boolean isPanelClosed = true;
	private MainMapFragement mapFragment;
	private HashMap<Marker, EventInfo> eventMarkerMap;
	private GoogleMap mMap;
	
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
		
		findViewById(R.id.map_button).setOnClickListener(this);
		findViewById(R.id.menu_button).setOnClickListener(this);
		findViewById(R.id.album_button).setOnClickListener(this);

		getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_home, childFragment = new FragmentHome()).commitAllowingStateLoss();
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.menu_button){
			mLayout.openPane();
		}
		else if (v.getId() == R.id.map_button){
			setUpEventSpots ();
		}
		else if (v.getId() == R.id.album_button){
			startActivity(new Intent (this, ActivityAlbumList.class));
		}
	}
	

	private void changeFragment (Fragment newFragment){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_home, newFragment);
		ft.commit();
		childFragment = newFragment;
	}

	private void setUpEventSpots() {

		if (mapFragment == null) {
			// map fragment
			mapFragment = new MainMapFragement(){
				@Override
				public void onActivityCreated(Bundle savedInstanceState) {
					super.onActivityCreated(savedInstanceState);
					mMap = mapFragment.getMap();
					mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					addMapPins();
				}
			};
		} else {
			if (mMap != null) {
				mMap.clear();
				addMapPins();
			}
		}

		getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_home, mapFragment).commit();
	}

	private void addMapPins (){
		
		// I'm going to make 2 EventInfo objects and place them on the map
		EventInfo firstEventInfo = new EventInfo(new LatLng(50.154, 4.35), "Right now - event", new Date(), "Party", R.drawable.pic6);
		EventInfo secondEventInfo = new EventInfo(new LatLng(51.25, 4.15), "Future Event", new Date(1032, 5, 25), "Convention", R.drawable.pic5);
		//this date constructor is deprecated but it's just to make a simple example
		

		Marker firstMarker= mapFragment.placeMarker(mMap, firstEventInfo);
		Marker secondMarker = mapFragment.placeMarker(mMap, secondEventInfo);
		
		eventMarkerMap = new HashMap<Marker, EventInfo>();
		eventMarkerMap.put(firstMarker, firstEventInfo);
		eventMarkerMap.put(secondMarker, secondEventInfo);
		
		
		//add the onClickInfoWindowListener
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				EventInfo eventInfo = eventMarkerMap.get(marker);
				Toast.makeText(getBaseContext(),
						"The date of " + eventInfo.getName() + " is " + eventInfo.getSomeDate().toLocaleString(),
						Toast.LENGTH_LONG).show();
				
			}
		});		
		
		mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this, eventMarkerMap));
		
		
		/**
		 *  set map common
		 */
		final LatLngBounds.Builder builder = new LatLngBounds.Builder();
		
		builder.include(firstMarker.getPosition());
		builder.include(secondMarker.getPosition());
		
		mMap.setOnCameraChangeListener(new OnCameraChangeListener(){
			@Override
			public void onCameraChange(CameraPosition arg0) {
				mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 2));
				mMap.setOnCameraChangeListener(null);
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long arg3) {
		// TODO Auto-generated method stub

		findViewById(R.id.map_button).setVisibility(View.VISIBLE);
		findViewById(R.id.refresh_button).setVisibility(View.VISIBLE);
		findViewById(R.id.album_button).setVisibility(View.GONE);
		
		
		if (isPanelClosed) // do nothing
			return;
		
		switch (position){
		case 0:
			changeFragment (new FragmentHome());
			mLayout.closePane();
			break;
		case 1:
			findViewById(R.id.map_button).setVisibility(View.GONE);
			findViewById(R.id.refresh_button).setVisibility(View.GONE);
			findViewById(R.id.album_button).setVisibility(View.VISIBLE);
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
