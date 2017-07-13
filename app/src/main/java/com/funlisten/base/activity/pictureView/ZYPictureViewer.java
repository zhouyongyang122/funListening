package com.funlisten.base.activity.pictureView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ZYPictureViewer {

    public static final String KEY_PICTURE_PATHS = "picture_paths";
    public static final String KEY_INDEX = "index";
    public static final String KEY_OPERATION = "operation";
    public static final String KEY_IS_SHOW_INDEX = "is_show_index";

    private Intent mViewerIntent;
    private Bundle mViewerOptionsBundle;

    public static ZYPictureViewer create() {
        return new ZYPictureViewer();
    }

    private ZYPictureViewer() {
        mViewerIntent = new Intent();
        mViewerOptionsBundle = new Bundle();
    }

    public ZYPictureViewer withData(ArrayList<String> pathList) {
        mViewerOptionsBundle.putStringArrayList(KEY_PICTURE_PATHS, pathList);
        return this;
    }

    public ZYPictureViewer withIndex(int index) {
        mViewerOptionsBundle.putInt(KEY_INDEX, index);
        return this;
    }

    public ZYPictureViewer withIsShowIndex(boolean isShow) {
        mViewerOptionsBundle.putBoolean(KEY_IS_SHOW_INDEX, isShow);
        return this;
    }

    public ZYPictureViewer withOperation(ZYIOperation operation) {
        mViewerOptionsBundle.putSerializable(KEY_OPERATION, operation);
        return this;
    }

    public void start(@NonNull Activity activity) {
        activity.startActivity(getIntent(activity));
    }

    public Intent getIntent(@NonNull Context context) {
        mViewerIntent.setClass(context, ZYPictureViewActivity.class);
        mViewerIntent.putExtras(mViewerOptionsBundle);
        return mViewerIntent;
    }

}
