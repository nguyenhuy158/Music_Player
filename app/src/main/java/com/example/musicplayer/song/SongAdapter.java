/*
 * Copyright (C) 11/28/22, 11:58 PM Nguyen Huy
 *
 * SongAdapter.java [lastModified: 11/28/22, 11:24 PM]
 *
 * Contact:
 * facebook: https://www.facebook.com/nguyenhuy158/
 * github: https://www.github.com/nguyenhuy158/
 */

package com.example.musicplayer.song;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
	List<Song> songList;
	private Context context;
	private int     layout;
	
	private IClickItemSong iClickItemSong;
	
	public SongAdapter(Context context,
	                   int layout) {
		this.context  = context;
		this.layout   = layout;
		this.songList = new ArrayList<>();
	}
	
	public SongAdapter(Context context,
	                   int layout,
	                   List<Song> songList) {
		this.context  = context;
		this.layout   = layout;
		this.songList = songList;
	}
	
	public SongAdapter(Context context,
	                   int layout,
	                   IClickItemSong iClickItemSong) {
		this.context        = context;
		this.layout         = layout;
		this.songList       = new ArrayList<>();
		this.iClickItemSong = iClickItemSong;
	}
	
	public SongAdapter(Context context,
	                   int layout,
	                   List<Song> songList,
	                   IClickItemSong iClickItemSong) {
		this.songList       = songList;
		this.context        = context;
		this.layout         = layout;
		this.iClickItemSong = iClickItemSong;
	}
	
	
	public List<Song> getSongList() {
		return songList;
	}
	
	public void setSongList(List<Song> songList) {
		this.songList.clear();
		this.songList.addAll(songList);
		this.notifyDataSetChanged();
	}
	
	public void add(Song song) {
		this.songList.add(0,
		                  song);
		this.notifyItemInserted(0);
	}
	
	private int index(Song song) {
		for (int i = 0; i < songList.size(); i++) {
			
			Song tempSong = songList.get(i);
			if (tempSong.getPath() != null) {
				if (tempSong
						.getPath()
						.equals(song.getPath())) {
					return i;
				}
			} else if (tempSong.getResource() == song.getResource()) {
				return i;
			}
		}
		return 0;
	}
	
	public Song getNext(Song song) {
		int index = index(song);
		index -= 1;
		if (index < 0) {
			index = songList.size() - 1;
		}
		return songList.get(index);
	}
	
	public Song getBack(Song song) {
		int index = index(song);
		index += 1;
		if (index >= songList.size()) {
			index = 0;
		}
		return songList.get(index);
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
	                                     int viewType) {
		View view = LayoutInflater
				.from(context)
				.inflate(layout,
				         parent,
				         false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder,
	                             int position) {
		if (holder == null) {return;}
		
		Song song = songList.get(position);
		
		holder.textViewSongName.setText(song.getTitle());
		holder.itemSong.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iClickItemSong.onClickItemSong(song);
			}
		});
	}
	
	@Override
	public int getItemCount() {
		if (songList == null) {
			return 0;
		}
		return songList.size();
	}
	
	class ViewHolder extends RecyclerView.ViewHolder {
		TextView       textViewSongName;
		RelativeLayout itemSong;
		
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			
			textViewSongName = itemView.findViewById(R.id.textViewSongName);
			itemSong         = itemView.findViewById(R.id.item_song);
		}
	}
}
