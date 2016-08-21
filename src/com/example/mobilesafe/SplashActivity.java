package com.example.mobilesafe;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class SplashActivity extends ActionBarActivity {

	private TextView versionName;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_activity_item_layout);
		
		versionName = (TextView) findViewById(R.id.tv_version);
		versionName.setText("�汾:" + getVersionName());
	}
	
	/**
	 * ��ȡ�汾��Ϣ
	 * */
	private String getVersionName(){
		PackageManager packageManager = getPackageManager();//��ȡ��������
		try {
			//��ȡ����Ϣ
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;//�汾��  "1"
			String versionName = packageInfo.versionName;//�汾�� 1.0 ...
			Log.d("MyTag", "versionName:" + versionName + "; versionCode:" + versionCode);
			return versionName;
		} catch (NameNotFoundException e) {
			//�Ҳ�������ʱ
			e.printStackTrace();
		}
		return "";
	}
}
