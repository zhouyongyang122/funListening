package com.funlisten;

import android.content.SharedPreferences;

/**
 * Created by ZY on 17/3/27.
 */

public class ZYPreferenceHelper {

    private static ZYPreferenceHelper instance;

    public static final String DEF_PRE_NAME = "def_pre_name";

    private SharedPreferences defPre;

    private ZYPreferenceHelper() {

    }

    public static ZYPreferenceHelper getInstance() {
        if (instance == null) {
            synchronized (ZYPreferenceHelper.class) {
                if (instance == null) {
                    instance = new ZYPreferenceHelper();
                }
            }
        }
        return instance;
    }

    public SharedPreferences getDefPre() {
        if (defPre == null) {
            defPre = ZYApplication.getInstance().getSharedPreferences(DEF_PRE_NAME, 0);
        }
        return defPre;
    }
}
