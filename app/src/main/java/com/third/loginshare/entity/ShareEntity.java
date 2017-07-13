package com.third.loginshare.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * @Class ShareEntity 
 * @Description
 * @author xiazehong 
 * @date 2015年8月6日 上午11:59:27
 */
public class ShareEntity implements Serializable
{
    /**
     * QQ头像数据：avatarUrl,avatarLocalPath
     */
    public String avatarUrl;
    public String avatarLocalPath;
    /**
     * 微信、微博头像数据
     */
    public Bitmap avatarBitmap;
    public String title;
    public String text;
    public String webUrl;
}
