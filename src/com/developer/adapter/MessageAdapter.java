package com.developer.adapter;

import java.util.ArrayList;

import com.developer.model.Message;
import com.example.androidapp.R;

import android.content.Context;
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

	private static final int		MSG_LEFT		= 0;
	private static final int		MSG_RIGHT 		= 1;


	public MessageAdapter(Context context, ArrayList<Message> messages) {
		super();
		this.mContext = context;
		this.mMessages = messages;
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
		
		holder.configureValues(message, convertView);
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView message, time;
		ImageView imgv;

		public void initUIElements(View view, Context context) {
			message	= (TextView) view.findViewById(R.id.message_text);
			time	= (TextView) view.findViewById(R.id.message_time);
			imgv	= (ImageView) view.findViewById(R.id.user_pic);
		}

		public void configureValues(Message message, View view) {

			if (message.isMine()){} 
			else {imgv.setImageResource(message.getImageResourceId());}
			
			this.message.setText(message.getMessage());
			this.time.setText(message.getTimeOfMessage());
		}
			
	}

	@Override
	public long getItemId(int position) {
		//Unimplemented, because we aren't using Sqlite.
		return position;
	}

}
