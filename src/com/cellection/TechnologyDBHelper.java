package com.cellection;

import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TechnologyDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "Technology";

	public TechnologyDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+ DB_NAME+ " (TechnologyID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Name TEXT);");

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
		Long id = database.insert(DB_NAME, null, values);
		database.close();
		//String stringValues = values.toString();
		return id;
	}

	public Cursor getTechnology(String name) {
		String query = "SELECT * FROM " + DB_NAME + " WHERE Name = \"" + name + "\"";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		database.close();
		return cursor;
	}

}