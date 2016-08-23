package com.example.mobilesafe.service;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {

	private SharedPreferences mPreferences;
	LocationManager lm;
	MyLocationListener listener;
	@Override
	public void onCreate() {
		super.onCreate();
		
		mPreferences = getSharedPreferences("config", MODE_PRIVATE);//准备保存位置信息到sp
		
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//		List<String> allProviders = lm.getAllProviders();//获取所有的位置提供者比如GPS,网络等
		
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);//是否允许扣费,比如使用3g网络定位
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//指明位置提供者的整体精确度级别
		String bestProvider = lm.getBestProvider(criteria, true);//获取最佳位置提供者
		
		listener = new MyLocationListener();

		/**
		 * 请求更新位置信息
		 * 参数1 : 位置提供者
		 * 参数2 : 最短更新时间
		 * 参数3 : 最短更新距离
		 * 参数4 : 监听事件
		 * */
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
	}
	public class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			String longitude = location.getLongitude() + "";//获取经度,浮点转为字符串
			String latitude = location.getLatitude() + "";
			mPreferences.edit().putString("location", longitude + ";" + latitude).commit();
			Log.d("MyTag", "经度:" + longitude + "--纬度:" + latitude);
			stopSelf();//把服务停掉,省电
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(listener);//服务销毁时停止更新位置,省电
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
