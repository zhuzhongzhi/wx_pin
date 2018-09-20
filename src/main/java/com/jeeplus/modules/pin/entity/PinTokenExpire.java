/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;

import java.util.Date;

/**
 * 拼团规则信息Entity
 * @author zzz
 * @version 2018-05-18
 */
public class PinTokenExpire {
	private String appid;		        // appid
	private String accessToken;		    // access token
	private Date expireDate;		        // 过期时间
	
	public PinTokenExpire() {
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
	
	public Date getExpiredDate() {
		return expireDate;
	}

	public void setExpiredDate(Date tokenExpiredDate) {
		this.expireDate = tokenExpiredDate;
	}
	
}