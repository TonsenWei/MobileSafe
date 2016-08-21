package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * 设置中心功能
 * */
public class SettingActivity extends Activity {

	private SettingItemView sivUpdate;
	private SharedPreferences mPref;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_activity_layout);
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
//		sivUpdate.setTitle("自动更新设置");
//		sivUpdate.setDesc("自动更新已开启");
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if (autoUpdate) {
//			sivUpdate.setDesc("自动更新已开启");
			sivUpdate.setChecked(true);
		} else {
//			sivUpdate.setDesc("自动更新已关闭");
			sivUpdate.setChecked(false);
		}
		
		
		//设置点击事件在项目上,而不是设置在CheckBox上,因为CheckBox的区域太小影响用户体验
		sivUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//判断当前勾选的状态
				if (sivUpdate.isChecked()) {//如果已经被勾选,则设置为不勾选
					sivUpdate.setChecked(false);
//					sivUpdate.setDesc("自动更新已关闭");
					mPref.edit().putBoolean("auto_update", false).commit();
				} else {					//如果没有被勾选,则设置为勾选
					sivUpdate.setChecked(true);
//					sivUpdate.setDesc("自动更新已开启");
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}

}
