package com.funlisten;

/**
 * Created by ZY on 17/4/13.
 */

public class ZYAppConstants {

    //微信
    public static final String WECHAT_APP_KEY = "wx2ce9b87064d829d5";
    public static final String WECHAT_APP_SECRET = "3661eb56e04171cbfb74b698be6cc295";
    public static final String WECHAT_GET_AUTH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String WECHAT_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

    //新浪
    public static final String SINA_APP_KEY = "2955025736";

    public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    public static final String SINA_SCOPE = "all";


    //腾讯
    public static final String TENCENT_APP_ID = "1106127950";

    //bugtags
    public static final String BUGTAGS_KEY = "2822fed353401211e4b1925337032876";

    public static String getShareUrl(int albumId) {
        return "http://tingshuishuo.cn/album/album.html?id=" + albumId;
    }
}

