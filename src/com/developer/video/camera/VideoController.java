package com.developer.video.camera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class VideoController {

    private Uri fileUri;
	private FragmentActivity mContext;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    
	public void startVideo (Fragment fragment){
		
		mContext = fragment.getActivity();
        // create new Intentwith with Standard Intent action that can be
        // sent to have the camera application capture an video and return it.
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        // create a file to save the video
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
         
        // set the image file name 
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); 
         
        // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        // start the Video Capture Intent
        fragment.startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
         
	}

	public String getFileDuration(Uri mUri) {
		MediaPlayer mp = MediaPlayer.create(mContext, mUri);
		int duration = mp.getDuration();
		mp.release();
		/*convert millis to appropriate time*/
		return String.format("%d min, %d sec", 
		        TimeUnit.MILLISECONDS.toMinutes(duration),
		        TimeUnit.MILLISECONDS.toSeconds(duration) - 
		        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
		    );
	}
	
    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
         
          return Uri.fromFile(getOutputMediaFile(type));
    }
 
    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
         
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "MyCameraVideo");
         
         
        // Create the storage directory(MyCameraVideo) if it does not exist
        if (! mediaStorageDir.exists()){
             
            if (! mediaStorageDir.mkdirs()){
                 
                Toast.makeText(mContext, "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();
                 
                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }
 
         
        // Create a media file name
         
        // For unique file name appending current timeStamp with file name
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                             .format(date.getTime());
         
        File mediaFile;
         
        if(type == MEDIA_TYPE_VIDEO) {
             
            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "VID_"+ timeStamp + ".mp4");
             
        } else {
            return null;
        }
 
        return mediaFile;
    }
     
}
