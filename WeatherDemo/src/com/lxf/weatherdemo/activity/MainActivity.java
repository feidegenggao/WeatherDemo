package com.lxf.weatherdemo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lxf.weatherdemo.R;
import com.lxf.weatherdemo.db.DBOpt;
import com.lxf.weatherdemo.model.City;
import com.lxf.weatherdemo.model.County;
import com.lxf.weatherdemo.model.Province;
import com.lxf.weatherdemo.util.HttpCallbackListener;
import com.lxf.weatherdemo.util.HttpUtil;
import com.lxf.weatherdemo.util.LogUtil;

public class MainActivity extends Activity {

	protected static final String TAG = "MainActivity";

	/* Widget */
	ListView areaLV;
	TextView titleTV;

	/* data level */
	private static final int PROVINCE_LEVEL = 0;
	private static final int CITY_LEVEL = 1;
	private static final int COUNTY_LEVEL = 2;
	private int CURRENT_LEVEL = PROVINCE_LEVEL;

	/* database */
	private DBOpt dBOpt;

	/* data set */
	private List<String> areaList;
	private ArrayAdapter<String> areaAdapter;

	private List<Province> currentProvinceList;
	private Province currentProvince;
	private List<City> currentCityList;
	private City currentCity;
	private List<County> currentCountyList;
	private County currentCounty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main_layout);

		dBOpt = DBOpt.getInstance(this);

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
				switch (CURRENT_LEVEL) {
				case PROVINCE_LEVEL:
					CURRENT_LEVEL = CITY_LEVEL;
					currentProvince = currentProvinceList.get(position);
					queryData();
					break;

				case CITY_LEVEL:
					CURRENT_LEVEL = COUNTY_LEVEL;
					currentCity = currentCityList.get(position);
					queryData();
					break;

				case COUNTY_LEVEL:
					// currentCounty = currentCountyList.get(position);
					break;

				default:
					break;
				}
			}
		});

		queryData();
	}

	private void queryData() {
		switch (CURRENT_LEVEL) {
		case PROVINCE_LEVEL:
			currentProvinceList = dBOpt.queryProvince();
			if (currentProvinceList.size() > 0) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						areaList.clear();
						for (Province p : currentProvinceList) {
							areaList.add(p.getName());
						}

						titleTV.setText("China");
						areaAdapter.notifyDataSetChanged();
					}
				});

			} else {
				LogUtil.d(TAG, "queryProvince null result");
				final String address = "http://www.weather.com.cn/data/list3/city.xml";
				HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

					@Override
					public void onFinish(String string) {
						dBOpt.saveProvince(string);

						queryData();
					}

					@Override
					public void onError(Exception e) {
						LogUtil.e(TAG, "senRequestError:" + e.getMessage());
					}
				});
			}
			break;

		case CITY_LEVEL:
			currentCityList = dBOpt.queryCity(currentProvince.getId());
			if (currentCityList.size() > 0) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						areaList.clear();
						for (City c : currentCityList) {
							areaList.add(c.getName());
						}

						titleTV.setText(currentProvince.getName());
						areaAdapter.notifyDataSetChanged();
					}
				});

			} else {
				final String address = "http://www.weather.com.cn/data/list3/city"
						+ currentProvince.getCode() + ".xml";
				HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

					@Override
					public void onFinish(String string) {
						dBOpt.saveCity(string, currentProvince.getId());

						queryData();
					}

					@Override
					public void onError(Exception e) {
						LogUtil.e(TAG,
								"send Reuqest City error:" + e.getMessage());
					}
				});
			}
			break;

		case COUNTY_LEVEL:
			currentCountyList = dBOpt.queryCounty(currentCity.getId());
			if (currentCountyList.size() > 0) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						areaList.clear();
						for (County c : currentCountyList) {
							areaList.add(c.getName());
						}

						titleTV.setText(currentCity.getName());
						areaAdapter.notifyDataSetChanged();
					}
				});
			} else {
				final String address = "http://www.weather.com.cn/data/list3/city"
						+ currentCity.getCode() + ".xml";
				LogUtil.d(TAG,
						"queryCounty result is null. we will query from server");
				LogUtil.d(TAG, "The url is :" + address);
				HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

					@Override
					public void onFinish(String string) {
						LogUtil.d(TAG,
								"queryCounty onFinish, save data to database");
						dBOpt.saveCounty(string, currentCity.getId());
						queryData();
					}

					@Override
					public void onError(Exception e) {
						LogUtil.e(TAG,
								"send Reuqest County error:" + e.getMessage());
					}
				});
			}
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		switch (CURRENT_LEVEL) {
		case PROVINCE_LEVEL:
			break;

		case CITY_LEVEL:
			break;

		case COUNTY_LEVEL:
			break;

		default:
			break;
		}
	}
}
