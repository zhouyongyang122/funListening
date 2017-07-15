package com.funlisten.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ZY on 17/4/9.
 */

public class ZYDateUtils {

    public static final String YYMMDDHHMMSS24 = "yyyy-MM-dd HH:mm:ss";
    public static final String YYMMDDHHMMSS12 = "yyyy-MM-dd hh:mm:ss";
    public static final String YYMMDDHHMM24 = "yyyy-MM-dd HH:mm";
    public static final String YYMMDDHHMM12 = "yyyy-MM-dd hh:mm";
    public static final String YYMMDDHH = "yyyy-MM-dd";
    public static final String HHMM24 = "HH:mm";

    /**
     * 获取传入的时间差的天时分  传入时间为秒
     *
     * @param otherTime
     * @return
     */
    public static int[] getTimeDiffOtherTime(long time, long otherTime) {
        long diff = 0;
        if (time > otherTime) {
            diff = time - otherTime;
        } else if (time < otherTime) {
            diff = otherTime - time;
        } else {
            return new int[]{0, 0, 0};
        }

        long oneDay = 24 * 60 * 60;
        long oneHours = 60 * 60;

        long day = diff / oneDay;
        long hours = (diff % oneDay) / oneHours;
        long minute = (diff % oneHours) / 60;
        return new int[]{(int) day, (int) hours, (int) minute};
    }

    public static String getTimeString(long timeInMills, String format) {
        try {
            Date date;
            if (timeInMills <= 0) {
                date = new Date();
            } else {
                date = new Date(timeInMills);
            }
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(date);
        } catch (Exception e) {

        }
        return "";
    }

    public static String getTimeString(String time, String inFormat, String outFormat) {
        try {
            DateFormat dateInFormat = new SimpleDateFormat(inFormat);
            Date date = dateInFormat.parse(time);

            SimpleDateFormat dateOutFormat = new SimpleDateFormat(outFormat);
            return dateOutFormat.format(date);
        } catch (Exception e) {

        }
        return "";
    }

}
