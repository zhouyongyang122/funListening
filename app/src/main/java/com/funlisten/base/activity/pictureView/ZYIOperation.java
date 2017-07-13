package com.funlisten.base.activity.pictureView;

import android.app.Activity;

import java.io.Serializable;

public interface ZYIOperation extends Serializable {

    void showOperation(Activity activity, String picturePath);
}
