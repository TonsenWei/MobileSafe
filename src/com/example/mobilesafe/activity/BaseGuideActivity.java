package com.example.mobilesafe.activity;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

public abstract class BaseGuideActivity extends Activity {
	
	private GestureDetector mGestureDetector;
	public SharedPreferences mSP;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSP = getSharedPreferences("config", MODE_PRIVATE);
		/**
		 * SimpleOnGestureListener需要导入android.view.GestureDetector.SimpleOnGestureListener
		 * e1: 滑动的起点 e2:滑动的终点 velocityX: 水平速度 velocityY: 垂直速度
		 * 要让这个监听起作用要在Activity的dispatchTouchEvent(MotionEvent ev)中接管监听
		 */
		mGestureDetector = new GestureDetector(this, new SimpleOnGestureListener() {
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

				if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {// 获取垂直方向上滑动的距离
					Toast.makeText(BaseGuideActivity.this, "滑动垂直距离过大", Toast.LENGTH_SHORT).show();
					return true;
				}
				if (Math.abs(velocityX) < 100) {// 滑动太慢
					Toast.makeText(BaseGuideActivity.this, "可以滑快点", Toast.LENGTH_SHORT).show();
					return true;
				}
				if ((e2.getRawX() - e1.getRawX()) > 200) {// 向右滑动,返回上一页
					enterPreviousPage();
					return true;
				}
				if ((e1.getRawX() - e2.getRawX()) > 200) {// 向左滑动,下一页
					enterNextPage();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}

		});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mGestureDetector.onTouchEvent(ev);//让手势管理器处理触摸事件
		return super.dispatchTouchEvent(ev);
	}
	
	//进入下一页的方法
	public abstract void enterNextPage();
	//返回上一页的方法
	public abstract void enterPreviousPage();
	
	//下一页按键的监听
	public void next(View view){
		enterNextPage();
	}
	//上一页按键的监听
	public void previous(View view){
		enterPreviousPage();
	}
}
