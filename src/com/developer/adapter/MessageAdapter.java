package com.developer.adapter;

import java.util.ArrayList;

import com.developer.model.Album;
import com.developer.model.Message;
import com.example.androidapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 *
 */
public class MessageAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<Message> mMessages;
	private ImageLoader imageLoader;

	private static final int		MSG_LEFT		= 0;
	private static final int		MSG_RIGHT 		= 1;


	public MessageAdapter(Context context, ArrayList<Message> messages, ImageLoader imageLoader) {
		super();
		this.mContext = context;
		this.mMessages = messages;
		this.imageLoader = imageLoader;
	}
	@Override
	public int getCount() {
		return mMessages.size();
	}
	
	@Override
	public Message getItem(int position) {		
		return mMessages.get(position);
	}

	@Override
    public int getItemViewType(int position) {
		Message message = getItem(position);
		if(message.isMine())
		{
			return MSG_RIGHT;
	            
        } else {
        	return MSG_LEFT;
            
        }
	}
	
    @Override
    public int getViewTypeCount() {
        return 2;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = (Message) this.getItem(position);

		ViewHolder holder; 
		if(convertView == null)
		{

		    if (getItemViewType(position) == MSG_RIGHT){
		    	convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_message_right, parent, false);
		    
		    } else if (getItemViewType(position) == MSG_LEFT) {
		    	
		    	convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_message_left, parent, false);
		    }
			holder = new ViewHolder();
			
			convertView.setTag(holder);

			holder.initUIElements(convertView, mContext);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.configureValues(message, convertView, imageLoader);
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView message, time, time1, album_title, album_total_pics;
		ImageView imgv, album_photo;
		View text_layout, album_layout;

		public void initUIElements(View view, Context context) {
			message	= (TextView) view.findViewById(R.id.message_text);
			time	= (TextView) view.findViewById(R.id.message_time);
			imgv	= (ImageView) view.findViewById(R.id.user_pic);
			text_layout = view.findViewById(R.id.simple_text_layout);
			
			// album fields
			time1 = (TextView) view.findViewById(R.id.message_time1);
			album_layout = view.findViewById(R.id.album_layout);
			album_title	= (TextView) view.findViewById(R.id.album_title);
			album_total_pics	= (TextView) view.findViewById(R.id.album_total_pics);
			album_photo	= (ImageView) view.findViewById(R.id.album_photo);
			
		}

		public void configureValues(Message message, View view, ImageLoader imageLoader) {

			if (message.isMine()){
			}
			// from other contact
			else {
				imgv.setImageResource(message.getImageResourceId());
			}
			
			// configure value with checking of message type

			if (message.hasAlbum()){
				album_layout.setVisibility(View.VISIBLE);
				text_layout.setVisibility(View.GONE);
				Album album = message.getAlbum();
				this.album_title.setText(album.album_title);
				this.album_total_pics.setText("Total "+album.total_album_pics);
				imageLoader.displayImage("file://" + album.pic_path,
						album_photo, new SimpleImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						album_photo.setImageResource(R.drawable.no_media);
						super.onLoadingStarted(imageUri, view);
					}
				});
			}else {
				text_layout.setVisibility(View.VISIBLE);
				album_layout.setVisibility(View.GONE);
				this.message.setText(message.getSpanMessage());
				this.time.setText(message.getTimeOfMessage());
			}
		}
			
	}

	@Override
	public long getItemId(int position) {
		//Unimplemented, because we aren't using Sqlite.
		return position;
	}

}
