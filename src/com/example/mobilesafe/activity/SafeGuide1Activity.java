package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SafeGuide1Activity extends BaseGuideActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.safe_guide1_layout);
	}	
	
	@Override
	public void enterNextPage() {//进入下一页
		startActivity(new Intent(SafeGuide1Activity.this, SafeGuide2Activity.class));
		finish();
		
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画		
	}

	@Override
	public void enterPreviousPage() {
		
	}

}
