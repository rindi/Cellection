package com.cellection;

import java.util.Map;

import com.cellection.CellUtil;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "Location";

	@SuppressLint("NewApi")
	public LocationDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DB_NAME + " (LocationID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Latitude DOUBLE, Longitude DOUBLE, CountryID INTEGER,"
				+ " sin_Latitude DOUBLE,sin_Longitude DOUBLE, cos_Latitude DOUBLE,cos_Longitude DOUBLE, FOREIGN KEY(CountryID) REFERENCES Country(CountryID));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
		onCreate(db);
	}

	public Long insertRecord(Map<String, String> queryValMap) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Latitude", queryValMap.get("Latitude"));
		values.put("Longitude", queryValMap.get("Longitude"));

		values.put("sin_Latitude", queryValMap.get("sin_Latitude"));
		values.put("sin_Longitude", queryValMap.get("sin_Longitude"));

		values.put("cos_Latitude", queryValMap.get("cos_Latitude"));
		values.put("cos_Longitude", queryValMap.get("cos_Longitude"));

		Long id = database.insert(DB_NAME, null, values);
		database.close();
		Log.d("LocationID is ", String.valueOf(id));
		return id;
	}

	public Cursor getLocation(double latitude, double longitude) {

		latitude = CellUtil.degreesToRadians(latitude);
		longitude = CellUtil.degreesToRadians(longitude);

		double sinLatitude = Math.sin(latitude);
		double cosLatitude = Math.cos(latitude);

		double sinLongitude = Math.sin(longitude);
		double cosLongitude = Math.cos(longitude);

		String query = "SELECT Location.*,(" + sinLatitude
				+ " * Location.sin_Latitude + " + cosLatitude + " * "
				+ "Location.cos_Latitude *(" + sinLongitude
				+ " * Location.sin_Longitude + " + cosLongitude
				+ " * Location.cos_Longitude)) AS distance FROM Location";

		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);

		return cursor;
	}
}
