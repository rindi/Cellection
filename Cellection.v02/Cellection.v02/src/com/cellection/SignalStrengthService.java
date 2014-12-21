package com.cellection;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.cellection.CellularNetworkType;
import com.cellection.CountryUtil;
import com.cellection.LocationUtil;
import com.cellection.OperatorUtil;
import com.cellection.TechnologyUtil;

public class SignalStrengthService extends Service {

	TelephonyManager tManager;
	MyPhoneStateListener MyListener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Initial values
		double latitude = 0;
		double longitude = 0;

		if (intent == null) {
			GPSTrackingService gpsTrackingService = new GPSTrackingService(this);
			if (gpsTrackingService.canGetLocation()) {
				latitude = gpsTrackingService.getLatitude();
				longitude = gpsTrackingService.getLongitude();
			} else {
				return START_STICKY;
			}

		} else {
			Bundle extras = intent.getExtras();
			latitude = extras.getDouble("Latitude");
			longitude = extras.getDouble("Longitude");
		}

		// Continue running until stopped
		MyListener = new MyPhoneStateListener();
		tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		Long countryID = CountryUtil.checkAndInsertCountry(tManager.getNetworkCountryIso(), this);

		int networkType = tManager.getNetworkType();
		String networkClass = CellularNetworkType.getLabel(networkType);
		Long technologyID = TechnologyUtil.checkAndInsertTechnology(networkClass, this);

		Log.d("Country ID is ", String.valueOf(countryID));
		Log.d("Technology ID is ", String.valueOf(technologyID));

		String operatorName = tManager.getNetworkOperatorName();
		Long operatorID = OperatorUtil.checkAndInsertOperator(operatorName,this);
		Log.d("Operator ID is ", String.valueOf(operatorID));

		OperatorUtil.insertOperatorCountry(operatorID, countryID, this);

		LocationUtil.validateAndInsertLocation(latitude, longitude, this);

		tManager.listen(MyListener,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

	// Start the PhoneState listener
	private class MyPhoneStateListener extends PhoneStateListener {

		// Get the Signal strength from the provider for every update in value
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			int strength = signalStrength.getGsmSignalStrength();
			if (strength < 10) {
				sendNotification(strength);
			}

		}

		//Send notification to the user when signal strength is poor
		@SuppressLint("NewApi")
		private void sendNotification(int strength)

		{
			Intent intent = new Intent();
			PendingIntent pIntent = PendingIntent.getActivity(SignalStrengthService.this, 0, intent, 0);
			Notification notification = new Notification.Builder(SignalStrengthService.this)
			.setTicker("Signal Strength Notification")
			.setContentTitle("Signal Strength decreased")
			.setContentText("Your signal strength has decreased to " + strength)
			.setContentIntent(pIntent).build();
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.notify(0, notification);
		}

	};/* End of private Class */

}
