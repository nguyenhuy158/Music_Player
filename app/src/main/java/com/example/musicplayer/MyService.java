package com.example.musicplayer;

import static android.content.ContentValues.TAG;
import static com.example.musicplayer.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

public class MyService extends Service {
	
	private MediaPlayer mediaPlayer;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG,
		      "onCreate: hereee");
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
		
		Bundle bundle = intent.getBundleExtra("bundle");
		if (bundle != null) {
			Log.d(TAG,
			      "onStartCommand: bundle not true");
			Song song = (Song) bundle.getSerializable("song");
			
			sendNotification(song);
			startMusic(song);
		}
		
		return START_NOT_STICKY;
	}
	
	private void startMusic(Song song) {
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(getApplicationContext(),
			                                 song.getResource());
		}
		mediaPlayer.start();
	}
	
	private void sendNotification(Song song) {
		RemoteViews notificationLayout = new RemoteViews(getPackageName(),
		                                                 R.layout.custom_notification_collapsed);
		notificationLayout.setTextViewText(R.id.textViewSongName,
		                                   song.getTitle());
		notificationLayout.setTextViewText(R.id.textViewAuthor,
		                                   song.getSingle());
		notificationLayout.setImageViewResource(R.id.imageViewForward,
		                                        R.drawable.ic_fast_forward);
		notificationLayout.setImageViewResource(R.id.imageViewPausePlay,
		                                        R.drawable.ic_pause);
		notificationLayout.setImageViewResource(R.id.imageViewRewind,
		                                        R.drawable.ic_fast_rewind);
		
		// click notification open intent
		Intent notifyIntent = new Intent(this,
		                                 MainActivity.class);
		// Set the Activity to start in a new, empty task
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// Create the PendingIntent
		PendingIntent notifyPendingIntent = PendingIntent.getActivity(this,
		                                                              getNotificationId(),
		                                                              notifyIntent,
		                                                              PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		
		
		Notification notification = new NotificationCompat.Builder(this,
		                                                           CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_notification_custom)
				.setCustomContentView(notificationLayout)
				.setCustomBigContentView(notificationLayout)
				.setCustomHeadsUpContentView(notificationLayout)
				.setContentIntent(notifyPendingIntent)
				.build();
		
		startForeground(1, notification);
		// NotificationManagerCompat notificationManager
		// 		= NotificationManagerCompat.from(this);
		//
		// notificationManager.notify(getNotificationId(),
		//                            notification);
	}
	
	private int getNotificationId() {
		return (int) new Date().getTime();
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
