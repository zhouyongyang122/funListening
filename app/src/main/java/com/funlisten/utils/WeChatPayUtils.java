package com.funlisten.utils;

import com.funlisten.ZYAppConstants;
import com.funlisten.ZYApplication;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by gd on 2017/7/29.
 */

public class WeChatPayUtils {

    private static WeChatPayUtils instance;

    String appId = ZYAppConstants.WECHAT_APP_KEY;

    private IWXAPI api = null;

    private WeChatPayUtils() {
        api = WXAPIFactory.createWXAPI(ZYApplication.getInstance().getApplicationContext(),appId , true);
        api.registerApp(appId);
    }

    public static WeChatPayUtils getInstance(){
        if(instance == null)instance = new WeChatPayUtils();
        return instance;
    }

    public void pay(PayReq payReq){
        payReq.appId = appId;
        api.sendReq(payReq);
    }
}
