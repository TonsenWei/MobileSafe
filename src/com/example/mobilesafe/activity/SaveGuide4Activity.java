package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class SaveGuide4Activity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.save_guide4_layout);
	}
	//设置完成,下一页进入防盗保护页面
	public void next(View view){
		startActivity(new Intent(SaveGuide4Activity.this, SaveActivity.class));
		finish();
		
		SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
		pref.edit().putBoolean("haveenter", true).commit();//标识已经进入过向导,下次就不再进入
		
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
	}
	//上一页
	public void previous(View view){
		startActivity(new Intent(SaveGuide4Activity.this, SaveGuide3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);//进入动画和退出动画
	}
}
