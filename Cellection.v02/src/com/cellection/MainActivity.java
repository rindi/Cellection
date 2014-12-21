package com.cellection;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Button;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.TextView;
public class MainActivity extends Activity implements LocationListener {

	private TelephonyManager telephonyManager;
	private LocationManager locationManager;
	private String operator;
	double latitude = 0;
	double longitude = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// If GPS not enabled, send user to GPS settings
		if (!enabled) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		} 

		Criteria criteria = new Criteria();
		operator = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(operator);

		// Initialize the location fields
		if (location != null) {
			onLocationChanged(location);
		} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(operator, 400, 1, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	private PhoneStateListener phoneStateListener = new PhoneStateListener() {
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength)
		{
			TextView operatorView = (TextView) findViewById(R.id.operator);
			operatorView.setText(telephonyManager.getNetworkOperatorName());

			TextView signalStrengthView = (TextView) findViewById(R.id.signalStrength);
			signalStrengthView.setText(getGsmSignalStrengthInDbm(signalStrength.getGsmSignalStrength()));

			//signalStrengthView.setText(signalStrength.getGsmSignalStrength());
			//TextView signalStrengthViewInDBm = (TextView) findViewById(R.id.signalStrengthInDBm);
			//signalStrengthViewInDBm.setText(getGsmSignalStrengthInDbm(signalStrength.getGsmSignalStrength()));

			super.onSignalStrengthsChanged(signalStrength);
		}
	};
	private String getGsmSignalStrengthInDbm(int gsmSignalStrength) {
		if (gsmSignalStrength == 99)
			return "Not Detectable";

		int dbmStrength = -113;
		dbmStrength = dbmStrength + (gsmSignalStrength * 2);
		String strength = Integer.toString(dbmStrength) + " dBm";
		return strength;
	}

	private String getGpsCoordinates(Location location) {
		DecimalFormat decimalFormat = new DecimalFormat("###.####");
		latitude = Double.valueOf(decimalFormat.format(location.getLatitude()));
		longitude = Double.valueOf(decimalFormat.format(location.getLongitude()));

		String gpsCoordinates = "Latitude : " + Double.toString(latitude) + " \n Longitude : " + Double.toString(longitude);

		return gpsCoordinates;
	}

	private String getLocation(Location location) {
		float accuracy = location.getAccuracy();

		String locationString = "Indoor";

		if (accuracy < 10)
			locationString = "Outdoor";

		locationString = locationString + " (Accuracy = " + Float.toString(accuracy) + " m)";

		return locationString;
	}


	@Override
	public void onLocationChanged(Location location) {
		TextView gpsCoordinatesView = (TextView) findViewById(R.id.gpsCoordinates);
		gpsCoordinatesView.setText(getGpsCoordinates(location));

		TextView locationView = (TextView) findViewById(R.id.location);
		locationView.setText(getLocation(location));
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

	public void onClick(View v) {

		if(v.getId() == R.id.button1){
			Intent intent = new Intent(this, DetailsActivity.class);
			startActivity(intent);   

		}else if(v.getId() == R.id.button2){
			Intent intent = new Intent(this, NeighboursActivity.class);
			startActivity(intent);  

		}else if(v.getId() == R.id.button3){
			Intent intent = new Intent(this, SignalStrengthService.class);
			Bundle extras = new Bundle();
			extras.putDouble("Latitude", latitude);
			extras.putDouble("Longitude", longitude);
			intent.putExtras(extras);
			startService(intent);
		}

	}
}