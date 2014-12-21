package com.cellection;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OperatorDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "Operator";

	public OperatorDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "
				+ DB_NAME
				+ " (OperatorID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Name TEXT);");

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
		return id;
	}

	public Cursor getOperator(String name)
	{
		String query = "Select * from " + DB_NAME +" where Name = \"" + name + "\"";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		database.close();
		return cursor;
	}

}

