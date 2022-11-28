package com.example.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
		
		
		Button buttonPlay = findViewById(R.id.buttonPlay);
		buttonPlay.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button) {
			sendCustomNotification(MyApplication.CHANNEL_ID);
		}
		if (view.getId() == R.id.buttonPlay) {
			playMusic();
		}
	}
	
	private void playMusic() {
		
		Song song = new Song("taivianhyeunguoikhac",
		                     "hanacamtien",
		                     R.drawable.big_image,
		                     R.raw.taivianhyeunguoikhac_hanacamtien);
		Intent intent = new Intent(this,
		                           MyService.class);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("song",
		                       song);
		intent.putExtra("bundle",
		                bundle);
		startService(intent);
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
		                                                 R.layout.custom_notification_collapsed);
		notificationLayout.setTextViewText(R.id.textViewSongName,
		                                   "TaiViAnhYeuNguoiKhac");
		notificationLayout.setTextViewText(R.id.textViewAuthor,
		                                   "HanaCamTien");
		
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
		                                                           channelId)
				.setSmallIcon(R.drawable.ic_notification_custom)
				.setCustomContentView(notificationLayout)
				.setCustomBigContentView(notificationLayout)
				.setCustomHeadsUpContentView(notificationLayout)
				.setContentIntent(notifyPendingIntent)
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