package com.third.loginshare;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.funlisten.R;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.third.loginshare.entity.ShareEntity;
import com.third.loginshare.entity.ThirdPartyUserInfo;
import com.third.loginshare.entity.WeChatUserInfo;
import com.third.loginshare.entity.WechatAuthInfo;
import com.third.loginshare.interf.IAuthCallBack;
import com.third.loginshare.interf.IShareCallBack;

/**
 * Created by zhou weilong on 2015/7/22.
 */
public class WechatHelper extends ShareAndAuthorProvider {
    private static WechatHelper mWechatHelper;
    private Application mApplication;
    private String mWechatAppId;
    private String mWechatSecret;

    private IWXAPI mApi;
    private IAuthCallBack<ThirdPartyUserInfo> mCallback;
    private IShareCallBack mShareCallBack;

    public static WechatHelper getInstance() {
        if (mWechatHelper == null) {
            synchronized (WechatHelper.class) {
                if (mWechatHelper == null) {
                    mWechatHelper = new WechatHelper();
                }
            }
        }
        return mWechatHelper;
    }

    private WechatHelper() {
    }

    public void init(Application application, String wechatAppId,String wechatSecret) {
        mApplication = application;
        mWechatAppId = wechatAppId;
        mWechatSecret = wechatSecret;
    }

    public boolean isWXAppInstalled() {
        return mApi.isWXAppInstalled();
    }

    @Override
    public void share(int shareType, Activity activity, ShareEntity shareEntity, IShareCallBack shareCallBack) {
        if (mApi == null) {
            initWechat();
        }
        if (!isWXAppInstalled()){
            Toast.makeText(activity, R.string.toast_no_wechat,Toast.LENGTH_SHORT).show();
            return;
        }
        switch (shareType) {
            case ShareProxy.SHARE_TYPE_WECHAT:
                share(activity, shareEntity, shareCallBack);
                break;
            case ShareProxy.SHARE_TYPE_WECHAT_FRIEND:
                shareFriend(activity, shareEntity, shareCallBack);
                break;
        }
    }

    @Override
    public void getAuthorInfo(Activity activity, IAuthCallBack authCallBack) {
        sendAuthRequest(activity, authCallBack);
    }

    /**
     * 前提：
     * 1AndroidManifest
     * 2Application 中 调用 ThirdBase init（），并且初始化Wechat相关key
     * 3添加package_name.wxapi.WXEntryActivity extend WxEntryBaseActivity
     *
     * @param activity
     * @param iAuthCallBack
     */
    public void sendAuthRequest(Activity activity, final IAuthCallBack<ThirdPartyUserInfo> iAuthCallBack) {
        mCallback = iAuthCallBack;
        sendWeChatAuthRequest();
    }

    public void sendWeChatAuthRequest() {
        if (mApi == null) {
            initWechat();
        }

        if (mApi != null) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "third_login";
            mApi.sendReq(req);
        }
    }

    private void initWechat() {
        mApi = WXAPIFactory.createWXAPI(mApplication, mWechatAppId, true);
        mApi.registerApp(mWechatAppId);
    }

    /**
     * 前提：
     * 1AndroidManifest
     * 2Application 中 调用 ThirdBase init（），并且初始化Wechat相关key
     * 3添加package_name.wxapi.WXEntryActivity extend WxEntryBaseActivity
     *
     * @param activity
     * @param shareEntity
     * @param iShareCallBack
     */
    public void share(Activity activity, ShareEntity shareEntity, IShareCallBack iShareCallBack) {
        mShareCallBack = iShareCallBack;
        weChatShare(activity, false, shareEntity);
    }

    /**
     * @param activity
     * @param shareEntity
     * @param iShareCallBack
     * @return void
     * @function shareFriend
     * @Description 前提：
     * 1.AndroidManifest
     * 2.Application 中 调用 ThirdBase init（），并且初始化Wechat相关key
     * 3.添加package_name.WXEntryActivity extend WxEntryBaseActivity
     * 4.注：只能用本地图片分享imageLocalPath
     * @author xiazehong
     * @date 2015年8月6日上午11:43:36
     */
    public void shareFriend(Activity activity, ShareEntity shareEntity, IShareCallBack iShareCallBack) {
        mShareCallBack = iShareCallBack;
        weChatShare(activity, true, shareEntity);
    }

    /**
     * @param activity
     * @param isFrient
     * @param shareEntity
     * @return void
     * @function weChatShare
     * @Description 只能用本地图片分享
     * @author xiazehong
     * @date 2015年8月6日上午11:41:07
     */
    private void weChatShare(Activity activity, final boolean isFrient, ShareEntity shareEntity) {
        if (mApi == null) {
            initWechat();
        }

        WXMediaMessage msg = new WXMediaMessage();
        // 设置缩略图
        msg.thumbData = ShareUtils.bmpToByteArray(shareEntity.avatarBitmap);
        msg.title = isFrient ? shareEntity.text : shareEntity.title;
        msg.description = shareEntity.text;

        WXWebpageObject webpageObj = new WXWebpageObject();
        webpageObj.webpageUrl = shareEntity.webUrl;
        msg.mediaObject = webpageObj;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isFrient ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        mApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void onResp(final Activity activity, BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp instanceof SendAuth.Resp) {
                    final SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                    if ("third_login".equals(sendResp.state)) {
                        new GetWeChatAuthInfoTask(sendResp.code) {
                            @Override
                            protected void onPostExecute(final WechatAuthInfo wechatAuthInfo) {
                                if (activity.isFinishing()) {
                                    return;
                                }

                                if (wechatAuthInfo != null) {
                                    new GetWeChatUserInfoTask(wechatAuthInfo.access_token, wechatAuthInfo.openid) {
                                        @Override
                                        protected void onPostExecute(WeChatUserInfo weChatUserInfo) {
                                            activity.finish();

                                            ThirdPartyUserInfo userInfo = new ThirdPartyUserInfo();
                                            userInfo.nickName = weChatUserInfo.nickname;
                                            userInfo.avatar = weChatUserInfo.headimgurl;
                                            userInfo.gender = weChatUserInfo.sex;
                                            if (mCallback != null) {
                                                mCallback.onAuthSuccess(weChatUserInfo.openid, userInfo);
                                            }
                                        }
                                    }.execute();
                                }
                            }
                        }.execute();
                        return;
                    }
                } else {
                    if (mShareCallBack != null) {
                        mShareCallBack.onShareSuccess();
                        mShareCallBack = null;
                    }
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (mShareCallBack != null) {
                    mShareCallBack.onShareCancel();
                    mShareCallBack = null;
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            default:
                onError(resp);
                break;
        }

        activity.finish();
    }

    private void onError(BaseResp resp) {
        if (resp instanceof SendAuth.Resp) {
            if (mCallback != null) {
                mCallback.onAuthFailed(0, resp.errStr);
            }
        } else {
            if (mShareCallBack != null) {
                mShareCallBack.onShareFailed(0, resp.errStr);
                mShareCallBack = null;
            }
        }
    }

    public void handleIntent(Intent intent, WXEntryBaseActivity wxEntryBaseActivity) {
        mApi.handleIntent(intent, wxEntryBaseActivity);
    }

    public String getWechatAppId() {
        return mWechatAppId;
    }

    public String getWechatSecret() {
        return mWechatSecret;
    }


    public void clearCallback() {
        mShareCallBack = null;
        mCallback = null;
    }
}
