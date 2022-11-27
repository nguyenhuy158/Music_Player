package com.example.musicplayer;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

public class MainActivity extends AppCompatActivity
		implements View.OnClickListener {
	
	private static final int NOTIFICATION_ID = 1508;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button = findViewById(R.id.button);
		button.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button) {
			sendCustomNotification(MyApplication.CHANNEL_ID);
		}
	}
	
	private void sendNotification(String channelId) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
		                                             R.drawable.big_image);
		
		
		Notification notification = new NotificationCompat.Builder(this,
		                                                           channelId)
				.setContentTitle("Title push notification")
				.setContentText("Content here")
				.setStyle(new NotificationCompat.BigTextStyle().bigText("Content here"))
				.setSmallIcon(R.drawable.ic_notification_custom)
				.setLargeIcon(bitmap)
				.setStyle(new NotificationCompat.BigPictureStyle()
						          .bigPicture(bitmap)
						          .bigLargeIcon(bitmap))
				.build();
		
		NotificationManagerCompat notificationManager
				= NotificationManagerCompat.from(this);
		
		notificationManager.notify(getNotificationId(),
		                           notification);
	}
	
	private void sendCustomNotification(String channelId) {
		RemoteViews notificationLayout = new RemoteViews(getPackageName(),
		                                                 R.layout.custom_notification);
		notificationLayout.setTextViewText(R.id.notification_title,
		                                   "New " + "Title here");
		
		Notification notification = new NotificationCompat.Builder(this,
		                                                           channelId)
				.setStyle(new NotificationCompat.BigTextStyle().bigText("Content here"))
				.setSmallIcon(R.drawable.ic_notification_custom)
				.setCustomContentView(notificationLayout)
				.build();
		
		NotificationManagerCompat notificationManager
				= NotificationManagerCompat.from(this);
		
		notificationManager.notify(getNotificationId(),
		                           notification);
	}
	
	private int getNotificationId() {
		return (int) new Date().getTime();
	}
}