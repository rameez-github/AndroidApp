package com.example.androidapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentProfile extends Fragment {
	
	
	private MainActivity mainActivity;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mainActivity = (MainActivity) activity;
		mainActivity.mLayout.canOpen = false;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		mainActivity.mLayout.canOpen = true;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_profile,
		        container, false);
		/**
		ImageView img1 = (ImageView) view.findViewById(R.id.user_pic);
	    Bitmap bm = BitmapFactory.decodeResource(getResources(),
	            R.drawable.pic2);
	    Bitmap resized = Bitmap.createScaledBitmap(bm, 100, 100, true);
	    Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
	    img1.setImageBitmap(conv_bm);
	    */
        // Locate the viewpager in activity_main.xml
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
 
        // Set the ViewPagerAdapter into ViewPager
        viewPager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager()));
        
		return view;
	}
	
	public class ViewPagerAdapter extends FragmentPagerAdapter {
		 
	    final int PAGE_COUNT = 3;
	    // Tab Titles
	    private String tabtitles[] = new String[] { "About", "Post", "Photos" };
	    Context context;
	 
	    public ViewPagerAdapter(FragmentManager fm) {
	        super(fm);
	    }
	 
	    @Override
	    public int getCount() {
	        return PAGE_COUNT;
	    }
	 
	    @Override
	    public Fragment getItem(int position) {
	        switch (position) {
	 
	            // Open FragmentTab1.java
	        case 0:
	            FragmentProfileAbout fragmenttab1 = new FragmentProfileAbout();
	            return fragmenttab1;
	 
	            // Open FragmentTab2.java
	        case 1:
	            FragmentProfilePost fragmenttab2 = new FragmentProfilePost();
	            return fragmenttab2;
	 
	            // Open FragmentTab3.java
	        case 2:
	            FragmentProfilePhotos fragmenttab3 = new FragmentProfilePhotos();
	            return fragmenttab3;
	        }
	        return null;
	    }
	 
	    @Override
	    public CharSequence getPageTitle(int position) {
	        return tabtitles[position];
	    }
	}
}
