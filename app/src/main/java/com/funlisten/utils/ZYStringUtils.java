package com.funlisten.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by ZY on 17/3/27.
 */

public class ZYStringUtils {

    public static String generateRandomStr(int len) {
        //字符源，可以根据需要删减
        String generateSource = "23456789abcdefghgklmnpqrstuvwxyz";//去掉1和i ，0和o
        String rtnStr = "";
        for (int i = 0; i < len; i++) {
            //循环随机获得当次字符，并移走选出的字符
            String nowStr = String.valueOf(generateSource.charAt((int) Math.floor(Math.random() * generateSource.length())));
            rtnStr += nowStr;
            generateSource = generateSource.replaceAll(nowStr, "");
        }
        return rtnStr;
    }

    /**
     * md5加密
     */
    public static String toMd5(String text) {
        if (!TextUtils.isEmpty(text)) {
            byte[] hash;
            try {
                hash = MessageDigest.getInstance("MD5").digest(text.getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Huh, MD5 should be supported?", e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Huh, UTF-8 should be supported?", e);
            }

            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } else {
            return "";
        }
    }

    /**
     * 是否全是数字(组名不能全是数字)
     */
    public static boolean checkIsAllDigit(String input) {
        return Pattern.matches("^[0-9]*$", input);
    }

    /**
     * 是否全是字母
     */
    public static boolean checkIsAllChar(String name) {
        return Pattern.matches("^[A-Za-z]*$", name);
    }

    /**
     * 判断字符串是否为同一字符
     */
    public static boolean isContainSameOneChar(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        char c = str.charAt(0);
        for (int i = 0; i < str.length(); i++) {
            if (c != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static String join(List list, String split) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i)).append(split);
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }
}
