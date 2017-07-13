package com.third.loginshare.entity;

import java.io.Serializable;

public class WechatAuthInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String access_token;
	public int expires_in;
	public String refresh_token;
	public String openid;
	public String scope;
	public String unionid;
}
