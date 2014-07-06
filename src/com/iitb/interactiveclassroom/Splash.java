package com.iitb.interactiveclassroom;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.splash);

		final Handler handler = new Handler();
		
		// Creating folder on launch if not present
		File folder = new File(Environment.getExternalStorageDirectory()
				.toString() + "/AakashApp/");

		// Check if the directory exists and there are users in it

		if (!(folder.exists() && folder.isDirectory() && folder.listFiles().length > 0)) {

			// If users and folder does not exist then create a folder and start
			// the NEW USER'S LOGIN activity

			folder.mkdirs();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(new Intent(Splash.this, Login.class));
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
					finish();
				}
			}, 1000);

		} else {
			
			// If users already exist then start the SELECT USER activity
			
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(new Intent(Splash.this,
							Users.class));
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
					finish();
				}
			}, 1000);
		}

	}

}
