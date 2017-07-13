package com.third.loginshare;

import android.os.AsyncTask;

import com.google.gson.reflect.TypeToken;
import com.third.loginshare.entity.WechatAuthInfo;
import com.third.loginshare.net.HttpUtil;
import com.third.loginshare.net.JsonUtil;
import com.third.loginshare.net.ThirdPartyServer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class GetWeChatAuthInfoTask extends AsyncTask<Void, Void, WechatAuthInfo>
{
    public String code;
    
    public GetWeChatAuthInfoTask(String code)
    {
        this.code = code;
    }
    
    @Override
    protected WechatAuthInfo doInBackground(Void... voids)
    {
        try
        {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("appid", WechatHelper.getInstance().getWechatAppId()));
            params.add(new BasicNameValuePair("secret", WechatHelper.getInstance().getWechatSecret()));
            params.add(new BasicNameValuePair("code", code));
            params.add(new BasicNameValuePair("grant_type", "authorization_code"));
            
            String json =
                HttpUtil.getInstace()
                    .httpGetRequestJson(ThirdPartyServer.getUrl(ThirdPartyServer.URL_WECHAT_GET_AUTH_TOKEN, params));
            
            return JsonUtil.gson.fromJson(json, new TypeToken<WechatAuthInfo>()
            {
            }.getType());
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
