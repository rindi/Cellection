package com.cellection;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OperatorCountryDBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "OperatorCountryTable";

	public OperatorCountryDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "
				+ DB_NAME
				+ " (OperatorCountryID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ " OperatorID INTEGER,"
				+ " CountryID INTEGER,"
				+ " FOREIGN KEY(OperatorID) REFERENCES Operator(OperatorID),"
				+ " FOREIGN KEY(CountryID) REFERENCES Country(CountryID));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);

		onCreate(db);
	}

	public Long insertRecord(Map<String, String> queryValMap) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("OperatorID", queryValMap.get("OperatorID"));
		values.put("CountryID", queryValMap.get("CountryID"));
		Long id = database.insert(DB_NAME, null, values);
		database.close();
		Log.d("Operator Country ID is ", String.valueOf(id));
		return id;
	}

	public Cursor getOperatorCountryRow(Long operatorID, Long countryID) {
		String query = "Select * from " + DB_NAME + " where OperatorID = \""
				+ operatorID + "\" and " + "CountryID = \"" + countryID + "\"";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(query, null);
		return cursor;
	}

}
