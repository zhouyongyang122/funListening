package com.funlisten.thirdParty.statistics;

import android.content.Context;

import com.funlisten.BuildConfig;
import com.funlisten.utils.ZYChannelUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by ZY on 17/3/15.
 */

public class DataStatistics {

    public final static String U_MENT_KEY = "59a28eec82b6351dba000fdd";

    public static void init(Context context) {

        //友盟统计
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(context, U_MENT_KEY, "def", MobclickAgent.EScenarioType.E_UM_NORMAL));
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }
}
