package com.lxf.weatherdemo.db;

import java.util.ArrayList;
import java.util.List;

import com.lxf.weatherdemo.util.LogUtil;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class DBOpt {

	private static final String TAG = "DBOpt";

	public static List<String> queryProvince(SQLiteDatabase sQliteDatabase) {
		List<String> result = new ArrayList<String>();
		Cursor cursor = sQliteDatabase.query(DBInfo.PROVINCE_TABLE_NAME, null,
				null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				result.add(cursor.getString(cursor
						.getColumnIndex(DBInfo.PROVINCE_COLUMN_NAME)));
			} while (cursor.moveToNext());
		}
		return result;
	}

	public static void saveProvince(SQLiteDatabase sQliteDatabase, String string) {
		if (!TextUtils.isEmpty(string)) {
			LogUtil.d(TAG, "Insert province into DB:" + string);
			String[] allProvince = string.split(",");
			for (String p : allProvince) {
				String[] provinceInfo = p.split("\\|");
				ContentValues values = new ContentValues();
				assert (provinceInfo.length == 2);

				values.put(DBInfo.PROVINCE_COLUMN_CODE, provinceInfo[0]);
				values.put(DBInfo.PROVINCE_COLUMN_NAME, provinceInfo[1]);
				sQliteDatabase.insert(DBInfo.PROVINCE_TABLE_NAME, null, values);

				

			}
		}
	}

}
