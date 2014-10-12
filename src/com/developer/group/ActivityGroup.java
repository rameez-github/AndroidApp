package com.developer.group;

import com.example.androidapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class ActivityGroup extends FragmentActivity implements OnClickListener{

	private Fragment childFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_group);
		
		
		getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_group, childFragment = new FragmentCreateGroupStep1()).commitAllowingStateLoss();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}
