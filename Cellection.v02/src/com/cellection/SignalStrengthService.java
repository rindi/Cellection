package com.cellection;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.cellection.CellularNetworkType;
import com.cellection.CellUtil;

public class SignalStrengthService extends Service {

	TelephonyManager telephonyManager;
	MyPhoneStateListener MyListener;
	private static final String TAG = "com.cellection.SignalStrengthService";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Service onBind");
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "Service onCreate");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.i(TAG, "Service onStartCommand");
		//Initial values
		double latitude = 0;
		double longitude = 0;

		if (intent == null) {
			GPSTrackingService gpsTrackingService = new GPSTrackingService(this);
			if (gpsTrackingService.locationAvailable()) {
				latitude = gpsTrackingService.getLatitude();
				longitude = gpsTrackingService.getLongitude();
			} else {
				return Service.START_STICKY_COMPATIBILITY;
			}

		} else {
			Bundle extras = intent.getExtras();
			latitude = extras.getDouble("Latitude");
			longitude = extras.getDouble("Longitude");
		}

		// Continue running until stopped
		MyListener = new MyPhoneStateListener();
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		// Return the ISO country code equivalent for the SIM provider's country code.
		Long countryID = CellUtil.checkAndInsertCountry(telephonyManager.getSimCountryIso(), this);

		int networkType = telephonyManager.getNetworkType();
		// Return the NETWORK_TYPE for current data connection - LTE or 3G etc.
		String networkClass = CellularNetworkType.getLabel(networkType);
		Long technologyID = CellUtil.checkAndInsertTechnology(networkClass, this);

		Log.d("Country ID is ", String.valueOf(countryID));
		Log.d("Technology ID is ", String.valueOf(technologyID));

		// Returns the alphabetic name of current registered operator.
		String operatorName = telephonyManager.getNetworkOperatorName();
		Long operatorID = CellUtil.checkAndInsertOperator(operatorName,this);
		Log.d("Operator ID is ", String.valueOf(operatorID));

		CellUtil.checkAndInsertLocation(latitude, longitude, this);

		telephonyManager.listen(MyListener,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		return Service.START_STICKY_COMPATIBILITY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Service onDestroy");
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

	// Start the PhoneState listener
	private class MyPhoneStateListener extends PhoneStateListener {

		// Get the Signal strength from the provider for every update in value
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			int strength = signalStrength.getGsmSignalStrength();
			if (strength < 25) {
				sendNotification(strength);
				
			}

		}

		//Send notification to the user when signal strength is poor
		@SuppressLint("NewApi")
		private void sendNotification(int strength)

		{
			Intent intent = new Intent();
			PendingIntent pendingIntent = PendingIntent.getActivity(SignalStrengthService.this, 0, intent, 0);

			Notification notification = new Notification.Builder(SignalStrengthService.this)
			.setTicker("Signal Strength Notification")
			.setContentTitle("Signal Strength Decreased")
			.setContentText("The network signal strength has decreased to " + strength)
			.setSmallIcon(R.drawable.alerticon)
			.setAutoCancel(true)
			.setContentIntent(pendingIntent).build();

			notification.flags = Notification.FLAG_AUTO_CANCEL;
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Uri soundnotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), soundnotification);
			ringtone.play();
			notificationManager.notify(0, notification);
		}

	};/* End of private Class */

}
