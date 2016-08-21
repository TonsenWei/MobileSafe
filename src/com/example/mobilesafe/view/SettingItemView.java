package com.example.mobilesafe.view;

import com.example.mobilesafe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.mobilesafe";
	private TextView tvTitle; 
	private TextView tvDesc; 
	private CheckBox cbUpdateStatus; 
	private String mTitle;
	private String mDescOn;
	private String mDescOff;
	/**
	 * 初始化布局
	 * */
	private void initView() {
		//将自定义好的布局文件设置给当前的SettingItemView
		View.inflate(getContext(), R.layout.setting_item_view_layout, this);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		cbUpdateStatus = (CheckBox) findViewById(R.id.cb_status);	
		
		tvTitle.setText(mTitle);
	}
	
	private void setDesc(String desc){
		tvDesc.setText(desc);
	}
	
	public boolean isChecked() {
		return cbUpdateStatus.isChecked();
	}
	public void setChecked(boolean check){
		cbUpdateStatus.setChecked(check);//复选框是否打钩
		//根据选择的状态显示不同文字描述状态
		if (check) {
			setDesc(mDescOn);
		} else {
			setDesc(mDescOff);
		}
	}
	
	@SuppressLint("NewApi")
	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mTitle = attrs.getAttributeValue(NAMESPACE, "title");//根据属性名称获取属性值
		mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
		initView();
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}

}
