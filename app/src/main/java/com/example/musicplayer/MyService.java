/*
 * Copyright (C) 11/28/22, 11:58 PM Nguyen Huy
 *
 * MyService.java [lastModified: 11/28/22, 11:54 PM]
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
import static com.example.musicplayer.resource.Constants.CHANNEL_ID;
import static com.example.musicplayer.resource.Constants.ID_FOREGROUND_MUSIC;
import static com.example.musicplayer.resource.Constants.KEY_PUT_ACTION;
import static com.example.musicplayer.resource.Constants.KEY_PUT_ACTION_MUSIC;
import static com.example.musicplayer.resource.Constants.KEY_PUT_BUNDLE;
import static com.example.musicplayer.resource.Constants.KEY_PUT_CURRENT_TIME;
import static com.example.musicplayer.resource.Constants.KEY_PUT_DATA_TO_ACTIVITY;
import static com.example.musicplayer.resource.Constants.KEY_PUT_SERIALIZABLE_SONG;
import static com.example.musicplayer.resource.Constants.KEY_PUT_STATUS_PLAYER;
import static com.example.musicplayer.resource.Constants.KEY_PUT_TOTAL_TIME;
import static com.example.musicplayer.resource.Constants.TAG;
import static com.example.musicplayer.resource.Constants.getNotificationId;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicplayer.song.Song;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
	
	private MediaPlayer mediaPlayer;
	private boolean     isPlay = false;
	private Song        song;
	private int         currentTime;
	private Timer       timer;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG,
		      "onCreate: here");
	}
	
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent,
	                          int flags,
	                          int startId) {
		
		Bundle bundle = intent.getBundleExtra(KEY_PUT_BUNDLE);
		if (bundle != null) {
			Log.d(TAG,
			      "onStartCommand: bundle not true");
			song = (Song) bundle.getSerializable(KEY_PUT_SERIALIZABLE_SONG);
			
			sendNotification(song);
			startMusic(song);
		}
		
		
		// activity sent action
		int actionMusic = intent.getIntExtra(KEY_PUT_ACTION,
		                                     0);
		currentTime = intent.getIntExtra(KEY_PUT_CURRENT_TIME,
		                                 0);
		if (intent.getSerializableExtra(KEY_PUT_SERIALIZABLE_SONG) != null) {
			song
					= (Song) intent.getSerializableExtra(KEY_PUT_SERIALIZABLE_SONG);
		}
		handleActionMusic(actionMusic);
		
		return START_NOT_STICKY;
	}
	
	private void startMusic(Song song) {
		if (mediaPlayer == null) {
			if (song.getResource() == R.raw.emlanhatmientay_volemijintuannam) {
				mediaPlayer = MediaPlayer.create(getApplicationContext(),
				                                 song.getResource());
			} else {
				mediaPlayer = MediaPlayer.create(getApplicationContext(),
				                                 Uri.parse(song.getPath()));
			}
		}
		playMusic();
		playTimer();
	}
	
	private void stopMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			isPlay      = false;
		}
	}
	
	private void playTimer() {
		// update seekbar
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			                          @Override
			                          public void run() {
				                          updateSeekBar();
			                          }
		                          },
		                          0,
		                          1000);
	}
	
	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
	}
	
	private void handleActionMusic(int action) {
		switch (action) {
			case ACTION_PLAY:
				playMusic();
				break;
			case ACTION_PAUSE:
				pauseMusic();
				break;
			case ACTION_NEXT:
				fastFiveSecond();
				break;
			case ACTION_BACK:
				removeFiveSecond();
				break;
			case ACTION_NEXT_MUSIC:
				nextMusic();
				break;
			case ACTION_BACK_MUSIC:
				backMusic();
				break;
			case ACTION_CLEAR:
				stopTimer();
				stopMusic();
				stopSelf();
				sendActionToActivity(ACTION_CLEAR);
				break;
			case ACTION_SEEK_BAR:
				updateSeekBar();
				break;
			case ACTION_SEEK_BAR_TRACKING:
				rewindMusic();
				break;
			default:
				break;
		}
		sendNotification(song);
		
	}
	
	private void nextMusic() {
		stopMusic();
		stopTimer();
		startMusic(song);
	}
	
	
	private void backMusic() {
		stopMusic();
		stopTimer();
		startMusic(song);
	}
	
	
	private void removeFiveSecond() {
		if (mediaPlayer != null && isPlay) {
			if (mediaPlayer.getCurrentPosition() - 5000 > 0) {
				mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
			}
		}
	}
	
	private void fastFiveSecond() {
		if (mediaPlayer != null && isPlay) {
			if (mediaPlayer.getCurrentPosition() + 5000 < mediaPlayer.getDuration()) {
				mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
			}
		}
	}
	
	private void updateSeekBar() {
		if (mediaPlayer != null && isPlay) {
			sendActionToActivity(ACTION_SEEK_BAR);
		}
	}
	
	private void rewindMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.seekTo(currentTime);
		}
	}
	
	private void playMusic() {
		if (mediaPlayer != null && !isPlay) {
			mediaPlayer.start();
			isPlay = true;
			sendActionToActivity(ACTION_START);
		}
	}
	
	private void pauseMusic() {
		if (mediaPlayer != null && isPlay) {
			mediaPlayer.pause();
			isPlay = false;
			sendActionToActivity(ACTION_START);
		}
	}
	
	private void sendNotification(Song song) {
		RemoteViews notificationLayoutCollapsed
				= new RemoteViews(getPackageName(),
				                  R.layout.custom_notification_collapsed);
		notificationLayoutCollapsed.setTextViewText(R.id.textViewSongName,
		                                            song.getTitle());
		notificationLayoutCollapsed.setTextViewText(R.id.textViewAuthor,
		                                            song.getSingle());
		notificationLayoutCollapsed.setImageViewResource(R.id.imageViewPausePlay,
		                                                 R.drawable.ic_pause);
		
		// action custom_notification_collapsed
		notificationLayoutCollapsed.setOnClickPendingIntent(R.id.imageViewClose,
		                                                    getPendingIntent(this,
		                                                                     ACTION_CLEAR));
		if (isPlay) {
			notificationLayoutCollapsed.setOnClickPendingIntent(R.id.imageViewPausePlay,
			                                                    getPendingIntent(this,
			                                                                     ACTION_PAUSE));
			notificationLayoutCollapsed.setImageViewResource(R.id.imageViewPausePlay,
			                                                 R.drawable.ic_pause);
		} else {
			notificationLayoutCollapsed.setOnClickPendingIntent(R.id.imageViewPausePlay,
			                                                    getPendingIntent(this,
			                                                                     ACTION_PLAY));
			notificationLayoutCollapsed.setImageViewResource(R.id.imageViewPausePlay,
			                                                 R.drawable.ic_play);
		}
		
		RemoteViews notificationLayoutExpanded
				= new RemoteViews(getPackageName(),
				                  R.layout.custom_notification_expanded);
		notificationLayoutExpanded.setTextViewText(R.id.textViewSongName,
		                                           song.getTitle());
		notificationLayoutExpanded.setTextViewText(R.id.textViewAuthor,
		                                           song.getSingle());
		notificationLayoutExpanded.setImageViewResource(R.id.imageViewForward,
		                                                R.drawable.ic_fast_forward);
		notificationLayoutExpanded.setImageViewResource(R.id.imageViewPausePlay,
		                                                R.drawable.ic_pause);
		notificationLayoutExpanded.setImageViewResource(R.id.imageViewRewind,
		                                                R.drawable.ic_fast_rewind);
		// action custom_notification_expanded
		notificationLayoutExpanded.setOnClickPendingIntent(R.id.imageViewForward,
		                                                   getPendingIntent(this,
		                                                                    ACTION_NEXT));
		notificationLayoutExpanded.setOnClickPendingIntent(R.id.imageViewRewind,
		                                                   getPendingIntent(this,
		                                                                    ACTION_BACK));
		notificationLayoutExpanded.setOnClickPendingIntent(R.id.imageViewClose,
		                                                   getPendingIntent(this,
		                                                                    ACTION_CLEAR));
		if (isPlay) {
			notificationLayoutExpanded.setOnClickPendingIntent(R.id.imageViewPausePlay,
			                                                   getPendingIntent(this,
			                                                                    ACTION_PAUSE));
			notificationLayoutExpanded.setImageViewResource(R.id.imageViewPausePlay,
			                                                R.drawable.ic_pause);
		} else {
			notificationLayoutExpanded.setOnClickPendingIntent(R.id.imageViewPausePlay,
			                                                   getPendingIntent(this,
			                                                                    ACTION_PLAY));
			notificationLayoutExpanded.setImageViewResource(R.id.imageViewPausePlay,
			                                                R.drawable.ic_play);
		}
		
		// click notification open intent
		Intent notifyIntent = new Intent(this,
		                                 MainActivity.class);
		// Set the Activity to start in a new, empty task
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		notifyIntent.putExtra(KEY_PUT_SERIALIZABLE_SONG,
		                      song);
		// Create the PendingIntent
		PendingIntent notifyPendingIntent = PendingIntent.getActivity(this,
		                                                              getNotificationId(),
		                                                              notifyIntent,
		                                                              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		
		// notification
		Notification notification = new NotificationCompat.Builder(this,
		                                                           CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_notification_custom)
				.setCustomContentView(notificationLayoutCollapsed)
				.setCustomBigContentView(notificationLayoutExpanded)
				.setCustomHeadsUpContentView(notificationLayoutCollapsed)
				.setContentIntent(notifyPendingIntent)
				.build();
		
		startForeground(ID_FOREGROUND_MUSIC,
		                notification);
	}
	
	private PendingIntent getPendingIntent(Context context,
	                                       int action) {
		Intent intent = new Intent(context,
		                           MyReceiver.class);
		intent.putExtra(KEY_PUT_ACTION,
		                action);
		return PendingIntent.getBroadcast(context.getApplicationContext(),
		                                  action,
		                                  intent,
		                                  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
		
	}
	
	private void sendActionToActivity(int action) {
		Intent intent = new Intent(KEY_PUT_DATA_TO_ACTIVITY);
		Bundle bundle = new Bundle();
		bundle.putSerializable(KEY_PUT_SERIALIZABLE_SONG,
		                       song);
		bundle.putBoolean(KEY_PUT_STATUS_PLAYER,
		                  isPlay);
		bundle.putInt(KEY_PUT_ACTION_MUSIC,
		              action);
		if (isPlay && mediaPlayer != null) {
			bundle.putInt(KEY_PUT_CURRENT_TIME,
			              mediaPlayer.getCurrentPosition());
			bundle.putInt(KEY_PUT_TOTAL_TIME,
			              mediaPlayer.getDuration());
		}
		
		intent.putExtra(KEY_PUT_BUNDLE,
		                bundle);
		
		LocalBroadcastManager
				.getInstance(this)
				.sendBroadcast(intent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
