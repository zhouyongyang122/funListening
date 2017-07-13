package com.third.loginshare.interf;

/**
 * Created by Administrator on 2015/7/22.
 */
public interface IShareCallBack {
    public void onShareFailed(int errorCode, String errorMsg);

    public void onShareSuccess();

    public void onShareCancel();
}
