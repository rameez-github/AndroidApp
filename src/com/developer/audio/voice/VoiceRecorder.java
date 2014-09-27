package com.developer.audio.voice;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

public class VoiceRecorder {

    private static final String LOG_TAG = "AudioRecordTest";
	private static final String FILE_PREFIX = "REC_";
	private static final String FILE_SUFFIX = ".3gp";
	private long startTime 					= 1000;


    private final Handler 	handler = new Handler();
    
	private TextView 		mTextRecordTimer;
    private MediaRecorder 	mRecorder = null;
    private String 			mFileName = null;
	private Context 		ctx;
	
	public VoiceRecorder (Context context){
		this.ctx = context;
	}
	
    public void setChatWindowVariable (TextView text_timer ){
    	this.mTextRecordTimer = text_timer;
    }
    
	private final Runnable updateTimeRecorder = new Runnable() {
		@Override
		public void run() {
            updateTextView(startTime);
            startTime += 1000;
        }
    };

    private void updateTextView(long currentTime) {
    	
		mTextRecordTimer.setText(String.format("%d min, %d sec", 
				TimeUnit.MILLISECONDS.toMinutes((long) currentTime),
				TimeUnit.MILLISECONDS.toSeconds((long) currentTime) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
						toMinutes((long) currentTime)))
				);
        handler.postDelayed(updateTimeRecorder, 1000);
	}
    
    public void startRecording() {
    	mFileName = pathAudioRecord();
    	mTextRecordTimer.setText("0 min, 0 sec");
    	resetStartTime();
    	vibrateAlert ();
    	
    	// recording code
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        
        mRecorder.start();
        handler.postDelayed(updateTimeRecorder, 1000);
    }

    public void stopRecording() {
    	vibrateAlert ();
    	
    	// stop recording code
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    	handler.removeCallbacks(updateTimeRecorder);
    }
    
	private void vibrateAlert (){
    	Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
    	 // Vibrate for 100 milliseconds
    	 v.vibrate(100);
    }

    public String pathAudioRecord() {
        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/"+getFileName();
        Log.v("new file", "path: "+mFileName);
        return mFileName;
    }

	private String getFileName() {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		return FILE_PREFIX + timeStamp + "_" + FILE_SUFFIX;
	}
	
	public String getCurrentFileName (){
		return mFileName;
	}
	
	private void resetStartTime (){
		this.startTime = 1000;
	}
	
	public long getElapsedTime (){
		return startTime;
	}
	
	public void play (){
		AudioManager audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
		float actualVolume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = actualVolume / maxVolume;
		// Is the sound loaded already?
		
		SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				
			}
		});
		int soundID = soundPool.load(mFileName, 1);
		soundPool.play(soundID, volume, volume, 1, 0, 1f); 

	}
}
