package com.third.loginshare.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/22.
 */
public class ThirdPartyUserInfo implements Serializable
{
    public static final int MALE   = 1;
    public static final int FEMALE = 2;
    public String           nickName;
    public String           avatar;
    public int              gender;
    
    @Override
    public String toString()
    {
        return "ThirdPartyUserInfo{" + "nickName='" + nickName + '\'' + ", avatar='" + avatar + '\'' + ", gender="
            + gender + '}';
    }
}
