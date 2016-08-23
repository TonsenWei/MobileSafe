package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.view.SettingItemView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

public class SafeGuide2Activity extends BaseGuideActivity {

	private SettingItemView sivBindSim;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.safe_guide2_layout);
	
		sivBindSim = (SettingItemView) findViewById(R.id.siv_bindsim);
		
		//进入绑定sim卡向导,先看配置文件是否已设置绑定
		String simStr = mSP.getString("simserialnumber", "");
		if (!TextUtils.isEmpty(simStr)) {
			sivBindSim.setChecked(true);
		} else {
			sivBindSim.setChecked(false);
		}
		
		sivBindSim.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sivBindSim.isChecked()) {//如果已经勾选,则取消勾选并清除配置信息
					sivBindSim.setChecked(false);
					mSP.edit().remove("simserialnumber").commit();
				} else {
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = telephonyManager.getSimSerialNumber();//获取sim卡序列号信息
					Log.d("MyTag", "SIM卡SN: " + simSerialNumber);
					mSP.edit().putString("simserialnumber", simSerialNumber).commit();//保存到sp
					sivBindSim.setChecked(true);
				}
				
			}
		});
	}
	//进入下一页
	@Override
	public void enterNextPage() {
		String simBind = mSP.getString("simserialnumber", "");
		if (TextUtils.isEmpty(simBind)) {//如果不绑定SIM
			Toast.makeText(SafeGuide2Activity.this, "必须绑定SIM卡才能进一步设置", Toast.LENGTH_SHORT).show();
			return;
		}
		startActivity(new Intent(SafeGuide2Activity.this, SafeGuide3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
		
	}
	//进入上一页
	@Override
	public void enterPreviousPage() {
		startActivity(new Intent(SafeGuide2Activity.this, SafeGuide1Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);// 进入动画和退出动画
		
	}
}
