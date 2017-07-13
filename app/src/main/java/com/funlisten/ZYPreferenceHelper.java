package com.funlisten;

import android.content.SharedPreferences;

/**
 * Created by ZY on 17/3/27.
 */

public class ZYPreferenceHelper {

    private static ZYPreferenceHelper instance;

    public static final String DEF_PRE_NAME = "def_pre_name";

    public static final String TIME_OFFSET = "TimeOffset";

    public static final String INSERT_DEF_BOOK = "insert_def_book";

    public static final String SHOW_TRACT_BG = "show_tract_bg";//是否显示句子的点击区域

    public static final String SHOW_TRACT_TRANS = "show_tract_trans";//是否显示句子的翻译

    public static final String TRACT_SPEED = "tract_speed";//句子朗读速度

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

    public void setShowTractBg(boolean showTractBg) {
        getDefPre().edit().putBoolean(SHOW_TRACT_BG, showTractBg).commit();
    }

    public boolean isShowTractBg() {
        return getDefPre().getBoolean(SHOW_TRACT_BG, false);
    }

    public void setShowTractTrans(boolean showTractTrans) {
        getDefPre().edit().putBoolean(SHOW_TRACT_TRANS, showTractTrans).commit();
    }

    public boolean isShowTractTrans() {
        return getDefPre().getBoolean(SHOW_TRACT_TRANS, false);
    }

    public void setTractSpeed(int tractSpeed) {
        getDefPre().edit().putInt(TRACT_SPEED, tractSpeed).commit();
    }

    public int getTractSpeed() {
        return getDefPre().getInt(TRACT_SPEED, 50);
    }

    /**
     * 设置已经插入默认的书籍
     *
     * @param isInserted
     */
    public void setInsertDefBook(boolean isInserted) {
        getDefPre().edit().putBoolean(INSERT_DEF_BOOK, isInserted).apply();
    }

    /**
     * 是否已经插入默认的书籍
     *
     * @return
     */
    public boolean isInsertDefBook() {
        return getDefPre().getBoolean(INSERT_DEF_BOOK, false);
    }

    /**
     * 设置请求验证的时间搓
     *
     * @param timeOffset
     */
    public void setTimeOffset(long timeOffset) {
        getDefPre().edit().putLong(TIME_OFFSET, timeOffset).commit();
    }


    /**
     * 返回请求验证的时间搓
     *
     * @return
     */
    public long getTimeOffset() {
        return getDefPre().getLong(TIME_OFFSET, 0);
    }

    public SharedPreferences getDefPre() {
        if (defPre == null) {
            defPre = ZYApplication.getInstance().getSharedPreferences(DEF_PRE_NAME, 0);
        }
        return defPre;
    }
}
