package com.cellection;

import android.telephony.TelephonyManager;

public enum CellularNetworkType {
	
	xRTT(TelephonyManager.NETWORK_TYPE_1xRTT, "2G"), 
	CDMA(TelephonyManager.NETWORK_TYPE_CDMA, "2G"),
	EDGE(TelephonyManager.NETWORK_TYPE_EDGE, "2G"),
	GPRS(TelephonyManager.NETWORK_TYPE_GPRS, "2G"),
	IDEN(TelephonyManager.NETWORK_TYPE_IDEN, "2G"),
	EHRPD(TelephonyManager.NETWORK_TYPE_EHRPD, "3G"),
	EVDO_0(TelephonyManager.NETWORK_TYPE_EVDO_0, "3G"),
	EVDO_A(TelephonyManager.NETWORK_TYPE_EVDO_A, "3G"),
	EVDO_B(TelephonyManager.NETWORK_TYPE_EVDO_B, "3G"),
	HSDPA(TelephonyManager.NETWORK_TYPE_HSDPA, "3G"),
	HSPA(TelephonyManager.NETWORK_TYPE_HSPA, "3G"),
	HSPAP(TelephonyManager.NETWORK_TYPE_HSPAP, "3G"),
	HSUPA(TelephonyManager.NETWORK_TYPE_HSUPA, "3G"),
	UMTS(TelephonyManager.NETWORK_TYPE_UMTS, "3G"),
	LTE(TelephonyManager.NETWORK_TYPE_LTE, "4G"),
	UNKNOWN(TelephonyManager.NETWORK_TYPE_UNKNOWN, "Unknown"),;

	private int value;
	private String label;

	private CellularNetworkType(int value, String label) {
		this.value = value;
		this.label = label;
	}

	public static String getLabel(int value) {
		CellularNetworkType[] cellularNetworkTypeArray = CellularNetworkType.values();
		for (int i = 0; i < cellularNetworkTypeArray.length; i++) {
			CellularNetworkType cellularNetworkType = cellularNetworkTypeArray[i];
			if (cellularNetworkType.value == value) {
				return cellularNetworkType.label;
			}
		}
		return null;
	}

	public static int getValue(String label) {
		CellularNetworkType[] cellularNetworkTypeArray = CellularNetworkType.values();
		for (int i = 0; i < cellularNetworkTypeArray.length; i++) {
			CellularNetworkType cellularNetworkType = cellularNetworkTypeArray[i];
			if (cellularNetworkType.label.equalsIgnoreCase( label)) {
				return cellularNetworkType.value;
			}
		}
		return 0;
	}

}
