package com.third.loginshare.net;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/7/22.
 */
public class ThirdPartyServer {
    public static String getUrl(String url, List<NameValuePair> params) {
        return url + "&" + URLEncodedUtils.format(params, "utf-8");
    }

    public static final String URL_WECHAT_GET_AUTH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?qupeiyin=1";
    public static final String URL_WECHAT_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?qupeiyin=1";
}
