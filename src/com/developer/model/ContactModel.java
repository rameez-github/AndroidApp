package com.developer.model;

public class ContactModel {
	
	public static final String DEVICE_CONTACT_DISPLAY_NAME = "contact_name";
	public static final String DEVICE_CONTACT_DISPLAY_NUMBER = "phone_number";

	public ContactModel(String name, String status, int imgRes) {
		// TODO Auto-generated constructor stub

		this.name = name;
		this.status = status;
		this.imgRes = imgRes;
		
	}
	public String 	name;
	public String 	status;
	public String 	imgUrl;
	public int 		imgRes;
}
