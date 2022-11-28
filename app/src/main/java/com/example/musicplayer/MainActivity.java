/*
 * Copyright (C) 11/28/22, 11:58 PM Nguyen Huy
 *
 * MainActivity.java [lastModified: 11/28/22, 11:53 PM]
 *
 * Contact:
 * facebook: https://www.facebook.com/nguyenhuy158/
 * github: https://www.github.com/nguyenhuy158/
 */

package com.example.musicplayer;

import static com.example.musicplayer.resource.Constants.ACTION_BACK;
import static com.example.musicplayer.resource.Constants.ACTION_BACK_MUSIC;
import static com.example.musicplayer.resource.Constants.ACTION_CLEAR;
import static com.example.musicplayer.resource.Constants.ACTION_NEXT;
import static com.example.musicplayer.resource.Constants.ACTION_NEXT_MUSIC;
import static com.example.musicplayer.resource.Constants.ACTION_PAUSE;
import static com.example.musicplayer.resource.Constants.ACTION_PLAY;
import static com.example.musicplayer.resource.Constants.ACTION_SEEK_BAR;
import static com.example.musicplayer.resource.Constants.ACTION_SEEK_BAR_TRACKING;
import static com.example.musicplayer.resource.Constants.ACTION_START;
import static com.example.musicplayer.resource.Constants.KEY_PUT_ACTION;
import static com.example.musicplayer.resource.Constants.KEY_PUT_ACTION_MUSIC;
import static com.example.musicplayer.resource.Constants.KEY_PUT_BUNDLE;
import static com.example.musicplayer.resource.Constants.KEY_PUT_CURRENT_TIME;
import static com.example.musicplayer.resource.Constants.KEY_PUT_DATA_TO_ACTIVITY;
import static com.example.musicplayer.resource.Constants.KEY_PUT_SERIALIZABLE_SONG;
import static com.example.musicplayer.resource.Constants.KEY_PUT_STATUS_PLAYER;
import static com.example.musicplayer.resource.Constants.KEY_PUT_TOTAL_TIME;
import static com.example.musicplayer.resource.Constants.REQUEST_CODE_PERMISSION;
import static com.example.musicplayer.resource.Constants.TAG;
import static com.example.musicplayer.resource.Constants.convertSecondsToMMss;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.song.IClickItemSong;
import com.example.musicplayer.song.Song;
import com.example.musicplayer.song.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	
	private RelativeLayout layoutMediaControl;
	private TextView       textViewSongName;
	private TextView       textViewCurrentTime;
	private TextView       textViewTotalTime;
	private SeekBar        seekBar;
	private Button         buttonBack;
	private Button         buttonNext;
	private Button         buttonPauseOrPlay;
	
	private Song    song;
	private boolean isPlay;
	private int     currentTime;
	private int     totalTime;
	
	private RecyclerView songList;
	private SongAdapter  songAdapter;
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context,
		                      Intent intent) {
			Bundle bundle = intent.getBundleExtra(KEY_PUT_BUNDLE);
			if (bundle != null) {
				song   = (Song) bundle.get(KEY_PUT_SERIALIZABLE_SONG);
				isPlay = bundle.getBoolean(KEY_PUT_STATUS_PLAYER);
				int actionMusic = bundle.getInt(KEY_PUT_ACTION_MUSIC);
				currentTime = bundle.getInt(KEY_PUT_CURRENT_TIME,
				                            0);
				totalTime   = bundle.getInt(KEY_PUT_TOTAL_TIME,
				                            0);
				
				handleLayoutMusic(actionMusic);
			}
		}
	};
	
	private void handleLayoutMusic(int actionMusic) {
		switch (actionMusic) {
			case ACTION_START:
				showSong();
				setStatusSong();
				break;
			case ACTION_PLAY:
				setStatusSong();
				break;
			case ACTION_PAUSE:
				setStatusSong();
				break;
			case ACTION_NEXT:
				break;
			case ACTION_BACK:
				break;
			case ACTION_CLEAR:
				layoutMediaControl.setVisibility(View.GONE);
				break;
			case ACTION_SEEK_BAR:
				rewindSeekBar();
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		methodRequestPermission();
		initView();
		LocalBroadcastManager
				.getInstance(this)
				.registerReceiver(broadcastReceiver,
				                  new IntentFilter(KEY_PUT_DATA_TO_ACTIVITY));
		getSongList();
		reOpenFromNotification();
	}
	
	
	private void methodRequestPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(new String[]{
					                   Manifest.permission.READ_EXTERNAL_STORAGE,
					                   Manifest.permission.WRITE_EXTERNAL_STORAGE,
					                   },
			                   REQUEST_CODE_PERMISSION);
		}
	}
	
	private void initView() {
		layoutMediaControl  = findViewById(R.id.layoutMediaControl);
		textViewSongName    = findViewById(R.id.textViewSongName);
		textViewCurrentTime = findViewById(R.id.textViewCurrentTime);
		textViewTotalTime   = findViewById(R.id.textViewTotalTime);
		seekBar             = findViewById(R.id.seekBar);
		buttonBack          = findViewById(R.id.buttonBack);
		buttonNext          = findViewById(R.id.buttonNext);
		buttonPauseOrPlay   = findViewById(R.id.buttonPauseOrPlay);
		songList            = findViewById(R.id.songList);
		songAdapter         = new SongAdapter(this,
		                                      R.layout.item_song,
		                                      new IClickItemSong() {
			                                      @Override
			                                      public void onClickItemSong(Song song) {
				                                      playMusic(song);
			                                      }
		                                      });
		
		songList.setLayoutManager(new LinearLayoutManager(this,
		                                                  RecyclerView.VERTICAL,
		                                                  true));
		songList.addItemDecoration(new DividerItemDecoration(this,
		                                                     DividerItemDecoration.VERTICAL));
		songList.setAdapter(songAdapter);
	}
	
	private void getSongList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Song songDemo = new Song("emlanhatmientay - default",
				                         "volemijintuannam",
				                         R.drawable.big_image,
				                         R.raw.emlanhatmientay_volemijintuannam);
				songAdapter.add(songDemo);
				List<Song> songs = getAllAudioFromDevice(MainActivity.this);
				for (Song song : songs) {
					songAdapter.add(song);
				}
			}
		}).start();
	}
	
	private void reOpenFromNotification() {
		if (getIntent().getSerializableExtra(KEY_PUT_SERIALIZABLE_SONG) != null) {
			Song tempSong
					= (Song) getIntent().getSerializableExtra(KEY_PUT_SERIALIZABLE_SONG);
			song = tempSong;
			showSong();
		}
	}
	
	private void rewindSeekBar() {
		seekBar.setMax(totalTime);
		seekBar.setProgress(currentTime);
		
		textViewCurrentTime.setText(convertSecondsToMMss(currentTime));
		textViewTotalTime.setText(convertSecondsToMMss(totalTime));
	}
	
	private void setStatusSong() {
		if (isPlay) {
			buttonPauseOrPlay.setText(R.string.pause);
		} else {
			buttonPauseOrPlay.setText(R.string.play);
		}
	}
	
	private void showSong() {
		if (song != null) {
			layoutMediaControl.setVisibility(View.VISIBLE);
			textViewSongName.setText(song.getTitle());
			
			buttonPauseOrPlay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isPlay) {
						sentActionToService(ACTION_PAUSE);
					} else {
						sentActionToService(ACTION_PLAY);
					}
				}
			});
			
			buttonBack.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					backMusic();
				}
			});
			
			buttonNext.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					nextMusic();
				}
			});
			
			seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar,
				                              int progress,
				                              boolean fromUser) {
					Log.d(TAG,
					      "onProgressChanged: " + seekBar.getProgress() + " " + seekBar.getMax());
					if (seekBar.getProgress() >= seekBar.getMax()) {
						Log.d(TAG,
						      "onProgressChanged: nextMusic");
						nextMusic();
					}
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					Log.d(TAG,
					      "onStartTrackingTouch: ");
				}
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					sentActionToService(ACTION_SEEK_BAR_TRACKING);
				}
			});
		}
	}
	
	
	private void sentActionToService(int action) {
		Intent intent = new Intent(this,
		                           MyService.class);
		intent.putExtra(KEY_PUT_ACTION,
		                action);
		intent.putExtra(KEY_PUT_CURRENT_TIME,
		                seekBar.getProgress());
		intent.putExtra(KEY_PUT_SERIALIZABLE_SONG,
		                song);
		startService(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager
				.getInstance(this)
				.unregisterReceiver(broadcastReceiver);
	}
	
	
	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       @NonNull String[] permissions,
	                                       @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode,
		                                 permissions,
		                                 grantResults);
		if (requestCode == REQUEST_CODE_PERMISSION && permissions.length > 0) {
			getAllAudioFromDevice(this);
		}
	}
	
	private void backMusic() {
		song = songAdapter.getBack(song);
		Log.d(TAG,
		      "onClick: " + song.getTitle());
		sentActionToService(ACTION_BACK_MUSIC);
	}
	
	private void nextMusic() {
		song = songAdapter.getNext(song);
		Log.d(TAG,
		      "onClick: " + song.getTitle());
		sentActionToService(ACTION_NEXT_MUSIC);
	}
	
	private void playMusic(Song song) {
		Intent intentService = new Intent(this,
		                                  MyService.class);
		stopService(intentService);
		Intent intent = new Intent(this,
		                           MyService.class);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(KEY_PUT_SERIALIZABLE_SONG,
		                       song);
		intent.putExtra(KEY_PUT_BUNDLE,
		                bundle);
		startService(intent);
	}
	
	public List<Song> getAllAudioFromDevice(Context context) {
		
		final List<Song> tempAudioList = new ArrayList<>();
		
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Log.d(TAG,
		      "getAllAudioFromDevice: " + uri.toString());
		String[] projection = {
				MediaStore.Audio.AudioColumns.DATA,
				MediaStore.Audio.ArtistColumns.ARTIST,
				};
		Cursor c = context
				.getContentResolver()
				.query(uri,
				       projection,
				       null,
				       null,
				       null);
		
		if (c != null) {
			while (c.moveToNext()) {
				
				Song   audioModel = new Song();
				String path       = c.getString(0);
				String artist     = c.getString(1);
				
				String name = path.substring(path.lastIndexOf("/") + 1);
				Log.d(TAG,
				      "getAllAudioFromDevice: " + path);
				
				audioModel.setTitle(name);
				audioModel.setSingle(artist);
				audioModel.setPath(path);
				
				Log.e("Name :",
				      name);
				Log.e("Path :" + path,
				      " Artist :" + artist);
				
				tempAudioList.add(audioModel);
			}
			c.close();
		}
		
		return tempAudioList;
	}
	
}