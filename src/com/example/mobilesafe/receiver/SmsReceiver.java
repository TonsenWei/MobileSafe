package com.example.mobilesafe.receiver;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.LocationService;

import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

@TargetApi(23)
public class SmsReceiver extends BroadcastReceiver {

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] bObjects = (Object[]) intent.getExtras().get("pdus");

		for (Object object : bObjects) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
			String orignatingAddress = message.getOriginatingAddress();// 来信息的号码
			String messageBody = message.getMessageBody();// 获取短信内容

			Log.d("MyTag", "SMSaddr:" + orignatingAddress + ",body:" + messageBody);
			
			if ("#*alarm*#".equals(messageBody)) {//用媒体播放铃声,不是来电铃声
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1.0f, 1.0f);//设置音量最大
				player.setLooping(true);//一直循环
				player.start();
				
				abortBroadcast();//中断短信传递让系统的应用接收不到
			} else if ("#*location*#".equals(messageBody)) {//定位
				context.startService(new Intent(context, LocationService.class));//开启定位服务
				
				SharedPreferences pref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
				String location = pref.getString("location", "Geting location...");
				Log.d("MyTag", location);
				abortBroadcast();//中断短信传递让系统的应用接收不到
				
				if (!"Geting location...".equals(location)) {//如果已经获取到位置信息就把服务关掉					
					context.stopService(new Intent(context, LocationService.class));
					pref.edit().remove("location").commit(); //获取位置后,把sp的位置信息先清除,以下次更新位置
					Log.d("MyTag", "service stopped,sp removed");
				}
			} else if ("#*lock*#".equals(messageBody)) {//锁屏
				//获取设备策略服务
				DevicePolicyManager dPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				//设备管理组件
				ComponentName mDeviceAdminSample = new ComponentName(context, AdminReceiver.class);
				if (dPM.isAdminActive(mDeviceAdminSample)) {
					dPM.lockNow();					//锁屏
					dPM.resetPassword("123456", 0); //重设PIN密码
				}else {
					Toast.makeText(context, "必须先激活设备管理器", Toast.LENGTH_SHORT).show();
				}
				abortBroadcast();//中断短信传递让系统的应用接收不到
			}
		}
	}

}
