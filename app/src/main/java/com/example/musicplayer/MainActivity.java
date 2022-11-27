package com.example.musicplayer;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
		
		Button button  = findViewById(R.id.button);
		Button button1 = findViewById(R.id.button1);
		button.setOnClickListener(this);
		button1.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button) {
			sendNotification(MyApplication.CHANNEL_ID);
		}
		if (view.getId() == R.id.button1) {
			sendNotification(MyApplication.CHANNEL_ID_2);
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
				.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(bitmap))
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