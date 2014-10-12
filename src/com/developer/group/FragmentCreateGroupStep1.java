package com.developer.group;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidapp.R;

public class FragmentCreateGroupStep1 extends Fragment implements OnClickListener{


	private 	ImageView			mImageViewPhoto;
    private 	Uri					mImageCaptureUri;
	private 	EditText 			mGroupNameEdit;
	private 	String 				mImageFilePath;
    //private 	Bitmap				mNewPhoto;

    private static final int		PICK_FROM_CAMERA = 1;
    private static final int 		CROP_FROM_CAMERA = 2;
    private static final int 		PICK_FROM_FILE = 3;
	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_create_group_step1,
		        container, false);
		
		mImageViewPhoto 		= (ImageView) view.findViewById(R.id.imgview_circular);
		mGroupNameEdit 			= (EditText) view.findViewById(R.id.edit_group_name);
		
		view.findViewById(R.id.imgview_circular).setOnClickListener(this);
		view.findViewById(R.id.btn_step_back).setOnClickListener(this);
		view.findViewById(R.id.btn_next).setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.imgview_circular){
			openChoosePictureDialog();
		}
		else if (v.getId() == R.id.btn_step_back){
			getActivity().finish();
		}
		else if (v.getId() == R.id.btn_next){
			if (mGroupNameEdit.getText().toString().length() == 0){
				Toast t = Toast.makeText(getActivity(), "Group Name cannot be empty", Toast.LENGTH_LONG);
				t.setGravity(Gravity.TOP, 0, 100);
				t.show();
				return;
			}
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			FragmentCreateGroupFinalStep fragmnt = new FragmentCreateGroupFinalStep();
		    Bundle arguments = new Bundle();
		    arguments.putString("group_name", mGroupNameEdit.getText().toString().trim());
		    arguments.putString("file_path", mImageFilePath);
		    fragmnt.setArguments(arguments);
			ft.replace(R.id.fragment_group, fragmnt);
			ft.addToBackStack(null);
			ft.commit();
		}

	    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}
	
	private void openChoosePictureDialog() {
        final String [] items			= new String [] {"Camera", "Gallery", "Cancel"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (getActivity(), android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(getActivity());
		
		builder.setTitle("Select photo");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) { //pick from camera
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(Environment.getExternalStorageDirectory(),
							   "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
					mImageFilePath = file.getAbsolutePath();
			    	Log.v("galler", "file: "+mImageFilePath);
					mImageCaptureUri = Uri.fromFile(file);

					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

					try {
						intent.putExtra("return-data", true);
						
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else if (item == 1) { //pick from file
					Intent intent = new Intent();
					
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                
	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );
		
		final AlertDialog dialog = builder.create();
		
		dialog.show();
    }
	

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != Activity.RESULT_OK) return;
	   
	    switch (requestCode) {
		    case PICK_FROM_CAMERA:
		    	doCrop();
		    	
		    	break;
		    	
		    case PICK_FROM_FILE: 
		    	mImageCaptureUri = data.getData();
	        	mImageFilePath = getRealPathFromURI(mImageCaptureUri);
		    	Log.v("galler", "file: "+mImageFilePath);
		    	doCrop();
	    
		    	break;	    	
	    
		    case CROP_FROM_CAMERA:	    	
		        Bundle extras = data.getExtras();
	
		        if (extras != null) {	        	
		            Bitmap photo = extras.getParcelable("data");
		            
		            mImageViewPhoto.setImageBitmap(photo);
		            //this.mNewPhoto = photo;
		            
		        }
		        //Log.v("fileexist", "fileexist: "+mImageCaptureUri.getPath());
		        //File f = new File(mImageCaptureUri.getPath());            
		        
		        //if (f.exists()) {f.delete(); Log.v("fileexist", "fileexist: deleted");}
	
		        break;

	    }
	}
    
    private void doCrop() {
		
    	Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities( intent, 0 );
        
        int size = list.size();
        
        if (size == 0) {	        
        	Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();
        	
            return;
        } else {
        	
        	//call the standard crop action intent (the user device may not support it)
        	Intent cropIntent = new Intent("com.android.camera.action.CROP");
        	    //indicate image type and Uri
        	cropIntent.setDataAndType(mImageCaptureUri, "image/*");
        	    //set crop properties
        	cropIntent.putExtra("crop", "true");
        	    //indicate aspect of desired crop
        	cropIntent.putExtra("aspectX", 1);
        	cropIntent.putExtra("aspectY", 1);
        	    //indicate output X and Y
        	cropIntent.putExtra("outputX", /*256*/128);
        	cropIntent.putExtra("outputY", /*256*/128);
        	    //retrieve data on return
        	cropIntent.putExtra("return-data", true);
        	    //start the activity - we handle returning in onActivityResult
        	startActivityForResult(cropIntent, CROP_FROM_CAMERA);
        }
	}
    
	private String getRealPathFromURI(Uri contentURI) {
	  Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
	  if (cursor == null) { // Source is Dropbox or other similar local file path
	      return contentURI.getPath();
	      } else { 
	      cursor.moveToFirst(); 
	      int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	      return cursor.getString(idx); 
	  }
	}
}
