package com.developer.album;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

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

public class ActivityAlbumDetail extends Activity{
	
	GridView gridGallery;
	GalleryAdapter adapter;
	ImageLoader imageLoader;
	private int index_of_album;
	private ArrayList<CustomGallery> dataT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_album_detail);

		initImageLoader();
		init();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		configure();
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

		index_of_album = getIntent().getIntExtra("index_of_album", 0);
		
		SafeJSONArray album_json_array = DataKeeper.sharedInstance().getAlbums(this);
		SafeJSONObject album_item = album_json_array.getJSONObject(index_of_album);
		
		TextView album_name = (TextView) findViewById(R.id.album_name);
		TextView created_at = (TextView) findViewById(R.id.created_at);
		TextView total_pics = (TextView) findViewById(R.id.album_total_pics);
		album_name.setText(album_item.getString("album_title"));
		total_pics.setText(album_item.getString("total_album_pics"));
		created_at.setText(DateUtility.changeTimeStampToString(album_item.getString("created_at"), "dd. MMMM yyyy"));

		gridGallery = (GridView) findViewById(R.id.gridGallery);
		gridGallery.setOnItemClickListener(new OnItemClickListener (){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				SafeJSONArray album_json_array = DataKeeper.sharedInstance().getAlbums(ActivityAlbumDetail.this);
				SafeJSONObject album_item = album_json_array.getJSONObject(index_of_album);
				SafeJSONArray all_path = album_item.getJSONArray("all_path");
					
				Intent i = new Intent(ActivityAlbumDetail.this, ActivityPhotoView.class);
				i.putExtra("photoPath", all_path.getString(position));
				i.putExtra("index_of_photo", position);
				i.putExtra("index_of_album", index_of_album);
				i.putExtra("controlable", true);
				startActivityForResult(i, 300);
			}
			
		});
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		adapter.setMultiplePick(false);
		gridGallery.setAdapter(adapter);
		
		((Button) findViewById(R.id.btn_add_photo))
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Intent i = new Intent(Action.ACTION_PICK);
				Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
				startActivityForResult(i, 100);
			}
		});
		

		((Button) findViewById(R.id.btn_remove_album))
		.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showAlertDialog ();
			}
		});
	}
	
	private void showAlertDialog (){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	DataKeeper dataKeeper = DataKeeper.sharedInstance();
					SafeJSONArray album_json_array = dataKeeper.getAlbums(ActivityAlbumDetail.this);
					SafeJSONArray new_array = album_json_array.removeSafeJsonObject(index_of_album, album_json_array);
					dataKeeper.saveAlbums(ActivityAlbumDetail.this, new_array.toString());
					setResult(RESULT_OK);
					finish();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
	}
	
	private void configure (){
		
		SafeJSONArray album_json_array = DataKeeper.sharedInstance().getAlbums(this);
		SafeJSONObject album_item = album_json_array.getJSONObject(index_of_album);
		SafeJSONArray all_path = album_item.getJSONArray("all_path");
		
		dataT = new ArrayList<CustomGallery>();

		for (int i=0; i<all_path.length(); i++) {
			CustomGallery item = new CustomGallery();
			item.sdcardPath = all_path.getString(i);
			//Log.v("sdcardPath", "////---"+item.sdcardPath);
			dataT.add(item);
		}
		adapter.addAll(dataT);
	}

	private void configure (String[] all_path){
		
		for (String string : all_path) {
			CustomGallery item = new CustomGallery();
			item.sdcardPath = string;
			//Log.v("sdcardPath", "////---"+string);
			dataT.add(item);
		}
		adapter.addAll(dataT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			setResult(RESULT_OK);
			String[] all_path = data.getStringArrayExtra("all_path");

			// update adapter
			configure (all_path);
			
			// update textview
			TextView total_pics = (TextView) findViewById(R.id.album_total_pics);
			total_pics.setText(""+dataT.size());
			
			// update json album
			DataKeeper dataKeeper = DataKeeper.sharedInstance();
			SafeJSONArray album_json_array = dataKeeper.getAlbums(this);
			
			// put path of pic
			SafeJSONArray all_path_array = album_json_array.getJSONObject(index_of_album).getJSONArray("all_path");
			for (int i=0; i<all_path.length; i++)
				all_path_array.put(all_path[i]);

			album_json_array.getJSONObject(index_of_album)
			.putInt("total_album_pics", dataT.size());// put total of pics
			
			dataKeeper.saveAlbums(this, album_json_array.toString());
			
		}
		else if (requestCode == 300 && resultCode == Activity.RESULT_OK){
			configure();
			// update textview
			TextView total_pics = (TextView) findViewById(R.id.album_total_pics);
			total_pics.setText(""+dataT.size());
						
			setResult(RESULT_OK);
		}
	}
}
