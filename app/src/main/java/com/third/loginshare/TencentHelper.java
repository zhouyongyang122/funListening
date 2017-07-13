package com.third.loginshare;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.third.loginshare.entity.ShareEntity;
import com.third.loginshare.entity.ThirdPartyUserInfo;
import com.third.loginshare.interf.IAuthCallBack;
import com.third.loginshare.interf.IShareCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhou weilong on 2015/7/22.
 */

public class TencentHelper extends ShareAndAuthorProvider {
    private static TencentHelper mTencentHelper;

    private String mTencentAppId;
    private Application mApplication;

    private Tencent mTencent;
    private QQShare mQQShare;
    private QQAuth mQQAuth;

    private IAuthCallBack<ThirdPartyUserInfo> mCallback;
    private IShareCallBack mShareCallback;

    private IUiListener mUiListener = new IUiListener() {
        public void onComplete(Object response) {
            try {
                final String openId =
                        ((JSONObject) response).getString("openid");
                QQToken qqToken = mTencent.getQQToken();
                UserInfo info =
                        new UserInfo(mApplication, qqToken);
                info.getUserInfo(new IUiListener() {
                    public void onComplete(
                            final Object response) {
                        try {
                            JSONObject json =
                                    (JSONObject) response;
                            ThirdPartyUserInfo userInfo =
                                    new ThirdPartyUserInfo();
                            userInfo.avatar =
                                    json.getString("figureurl_qq_2");
                            userInfo.nickName =
                                    json.getString("nickname");
                            userInfo.gender =
                                    json.getString("gender")
                                            .equals("男") ? ThirdPartyUserInfo.MALE
                                            : ThirdPartyUserInfo.FEMALE;
                            if (mCallback != null) {
                                mCallback.onAuthSuccess(openId,
                                        userInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (mCallback != null) {
                                mCallback.onAuthFailed(0,
                                        e.getMessage());
                            }
                        }
                    }

                    public void onCancel() {

                    }

                    public void onError(UiError arg0) {
                        if (mCallback != null) {
                            mCallback.onAuthFailed(0,
                                    arg0.errorMessage);
                        }
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                if (mCallback != null) {
                    mCallback.onAuthFailed(0, e.getMessage());
                }
            }
        }

        public void onError(UiError arg0) {
            if (mCallback != null) {
                mCallback.onAuthFailed(0, arg0.errorMessage);
            }
        }

        @Override
        public void onCancel() {
        }

    };

    private IUiListener mShareUiListener = new IUiListener() {

        @Override
        public void onCancel() {
            if (mShareCallback != null) {
                mShareCallback.onShareCancel();
                mShareCallback = null;
            }
        }

        @Override
        public void onComplete(Object response) {
            if (mShareCallback != null) {
                mShareCallback.onShareSuccess();
                mShareCallback = null;
            }
        }

        @Override
        public void onError(final UiError e) {
            if (mShareCallback != null) {
                mShareCallback.onShareFailed(0, e.errorMessage);
                mShareCallback = null;
            }
        }

    };

    public static TencentHelper getInstance() {
        if (mTencentHelper == null) {
            synchronized (TencentHelper.class) {
                if (mTencentHelper == null) {
                    mTencentHelper = new TencentHelper();
                }
            }
        }
        return mTencentHelper;
//        return new TencentHelper();
    }

    private TencentHelper() {
    }

    public void init(Application application, String tecentAppId) {
        mApplication = application;
        mTencentAppId = tecentAppId;
    }

    @Override
    public void share(int shareType, Activity activity, ShareEntity shareEntity, IShareCallBack shareCallBack) {
        switch (shareType) {
            case ShareProxy.SHARE_TYPE_QQ:
                share(activity, shareEntity, shareCallBack);
                break;
            case ShareProxy.SHARE_TYPE_QZONE:
                shareQZone(activity, shareEntity, shareCallBack);
                break;
        }
    }

    @Override
    public void getAuthorInfo(Activity activity, IAuthCallBack authCallBack) {
        sendAuthRequest(activity, authCallBack);
    }

    /**
     * 分享前提：
     * 1AndroidManifest
     * 2Application 中 调用 ThirdBase init（），并且初始化Tecent相关key
     *
     * @param activity
     * @param iAuthCallBack
     */
    public void sendAuthRequest(Activity activity, final IAuthCallBack<ThirdPartyUserInfo> iAuthCallBack) {
        if (mTencent == null) {
            initTencent();
        }

        mCallback = iAuthCallBack;
        mTencent.login(activity, "all", mUiListener);
    }

    private void initTencent() {
        mTencent = Tencent.createInstance(mTencentAppId, mApplication);
        mQQAuth = QQAuth.createInstance(mTencentAppId, mApplication);
        mQQShare = new QQShare(mApplication, this.mQQAuth.getQQToken());
    }

    /**
     * @param activity
     * @param shareEntity
     * @param iShareCallBack
     * @return
     * @function share
     * @Description 分享配置：
     * 1.AndroidManifest
     * 2.Application 中 调用 ThirdBase init()，并且初始化Tecent相关appId
     * 3.分享icon来源优先级别：imageUrl，imageLocalPath
     * @author xiazehong
     * @date 2015年8月6日上午10:53:33
     */
    public void share(final Activity activity, ShareEntity shareEntity, IShareCallBack iShareCallBack) {
        if (mTencent == null) {
            initTencent();
        }

        mShareCallback = iShareCallBack;
        final Bundle params = new Bundle();
        if (!TextUtils.isEmpty(shareEntity.avatarUrl)) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareEntity.avatarUrl);
        }
        if (!TextUtils.isEmpty(shareEntity.avatarLocalPath)) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, shareEntity.avatarLocalPath);
        }
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareEntity.title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareEntity.text);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareEntity.webUrl);

        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, mApplication.getApplicationInfo().name);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mQQShare.shareToQQ(activity, params, mShareUiListener);
    }

    /**
     * @param activity
     * @param shareEntity
     * @param iShareCallBack
     * @return void
     * @function shareQZone
     * @Description 分享配置：
     * 1.AndroidManifest
     * 2.Application 中 调用 ThirdBase init()，并且初始化Tecent相关appId
     * 3.分享icon来源优先级别：imageUrl，imageLocalPath
     * @author xiazehong
     * @date 2015年8月6日上午10:50:50
     */
    public void shareQZone(Activity activity, ShareEntity shareEntity, IShareCallBack iShareCallBack) {
        if (mTencent == null) {
            initTencent();
        }

        mShareCallback = iShareCallBack;
        Bundle params = new Bundle();
        ArrayList<String> imageUrls = new ArrayList<String>();
        if (!TextUtils.isEmpty(shareEntity.avatarUrl)) {
            imageUrls.add(shareEntity.avatarUrl);
        } else if (!TextUtils.isEmpty(shareEntity.avatarLocalPath)) {
            imageUrls.add(shareEntity.avatarLocalPath);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);

        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareEntity.title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareEntity.text);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareEntity.webUrl);
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        mTencent.shareToQzone(activity, params, mShareUiListener);
    }

    public void clearCallback() {
        mShareCallback = null;
        mCallback = null;
    }
}
