package com.cellection;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.TextView;

public class NeighboursActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neighbours);
	}
	@Override
	public void onStart() 
	{ 
		super.onStart();
		//setContentView(R.layout.activity_neighbors);
		TextView textGsmCellLocation = (TextView) findViewById(R.id.gsmcelllocation);
		TextView textMCC = (TextView) findViewById(R.id.mobileCountryCode);
		TextView textMNC = (TextView) findViewById(R.id.mobileNetworkCode);
		TextView textCID = (TextView) findViewById(R.id.cellID);

		//retrieve a reference to an instance of TelephonyManager
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

		String networkOperator = telephonyManager.getNetworkOperator();
		String mobileCountryCode = networkOperator.substring(0, 3);
		String mobileNetworkCode = networkOperator.substring(3);
		textMCC.setText("Mobile Country Code: " + mobileCountryCode);
		textMNC.setText("Mobile Network Code: " + mobileNetworkCode);

		int cellID = cellLocation.getCid();
		//int locationAreaCode = cellLocation.getLac();
		textGsmCellLocation.setText(cellLocation.toString());
		textCID.setText("GSM Cell ID: " + String.valueOf(cellID));

		TextView Neighboring = (TextView) findViewById(R.id.neighboring);
		List<NeighboringCellInfo> NeighboringList = telephonyManager.getNeighboringCellInfo();

		String stringNeighboring = "Neighboring List- Lac : Cid : RSSI\n";
		for(int i=0; i < NeighboringList.size(); i++)
		{

			String dBm;
			int rssi = NeighboringList.get(i).getRssi();
			if(rssi == NeighboringCellInfo.UNKNOWN_RSSI)
			{
				dBm = "Unknown RSSI";
			}
			else
			{
				dBm = String.valueOf(-113 + 2 * rssi) + " dBm";
			}

			stringNeighboring = stringNeighboring
					+ String.valueOf(NeighboringList.get(i).getLac()) +" : "
					+ String.valueOf(NeighboringList.get(i).getCid()) +" : "
					+ dBm +"\n";
		}

		Neighboring.setText(stringNeighboring);
	}
}
