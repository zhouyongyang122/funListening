package com.funlisten;

import android.content.SharedPreferences;
import android.os.Trace;

/**
 * Created by ZY on 17/3/27.
 */

public class ZYPreferenceHelper {

    private static ZYPreferenceHelper instance;

    public static final String DEF_PRE_NAME = "def_pre_name";

    public static final String OPEN_4G_PLAY = "open_4g_play";

    public static final String OPEN_MSG = "open_msg";

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

    public boolean isOpen4GPlay() {
        return getDefPre().getBoolean(OPEN_4G_PLAY, true);
    }

    public void saveOpen4GPlay(boolean open) {
        getDefPre().edit().putBoolean(OPEN_4G_PLAY, open).commit();
    }

    public boolean isOpenMsg() {
        return getDefPre().getBoolean(OPEN_MSG, true);
    }

    public void saveOpenMsg(boolean open) {
        getDefPre().edit().putBoolean(OPEN_MSG, open).commit();
    }

    public SharedPreferences getDefPre() {
        if (defPre == null) {
            defPre = ZYApplication.getInstance().getSharedPreferences(DEF_PRE_NAME, 0);
        }
        return defPre;
    }
}
