package com.developer.album;

public class Album {

	public String[] album_pic_path;
	public String
	album_name,
	created_at,
	total_pics;
	
	public int getAlbumLength (){
		if (album_pic_path != null && album_pic_path.length > 0)
			return album_pic_path.length;
		
		return 0;
	}
}
