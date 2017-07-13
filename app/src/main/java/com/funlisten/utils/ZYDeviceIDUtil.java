package com.funlisten.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Created by ZY on 17/3/16.
 */

public class ZYDeviceIDUtil {

    private static ZYDeviceIDUtil instance;

    private SharedPreferences sharedPreferences;

    private ZYDeviceIDUtil() {

    }

    public static ZYDeviceIDUtil getInstance() {
        if (instance == null) {
            synchronized (ZYDeviceIDUtil.class) {
                if (instance == null) {
                    instance = new ZYDeviceIDUtil();
                }
            }
        }
        return instance;
    }

    public String getDeviceID(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("FZDeviceIDUtil", 0);
        }
        String deviceID = sharedPreferences.getString("deviceID", null);
        if (deviceID == null) {
            try {
                TelephonyManager tm = (TelephonyManager)
                        context.getSystemService(context.TELEPHONY_SERVICE);
                deviceID = tm.getDeviceId();
            } catch (Exception e) {

            }

            if (deviceID == null || deviceID.trim().length() <= 0) {
                try {
                    deviceID = Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                } catch (Exception e) {

                }
            }

            if (deviceID == null || deviceID.trim().length() <= 0) {

                try {
                    WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    if (manager != null) {
                        deviceID = manager.getConnectionInfo().getMacAddress();
                    }
                } catch (Exception e) {

                }
            }

            if (deviceID == null || deviceID.trim().length() <= 0) {
                try {
                    deviceID = UUID.randomUUID().toString();
                } catch (Exception ex) {

                }
            }
            sharedPreferences.edit().putString("deviceID", deviceID).commit();
        }
        return deviceID;
    }
}
