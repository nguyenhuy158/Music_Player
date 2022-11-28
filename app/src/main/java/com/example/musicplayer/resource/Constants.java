/*
 * Copyright (C) 11/28/22, 11:58 PM Nguyen Huy
 *
 * Constants.java [lastModified: 11/28/22, 11:51 PM]
 *
 * Contact:
 * facebook: https://www.facebook.com/nguyenhuy158/
 * github: https://www.github.com/nguyenhuy158/
 */

package com.example.musicplayer.resource;

import java.util.Date;

public final class Constants {
	public static final String CHANNEL_ID                = "CHANEL_1508";
	public static final String CHANNEL_ID_2              = "CHANEL_0505";
	public static final int    REQUEST_CODE_PERMISSION   = 1508;
	public static final int    ID_FOREGROUND_MUSIC       = 1;
	public static final int    ACTION_START              = 0;
	public static final int    ACTION_PLAY               = 1;
	public static final int    ACTION_PAUSE              = 2;
	public static final int    ACTION_NEXT               = 3;
	public static final int    ACTION_NEXT_MUSIC         = 4;
	public static final int    ACTION_BACK               = 5;
	public static final int    ACTION_BACK_MUSIC         = 6;
	public static final int    ACTION_CLEAR              = 7;
	public static final int    ACTION_SEEK_BAR           = 8;
	public static final int    ACTION_SEEK_BAR_TRACKING  = 9;
	public static final String KEY_PUT_CURRENT_TIME
	                                                     = "KEY_PUT_CURRENT_TIME";
	public static final String KEY_PUT_TOTAL_TIME        = "KEY_PUT_TOTAL_TIME";
	public static final String TAG
	                                                     = "DEBUG TAG - Nguyen Huy";
	public static final String KEY_PUT_BUNDLE            = "bundle";
	public static final String KEY_PUT_SERIALIZABLE_SONG = "song";
	public static final String KEY_PUT_ACTION            = "action";
	public static final String KEY_PUT_ACTION_MUSIC      = "action_music";
	public static final String KEY_PUT_STATUS_PLAYER     = "status_player";
	public static final String KEY_PUT_DATA_TO_ACTIVITY
	                                                     = "send_data_to_activity";
	public static final String KEY_PUT_DATA_TO_SERVICE
	                                                     = "send_data_to_service";
	public static final int    NOTIFICATION_ID           = 1508;
	
	private Constants() {
	}
	
	public static int getNotificationId() {
		return (int) new Date().getTime();
	}
	
	public static String convertSecondsToMMss(long millisecond) {
		millisecond /= 1000;
		long s = millisecond % 60;
		long m = (millisecond / 60) % 60;
		return String.format("%02d:%02d",
		                     m,
		                     s);
	}
}
