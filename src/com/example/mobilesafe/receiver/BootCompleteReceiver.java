package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.telephony.SmsManager;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		SharedPreferences spref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		boolean flagProtect = spref.getBoolean("protect", false);//是否开启防盗保护
		String protectSim = spref.getString("simserialnumber", "");
		
		if (flagProtect) {//已经开启防盗保护则检测sim是否已更换
			TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = tManager.getSimSerialNumber();//获取当前手机sim卡sn
			if (currentSim.equals(protectSim)) {//当前SIM卡与设置的保护SIM卡是同一张卡
				Log.d("MyTag", "sim卡是安全卡");
			}else {//sim卡有变化
				Log.d("MyTag", "sim卡已更换");
				String safePhone = spref.getString("safe_number", "");
				
				//发送短信到安全号码
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(safePhone, null, "Your Phone's sim had change", null, null);
			}
		}
	}

}
