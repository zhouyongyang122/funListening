package com.third.loginshare;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.third.loginshare.entity.ShareEntity;
import com.third.loginshare.entity.ThirdPartyUserInfo;
import com.third.loginshare.entity.WeiboUserInfo;
import com.third.loginshare.interf.IAuthCallBack;
import com.third.loginshare.interf.IShareCallBack;
import com.third.loginshare.net.HttpUtil;
import com.third.loginshare.net.JsonUtil;
import com.third.loginshare.net.WeiboShareUtils;

/**
 * Created by zhou weilong on 2015/7/22.
 */
public class WeiboHelper extends ShareAndAuthorProvider {
    private static WeiboHelper mWeiboHelper;

    private String mSinaRedirectUrl;
    private String mSinaScope;
    private String mSinaAppKey;
    private Application mApplication;

    private IWeiboShareAPI mWeiboShareAPI;
    private String OPENID_HEAD = "Weibo";

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;

    private WeiboAuthListener mWeiboAuthListener = new WeiboAuthListener() {

        @Override
        public void onWeiboException(
                WeiboException arg0) {
            if (mCallback != null) {
                mCallback.onAuthFailed(0,
                        arg0.getMessage());
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onComplete(Bundle arg0) {
            if (arg0 == null) {
                if (mCallback != null) {
                    mCallback.onAuthFailed(0, "验证失败，请检查网络后重试");
                }
                return;
            }

            final String openId =
                    arg0.getString("uid");
            final String authToken =
                    arg0.getString("access_token");
            new GetWeiboUserInfoTask(openId,
                    authToken).execute();
        }
    };

    private IAuthCallBack<ThirdPartyUserInfo> mCallback;
    private IShareCallBack mShareCallback;
    private IWeiboHandler.Response mResponse = new WeiboShareActivity() {
        @Override
        public void onResponse(
                BaseResponse baseResponse) {
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    if (mShareCallback != null) {
                        mShareCallback.onShareSuccess();
                        mShareCallback = null;
                    }
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    if (mShareCallback != null) {
                        mShareCallback.onShareCancel();
                        mShareCallback = null;
                    }
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    if (mShareCallback != null) {
                        mShareCallback.onShareFailed(0, baseResponse.errMsg);
                        mShareCallback = null;
                    }
                    break;
            }
        }
    };

    public static WeiboHelper getInstance() {
        if (mWeiboHelper == null) {
            synchronized (WechatHelper.class) {
                if (mWeiboHelper == null) {
                    mWeiboHelper = new WeiboHelper();
                }
            }
        }
        return mWeiboHelper;
    }

    private WeiboHelper() {
    }

    public void init(Application application, String sinaAppKey, String sinaScope, String sinaRedirectUrl) {
        mApplication = application;
        mSinaAppKey = sinaAppKey;
        mSinaScope = sinaScope;
        mSinaRedirectUrl = sinaRedirectUrl;
    }

    @Override
    public void share(int shareType, Activity activity, ShareEntity shareEntity, IShareCallBack shareCallBack) {
        share(activity, shareEntity, shareCallBack);
    }

    @Override
    public void getAuthorInfo(Activity activity, IAuthCallBack authCallBack) {
        sendAuthRequest(activity, authCallBack);
    }

    /**
     * 前提：
     * 1AndroidManifest
     * 2Application 中 调用 ThirdBase init（），并且初始化Weibo相关key
     * 3在Activity 的 onActivityResult 调用 WeiboHelper.authorizeCallBack;
     *
     * @param activity
     * @param iAuthCallBack
     */
    public void sendAuthRequest(Activity activity, final IAuthCallBack<ThirdPartyUserInfo> iAuthCallBack) {
        if (mAuthInfo == null) {
            initWeibo();
        }

        mCallback = iAuthCallBack;
        mSsoHandler = new SsoHandler(activity, mAuthInfo);
        mSsoHandler.authorize(mWeiboAuthListener);
    }

    private void initWeibo() {
        mAuthInfo = new AuthInfo(mApplication, mSinaAppKey, mSinaRedirectUrl, mSinaScope);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mApplication, mSinaAppKey);
        mWeiboShareAPI.registerApp();
    }

    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * @param activity
     * @param shareEntity
     * @param iShareCallBack
     * @return
     * @function share
     * @Description 1.AndroidManifest
     * 2.Application 中 调用 ThirdBase init()，并且初始化Weibo相关key
     * 3.在当前 Activity 的 onCreate 和 onNewIntent 中分别调用 WeiboHelper.handleWeiboResponseOnNewIntent, WeiboHelper.handleWeiboResponseOnCreate；
     * 4.注：微博只能avatar的byte[]或bitmap
     * @author xiazehong
     * @date 2015年8月6日下午12:00:40
     */
    public void share(final Activity activity, ShareEntity shareEntity, IShareCallBack iShareCallBack) {
        if (mAuthInfo == null) {
            initWeibo();
        }
        mShareCallback = iShareCallBack;
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj(shareEntity);
        weiboMessage.imageObject = getImageObj(shareEntity);
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
        //mWeiboShareAPI.sendRequest(activity, request, mAuthInfo, "", mWeiboShareAuthListener);
        Oauth2AccessToken accessToken = WeiboShareUtils.readAccessToken(activity);
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(activity, request, mAuthInfo,token, new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                WeiboShareUtils.writeAccessToken(activity, newToken);
                Log.e("mAuthInfo"," getRedirectUrl = " + mAuthInfo.getRedirectUrl());
                if (mShareCallback != null){
                    mShareCallback.onShareSuccess();
                    mShareCallback = null;
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                if (mShareCallback != null) {
                    mShareCallback.onShareFailed(0, e.getMessage());
                    mShareCallback = null;
                }
            }

            @Override
            public void onCancel() {
                if (mShareCallback != null){
                    mShareCallback.onShareCancel();
                    mShareCallback = null;
                }
            }
        });
        //        // 1. 初始化微博的分享消息
        //        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        //        weiboMessage.mediaObject = getWebpageObj(shareEntity);
        //        // 2. 初始化从第三方到微博的消息请求
        //        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        //        // 用transaction唯一标识一个请求
        //        request.transaction = String.valueOf(System.currentTimeMillis());
        //        request.multiMessage = weiboMessage;
        //        // 3. 发送请求消息到微博，唤起微博分享界面
        //        mWeiboShareAPI.sendRequest(activity, request, mAuthInfo, "", mWeiboShareAuthListener);
    }




    private WeiboAuthListener mWeiboShareAuthListener = new WeiboAuthListener() {
        @Override
        public void onWeiboException(
                WeiboException arg0) {
            if (mShareCallback != null) {
                mShareCallback.onShareFailed(0, arg0.getMessage());
                mShareCallback = null;
            }
        }

        @Override
        public void onComplete(Bundle bundle) {

            if (mShareCallback != null){
                mShareCallback.onShareSuccess();
                mShareCallback = null;
            }
        }

        @Override
        public void onCancel() {
            if (mShareCallback != null){
                mShareCallback.onShareCancel();
                mShareCallback = null;
            }
        }
    };
    private TextObject getTextObj(ShareEntity shareEntity) {
        TextObject textObject = new TextObject();
        textObject.text = "#" + shareEntity.title + "#\n" + shareEntity.text + "\n" + shareEntity.webUrl;
        return textObject;
    }

    private ImageObject getImageObj(ShareEntity shareEntity) {
        ImageObject imageObject = new ImageObject();
        if (null != shareEntity.avatarBitmap) {
            imageObject.setImageObject(shareEntity.avatarBitmap);
        }
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(ShareEntity shareEntity) {
        WebpageObject WebpageObject = new WebpageObject();
        WebpageObject.identify = Utility.generateGUID();
        WebpageObject.thumbData = ShareUtils.bmpToByteArray(shareEntity.avatarBitmap);
        WebpageObject.title = WebpageObject.title;
        WebpageObject.description = shareEntity.text;
        WebpageObject.actionUrl = shareEntity.webUrl;
        // 设置 Bitmap 类型的图片到视频对象里
        //        WebpageObject.setThumbImage(ShareUtils.createBtimap(shareEntity.avatarLocalPath));
        WebpageObject.defaultText = "Webpage 默认文案";
        return WebpageObject;
    }

    private class GetWeiboUserInfoTask extends AsyncTask<Void, Void, WeiboUserInfo> {
        private String mWeiboUid;
        private String mAuthToken;

        public GetWeiboUserInfoTask(String uid, String authToken) {
            mWeiboUid = uid;
            mAuthToken = authToken;
        }

        @Override
        protected WeiboUserInfo doInBackground(Void... voids) {
            try {

                String json =
                        HttpUtil.getInstace().httpGetRequestJson("https://api.weibo.com/2/users/show.json?uid=" + mWeiboUid
                                + "&access_token=" + mAuthToken);
                return JsonUtil.gson.fromJson(json, new TypeToken<WeiboUserInfo>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(WeiboUserInfo weiboUserInfo) {
            super.onPostExecute(weiboUserInfo);
            if (weiboUserInfo != null) {
                if (mCallback != null) {
                    String openId = Md5EncodeUtil.getMD5Str(OPENID_HEAD + mWeiboUid);
                    ThirdPartyUserInfo userInfo = new ThirdPartyUserInfo();
                    userInfo.nickName = weiboUserInfo.screen_name;
                    userInfo.avatar = weiboUserInfo.profile_image_url;
                    userInfo.gender =
                            weiboUserInfo.gender.equalsIgnoreCase("m") ? ThirdPartyUserInfo.MALE
                                    : ThirdPartyUserInfo.FEMALE;
                    mCallback.onAuthSuccess(openId, userInfo);
                }
            } else {
                if (mCallback != null) {
                    mCallback.onAuthFailed(0, "");
                }
            }
        }
    }

    /**
     * @param intent
     * @return void
     * @function handleWeiboResponseOnNewIntent
     * @Description 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在OnNewIntent处调用该函数
     * 来接收微博客户端返回的数据；执行成功，返回 true，并调用.失败返回 false，不调用上述回调
     * <action android:name="com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY"/>
     * <category android:name="android.intent.category.DEFAULT"
     * @author xiazehong
     * @date 2015年8月7日下午2:00:20
     */
    public void handleWeiboResponseOnNewIntent(Intent intent) {
        if (intent != null) {
            mWeiboShareAPI.handleWeiboResponse(intent, mResponse);
        }
    }

    //    // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
    //    // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
    //    // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
    //    // 失败返回 false，不调用上述回调
    public void handleWeiboResponse(Intent intent) {
        if (intent != null) {
            mWeiboShareAPI.handleWeiboResponse(intent, mResponse);
        }
    }

    public void clearCallback() {
        mShareCallback = null;
        mCallback = null;
    }
}
