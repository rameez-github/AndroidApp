package com.developer.adapter;

import java.io.File;
import java.util.ArrayList;

import com.developer.model.ChatModel;
import com.example.androidapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private ArrayList<ChatModel> list;

	public ChatAdapter (Context context, ArrayList<ChatModel> list) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.list = list;
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
			view = mInflater.inflate(R.layout.adapter_chat_list_item, null);
			holder = new ViewHolder();
			
			holder.user_pic = (ImageView) view.findViewById(R.id.user_pic);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.contact_message = (TextView) view.findViewById(R.id.contact_message);
			holder.time = (TextView) view.findViewById(R.id.time);
			holder.status = (ImageView) view.findViewById(R.id.marker);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.name.setText(list.get(position).name);
		holder.contact_message.setText(list.get(position).message);
		holder.time.setText(list.get(position).time);
		
		if (list.get(position).isFilePath){
			File imgFile = new  File(list.get(position).filePath);
			if(imgFile.exists()){
			    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			    holder.user_pic.setImageBitmap(myBitmap);

			}
		}else {
			holder.user_pic.setImageResource(list.get(position).imgRes);
		}
		holder.status.setImageResource(list.get(position).statusRes);
		
		return view;
	}
	
	static class ViewHolder {
		public ImageView user_pic, status;
		public TextView
		name,
		contact_message,
		time;
	}
}
