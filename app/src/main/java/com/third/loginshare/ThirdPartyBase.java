package com.third.loginshare;

import android.app.Application;
import android.text.TextUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xiazehong
 * @Class ThirdPartyBase
 * @Description 无安装                                            已安装
 * QQ:   登录h5，分享                                   登录分享 QQ客户端
 * 微信：    登录h5，分享提示未安装微信         登录分享 微信客户端
 * 微博：    登录h5，分享h5            登录分享 微博app
 * <p>
 * 第三方分享avatar来源：
 * 1、QQ分享：优先级avatarUrl，avatarLocalPath
 * 2、微信分享：只能用avatarLocalPath，解析成bitmap，转成byte[]
 * 3、微博分享：只能用avatarLocalPath，解析成bitmap，转成byte[]
 * @date 2015年8月6日 下午1:19:23
 */
public class ThirdPartyBase {
    private static ThirdPartyBase instance;

    public List<ShareAndAuthorProvider> mShareAndAuthorBases = new LinkedList<ShareAndAuthorProvider>();

    public static ThirdPartyBase getInstance() {
        if (instance == null) {
            synchronized (ThirdPartyBase.class) {
                if (instance == null) {
                    instance = new ThirdPartyBase();
                }
            }
        }
        return instance;
    }

    private ThirdPartyBase() {

    }

    public void initQQ(Application application, String tecentAppId) {
        if (application == null || TextUtils.isEmpty(tecentAppId)) {
            return;
        }
        TencentHelper tencentHelper = TencentHelper.getInstance();
        tencentHelper.init(application, tecentAppId);
        tencentHelper.addShareType(ShareProxy.SHARE_TYPE_QQ);
        tencentHelper.addShareType(ShareProxy.SHARE_TYPE_QZONE);
        tencentHelper.setAuthorType(AuthorProxy.AUTHOR_TYPE_QQ);

        mShareAndAuthorBases.add(tencentHelper);
    }

    public void initWeibo(Application application, String sinaAppKey, String sinaScope, String sinaRedirectUrl) {
        if (application == null || TextUtils.isEmpty(sinaAppKey) || TextUtils.isEmpty(sinaScope) || TextUtils.isEmpty(sinaRedirectUrl)) {
            return;
        }
        WeiboHelper weiboHelper = WeiboHelper.getInstance();
        weiboHelper.init(application, sinaAppKey, sinaScope, sinaRedirectUrl);
        weiboHelper.addShareType(ShareProxy.SHARE_TYPE_WEIBO);
        weiboHelper.setAuthorType(AuthorProxy.AUTHOR_TYPE_WEIBO);
        mShareAndAuthorBases.add(weiboHelper);
    }

    public void initWechat(Application application, String wechatAppId, String wechatSecret) {
        if (application == null || TextUtils.isEmpty(wechatAppId)) {
            return;
        }
        WechatHelper wechatHelper = WechatHelper.getInstance();
        wechatHelper.init(application, wechatAppId, wechatSecret);
        wechatHelper.addShareType(ShareProxy.SHARE_TYPE_WECHAT);
        wechatHelper.addShareType(ShareProxy.SHARE_TYPE_WECHAT_FRIEND);
        wechatHelper.setAuthorType(AuthorProxy.AUTHOR_TYPE_WECHAT);

        mShareAndAuthorBases.add(wechatHelper);
    }

    ShareAndAuthorProvider getProviderByShareType(int shareType) {
        for (ShareAndAuthorProvider provider : mShareAndAuthorBases) {
            if (provider.checkIfContainShareType(shareType)) {
                return provider;
            }
        }

        return null;
    }

    ShareAndAuthorProvider getProviderByAuthorType(int authType) {
        for (ShareAndAuthorProvider provider : mShareAndAuthorBases) {
            if (provider.checkAuthorTypeEqual(authType)) {
                return provider;
            }
        }

        return null;
    }
}
