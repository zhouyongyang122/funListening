package com.third.loginshare.entity;

import java.io.Serializable;
import java.util.List;

public class WeChatUserInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String openid;
    public String nickname;
    public int sex;
    public String province;
    public String city;
    public String country;
    public String headimgurl;
    public List<String> privilege;
    public List<String> PRIVILEGE1;
    public String unionid;
}
