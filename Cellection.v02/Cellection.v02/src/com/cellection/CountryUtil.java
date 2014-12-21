package com.cellection;

import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import android.content.Context;
import android.database.Cursor;

import com.cellection.CountryDBHelper;

public class CountryUtil {

	public static Long checkAndInsertCountry(String countryCode,Context signalStrengthService) {
		CountryDBHelper countryDBHelper = new CountryDBHelper(signalStrengthService);
		Cursor cursor = countryDBHelper.getCountry(countryCode);
		boolean addCountry = true;
		Long id = null;
		if (cursor.moveToFirst()) {
			id = cursor.getLong(0);
			addCountry = false;
		}
		if (addCountry) {
			id = insertCountry(countryCode, countryDBHelper);
		}
		return id;
	}

	private static Long  insertCountry(String countryCode,CountryDBHelper countryDBHelper)
	{
		Map<String, String> countryDetails = new HashMap<String, String>();
		countryDetails.put("CountryCode", countryCode);
		Locale locale = new Locale("", countryCode);
		countryDetails.put("Name", locale.getDisplayCountry());
		return countryDBHelper.insertRecord(countryDetails);
	}
}
