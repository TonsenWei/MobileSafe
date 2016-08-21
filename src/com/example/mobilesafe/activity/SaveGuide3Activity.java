package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class SaveGuide3Activity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.save_guide3_layout);
	}
	//下一页
	public void next(View view){
		startActivity(new Intent(SaveGuide3Activity.this, SaveGuide4Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
	}
	//上一页
	public void previous(View view){
		startActivity(new Intent(SaveGuide3Activity.this, SaveGuide2Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);//进入动画和退出动画
	}
}
