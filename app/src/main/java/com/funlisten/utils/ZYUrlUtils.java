package com.funlisten.utils;

import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by ZY on 17/3/18.
 */

public class ZYUrlUtils {

    /**
     * 读取baseurl
     *
     * @param url
     * @return
     */
    public static String getBasUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }

    /**
     * 获取带后缀的文件名
     *
     * @param url
     * @return
     */
    public static String getFileNameAndSuffixByUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        Uri uri = Uri.parse(url);
        return uri.getLastPathSegment();
    }

    /**
     * 获取文件名
     *
     * @param url
     * @return
     */
    public static String getFileNameByUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String fileName = getFileNameAndSuffixByUrl(url);
        return fileName.substring(0, fileName.indexOf("."));
    }

    /**
     * 获取文件名
     *
     * @param url
     * @return
     */
    public static String getSuffix(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String fileName = getFileNameAndSuffixByUrl(url);
        return fileName.substring(fileName.indexOf(".") + 1);
    }
}
