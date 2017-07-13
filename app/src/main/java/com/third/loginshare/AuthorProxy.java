package com.third.loginshare;

import android.app.Activity;

import com.third.loginshare.interf.IAuthCallBack;

/**
 * Created by weilong zhou on 2015/9/9.
 * Email:zhouwlong@gmail.com
 */
public class AuthorProxy {
    public static final int AUTHOR_TYPE_QQ = 0;
    public static final int AUTHOR_TYPE_WECHAT = 1;
    public static final int AUTHOR_TYPE_WEIBO = 2;

    private static AuthorProxy instance;

    private AuthorProxy() {
    }

    public static AuthorProxy getInstance() {
        if (instance == null) {
            synchronized (AuthorProxy.class) {
                if (instance == null) {
                    instance = new AuthorProxy();
                }
            }
        }
        return instance;
    }

    public void getAuthorInfo(int authType, Activity activity, IAuthCallBack authCallBack) {
        ShareAndAuthorProvider provider = ThirdPartyBase.getInstance().getProviderByAuthorType(authType);
        if (provider != null) {
            provider.getAuthorInfo(activity, authCallBack);
        }
    }
}
