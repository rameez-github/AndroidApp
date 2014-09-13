/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.developer.album;

import org.json.JSONArray;

import com.developer.utils.DataKeeper;
import com.developer.utils.SafeJSONArray;
import com.developer.utils.SafeJSONObject;
import com.example.androidapp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class ActivityPhotoView extends Activity {

    private PhotoViewAttacher 	mAttacher;
    private boolean				mControlable;
	private int 				index_of_album;
	private int					index_of_photo;
	private ViewPager mViewPager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);

        index_of_album = getIntent().getIntExtra("index_of_album", 0);
        index_of_photo = getIntent().getIntExtra("index_of_photo", 0);
        
     // Locate the viewpager in activity_main.xml
        mViewPager = (ViewPager) findViewById(R.id.pager);
 
        // Set the ViewPagerAdapter into ViewPager
        mViewPager.setAdapter(new PhotoPagerAdapter(this));
        mViewPager.setCurrentItem(index_of_photo);
        mControlable = getIntent().getExtras().getBoolean("controlable");
        
        if (mControlable) {
        	((FrameLayout)findViewById(R.id.framelayout_control_panel)).setVisibility(View.VISIBLE);
        	
        	((ImageButton) findViewById(R.id.imagebutton_remove)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showAlertDialog ();
				}
        	});
        } else {
        	((FrameLayout)findViewById(R.id.framelayout_control_panel)).setVisibility(View.GONE);
        }
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAttacher.cleanup();
    }
    
    private class PhotoTapListener implements OnPhotoTapListener {
        @Override
        public void onPhotoTap(View view, float x, float y) {
        	setResult(RESULT_CANCELED);
        	finish();
        }
    }

    private class MatrixChangeListener implements OnMatrixChangedListener {
        @Override
        public void onMatrixChanged(RectF rect) {
        }
    }

	private class PhotoPagerAdapter extends PagerAdapter {
		
		private Context context;

		public PhotoPagerAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}
		
		private String getPhotoPath (int position){
			return DataKeeper.sharedInstance().getAlbums(context)
		    		  .getJSONObject(index_of_album).getJSONArray("all_path")
		    		  .getString(position);
		}

		@Override
		public int getCount() {
			
		      return DataKeeper.sharedInstance().getAlbums(context)
		    		  .getJSONObject(index_of_album).getJSONArray("all_path")
		    		  .length();
		}

		@Override
		public Object instantiateItem(View collection, int position) {

			LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			LinearLayout view = (LinearLayout) inflater.inflate(R.layout.adapter_photo_view_item, null);
			

	        ImageView mImageView = (ImageView) view.findViewById(R.id.iv_photo);
	        
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	        Bitmap bitmap = BitmapFactory.decodeFile(getPhotoPath(position), options);
	        
	        mImageView.setImageBitmap(bitmap);
	        
	        mAttacher = new PhotoViewAttacher(mImageView);

	        mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
	        mAttacher.setOnPhotoTapListener(new PhotoTapListener());
	        
		    	  
			((ViewPager) collection).addView(view);
		
			return view;
			
		}
		
		@Override
		public void destroyItem(View collection, int position, Object view) {
			LinearLayout layout= (LinearLayout) view;
			((ViewPager) collection).removeView(layout);
			layout = null;
		}
		
		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view==((LinearLayout)object);

		}
		
		@Override
		public void finishUpdate(View arg0) {}
		
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}

		@Override
		public Parcelable saveState() {

			return null;

		}
		
		@Override
		public void startUpdate(View arg0) {}
		
	}
	
	private void showAlertDialog (){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
		        	DataKeeper dataKeeper = DataKeeper.sharedInstance();
		        	SafeJSONArray album_json_array = dataKeeper.getAlbums(ActivityPhotoView.this);
					SafeJSONObject album_item = album_json_array.getJSONObject(index_of_album);
					SafeJSONArray all_path = album_item.getJSONArray("all_path");
					JSONArray new_all_path = all_path.remove(mViewPager.getCurrentItem(), all_path);
					
					//update values
					album_item.putSafeJSONArray("all_path", new_all_path);
					album_item.putInt("total_album_pics", new_all_path.length());
					
					// save and finish
					dataKeeper.saveAlbums(ActivityPhotoView.this, album_json_array.toString());
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
}
