package com.cellection;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.MapFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cellection.GPSTrackingService;

public class DetailsActivity extends FragmentActivity {

	GPSTrackingService GPS;
	GoogleMap map;
	static String operator;
	/*public static final String LOCATION_PREFERENCES = "LocPrefs";
	SharedPreferences settings = getSharedPreferences(LOCATION_PREFERENCES, MODE_PRIVATE);
	SharedPreferences.Editor prefEditor;
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    operator = extras.getString("Operator");
		}

		GPS = new GPSTrackingService(DetailsActivity.this);

		setContentView(R.layout.activity_details);
		// Check if GPS enabled
		if (GPS.locationAvailable()) {

			double latitude = GPS.getLatitude();
			double longitude = GPS.getLongitude();

			if (map == null) {
				map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
			}

			PoorNetworkDBHelper info = new PoorNetworkDBHelper(this);
			String data = info.getRecord(operator).toString();
			info.close();
			Log.i("Data from DB",data);
			
			MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("New Marker");
			map.setMyLocationEnabled(true);
			map.addMarker(marker);

			CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
			CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

			map.moveCamera(cameraUpdate);
			map.animateCamera(zoom);
			/*
			Intent intent = new Intent(this, SignalStrengthService.class);
			Bundle extras = new Bundle();
			extras.putDouble("Latitude", latitude);
			extras.putDouble("Longitude", longitude);
			intent.putExtras(extras);
			startService(intent);*/

		}
		else {
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			GPS.showSettingsAlert();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_details, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {

		if (map == null) { 
			// Try to obtain the map from the SupportMapFragment.
			map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (map != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
