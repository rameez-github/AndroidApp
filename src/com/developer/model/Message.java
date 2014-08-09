package com.developer.model;

/**
 * Message is a Custom Object to encapsulate message information/fields
 *
 */
public class Message {
	
	String message;
	
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
	
	public Message(String message, String time, boolean isMine) {
		super();
		this.message = message;
		this.time	 = time;
		this.isMine = isMine;
	}
	
	public Message(String message, String time, int imgRes, boolean isMine) {
		super();
		this.message	= message;
		this.time	 	= time;
		this.imgRes		= imgRes; 
		this.isMine 	= isMine;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
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
	
}
