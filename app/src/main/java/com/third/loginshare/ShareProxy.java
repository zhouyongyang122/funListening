package com.third.loginshare;

import android.app.Activity;

import com.third.loginshare.entity.ShareEntity;
import com.third.loginshare.interf.IShare;
import com.third.loginshare.interf.IShareCallBack;

/**
 * Created by weilong zhou on 2015/9/9.
 * Email:zhouwlong@gmail.com
 */
public class ShareProxy implements IShare {
    public static final int SHARE_TYPE_QQ = 0;
    public static final int SHARE_TYPE_QZONE = 1;
    public static final int SHARE_TYPE_WECHAT = 2;
    public static final int SHARE_TYPE_WECHAT_FRIEND = 3;
    public static final int SHARE_TYPE_WEIBO = 4;

    private static ShareProxy instance;

    private ShareProxy() {
    }

    public static ShareProxy getInstance() {
        if (instance == null) {
            synchronized (ShareProxy.class) {
                if (instance == null) {
                    instance = new ShareProxy();
                }
            }
        }
        return instance;
    }

    @Override
    public void share(int shareType, Activity activity, ShareEntity shareEntity, IShareCallBack shareCallBack) {
        ShareAndAuthorProvider provider = ThirdPartyBase.getInstance().getProviderByShareType(shareType);
        if (provider != null) {
            provider.share(shareType, activity, shareEntity, shareCallBack);
        }
    }
}
