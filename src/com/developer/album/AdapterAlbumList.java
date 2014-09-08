package com.developer.album;

import com.developer.utils.DateUtility;
import com.developer.utils.SafeJSONArray;
import com.developer.utils.SafeJSONObject;
import com.example.androidapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterAlbumList extends BaseAdapter{
	
	private SafeJSONArray list;
	private Context mContext;
	private ImageLoader imageLoader;

	private static final int		ALBUM_OF_1		= 0;
	private static final int		ALBUM_OF_2		= 1;
	private static final int		ALBUM_OF_3		= 2;
	private static final int		ALBUM_OF_4		= 3;
	private static final int		ALBUM_OF_6		= 4;


	public AdapterAlbumList(Context context, ImageLoader imageLoader1, SafeJSONArray list2) {
		super();
		this.mContext = context;
		this.list = list2;
		this.imageLoader = imageLoader1;
	}

	public void addAll(SafeJSONArray list2) {

		try {
			this.list = list2; 
		} catch (Exception e) {
			e.printStackTrace();
		}

		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.length();
	}
	
	@Override
	public SafeJSONObject getItem(int position) {		
		return list.getJSONObject(position);
	}

	@Override
	public long getItemId(int position) {
		//Unimplemented, because we aren't using Sqlite.
		return position;
	}

	@Override
    public int getItemViewType(int position) {
		SafeJSONObject album = getItem(position);
		int length = album.getInt("total_album_pics");
		if(length > 4)
		{
			return ALBUM_OF_6;
	            
        } else if(length == 4){
        	return ALBUM_OF_4;
            
        } else if(length == 3){
        	return ALBUM_OF_3;
            
        } else if(length == 2){
        	return ALBUM_OF_2;
            
        } else {
        	return ALBUM_OF_1;
            
        }
	}
	
    @Override
    public int getViewTypeCount() {
        return 5;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SafeJSONObject album = this.getItem(position);

		ViewHolder holder; 
		if(convertView == null)
		{

		    if (getItemViewType(position) == ALBUM_OF_6){
		    	convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_album_of_6, parent, false);
		    
		    } else if (getItemViewType(position) == ALBUM_OF_4) {
		    	
		    	convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_album_of_4, parent, false);
		    } else if (getItemViewType(position) == ALBUM_OF_3) {
		    	
		    	convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_album_of_3, parent, false);
		    } else if (getItemViewType(position) == ALBUM_OF_2) {
		    	
		    	convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_album_of_2, parent, false);
		    } else {// if (getItemViewType(position) == ALBUM_OF_1) {
		    	
		    	convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_album_of_1, parent, false);
		    }
		    
			holder = new ViewHolder();
			
			convertView.setTag(holder);

			holder.initUIElements(convertView, mContext, getItemViewType(position), imageLoader);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.configureValues(album, convertView);
		return convertView;
	}

	private static class ViewHolder {
		private ImageView[] album_pics;
		private TextView album_name, created_at, total_pics;
		private ImageLoader imageLoader;
		
		public void initUIElements(View view, Context context, int album_type, ImageLoader imageLoader) {
			if(album_type == ALBUM_OF_6){
				album_pics = new ImageView [6];
				album_pics[0] = (ImageView) view.findViewById(R.id.album_pic1);
				album_pics[1] = (ImageView) view.findViewById(R.id.album_pic2);
				album_pics[2] = (ImageView) view.findViewById(R.id.album_pic3);
				album_pics[3] = (ImageView) view.findViewById(R.id.album_pic4);
				album_pics[4] = (ImageView) view.findViewById(R.id.album_pic5);
				album_pics[5] = (ImageView) view.findViewById(R.id.album_pic6);
				
	        } else if(album_type == ALBUM_OF_4){
				album_pics = new ImageView [4];
				album_pics[0] = (ImageView) view.findViewById(R.id.album_pic1);
				album_pics[1] = (ImageView) view.findViewById(R.id.album_pic2);
				album_pics[2] = (ImageView) view.findViewById(R.id.album_pic3);
				album_pics[3] = (ImageView) view.findViewById(R.id.album_pic4);
				
	        } else if(album_type == ALBUM_OF_3){
				album_pics = new ImageView [3];
				album_pics[0] = (ImageView) view.findViewById(R.id.album_pic1);
				album_pics[1] = (ImageView) view.findViewById(R.id.album_pic2);
				album_pics[2] = (ImageView) view.findViewById(R.id.album_pic3);
	        } else if(album_type == ALBUM_OF_2){
				album_pics = new ImageView [2];
				album_pics[0] = (ImageView) view.findViewById(R.id.album_pic1);
				album_pics[1] = (ImageView) view.findViewById(R.id.album_pic2);
	        } else { // ALBUM_OF_1){
				album_pics = new ImageView [1];
				album_pics[0] = (ImageView) view.findViewById(R.id.album_pic1);
	        }
			album_name	= (TextView) view.findViewById(R.id.album_name);
			created_at	= (TextView) view.findViewById(R.id.created_at);
			total_pics	= (TextView) view.findViewById(R.id.album_total_pics);
			
			this.imageLoader = imageLoader;
		}

		public void configureValues(SafeJSONObject album, View view) {
			album_name.setText(album.getString("album_title"));
			total_pics.setText(album.getString("total_album_pics"));
			created_at.setText(DateUtility.changeTimeStampToString(album.getString("created_at"), "dd. MMMM yyyy"));

			for (int i=0; i<album_pics.length; i++){
				try {
					final ImageView imgQueue = album_pics[i];
					this.imageLoader.displayImage("file://" + album.getJSONArray("all_path").getString(i),
							imgQueue, new SimpleImageLoadingListener() {
								@Override
								public void onLoadingStarted(String imageUri, View view) {
									imgQueue
											.setImageResource(R.drawable.no_media);
									super.onLoadingStarted(imageUri, view);
								}
							});	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
