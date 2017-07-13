package com.funlisten.utils;

import com.funlisten.ZYApplication;

/**
 * Created by ZY on 17/3/22.
 */

public class ZYResourceUtils {

    public static int getColor(int res) {
        return ZYApplication.getInstance().getResources().getColor(res);
    }

    public static String getString(int res) {
        return ZYApplication.getInstance().getString(res);
    }
}
