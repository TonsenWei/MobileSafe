package com.example.mobilesafe.activity;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends ActionBarActivity {

	protected static final int CODE_UPDATE_DAILOG = 1;
	protected static final int CODE_URL_ERROR = 2;
	protected static final int CODE_NET_ERROR = 3;
	protected static final int CODE_JSON_ERROR = 4;
	protected static final int CODE_NO_UPDATE = 5;
	
	private TextView versionName;
	private TextView downloadProgress;
	
	private String mVersionName;	//服务器版本名
	private int	mVersionCode;		//服务器版本号
	private String mDescription;    //服务器版本描述
	private String mDownloadUrl;	//服务器版本下载地址
	
	private int versionCode;		//当前版本号
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg){
			switch (msg.what) {
			case CODE_UPDATE_DAILOG:
				showUpdateDailog();//显示对话框
				break;
			case CODE_NO_UPDATE:
				enterHome();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			default:
				break;
			}
		};
	};
	
	private RelativeLayout rlRoot;//根布局
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_activity_layout);
		
		versionName = (TextView) findViewById(R.id.tv_version);
		downloadProgress = (TextView) findViewById(R.id.tv_progress);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		
		versionName.setText("当前版本:" + getVersionName());
		
		
		//从配置文件中取出数据,判断是否已经在配置中心中设置了如果有新版本则自动更新
		SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
		if (pref.getBoolean("auto_update", true)) {
			checkVersion(); //如果设置了自动更新则检测服务器是否有新版本后更新
		}else {
//			enterHome();//如果没有勾选自动更新版本则直接进入主界面
			
			//延迟2秒让用户看到初始界面信息再发消息进入主界面
			mHandler.sendEmptyMessageDelayed(CODE_NO_UPDATE, 2000);
		}
		
		AlphaAnimation aAnim = new AlphaAnimation(0.2f, 1.0f);
		aAnim.setDuration(2000);
		rlRoot.startAnimation(aAnim);//让splash界面渐变
	}
	/**
	 * 进入主界面
	 * */
	protected void enterHome() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();//销毁主界面
	}
	private void checkVersion(){
		new Thread(){
			public void run(){
				URL url;
				HttpURLConnection conn = null;
				Message message = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {
					url = new URL("http://192.168.67.194:8080/day2/update.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					conn.connect();
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = conn.getInputStream();
						String resultJson = StreamUtils.stream2StrUtil(inputStream);
						//解析JSON数据
						JSONObject jo = new JSONObject(resultJson);
						mVersionCode = jo.getInt("versionCode");
						mVersionName = jo.getString("versionName");
						mDescription = jo.getString("description");
						mDownloadUrl = jo.getString("downloadUrl");
						
						//Log.d("MyTag", "code:" + mVersionCode + ",name:" + mVersionName + ",desc:" + mDescription + ",DURL:" + mDownloadUrl);
						if (mVersionCode > versionCode) {//有新版本
							message.what = CODE_UPDATE_DAILOG;//发送弹出更新版本的对话框的消息
						}else { //没有新版本
							message.what = CODE_NO_UPDATE;
						}
					}
				} catch (MalformedURLException e) {//url错误异常
					message.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {	//网络错误
					message.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {//Json解析错误
					message.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;
					if (timeUsed < 2000) {//网络太快,为了能让客户看到界面的版本信息停到2秒
						try {
							Thread.sleep(2000-timeUsed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mHandler.sendMessage(message);
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}.start();
	}
	/**
	 * 显示更新版本的对话框
	 * */
	protected void showUpdateDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("当前版本:" + mVersionName);
		builder.setMessage(mDescription);
		builder.setPositiveButton("立即更新", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downloadNewVersion();//下载新版本
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//Log.d("MyTag", "以后再说,直接跳到主界面");
				enterHome();//进入主界面
			}
		});
		//用户在对话框界面点击了返回按键,则直接跳到主界面
		builder.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				enterHome();//进入主界面
			}
		});
		builder.show();
	}
	/**
	 * 下载apk
	 * */
	protected void downloadNewVersion() {
		//检查sdcard状态
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String saveDir = Environment.getExternalStorageDirectory() + "/update.apk";
			downloadProgress.setVisibility(View.VISIBLE);//显示进度
			//XUtils开源工具下载
			HttpUtils utils = new HttpUtils();
			utils.download(mDownloadUrl, saveDir, new RequestCallBack<File>() {
				
				public void onLoading(long total, long current, boolean isUploading) {
					downloadProgress.setText("下载进度:" + current*100/total + "%");
					super.onLoading(total, current, isUploading);
				}
				public void onSuccess(ResponseInfo<File> arg0) {
					Toast.makeText(SplashActivity.this, "下载完成!", Toast.LENGTH_SHORT).show();
					//启动系统的apk包安装工具
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result), "application/vnd.android.package-archive");
					startActivityForResult(intent, 0);//系统安装工具安装完成返回主界面
				}
				public void onFailure(HttpException arg0, String arg1) {
					
				}
			});
		}
	}
	//系统安装工具安装完成返回主界面
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		enterHome();		
		super.onActivityResult(arg0, arg1, arg2);
	}
	/**
	 * 获取版本号
	 * */
	private String getVersionName(){
		PackageManager packageManager = getPackageManager();
		try {
			//
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionCode = packageInfo.versionCode;//
			String versionName = packageInfo.versionName;//
			Log.d("MyTag", "versionName:" + versionName + "; versionCode:" + versionCode);
			return versionName;
		} catch (NameNotFoundException e) {
			//
			e.printStackTrace();
		}
		return "";
	}
}
