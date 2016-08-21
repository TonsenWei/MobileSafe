package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主界面显示
 */
public class HomeActivity extends Activity {

	SharedPreferences sharedPreferences;
	private int backPressCounter = 0;
	// private TextView funcTextView;
	private GridView homeGridView;
	private String[] funcNameStr = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private int[] funcIcon = { R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
			R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity_layout);

		homeGridView = (GridView) findViewById(R.id.gv_home);
		homeGridView.setAdapter(new HomeAdapter());
		// 功能列表里的图标点击事件
		homeGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:// 进入手机防盗先设置密码或输入密码
					showPasswordDailog();
					break;
				case 8:// 进入设置中心
					startActivity(new Intent(HomeActivity.this, SettingActivity.class));
					break;

				default:
					break;
				}
			}
		});
	}

	protected void showPasswordDailog() {
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		String getPasswordStr = sharedPreferences.getString("password", "");
		if (TextUtils.isEmpty(getPasswordStr)) {// 没有设置过密码
			showSetPasswordDailog();
		} else { // 已经设置过密码
			showInputPasswordDailog(getPasswordStr);
		}
	}

	private void showInputPasswordDailog(final String password) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dailog = builder.create();

		View view = View.inflate(this, R.layout.dailog_inputpassword_layout, null);// 把布局转成View
		// dailog.setView(view;//将自定义的布局文件设置给dailog
		dailog.setView(view, 0, 0, 0, 0);// 设置边距为0, 保证2.x版本android上兼容问题
		dailog.show();

		final EditText etInputPassword = (EditText) view.findViewById(R.id.et_inputpwd);
		Button btInputOK = (Button) view.findViewById(R.id.bt_inputok);
		Button btInputCancel = (Button) view.findViewById(R.id.bt_inputcancel);

		btInputOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String getInputPassword = etInputPassword.getText().toString().trim();
				if (MD5Utils.MD5Encode(getInputPassword).equals(password)) {// 密码正确
					if (sharedPreferences.getBoolean("haveenter", false)) {// 进入过向导
						startActivity(new Intent(HomeActivity.this, SaveActivity.class));// 进入过则直接到防盗界面
					} else {// 没有进入过,开启向导
						startActivity(new Intent(HomeActivity.this, SaveGuide1Activity.class));
					}
					dailog.dismiss();
				} else {// 密码错误
					Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
				}
			}
		});
		btInputCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dailog.dismiss();
			}
		});

	}

	protected void showSetPasswordDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dailog = builder.create();
		View view = View.inflate(this, R.layout.dailog_setpassword_layout, null);// 把布局转成View
		// dailog.setView(view;//将自定义的布局文件设置给dailog
		dailog.setView(view, 0, 0, 0, 0);// 设置边距为0, 保证2.x版本android上兼容问题
		dailog.show();

		final EditText etFirstPassword = (EditText) view.findViewById(R.id.et_firstpwd);
		final EditText etConfirmPassword = (EditText) view.findViewById(R.id.et_confirmpwd);
		Button btOK = (Button) view.findViewById(R.id.bt_ok);

		Button btCancel = (Button) view.findViewById(R.id.bt_cancel);

		btOK.setOnClickListener(new OnClickListener() {// 按下了确认键
			public void onClick(View v) {
				String firstPassword = etFirstPassword.getText().toString().trim();
				String confirmPassword = etConfirmPassword.getText().toString().trim();
				if (!TextUtils.isEmpty(firstPassword) && !confirmPassword.isEmpty()) {
					if (confirmPassword.equals(firstPassword)) {// 如果两次输入的密码相同
						sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
						// 把密码MD5加密后保存到sp
						sharedPreferences.edit().putString("password", MD5Utils.MD5Encode(confirmPassword)).commit();
						if (sharedPreferences.getBoolean("haveenter", false)) {// 进入过向导
							startActivity(new Intent(HomeActivity.this, SaveActivity.class));// 进入过则直接到防盗界面
						} else {// 没有进入过,开启向导
							startActivity(new Intent(HomeActivity.this, SaveGuide1Activity.class));
						}
						dailog.dismiss();
					} else {// 密码不匹配
						Toast.makeText(HomeActivity.this, "两次输入的密码不匹配", Toast.LENGTH_SHORT).show();
					}
				} else {// 密码为空
					Toast.makeText(HomeActivity.this, "密码不能为空" + firstPassword + confirmPassword, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		btCancel.setOnClickListener(new OnClickListener() { // 点击取消按钮隐藏对话框
			public void onClick(View v) {
				dailog.dismiss();
			}
		});
	}

	class HomeAdapter extends BaseAdapter {
		public int getCount() {
			return funcNameStr.length;
		}

		@Override
		public Object getItem(int position) {
			return funcNameStr[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(HomeActivity.this, R.layout.home_item_list_layout, null);
			} else {
				view = convertView;
			}

			ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

			ivIcon.setImageResource(funcIcon[position]);
			tvItem.setText(funcNameStr[position]);

			return view;
		}
	}

	@Override
	public void onBackPressed() {
		backPressCounter++;
		if (backPressCounter < 2) {
			Toast.makeText(HomeActivity.this, "再按一次退出手机卫士", Toast.LENGTH_SHORT).show();
		} else {
			finish();
		}
		// super.onBackPressed();
	}

}
