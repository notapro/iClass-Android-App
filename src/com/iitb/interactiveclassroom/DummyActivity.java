package com.iitb.interactiveclassroom;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class DummyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//setContentView(R.layout.help);

		finish();
		
		/*
		if (AudioDoubt.ismin==1)
		{
		Intent confirm1 = new Intent(DummyActivity.this, AudioMainActivity.class); // START
		confirm1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);															// SESSION
        startActivity(confirm1);
		}
		*/
	}

}
