package com.example.musicplayer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
		implements View.OnClickListener {
	
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
		}
	}
	
	private void sendNotification() {
	
	}
}