package com.cellection;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class CellUtil {

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
	
	public static Long checkAndInsertLocation(double latitude,double longitude, Context signalStrengthService) {
		LocationDBHelper LocationDBHelper = new LocationDBHelper(signalStrengthService);
		Cursor cursor = LocationDBHelper.getLocation(latitude, longitude);
		boolean addLocation = true;
		Long id = null;
		if (cursor.moveToFirst()) {
			id = cursor.getLong(0);
			Log.d("Distance  is ", cursor.getString(5));
			addLocation = false;
		}
		if (addLocation) {
			id = insertLocation(latitude, longitude, LocationDBHelper);
		}
		return id;
	}

	private static Long insertLocation(double latitude, double longitude, LocationDBHelper locationDBHelper) {
		Map<String, String> locationDetails = new HashMap<String, String>();
		locationDetails.put("Latitude", String.valueOf(latitude));
		locationDetails.put("Longitude", String.valueOf(longitude));

		latitude =  degreesToRadians(latitude);
		longitude = degreesToRadians(longitude);

		double sinLatitude = Math.sin(latitude);
		double sinLongitude = Math.sin(longitude);
		locationDetails.put("sin_Latitude", String.valueOf(sinLatitude));
		locationDetails.put("sin_Longitude", String.valueOf(sinLongitude));

		double cosLatitude = Math.cos(latitude);
		double cosLongitude = Math.cos(longitude);
		locationDetails.put("cos_Latitude", String.valueOf(cosLatitude));
		locationDetails.put("cos_Longitude", String.valueOf(cosLongitude));

		return locationDBHelper.insertRecord(locationDetails);
	}

	public static double degreesToRadians(double degrees) {
		return (degrees * Math.PI / 180.0);
	}
	
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

	public static Long checkAndInsertTechnology(String name,Context signalStrengthService) {
		TechnologyDBHelper techDBHelper = new TechnologyDBHelper(signalStrengthService);
		Cursor cursor = techDBHelper.getTechnology(name);
		boolean addCountry = true;
		Long id = null;
		if (cursor.moveToFirst()) {
			id = cursor.getLong(0);
			addCountry = false;
		}
		if (addCountry) {
			id = insertCountry(name, techDBHelper);
		}
		return id;
	}

	private static Long insertCountry(String name,TechnologyDBHelper techDBHelper) {
		Map<String, String> technologyDetails = new HashMap<String, String>();
		technologyDetails.put("Name", name);
		return techDBHelper.insertRecord(technologyDetails);
	}

}
