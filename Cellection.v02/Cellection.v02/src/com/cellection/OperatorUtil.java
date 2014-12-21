package com.cellection;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class OperatorUtil {
	public static Long checkAndInsertOperator(String name,Context signalStrengthService) {
		OperatorDBHelper operatorDBHelper = new OperatorDBHelper(signalStrengthService);
		Cursor cursor = operatorDBHelper.getOperator(name);
		boolean addCountry = true;
		Long id = null;
		if (cursor.moveToFirst()) {
			id = cursor.getLong(0);
			Log.d("Operator name is ", cursor.getString(1));
			addCountry = false;
		}
		if (addCountry) {
			id = insertOperator(name, operatorDBHelper);
		}
		return id;
	}

	private static Long insertOperator(String name,OperatorDBHelper operatorDBHelper) {
		Map<String, String> operatorDetails = new HashMap<String, String>();
		operatorDetails.put("Name", name);
		return operatorDBHelper.insertRecord(operatorDetails);
	}

	public static void insertOperatorCountry(Long operatorID, Long countryID,
			Context signalStrengthService) {
		OperatorCountryDBHelper operatorCountryDBHelper = new OperatorCountryDBHelper(signalStrengthService);
		Cursor cursor = operatorCountryDBHelper.getOperatorCountryRow(operatorID, countryID);
		boolean addRecord = true;
		Long id = null;
		if (cursor.moveToFirst()) {
			id = cursor.getLong(0);
			addRecord = false;
		}
		if (addRecord) {
			id = insertOperatorCountry(operatorID,countryID, operatorCountryDBHelper);
		}
		Log.d("Operator Country Id is ", String.valueOf(id));
	}


	private static Long insertOperatorCountry(Long operatorID,Long countryID,
			OperatorCountryDBHelper operatorCountryDBHelper) {
		Map<String, String> operatorDetails = new HashMap<String, String>();
		operatorDetails.put("OperatorID", String.valueOf(operatorID));
		operatorDetails.put("CountryID", String.valueOf(countryID));
		return operatorCountryDBHelper.insertRecord(operatorDetails);
	}

}

