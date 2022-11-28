/*
 * Copyright (C) 11/28/22, 11:58 PM Nguyen Huy
 *
 * Song.java [lastModified: 11/28/22, 11:24 PM]
 *
 * Contact:
 * facebook: https://www.facebook.com/nguyenhuy158/
 * github: https://www.github.com/nguyenhuy158/
 */

package com.example.musicplayer.song;

import java.io.Serializable;

public class Song implements Serializable {
	private String title;
	private String single;
	private int    image;
	private int    resource;
	private String path;
	
	public Song() {
	}
	
	public Song(String title,
	            String single,
	            int image,
	            int resource) {
		this.title    = title;
		this.single   = single;
		this.image    = image;
		this.resource = resource;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSingle() {
		return single;
	}
	
	public void setSingle(String single) {
		this.single = single;
	}
	
	public int getImage() {
		return image;
	}
	
	public void setImage(int image) {
		this.image = image;
	}
	
	public int getResource() {
		return resource;
	}
	
	public void setResource(int resource) {
		this.resource = resource;
	}
}
