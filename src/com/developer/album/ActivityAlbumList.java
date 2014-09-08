package com.developer.album;

import com.developer.utils.DataKeeper;
import com.developer.utils.SafeJSONArray;
import com.example.androidapp.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ActivityAlbumList extends Activity implements OnItemClickListener{


	private Button btnGalleryPickMul;
	private ImageLoader imageLoader;
	private AdapterAlbumList mAdapterAlbumList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_album_list);

		initImageLoader();
		init();
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
		ListView listview = (ListView) findViewById(R.id.list_view);
		listview.setOnItemClickListener(this);
		
		SafeJSONArray list = DataKeeper.sharedInstance().getAlbums(this);
		Log.v("log album", "--- "+list.toString());
		mAdapterAlbumList = new AdapterAlbumList(this, imageLoader, list);
		listview.setAdapter(mAdapterAlbumList);
		btnGalleryPickMul = (Button) findViewById(R.id.btn_create_album);
		btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
				startActivityForResult(i, 200);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 200 && resultCode == Activity.RESULT_OK) {

			String[] all_path = data.getStringArrayExtra("all_path");
			Intent i = new Intent(ActivityAlbumList.this, ActivityCreateAlbum.class);
			i.putExtra("all_path", all_path);
			startActivityForResult(i, 100);
		}
		else if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
			//Toast.makeText(this, "CHEERS", Toast.LENGTH_LONG).show();

			SafeJSONArray list = DataKeeper.sharedInstance().getAlbums(this);
			Log.v("log album", "--- "+list.toString());
			mAdapterAlbumList.addAll(list);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent i = new Intent(ActivityAlbumList.this, ActivityAlbumDetail.class);
		i.putExtra("index_of_album", position);
		startActivityForResult(i , 100);
	}
}
