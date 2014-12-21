package com.cellection;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.LinearLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class NeighboursActivity extends Activity {

	TableLayout tableLayout;
	int flag = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neighbours);

	}
	@SuppressWarnings("deprecation")
	@Override
	public void onStart() 
	{ 
		super.onStart();
		TextView textGsmCellLocation = (TextView) findViewById(R.id.gsmcelllocation);
		TextView textMCC = (TextView) findViewById(R.id.mobileCountryCode);
		TextView textMNC = (TextView) findViewById(R.id.mobileNetworkCode);
		TextView textCID = (TextView) findViewById(R.id.cellID);

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

		String networkOperator = telephonyManager.getNetworkOperator();
		String mobileCountryCode = networkOperator.substring(0, 3);
		String mobileNetworkCode = networkOperator.substring(3);
		textGsmCellLocation.setText("GSM Cell Location : ");
		textMCC.setText("Mobile Country Code : " + mobileCountryCode);
		textMNC.setText("Mobile Network Code : " + mobileNetworkCode);

		int cellID = cellLocation.getCid();
		//int locationAreaCode = cellLocation.getLac();
		textGsmCellLocation.setText(cellLocation.toString());
		textCID.setText("GSM Cell ID: " + String.valueOf(cellID));

		TextView Neighboring = (TextView) findViewById(R.id.neighboring);
		List<NeighboringCellInfo> NeighboringList = telephonyManager.getNeighboringCellInfo();

		if(flag==0)
		{
			String stringNeighboring = "Neighboring List- Lac : Cid : RSSI\n";
			tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
			TableRow tableHead = new TableRow(this);
			TextView tv0 = new TextView(this);
			tv0.setText(" Sl.No ");
			tv0.setTextColor(Color.WHITE);
			tableHead.addView(tv0);
			TextView tv1 = new TextView(this);
			tv1.setText(" Location Area Code ");
			tv1.setTextColor(Color.WHITE);
			tableHead.addView(tv1);
			TextView tv2 = new TextView(this);
			tv2.setText(" Cell ID ");
			tv2.setTextColor(Color.WHITE);
			tableHead.addView(tv2);
			TextView tv3 = new TextView(this);
			tv3.setText(" RSSI (dBm) ");
			tv3.setTextColor(Color.WHITE);
			tableHead.addView(tv3);
			tableLayout.addView(tableHead);

			for(int i=0; i < NeighboringList.size(); i++)
			{
				TableRow row = new TableRow(this);
				row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				String dBm;
				int rssi = NeighboringList.get(i).getRssi();
				if(rssi == NeighboringCellInfo.UNKNOWN_RSSI)
					dBm = "Unknown RSSI";
				else
					dBm = String.valueOf(-113 + 2 * rssi) + " dBm";

				for (int j = 1; j <= 3; j++) 
				{
					TextView textView = new TextView(this);
					LinearLayout outerLayout = new LinearLayout(this);
					LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

					textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
					textView.setBackgroundResource(R.drawable.rect_pressed);
					textView.setPadding(5, 5, 5, 5);
					if(j==1)
						textView.setText(String.valueOf(NeighboringList.get(i).getLac()));
					else if(j==2)
						textView.setText(String.valueOf(NeighboringList.get(i).getCid()));
					else
						textView.setText(dBm);

					outerLayout.addView(textView, textLayoutParams);
					row.addView(outerLayout);
				}

				tableLayout.addView(row);
				//setContentView(tableLayout);
				/*tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
					stringNeighboring = stringNeighboring
					+ String.valueOf(NeighboringList.get(i).getLac()) +" : "
					+ String.valueOf(NeighboringList.get(i).getCid()) +" : "
					+ dBm +"\n";*/
			}
			//Neighboring.setText(stringNeighboring);
			flag=1;
		}
	}

}
