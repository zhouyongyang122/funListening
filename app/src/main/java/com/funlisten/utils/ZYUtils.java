package com.funlisten.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.funlisten.R;
import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZY on 17/3/14.
 */

public class ZYUtils {

    /**
     * 描述：判断网络是否有效.
     *
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean existSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    public static void showInput(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideInput(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 压缩到指定大小
     *
     * @param filePath 原图路径
     * @param toFile   保存路径
     * @param size     指定大小
     * @return
     */
    public static boolean compressToSize(String filePath, File toFile, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            int quality = 90;

            bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);

            Logger.d("compress size = " + baos.toByteArray().length);
            while (baos.toByteArray().length > size) {
                baos.reset();
                quality -= 10;
                bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                Logger.d("compress size = " + baos.toByteArray().length);
            }
            baos.writeTo(new FileOutputStream(toFile));
            bm.recycle();
            bm = null;
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static byte[] toByteArray(File f) throws IOException {
        if (!f.exists()) {
            throw new FileNotFoundException("path not found!");
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * 检测密码复杂度
     * 6-18位字母与数字组合密码；对于少于六位、纯数字或者纯字母的密码不可用
     */
    public static boolean checkPassword(Context context, String password) {
        if (TextUtils.isEmpty(password)) {
            ZYToast.show(context, context.getString(R.string.toast_password_not_null));
            return false;
        } else {
            // 密码长度大于6为位
            if (password.length() < 6) {
                ZYToast.show(context, context.getString(R.string.toast_password_limit));
                return false;
            }

            // 密码不能全为同一符号
            if (isContainSameoneChar(password)) {
                ZYToast.show(context, context.getString(R.string.toast_password_limit));
                return false;
            }

            // 密码不能全为数字
            if (isContainDigitalOnly(password)) {
                ZYToast.show(context, context.getString(R.string.toast_password_limit));
                return false;
            }

            // 密码不能全为字母
            if (isContainCharaterOnly(password)) {
                ZYToast.show(context, context.getString(R.string.toast_password_limit));
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为同一字符
     */
    public static boolean isContainSameoneChar(String str) {
        char c = str.charAt(0);
        for (int i = 0; i < str.length(); i++) {
            if (c != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为纯字母
     */
    public static boolean isContainCharaterOnly(String str) {
        if (str.matches("[a-zA-Z]*")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是纯数字
     */
    public static boolean isContainDigitalOnly(String pwd) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(pwd);
        return isNum.matches();
    }

    public static String getRawContent(Context context, int rawResId) {
        String content;
        Resources resources = context.getResources();
        InputStream is = null;
        try {
            is = resources.openRawResource(rawResId);
            byte buffer[] = new byte[is.available()];
            is.read(buffer);
            content = new String(buffer);
            return content;
        } catch (IOException e) {
        } catch (OutOfMemoryError e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
