package com.lxf.weatherdemo.db;

public class DBInfo {
	public static final String DB_NAME = "area.db";
	public static final String DB_VERSION = "1.0";

	public static final String PROVINCE_TABLE_NAME = "province";
	public static final String PROVINCE_COLUMN_ID = "id";
	public static final String PROVINCE_COLUMN_CODE = "code";
	public static final String PROVINCE_COLUMN_NAME = "name";
	public static final String PROVINCE_CREATE_TABLE = "create table "
			+ PROVINCE_TABLE_NAME + "(" + PROVINCE_COLUMN_ID
			+ " integer primary key autoincrement, " + PROVINCE_COLUMN_CODE
			+ " text, " + PROVINCE_COLUMN_NAME + " text)";

	public static final String CITY_TABLE_NAME = "city";
	public static final String CITY_COLUMN_ID = "id";
	public static final String CITY_COLUMN_CODE = "code";
	public static final String CITY_COLUMN_NAME = "name";
	public static final String CITY_COLUMN_PROVINCE = "province";
	public static final String CITY_CREATE_TABLE = "create table "
			+ CITY_TABLE_NAME + "(" + CITY_COLUMN_ID
			+ " integer primary key autoincrement, " + CITY_COLUMN_CODE
			+ " text, " + CITY_COLUMN_NAME + " text, " + CITY_COLUMN_PROVINCE
			+ " integer)";

	public static final String COUNTY_TABLE_NAME = "county";
	public static final String COUNTY_COLUMN_ID = "id";
	public static final String COUNTY_COLUMN_CODE = "code";
	public static final String COUNTY_COLUMN_NAME = "name";
	public static final String COUNTY_COLUMN_CITY = "city";
	public static final String COUNTY_CREATE_TABLE = "create table "
			+ COUNTY_TABLE_NAME + "(" + COUNTY_COLUMN_ID
			+ " integer primary key autoincrement, " + COUNTY_COLUMN_CODE
			+ " text, " + COUNTY_COLUMN_NAME + " text, " + COUNTY_COLUMN_CITY
			+ " integer)";

}
