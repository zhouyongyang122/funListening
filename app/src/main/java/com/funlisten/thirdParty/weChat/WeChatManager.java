package com.funlisten.thirdParty.weChat;

import android.app.Activity;

import com.funlisten.ZYAppConstants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by ZY on 17/4/13.
 */

public class WeChatManager {

    private String auth_url;

    private IWXAPI mloginApi;

    private static WeChatManager instance;

    private WeChatManager() {

    }

    public static WeChatManager getInstance() {
        if (instance == null) {
            instance = new WeChatManager();
        }
        return instance;
    }

    public void initLogin(Activity activity) {
        try {
            mloginApi = WXAPIFactory.createWXAPI(activity, ZYAppConstants.WECHAT_APP_KEY, true);
            mloginApi.registerApp(ZYAppConstants.WECHAT_APP_KEY);
        } catch (Exception e) {
            mloginApi = null;
        }
    }

    public boolean sendWeChatAuthRequest() {
        if (mloginApi != null) {
            if (mloginApi.isWXAppInstalled()) {
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "third_login";
                mloginApi.sendReq(req);
                return true;
            }
        }
        return false;
    }

    public String getAuth_url() {
        return auth_url;
    }

    public void setAuth_url(String auth_url) {
        this.auth_url = auth_url;
    }

    public IWXAPI getLoginApi() {
        return mloginApi;
    }
}
