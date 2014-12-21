package com.cellection;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.cellection.LocationDBHelper;

public class LocationUtil {
	public static Long validateAndInsertLocation(double latitude,double longitude, Context signalStrengthService) {
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

}
