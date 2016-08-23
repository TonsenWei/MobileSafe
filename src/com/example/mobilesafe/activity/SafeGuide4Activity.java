package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SafeGuide4Activity extends BaseGuideActivity {

	private CheckBox cbSetProtect;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.safe_guide4_layout);
		
		cbSetProtect = (CheckBox) findViewById(R.id.cb_protect);
		boolean flagProtect = mSP.getBoolean("protect", false);
		if (flagProtect) {
			cbSetProtect.setText("您已经开启防盗保护");
			cbSetProtect.setChecked(true);
		}else {
			cbSetProtect.setText("您没有开启防盗保护");
			cbSetProtect.setChecked(false);
		}
		cbSetProtect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					cbSetProtect.setText("您已经开启防盗保护");
					mSP.edit().putBoolean("protect", true).commit();
				}else {
					cbSetProtect.setText("您没有开启防盗保护");
					mSP.edit().putBoolean("protect", false).commit();
				}
			}
		});
	}

	//设置完成,下一页进入防盗保护页面
	@Override
	public void enterNextPage() {
		startActivity(new Intent(SafeGuide4Activity.this, SafeActivity.class));
		finish();
		
		mSP.edit().putBoolean("haveenter", true).commit();//标识已经进入过向导,下次就不再进入
		
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
		
	}
	//上一页
	@Override
	public void enterPreviousPage() {
		startActivity(new Intent(SafeGuide4Activity.this, SafeGuide3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);//进入动画和退出动画
	}
}
