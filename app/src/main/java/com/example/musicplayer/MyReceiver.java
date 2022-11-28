/*
 * Copyright (C) 11/28/22, 11:58 PM Nguyen Huy
 *
 * MyReceiver.java [lastModified: 11/28/22, 11:24 PM]
 *
 * Contact:
 * facebook: https://www.facebook.com/nguyenhuy158/
 * github: https://www.github.com/nguyenhuy158/
 */

package com.example.musicplayer;

import static com.example.musicplayer.resource.Constants.KEY_PUT_ACTION;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context,
	                      Intent intent) {
		int actionMusic = intent.getIntExtra(KEY_PUT_ACTION,
		                                     0);
		Intent intentService = new Intent(context,
		                                  MyService.class);
		intentService.putExtra(KEY_PUT_ACTION,
		                       actionMusic);
		context.startService(intentService);
	}
}
