/*
 * Copyright (C) 11/28/22, 11:58 PM Nguyen Huy
 *
 * MyApplication.java [lastModified: 11/28/22, 11:24 PM]
 *
 * Contact:
 * facebook: https://www.facebook.com/nguyenhuy158/
 * github: https://www.github.com/nguyenhuy158/
 */

package com.example.musicplayer;

import static com.example.musicplayer.resource.Constants.CHANNEL_ID;
import static com.example.musicplayer.resource.Constants.CHANNEL_ID_2;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

public class MyApplication extends Application {

	
	@Override
	public void onCreate() {
		super.onCreate();
		createNotificationChannel();
	}
	
	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			Uri sound
					= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound_notification_custom);
			AudioAttributes audioAttributes = new AudioAttributes.Builder()
					.setUsage(AudioAttributes.USAGE_NOTIFICATION)
					.build();
			// Config channel 1
			CharSequence name        = getString(R.string.channel_name);
			String       description = getString(R.string.channel_description);
			int          importance  = NotificationManager.IMPORTANCE_MAX;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
			                                                      name,
			                                                      importance);
			channel.setDescription(description);
			channel.setSound(sound,
			                 audioAttributes);
			channel.setSound(null,
			                 null);
			
			// Config channel 2
			CharSequence name_2        = getString(R.string.channel_name_2);
			String       description_2
			                           = getString(R.string.channel_description_2);
			int          importance_2  = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel_2
					= new NotificationChannel(CHANNEL_ID_2,
					                          name_2,
					                          importance_2);
			channel_2.setDescription(description_2);
			
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			NotificationManager notificationManager
					= getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
			notificationManager.createNotificationChannel(channel_2);
		}
	}
}
