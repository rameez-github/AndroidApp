package com.developer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.model.ContactModel;
import com.example.androidapp.R;

public class ContactGridAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private ArrayList<ContactModel> list;
	private int width;

	public ContactGridAdapter (Activity context, ArrayList<ContactModel> list) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.list = list;
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (view == null) {
			view = mInflater.inflate(R.layout.adapter_contact_grid_item, null);
			holder = new ViewHolder();
			
			holder.user_pic = (ImageView) view.findViewById(R.id.user_pic);
			holder.name = (TextView) view.findViewById(R.id.name);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.name.setText(list.get(position).name);
		holder.user_pic.setScaleType(ImageView.ScaleType.FIT_XY);
		holder.user_pic.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, width/3)); //100, 115
        
		holder.user_pic.setImageResource(list.get(position).imgRes);
		
		return view;
	}
	
	static class ViewHolder {
		public ImageView user_pic;
		public TextView name;
	}

}
