package com.funlisten.base.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.funlisten.base.activity.picturePicker.ZYPicturePickerActivity;

import java.util.ArrayList;

/**
 * Created by ZY on 17/4/7.
 */

public class ZYPicker {

    public static final int REQUEST_PICKER = 44;

    public static final String KEY_MAX_SELECT = "max_select";
    public static final String KEY_SELECTED_PICTURES = "selected_pictures";
    public static final String KEY_IS_SINGLE = "is_single";
    public static final String KEY_SINGLE_URI = "single_uri";

    private Intent mPickerIntent;
    private Bundle mPickerOptionsBundle;

    public static ZYPicker create() {
        return new ZYPicker();
    }

    private ZYPicker() {
        mPickerIntent = new Intent();
        mPickerOptionsBundle = new Bundle();
    }

    public ZYPicker setMax(int max) {
        mPickerOptionsBundle.putInt(KEY_MAX_SELECT, max);
        return this;
    }

    public ZYPicker single() {
        mPickerOptionsBundle.putBoolean(KEY_IS_SINGLE, true);
        return this;
    }

    public void start(@NonNull Activity activity) {
        activity.startActivityForResult(getIntent(activity), REQUEST_PICKER);
    }

    public void start(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    public Intent getIntent(@NonNull Context context) {
        mPickerIntent.setClass(context, ZYPicturePickerActivity.class);
        mPickerIntent.putExtras(mPickerOptionsBundle);
        return mPickerIntent;
    }

    public static ArrayList<String> getOutput(@NonNull Intent data) {
        return data.getStringArrayListExtra(KEY_SELECTED_PICTURES);
    }

    public static Uri getSingleOutput(@NonNull Intent data) {
        return data.getParcelableExtra(KEY_SINGLE_URI);
    }
}
