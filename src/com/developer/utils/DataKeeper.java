package com.developer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DataKeeper {
	
	private static DataKeeper instance;
	
	public static DataKeeper sharedInstance() {
		if (instance == null) {
			instance = new DataKeeper();
		}
		
		return instance;
	}
	
	/**
	 * Defined by developer.
	 * @param context
	 * @param album
	 */
	public void saveAlbums (Context context, String album){
		SharedPreferences sharedPreferences = context.getSharedPreferences("album_preferences", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("album", album);
		editor.commit();
	}

	/**
	 * Defined by developer.
	 * @param context
	 */
	public SafeJSONArray getAlbums (Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences("album_preferences", Context.MODE_PRIVATE);
		String album = sharedPreferences.getString("album", null);
		
		if (album != null)
			return new SafeJSONArray(album) ;
		
		return new SafeJSONArray("[]");
	}
	
	public void saveAudio (Context context, String audio){
		SharedPreferences sharedPreferences = context.getSharedPreferences("audio_preferences", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("album", audio);
		editor.commit();
	}

	public SafeJSONArray getAudios (Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences("audio_preferences", Context.MODE_PRIVATE);
		String album = sharedPreferences.getString("album", null);
		
		if (album != null)
			return new SafeJSONArray(album) ;
		
		return new SafeJSONArray("[]");
	}
}
