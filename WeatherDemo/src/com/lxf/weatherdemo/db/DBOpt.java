package com.lxf.weatherdemo.db;

import java.util.ArrayList;
import java.util.List;

import com.lxf.weatherdemo.model.City;
import com.lxf.weatherdemo.model.County;
import com.lxf.weatherdemo.model.Province;
import com.lxf.weatherdemo.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class DBOpt {

	private static final String TAG = "DBOpt";

	private static final String DB_NAME = "area.db";
	private static final int DB_VERSION = 1;
	private DBHelper dBHelper;
	private SQLiteDatabase sQliteDatabase;

	private static DBOpt dBOpt;

	private DBOpt(Context context) {
		dBHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
		sQliteDatabase = dBHelper.getWritableDatabase();
	}

	public synchronized static DBOpt getInstance(Context context) {
		if (dBOpt == null) {
			dBOpt = new DBOpt(context);
		}
		return dBOpt;
	}

	public List<Province> queryProvince() {
		List<Province> result = new ArrayList<Province>();
		Cursor cursor = sQliteDatabase.query(DBInfo.PROVINCE_TABLE_NAME, null,
				null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province p = new Province();
				p.setId(cursor.getInt(cursor
						.getColumnIndex(DBInfo.PROVINCE_COLUMN_ID)));
				p.setCode(cursor.getString(cursor
						.getColumnIndex(DBInfo.PROVINCE_COLUMN_CODE)));
				p.setName(cursor.getString(cursor
						.getColumnIndex(DBInfo.PROVINCE_COLUMN_NAME)));

				result.add(p);
			} while (cursor.moveToNext());
		}
		return result;
	}

	public void saveProvince(String string) {
		if (!TextUtils.isEmpty(string)) {
			LogUtil.d(TAG, "Insert province into DB:" + string);
			String[] allProvince = string.split(",");
			for (String p : allProvince) {
				String[] provinceInfo = p.split("\\|");
				ContentValues values = new ContentValues();

				values.put(DBInfo.PROVINCE_COLUMN_CODE, provinceInfo[0]);
				values.put(DBInfo.PROVINCE_COLUMN_NAME, provinceInfo[1]);
				sQliteDatabase.insert(DBInfo.PROVINCE_TABLE_NAME, null, values);
			}
		}
	}

	public void saveCity(String string, int provinceId) {
		if (!TextUtils.isEmpty(string)) {
			LogUtil.d(TAG, "Insert city into DB:" + string);
			String[] allCities = string.split(",");
			for (String c : allCities) {
				String[] cityInfo = c.split("\\|");

				ContentValues values = new ContentValues();
				values.put(DBInfo.CITY_COLUMN_CODE, cityInfo[0]);
				values.put(DBInfo.CITY_COLUMN_NAME, cityInfo[1]);
				values.put(DBInfo.CITY_COLUMN_PROVINCE, provinceId);
				sQliteDatabase.insert(DBInfo.CITY_TABLE_NAME, null, values);
				LogUtil.d(TAG, "Insert City, Code:" + cityInfo[0] + "Name:"
						+ cityInfo[1] + "ProvinceId:" + provinceId);
			}
		}
	}

	public List<City> queryCity(int currentProvinceId) {
		List<City> result = new ArrayList<City>();
		Cursor cursor = sQliteDatabase.query(DBInfo.CITY_TABLE_NAME, null,
				DBInfo.CITY_COLUMN_PROVINCE + " = ?",
				new String[] { String.valueOf(currentProvinceId) }, null, null,
				null);
		LogUtil.d(TAG, "Query city, provinceId:" + currentProvinceId
				+ "Result size:" + cursor.getCount());
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor
						.getColumnIndex(DBInfo.CITY_COLUMN_ID)));
				city.setCode(cursor.getString(cursor
						.getColumnIndex(DBInfo.CITY_COLUMN_CODE)));
				city.setName(cursor.getString(cursor
						.getColumnIndex(DBInfo.CITY_COLUMN_NAME)));
				city.setProvince(cursor.getInt(cursor
						.getColumnIndex(DBInfo.CITY_COLUMN_PROVINCE)));
				result.add(city);
			} while (cursor.moveToNext());
		}
		return result;
	}

	public List<County> queryCounty(int id) {
		List<County> result = new ArrayList<County>();
		Cursor cursor = sQliteDatabase.query(DBInfo.COUNTY_TABLE_NAME, null,
				DBInfo.COUNTY_COLUMN_CITY + " = ?",
				new String[] { String.valueOf(id) }, null, null, null);
		LogUtil.d(TAG, "queryCounty, id:" + id);
		LogUtil.d(TAG, "Result of county:" + cursor.getCount());
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor
						.getColumnIndex(DBInfo.COUNTY_COLUMN_ID)));
				county.setCode(cursor.getString(cursor
						.getColumnIndex(DBInfo.COUNTY_COLUMN_CODE)));
				county.setName(cursor.getString(cursor
						.getColumnIndex(DBInfo.COUNTY_COLUMN_NAME)));
				county.setCity(cursor.getInt(cursor
						.getColumnIndex(DBInfo.COUNTY_COLUMN_CITY)));
				result.add(county);
			} while (cursor.moveToNext());
		}
		return result;
	}

	public void saveCounty(String string, int id) {
		String[] allCounty = string.split(",");
		for (String c : allCounty) {
			String[] countyInfo = c.split("\\|");
			ContentValues values = new ContentValues();
			values.put(DBInfo.COUNTY_COLUMN_CODE, countyInfo[0]);
			values.put(DBInfo.COUNTY_COLUMN_NAME, countyInfo[1]);
			values.put(DBInfo.COUNTY_COLUMN_CITY, id);

			sQliteDatabase.insert(DBInfo.COUNTY_TABLE_NAME, null, values);
		}
	}
}
