package com.funlisten.business.login.model.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZY on 17/4/14.
 */

public class ZYThridLoginParamas {

    //1 qq登录 2 微博登录 3 微信登陆

    public static final String TYPE_TENCENT = "qq";
    public static final String TYPE_SINA = "weiBo";
    public static final String TYPE_WECHAT = "weChat";

    String token;

    String auth_url;

    String type;

    String nickname;

    String avatar;

    int sex;

    String signature;

    public Map<String, String> getParamas() {
        Map<String, String> paramas = new HashMap<String, String>();
        paramas.put("openId", token + "");
        paramas.put("loginChannel", type + "");
        paramas.put("openLoginChannel",type + "");
        paramas.put("nickName", nickname + "");
//        paramas.put("auth_url", auth_url);
//        paramas.put("avatar", avatar);
//        paramas.put("sex", sex + "");
//        paramas.put("signature", signature);
        return paramas;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAuth_url(String auth_url) {
        this.auth_url = auth_url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
