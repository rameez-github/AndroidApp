package com.developer.model;

public class ChatModel {


	public String 	name;
	public String 	message;
	public String 	imgUrl;
	public String	time;
	public String	filePath;
	public int		statusRes;
	public int 		imgRes;
	public boolean	isFilePath;
	
	public ChatModel (String name, String message, String time, int imgRes, int statusRes){
		this.name 		= name;
		this.message 	= message;
		this.time		= time;
		this.imgRes		= imgRes;
		this.statusRes	= statusRes;
	}

	public ChatModel (String name, String message, String time, String imgUrl, int statusRes){
		this.name 		= name;
		this.message 	= message;
		this.time		= time;
		this.imgUrl		= imgUrl;
		this.statusRes	= statusRes;
	}

	public ChatModel (String name, String message, String time, String filepath, int statusRes, boolean isfilepath){
		this.name 		= name;
		this.message 	= message;
		this.time		= time;
		this.filePath	= filepath;
		this.statusRes	= statusRes;
		this.isFilePath = isfilepath;
	}
}
