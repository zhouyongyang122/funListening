package com.third.loginshare.interf;

import android.app.Activity;

import com.third.loginshare.entity.ShareEntity;

/**
 * Created by weilong zhou on 2015/9/10.
 * Email:zhouwlong@gmail.com
 */
public interface IShare {
    public void share(int shareType, Activity activity, ShareEntity shareEntity, IShareCallBack shareCallBack);
}
