/*
 * 
 * @FileName Utils.java
 * @Description This class is the Utils of other classes.
 * 
 * @author wlf
 * @data 2012-10-10
 * 
 * @note 
 * @note 
 * 
 * @warning
 */
package com.funlisten.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

/**
 * This class is the Utils of other classes
 *
 * @author weilinfeng
 * @Data 2013-10-24
 */
@SuppressWarnings("deprecation")
public class ZYSystemUtils {

    /**
     * 变量/常量说明
     */
    private static final String TAG = "ZYSystemUtils";
    /**
     * 变量/常量说明
     */
    private static WakeLock mWakeLock = null;
    /**
     * 变量/常量说明
     */

    private static KeyguardLock mKeyguardLock = null;

    /**
     * 变量/常量说明
     */
    public static final int OS_2_3 = 9;
    /**
     * 变量/常量说明
     */
    public static final int OS_2_3_3 = 10;
    /**
     * 变量/常量说明
     */
    public static final int OS_3_0 = 11;
    /**
     * 变量/常量说明
     */
    public static final int OS_4_0_3 = 15;
    private static String versionName;
    private static int versionCode;
    private static String deviceId;

    /**
     * light the screen
     *
     * @param context
     * @since V1.0
     */
    public static void acquireWakeLock(Context context) {
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        }


        mWakeLock.acquire();
    }

    /**
     * unlight the screen
     *
     * @since V1.0
     */
    public static void releaseWakeLock() {
        if (null == mWakeLock) {
            Log.d(TAG, "releaseWakeLock");
            return;
        }

        try {
            mWakeLock.release();//always release before acquiring for safety just in case
        } catch (Exception e) {
            //probably already released
            Log.e(TAG, e.getMessage());
        }
        mWakeLock = null;
    }

    /**
     * unlock the screen
     *
     * @param context
     * @since V1.0
     */
    public static void disableKeyguard(Context context) {
        if (null == mKeyguardLock) {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            mKeyguardLock = keyguardManager.newKeyguardLock("unLock");
        }

        try {
            mKeyguardLock.disableKeyguard();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * unlock the screen
     *
     * @since V1.0
     */
    public static void releaseKeyguard() {
        if (null == mKeyguardLock) {
            Log.d(TAG, "releasePowerManager");
            return;
        }

        try {
            //always before acquiring for safety just in case
            mKeyguardLock.reenableKeyguard();
        } catch (Exception e) {
            //probably already released
            Log.e(TAG, e.getMessage());
        }

        mKeyguardLock = null;
    }

    /**
     * 获取CPU序列号
     *
     * @return
     * @since V1.0
     */
    public static String getCPUSerial() {
        String str = "", strCPU = "", cpuAddress = "0000000000000000";
        Process pp = null;
        InputStreamReader ir = null;
        LineNumberReader input = null;
        try {
            // 读取CPU信息
            pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            ir = new InputStreamReader(pp.getInputStream());
            input = new LineNumberReader(ir);
            // 查找CPU序列号
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    // 查找到序列号所在行
                    if (str.indexOf("Serial") > -1) {
                        // 提取序列号
                        strCPU = str.substring(str.indexOf(":") + 1, str.length());
                        // 去空格
                        cpuAddress = strCPU.trim();
                        break;
                    }
                } else {
                    // 文件结尾
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                input = null;
            }
            if (ir != null) {
                try {
                    ir.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ir = null;
            }
            if (pp != null) {
                pp.destroy();
                pp = null;
            }
        }
        return cpuAddress;

    }

    /**
     * 获取APP版本名称
     *
     * @param context
     * @return
     * @since V1.0
     */
    public static String getAppVersionName(Context context) {
        if (versionName == null) {
            try {
                // ---get the package info---
                PackageManager pm = context.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                versionName = pi.versionName;
                if (versionName == null || versionName.length() <= 0) {
                    return "";
                }
            } catch (Exception e) {
                Log.e("VersionInfo", "Exception", e);
            }
        }

        return versionName;
    }

    /**
     * 获取APP版本Code
     *
     * @param context
     * @return
     * @since V1.0
     */
    public static int getAppVersionCode(Context context) {
        if (versionCode == 0) {
            try {
                // ---get the package info---
                PackageManager pm = context.getPackageManager();
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                versionCode = pi.versionCode;
            } catch (Exception e) {
                Log.e("VersionInfo", "Exception", e);
            }
        }

        return versionCode;
    }

    /**
     * 获取版本名称,如3.212，只保留以为小数
     *
     * @param version
     * @return
     */
    private static String formatVersion(String version) {
        String fv = "";
        boolean bDot = false;

        char[] vesrionArr = version.toCharArray();
        for (char c : vesrionArr) {
            if (c == '.') {
                if (!bDot) {
                    fv += c;
                    bDot = true;
                }
            } else if (c >= '0' && c <= '9') {
                fv += c;
            }
        }

        return fv;
    }

    /**
     * 判断 app是栈顶
     *
     * @param packageName
     * @param context
     * @return
     * @since V1.0
     */
    public static boolean isTopApp(String packageName, Context context) {
        Log.d(TAG, "isTopApp packageName:" + packageName);
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            String packageNameInfo = tasksInfo.get(0).topActivity.getPackageName().trim();
            Log.d(TAG, "isTopApp packageNameInfo:" + packageNameInfo);
            // 应用程序位于堆栈的顶层
            if (packageName.trim().equalsIgnoreCase(packageNameInfo)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 activity是栈顶
     *
     * @param
     * @param context
     * @return
     * @since V1.0
     */
    public static boolean isTopActivity(String className, Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo != null) {
            String packageNameInfo = tasksInfo.get(0).topActivity.getPackageName();
            String classNameInfo = tasksInfo.get(0).topActivity.getClassName();
            Log.d(TAG, "isTopActivity package name:" + packageNameInfo);
            Log.d(TAG, "isTopActivity class name:" + classNameInfo);

            // 应用程序位于堆栈的顶层
            if (classNameInfo.equalsIgnoreCase(className)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 这里对方法做描述
     *
     * @param className
     * @param context
     * @return
     * @since V1.0
     */
    public static boolean isHaveActivity(String className, Context context) {
        boolean isHave = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> activityList = am.getRunningTasks(100);
        if (!(activityList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < activityList.size(); i++) {
            String topClassNameInfo = activityList.get(i).topActivity.getClassName();
            String baseClassNameInfo = activityList.get(i).baseActivity.getClassName();
            Log.d(TAG, "isTopActivity topClassNameInfo:" + topClassNameInfo);
            Log.d(TAG, "isTopActivity baseClassNameInfo:" + baseClassNameInfo);
            if (topClassNameInfo.equals(className) || baseClassNameInfo.equals(className)) {
                isHave = true;
                break;
            }
        }

        return isHave;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 获取手机的sim卡卡号
     *
     * @see
     * @since V1.0
     */
    public static String getSimNumber(Context context) {
        String simNumber = null;
        @SuppressWarnings("static-access")
        TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        // 取得相关系统服务
        simNumber = tm.getSimSerialNumber();
        if (null == simNumber || "".equals(simNumber)) {
            simNumber = newRandomUUID();
        }
        return simNumber;
    }

    /**
     * 生成UUID
     *
     * @see
     * @since V1.0
     */
    /**
     * 这里对方法做描述
     *
     * @return
     * @since V1.0
     */
    private static String newRandomUUID() {
        String uuidRaw = UUID.randomUUID().toString();
        return uuidRaw.replaceAll("-", "");
    }

    /**
     * 这里对方法做描述
     *
     * @param context
     * @since V1.0
     */
    public static void clearAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    /**
     * 这里对方法做描述
     *
     * @return
     * @since V1.0
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {

        }
        return version;
    }

    /**
     * 这里对方法做描述
     *
     * @param context
     * @return String
     * @since V1.0
     */
    public static String getUUID(Context context) {
        String uuid = "";
        synchronized (ZYSystemUtils.class) {
            final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                } else {
                    final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                            .getDeviceId();
                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID
                            .randomUUID().toString();
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        return uuid;
    }


    /**
     * 这里对方法做描述
     *
     * @param context
     * @return
     * @since V1.0
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static String getDeviceId(Context context) {
        if (context == null) {
            return "";
        }

        return ZYDeviceIDUtil.getInstance().getDeviceID(context);
    }

    public static String getAndroidId(Context context) {
        if (context == null) {
            return "";
        }

        String androidId = Secure.getString(
                context.getContentResolver(),
                Secure.ANDROID_ID);
        if (TextUtils.isEmpty(androidId)) {
            return "";
        } else {
            return androidId;
        }
    }

    public static String getMacAddress(Context context) {
        if (context == null) {
            return "";
        }
        WifiManager wifiMng = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfor = wifiMng.getConnectionInfo();
        String mac = wifiInfor.getMacAddress();
        if (TextUtils.isEmpty(mac)) {
            return "";
        } else {
            return mac;
        }
    }

    public static String getImsi(Context context) {
        if (context == null) {
            return "";
        }

        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
        if (TextUtils.isEmpty(imsi)) {
            return "";
        } else {
            return imsi;
        }
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
