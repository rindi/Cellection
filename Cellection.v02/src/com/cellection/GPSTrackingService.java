package com.cellection;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GPSTrackingService  extends Service implements LocationListener  {

	private final Context context;

	Location location; // Location
	double latitude; // Latitude
	double longitude; // Longitude

	// Flag for GPS status
	boolean isGPSEnabled = false;

	// Flag for Network status
	boolean isNetworkEnabled = false;

	// Flag for location availability
	boolean locationAvailable = false;

	// Minimum distance to track changes in meters
	private static final long MIN_DISTANCE_REQUIRED_FOR_UPDATES = 20; // 20 meters

	// Minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	// Location Manager
	protected LocationManager locationManager;

	public GPSTrackingService(Context mContext) {
		this.context = mContext;
		getLocation();
	}

	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

			// Getting GPS status
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// Getting Network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// No Network provider is enabled
			} else {
				this.locationAvailable = true;
				// Get location from Network Provider
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_REQUIRED_FOR_UPDATES, this);
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// If GPS Enabled, get lat/long coordinates
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_REQUIRED_FOR_UPDATES, this);
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}


	public boolean locationAvailable() {
		return this.locationAvailable;
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}

		return longitude;
	}

	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		// Setting Dialog Title
		alertDialog.setTitle("GPS Settings");

		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Open settings?");

		// Open Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
			}
		});

		// Cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Show Alert Message
		alertDialog.show();
	}

}
