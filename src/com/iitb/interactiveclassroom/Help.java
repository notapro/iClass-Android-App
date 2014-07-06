package com.iitb.interactiveclassroom;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.help);
	}

}
