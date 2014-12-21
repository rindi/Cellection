package com.cellection;

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
		db.execSQL("CREATE TABLE "+ DB_NAME+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Latitude DOUBLE, Longitude DOUBLE, Signal INTEGER, OperatorID INTEGER, Name TEXT);");

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
		values.put("Name", queryValMap.get("Name"));

		Long id = database.insert(DB_NAME, null, values);
		database.close();
		Log.d("Poor network issue recorded", String.valueOf(id));
		return id;
	}

	public Cursor getRecord(String name) {
		String query = "SELECT * FROM " + DB_NAME + " WHERE Name = \"" + name + "\"";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		return cursor;
	}

}