package com.cellection;

import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CountryDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "Country";

	public CountryDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "
				+ DB_NAME
				+ " (CountryID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Name TEXT, CountryCode TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
		onCreate(db);
	}

	public Long insertRecord(Map<String, String> queryValMap) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Name", queryValMap.get("Name"));
		values.put("CountryCode", queryValMap.get("CountryCode"));
		Long id = database.insert(DB_NAME, null, values);
		database.close();
		return id;
	}

	public Cursor getCountry(String countryCode)
	{
		String query = "SELECT * FROM " + DB_NAME + " WHERE CountryCode = \"" + countryCode + "\"";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		return cursor;
	}

}
