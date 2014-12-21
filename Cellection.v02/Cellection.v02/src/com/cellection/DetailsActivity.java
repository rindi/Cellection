package com.cellection;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.gms.maps.MapFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cellection.GPSTrackingService;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DetailsActivity extends Activity {

	GPSTrackingService GPS;
	private GoogleMap map;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GPS = new GPSTrackingService(DetailsActivity.this);

		setContentView(R.layout.activity_details);
		// Check if GPS enabled
		if (GPS.canGetLocation()) {

			double latitude = GPS.getLatitude();
			double longitude = GPS.getLongitude();

			if (map == null) {
				map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
			}

			MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("New Marker");

			map.setMyLocationEnabled(true);

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
}
