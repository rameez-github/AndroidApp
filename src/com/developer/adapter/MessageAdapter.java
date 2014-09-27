package com.developer.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.developer.model.Album;
import com.developer.model.Audio;
import com.developer.model.Message;
import com.example.androidapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 *
 */
public class MessageAdapter extends BaseAdapter{
	private Context 				mContext;
	private ArrayList<Message> 		mMessages;
	private ImageLoader 			imageLoader;

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
	    private Message message;

		// simple message fields
		View 			text_layout;
		TextView 		message_text;
		TextView 		time; 
		ImageView 		user_pic;

		// album fields
		View 			album_layout;
		TextView 		time1; 
		TextView 		album_title;
		TextView 		album_total_pics;
		ImageView 		album_photo;

		// audio fields
		View 			audio_layout;
		TextView 		audio_text_duration;
		ImageView 		audio_user_pic;
		ImageView 		btn_play;
		SeekBar 		mSeekbar;
		
	    MediaPlayer   	mPlayer = null;
	    final Handler 	handler = new Handler();
	    
		public void initUIElements(View view, Context context) {
			
			// simple message fields
			message_text	= (TextView) view.findViewById(R.id.message_text);
			time	= (TextView) view.findViewById(R.id.message_time);
			user_pic	= (ImageView) view.findViewById(R.id.user_pic);
			text_layout = view.findViewById(R.id.simple_text_layout);
			
			// album fields
			time1 = (TextView) view.findViewById(R.id.message_time1);
			album_layout = view.findViewById(R.id.album_layout);
			album_title	= (TextView) view.findViewById(R.id.album_title);
			album_total_pics	= (TextView) view.findViewById(R.id.album_total_pics);
			album_photo	= (ImageView) view.findViewById(R.id.album_photo);
			
			// audio fields
			audio_layout = view.findViewById(R.id.audio_layout);
			btn_play = (ImageView) view.findViewById(R.id.btn_play);
			audio_user_pic = (ImageView) view.findViewById(R.id.audio_user_pic);
	        mSeekbar = (SeekBar) view.findViewById(R.id.seekbar);
			audio_text_duration	= (TextView) view.findViewById(R.id.text_duration);
			
		}

		public void configureValues(Message message, View view, ImageLoader imageLoader) {
			this.message = message;
			if (message.isMine()){
			}
			// from other contact
			else {
				if (message.hasAudio()){
					user_pic.setVisibility(View.GONE);
				}else {
					user_pic.setVisibility(View.VISIBLE);
					user_pic.setImageResource(message.getImageResourceId());
				}
			}
			
			// configure value with checking of message type

			if (message.hasAlbum()){
				album_layout.setVisibility(View.VISIBLE);
				text_layout.setVisibility(View.GONE);
				audio_layout.setVisibility(View.GONE);
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
			}else if (message.hasAudio()){
				audio_layout.setVisibility(View.VISIBLE);
				text_layout.setVisibility(View.GONE);
				album_layout.setVisibility(View.GONE);
				final Audio audio = message.getAudio();
				audio_text_duration.setText(audio.duration_text);
				mSeekbar.setOnSeekBarChangeListener(seekBarChanged);
				btn_play.setOnClickListener(new OnClickListener (){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startPlaying ();
					}
					
				});
			}
			else {
				text_layout.setVisibility(View.VISIBLE);
				album_layout.setVisibility(View.GONE);
				audio_layout.setVisibility(View.GONE);
				this.message_text.setText(message.getSpanMessage());
				this.time.setText(message.getTimeOfMessage());
			}
		}

	    private void startPlaying() {
	    	if (mPlayer == null){
	    		btn_play.setImageResource(R.drawable.pause);
		        mPlayer = new MediaPlayer();
		        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
		            
		            @Override
		            public void onCompletion(MediaPlayer mp) {
		                    stopPlaying();
		            }
		        });
		        
		        // set seekbar progress
		        mSeekbar.setProgress(0);
		        
		        try {
		            mPlayer.setDataSource(message.getAudio().path);
		            mPlayer.prepare();
		            mPlayer.start();
		        } catch (IOException e) {
		            Log.e("", "prepare() failed");
		        }
		        
		        // set max duration to seekbar
		        mSeekbar.setMax(mPlayer.getDuration());
		        
		        // update position
		        updatePosition();
	    	}
	    	else {
	    		if (mPlayer.isPlaying()){
	    			btn_play.setImageResource(R.drawable.play);
	                mPlayer.pause();
	    		}else {
	    			btn_play.setImageResource(R.drawable.pause);
	    			mPlayer.seekTo(mPlayer.getCurrentPosition());
	    			mPlayer.start();
	    	        updatePosition();
	    		}
	    	}
	    }
	    
	    private void startSeekTo(int progress) {
	    	btn_play.setImageResource(R.drawable.pause);
	    	mPlayer = new MediaPlayer();
	    	mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	    		
	    		@Override
	    		public void onCompletion(MediaPlayer mp) {
	    			stopPlaying();
	    		}
	    	});
		    
	    	try {
	    		mPlayer.setDataSource(message.getAudio().path);
	    		mPlayer.prepare();
	    		mPlayer.seekTo(progress);
	    		mPlayer.start();
	    	} catch (IOException e) {
	    		Log.e("", "prepare() failed");
	    	}
		        
	    	// set max duration to seekbar
	    	mSeekbar.setMax(mPlayer.getDuration());
	    	
	    	// update position
	    	updatePosition();
	    }
	    
	    private void stopPlaying() {
	    	// remove call backs before release media player
	    	handler.removeCallbacks(updatePositionRunnable);
	    	
	    	// update drawable
	    	btn_play.setImageResource(R.drawable.play);
	    	
	        mPlayer.release();
	        mPlayer = null;

	        // set seekbar progress to zero
	        mSeekbar.setProgress(0);
	    }

	    private void updatePosition(){
	    	handler.removeCallbacks(updatePositionRunnable);
	    	
	    	mSeekbar.setProgress(mPlayer.getCurrentPosition());
	            
	    	handler.postDelayed(updatePositionRunnable, Audio.UPDATE_FREQUENCY);
	    }
	    

	    private final Runnable updatePositionRunnable = new Runnable() {
	        
	    	@Override
	    	public void run() {
	    		int startTime = mPlayer.getCurrentPosition();
	    		audio_text_duration.setText(String.format("%d min, %d sec", 
	    				TimeUnit.MILLISECONDS.toMinutes((long) startTime),
	    				TimeUnit.MILLISECONDS.toSeconds((long) startTime) - 
	    				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
	    						toMinutes((long) startTime)))
	    				);
	    		updatePosition();
	    	}
	    };

	    private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {
	        private boolean isMoveingSeekBar;

			@Override
	        public void onStopTrackingTouch(SeekBar seekBar) {
				isMoveingSeekBar = false;
	        }
	        
	        @Override
	        public void onStartTrackingTouch(SeekBar seekBar) {
	        	isMoveingSeekBar = true;
	        }
	        
	        @Override
	        public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
	        	if(isMoveingSeekBar)
	        	{
	        		if (mPlayer == null){
	        			Log.v("seek bar changed", " ---- changed seek bar");
	        			startSeekTo (progress);
	        		}else {
	        			mPlayer.seekTo(progress);
	        		}
	        		Log.i("OnSeekBarChangeListener","onProgressChanged");
	        	}
	        }
	    };
	    
	}

	@Override
	public long getItemId(int position) {
		//Unimplemented, because we aren't using Sqlite.
		return position;
	}

}
