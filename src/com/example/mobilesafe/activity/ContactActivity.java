package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ContactActivity extends Activity {
	

	private List<HashMap<String, String>> contactsLists;
	private ListView lvContact;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_layout);

		lvContact = (ListView) findViewById(R.id.lv_contactlist);
		
		contactsLists = new ArrayList<HashMap<String,String>>();
		readContacts();
		
		lvContact.setAdapter(new SimpleAdapter(this, contactsLists, 
				R.layout.contact_item_layout, new String[]{"name", "phone"}, 
				new int[]{R.id.tv_name, R.id.tv_phone}));
		
		lvContact.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String phone = contactsLists.get(position).get("phone");
				Intent intent = new Intent();
				intent.putExtra("phone", phone);
				setResult(Activity.RESULT_OK, intent);
				
				finish();
			}
		});
		
	}


	protected void readContacts() {
		Uri rawContactUri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataContactUrl = Uri.parse("content://com.android.contacts/data");
		Cursor rawCursor = getContentResolver().query(rawContactUri, new String[]{"contact_id"}, null, null, null);
		if (rawCursor != null) {
			while (rawCursor.moveToNext()) {
				String contactId = rawCursor.getString(0);
				
				Cursor dataCursor = getContentResolver().query(dataContactUrl, 
						new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contactId}, null);
				if (dataCursor != null) {
					HashMap<String, String> contactMap = new HashMap<String, String>();
					while (dataCursor.moveToNext()) {
						String data1 = dataCursor.getString(0);
						String mimetype = dataCursor.getString(1);
						if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
							contactMap.put("phone", data1);
						} else if ("vnd.android.cursor.item/name".equals(mimetype)) {
							contactMap.put("name", data1);	
						}
					}
					contactsLists.add(contactMap);
					dataCursor.close();
				}
			}
			rawCursor.close();
		}
	}
}
