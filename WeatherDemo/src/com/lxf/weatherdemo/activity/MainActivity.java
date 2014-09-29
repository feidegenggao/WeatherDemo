package com.lxf.weatherdemo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lxf.weatherdemo.R;
import com.lxf.weatherdemo.db.DBHelper;
import com.lxf.weatherdemo.db.DBOpt;
import com.lxf.weatherdemo.util.HttpCallbackListener;
import com.lxf.weatherdemo.util.HttpUtil;
import com.lxf.weatherdemo.util.LogUtil;

public class MainActivity extends Activity {

	protected static final String TAG = "MainActivity";

	/* Widget */
	ListView areaLV;
	TextView titleTV;

	/* list view */
	private List<String> areaList;
	private ArrayAdapter<String> areaAdapter;

	/* data level */
	private static final int PROVINCE_LEVEL = 0;
	private static final int CITY_LEVEL = 1;
	private static final int COUNTY_LEVEL = 2;
	private int CURRENT_LEVEL = PROVINCE_LEVEL;

	/* database */
	private static final String DBName = "area.db";
	private static final int DBVersion = 1;
	private DBHelper dBHelper;
	private SQLiteDatabase sQliteDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_layout);

		dBHelper = new DBHelper(this, DBName, null, DBVersion);
		sQliteDatabase = dBHelper.getWritableDatabase();

		areaLV = (ListView) findViewById(R.id.areaLV);
		titleTV = (TextView) findViewById(R.id.titleTV);

		areaList = new ArrayList<String>();
		areaAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, areaList);
		areaLV.setAdapter(areaAdapter);
		areaLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO
			}
		});

		queryData();
	}

	private void queryData() {
		switch (CURRENT_LEVEL) {
		case PROVINCE_LEVEL:
			List<String> temp = DBOpt.queryProvince(sQliteDatabase);
			if (temp.size() > 0) {
				areaList.clear();
				for (String s : temp) {
					areaList.add(s);
				}
				
				titleTV.setText("China");
				// areaAdapter.notifyDataSetChanged();

			} else {
				LogUtil.d(TAG, "queryProvince null result");
				final String address = "http://www.weather.com.cn/data/list3/city.xml";
				HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

					@Override
					public void onFinish(String string) {
						DBOpt.saveProvince(sQliteDatabase, string);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								queryData();
							}
						});
					}

					@Override
					public void onError(Exception e) {
						LogUtil.e(TAG, "senRequestError:" + e.getMessage());
					}
				});
			}
			break;

		default:
			break;
		}
	}
}
