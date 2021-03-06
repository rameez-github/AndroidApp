package com.developer.model;

import android.text.Spanned;

/**
 * Message is a Custom Object to encapsulate message information/fields
 *
 */
public class Message {
	Spanned spanMsg;
	/**
	 * Time of the message when message is received or sent
	 */
	String time;
	
	/**
	 * boolean to determine, who is sender of this message
	 */
	boolean isMine;
	
	/**
	 * image resource id
	 */
	int imgRes;

	/**
	 * boolean to determine, does message has album or not. 
	 */
	boolean hasAlbum;
	
	Album album;

	/**
	 * boolean to determine, does message has audio voice or not. 
	 */
	boolean hasAudio;
	
	Audio audio;

	/**
	 * boolean to determine, does message has video clip or not. 
	 */
	boolean hasVideo;
	
	Video video;

	public Message (Video mVideo, boolean isMine){
		this.video = mVideo;
		this.hasVideo = true;
		this.isMine = isMine;
	}
	
	public Message (Audio mAudio, boolean isMine){
		this.audio = mAudio;
		this.hasAudio = true;
		this.isMine = isMine;
	}
	
	public Message (Album mAlbum, boolean isMine){
		this.album = mAlbum;
		this.hasAlbum = true;
		this.isMine = isMine;
	}
	
	public Message(Spanned spanned, String time, boolean isMine) {
		super();
		this.spanMsg = spanned;
		this.time	 = time;
		this.isMine = isMine;
	}
	
	public Message(Spanned spanned, String time, int imgRes, boolean isMine) {
		this.spanMsg	= spanned;
		this.time 		= time;
		this.imgRes		= imgRes; 
		this.isMine 	= isMine;
	}

	public Spanned getSpanMessage() {
		return spanMsg;
	}
	
	public boolean isMine() {
		return isMine;
	}
	
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}
	
	public String getTimeOfMessage (){
		return this.time;
	}
	
	public void setTimeOfMessage (String time){
		this.time = time;
	}
	
	public int getImageResourceId (){
		return imgRes;
	}
	
	public boolean hasAlbum (){
		return hasAlbum;
	}
	
	public Album getAlbum (){
		return album;
	}
	
	public boolean hasAudio (){
		return hasAudio;
	}
	
	public Audio getAudio (){
		return audio;
	}
	
	public boolean hasVideo (){
		return hasVideo;
	}
	
	public Video getVideo (){
		return video;
	}
}
