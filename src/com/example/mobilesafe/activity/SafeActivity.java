package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class SafeActivity extends Activity {

	private TextView tvSafeNumber;
	private ImageView ivProtectFlag;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.safe_activity_layout);
		
		tvSafeNumber = (TextView) findViewById(R.id.tv_safenumber);
		ivProtectFlag = (ImageView) findViewById(R.id.iv_protectflag);
		
		SharedPreferences spref = getSharedPreferences("config", MODE_PRIVATE);
		String phone = spref.getString("safe_number", "");
		boolean flagProtect = spref.getBoolean("protect", false);
		if (TextUtils.isEmpty(phone)) {
			tvSafeNumber.setText("未设置");
		}else {
			tvSafeNumber.setText(phone);
		}
		if (flagProtect) {
			ivProtectFlag.setImageResource(R.drawable.lock);
		}else {
			ivProtectFlag.setImageResource(R.drawable.unlock);
		}
	}
	//重新进入设置向导的监听事件
	public void reEnterGuide(View view) {
		startActivity(new Intent(SafeActivity.this, SafeGuide1Activity.class));
		finish();
	}
}
