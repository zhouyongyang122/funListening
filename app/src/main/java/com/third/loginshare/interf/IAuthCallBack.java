package com.third.loginshare.interf;

/**
 * Created by Administrator on 2015/7/22.
 */
public interface IAuthCallBack<T> {
    public void onAuthFailed(int errorCode, String errorMsg);

    public void onAuthSuccess(String openId, T userInfo);
}
