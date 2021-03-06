package com.cellection;

import java.util.ArrayList;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PoorNetworkDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "PoorNetwork";

	public PoorNetworkDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+ DB_NAME+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Latitude DOUBLE, Longitude DOUBLE, Signal INTEGER, Operator TEXT);");

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
		values.put("Signal", queryValMap.get("Signal"));
		values.put("Operator", queryValMap.get("Operator"));

		Long id = database.insert(DB_NAME, null, values);
		database.close();
		Log.d("Poor network issue recorded", String.valueOf(id));
		return id;
	}

	public Cursor getRecord(String operator) {
		String query = "SELECT * FROM " + DB_NAME + " WHERE Operator = \"" + operator + "\"";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		return cursor;
	}

	public ArrayList<String>  getRecords(){
		String query = "SELECT * FROM " + DB_NAME +" LIMIT 5";
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<String> data = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			if (cursor != null)
			{
				if (cursor.moveToFirst()) {
					do{               
						double Latitude = cursor.getDouble(cursor.getColumnIndex("Latitude"));
						double Longitude = cursor.getDouble(cursor.getColumnIndex("Longitude"));
						data.add(Latitude+ ","+Longitude);
					}
					while(cursor.moveToNext());  				
				}
			}
			database.close();
		}
		return data;
	}
}