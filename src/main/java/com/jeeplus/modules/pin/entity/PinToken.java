/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;


/**
 * 拼团规则信息Entity
 * @author zzz
 * @version 2018-05-18
 */
public class PinToken {
	private String appid;		        // appid
	private String accessToken;		    // access token
	private String tokenExpiredDate;		// 过期时间
	
	public PinToken() {
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getTokenExpiredDate() {
		return tokenExpiredDate;
	}

	public void setTokenExpiredDate(String tokenExpiredDate) {
		this.tokenExpiredDate = tokenExpiredDate;
	}
	
}