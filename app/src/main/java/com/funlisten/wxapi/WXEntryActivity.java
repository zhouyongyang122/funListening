package com.funlisten.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.funlisten.R;
import com.funlisten.ZYAppConstants;
import com.funlisten.service.net.ZYOkHttpNetManager;
import com.funlisten.thirdParty.weChat.EventWeChatAuthor;
import com.funlisten.thirdParty.weChat.WeChatManager;
import com.funlisten.utils.ZYStatusBarUtils;
import com.funlisten.utils.ZYToast;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.third.loginshare.entity.WeChatUserInfo;
import com.third.loginshare.entity.WechatAuthInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Created by ZY on 17/4/13.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZYStatusBarUtils.tintStatusBar(this, Color.TRANSPARENT, 0);
        init();
    }

    private void init() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        mIWXAPI = WXAPIFactory.createWXAPI(this, ZYAppConstants.WECHAT_APP_KEY, true);
        mIWXAPI.registerApp(ZYAppConstants.WECHAT_APP_KEY);
        mIWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        mIWXAPI.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(final BaseResp resp) {
        int result = 0;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.text_errcode_success;
                if (resp instanceof SendAuth.Resp) {
                    SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                    if ("third_login".equals(sendResp.state)) {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("appid", ZYAppConstants.WECHAT_APP_KEY);
                        params.put("secret", ZYAppConstants.WECHAT_APP_SECRET);
                        params.put("code", sendResp.code);
                        params.put("grant_type", "authorization_code");
                        ZYOkHttpNetManager.getInstance().requestPost(WechatAuthInfo.class, ZYAppConstants.WECHAT_GET_AUTH_TOKEN
                                , params, new ZYOkHttpNetManager.OkHttpNetListener<WechatAuthInfo>() {
                                    @Override
                                    public void onFailure(String message) {
                                        EventBus.getDefault().post(new EventWeChatAuthor(null));
                                        finish();
                                    }

                                    @Override
                                    public void onSuccess(WechatAuthInfo response) {
                                        if (response != null) {
                                            HashMap<String, String> params = new HashMap<String, String>();
                                            params.put("access_token", response.access_token);
                                            params.put("openid", response.openid);
                                            WeChatManager.getInstance().setAuth_url(ZYAppConstants.WECHAT_GET_USER_INFO + "?access_token=" + response.access_token + "&openid=" + response.openid);
                                            ZYOkHttpNetManager.getInstance().requestPost(WeChatUserInfo.class, ZYAppConstants.WECHAT_GET_USER_INFO, params,
                                                    new ZYOkHttpNetManager.OkHttpNetListener<WeChatUserInfo>() {
                                                        @Override
                                                        public void onFailure(String message) {
                                                            EventBus.getDefault().post(new EventWeChatAuthor(null));
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onSuccess(WeChatUserInfo response) {
                                                            EventBus.getDefault().post(new EventWeChatAuthor(response));
                                                            finish();
                                                        }
                                                    });
                                        }
                                    }
                                });
                        return;
                    }
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.text_errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.text_errcode_deny;
                break;
            default:
                result = R.string.text_errcode_unknown;
                break;
        }

        ZYToast.show(this, result);
        EventBus.getDefault().post(new EventWeChatAuthor(null));
        finish();
    }
}
