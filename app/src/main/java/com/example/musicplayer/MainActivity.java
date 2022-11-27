package com.example.musicplayer;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
			sendNotification();
			Log.d(TAG,
			      "onClick: ok");
		}
	}
	
	private void sendNotification() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
		                                             R.drawable.ic_launcher_background);
		Notification notification = new NotificationCompat.Builder(this,
		                                                           MyApplication.CHANNEL_ID)
				.setContentTitle("Title push notification")
				.setContentText("Content here")
				.setSmallIcon(R.drawable.ic_notification_custom)
				.setLargeIcon(bitmap)
				.build();
		
		NotificationManager notificationManager
				= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		notificationManager.notify(getNotificationId(),
		                           notification);
		Log.d(TAG,
		      "sendNotification: notify");
	}
	
	private int getNotificationId() {
		return (int) new Date().getTime();
	}
}