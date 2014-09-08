package com.developer.album;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import com.developer.utils.DataKeeper;
import com.developer.utils.DateUtility;
import com.developer.utils.SafeJSONArray;
import com.developer.utils.SafeJSONObject;
import com.example.androidapp.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ActivityCreateAlbum extends Activity{
	
	GridView gridGallery;
	Handler handler;
	GalleryAdapter adapter;
	ImageLoader imageLoader;
	private String[] all_path;
	private EditText edittext_album;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_album);

		initImageLoader();
		init();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		configure(getIntent());
	}
	
	private void initImageLoader() {
		@SuppressWarnings("deprecation")
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(defaultOptions).memoryCache(
				new WeakMemoryCache());

		ImageLoaderConfiguration config = builder.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
	}

	private void init() {

		handler = new Handler();
		edittext_album = (EditText) findViewById(R.id.edittext_album);
		gridGallery = (GridView) findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		adapter.setMultiplePick(false);
		gridGallery.setAdapter(adapter);
		
		((Button) findViewById(R.id.btn_create_album))
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DataKeeper dataKeeper = DataKeeper.sharedInstance();
				SafeJSONArray album_json_array = dataKeeper.getAlbums(ActivityCreateAlbum.this);
				Long tsLong = System.currentTimeMillis()/1000;
				String tstamp = tsLong.toString();
				
				SafeJSONObject json = new SafeJSONObject();
				
				if (edittext_album.getText().toString().equals(""))
					json.putString("album_title", DateUtility.changeTimeStampToString(tstamp, "dd. MMMM yyyy"));
				else
					json.putString("album_title", edittext_album.getText().toString());
				
				json.putString("created_at", tstamp);
				json.putInt("total_album_pics", all_path.length);
				SafeJSONArray json_paths = new SafeJSONArray();
				for (String path : all_path) {
					json_paths.put(path);
				}
				
				json.putSafeJSONArray("all_path", json_paths.getJSONArrayObject());
				album_json_array.put(json.getObject());
				dataKeeper.saveAlbums(ActivityCreateAlbum.this, album_json_array.toString());
				setResult(RESULT_OK);
				finish ();
				/*Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
				startActivityForResult(i, 200);*/
			}
		});
		
	}
	
	private void configure (Intent data){
		all_path = data.getStringArrayExtra("all_path");

		ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

		for (String string : all_path) {
			CustomGallery item = new CustomGallery();
			item.sdcardPath = string;
			Log.v("sdcardPath", "////---"+string);
			dataT.add(item);
		}
		adapter.addAll(dataT);
	}
}
