package com.example.mobilesafe.activity;


import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * 设置安全号码向导界面
 * */
public class SafeGuide3Activity extends BaseGuideActivity {

	private Button btPickContact;
	private EditText etInputPhone;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.safe_guide3_layout);
		
		etInputPhone = (EditText) findViewById(R.id.et_safenumber);
		btPickContact = (Button) findViewById(R.id.bt_selectcontact);
		
		
		String safeNumber = mSP.getString("safe_number", "");//如果已经设置过安全号码就显示到et上
		if (!TextUtils.isEmpty(safeNumber)) {
			etInputPhone.setText(safeNumber);
		}
		
		btPickContact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SafeGuide3Activity.this, ContactActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			String phone = data.getStringExtra("phone");
			etInputPhone.setText(phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 下一页
	@Override
	public void enterNextPage() {
		String inputStr = etInputPhone.getText().toString().trim();
		if (TextUtils.isEmpty(inputStr)) {//安全号码为空则提示
			Toast.makeText(SafeGuide3Activity.this, "请先输入安全号码", Toast.LENGTH_SHORT).show();
			return;
		} else {
			mSP.edit().putString("safe_number", inputStr).commit();
		}
		startActivity(new Intent(SafeGuide3Activity.this, SafeGuide4Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画

	}

	// 上一页
	@Override
	public void enterPreviousPage() {
		startActivity(new Intent(SafeGuide3Activity.this, SafeGuide2Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);// 进入动画和退出动画

	}
}
