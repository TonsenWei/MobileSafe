package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class SaveActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.save_activity_layout);
		
	}

}
