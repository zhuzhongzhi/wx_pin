package com.jeeplus.modules.api.util;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.time.DateFormatUtil;
import com.jeeplus.modules.sys.utils.HttpClientUtil;

import freemarker.template.utility.StringUtil;

/**
 * 第三方授权管理
 * 
 * @author va
 * @version 2017-11-29
 */
@Service
public class AuthTokenUtil {

	Logger logger = LoggerFactory.getLogger(getClass());
	final public static String AUTH_ACCESS_TOKEN_CACHE_ID = "AUTH_ACCESS_TOKEN_CACHE_ID";
	final public static String AUTH_REFRESH_ACCESS_TOKEN_CACHE_ID = "AUTH_REFRESH_ACCESS_TOKEN_CACHE_ID";
	final public static String AUTH_ACCESS_TOKEN_TIMEOUT_CACHE_ID = "AUTH_ACCESS_TOKEN_TIMEOUT_CACHE_ID";
	private Boolean isFirst = false;
	
	@Autowired
	private WeixinServiceUtil weixinServiceUtil;
	
	/**
	 * @param appSecret
	 * @param appId
	 * @param ticket
	 * @return
	 */
	public String getCompTokenByTicket(String appId, String appSecret, String ticket) throws Exception {

		String url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("component_appid", appId);
		jsonParam.put("component_appsecret", appSecret);
		jsonParam.put("component_verify_ticket", ticket);
		String token = "";
		String resp = HttpClientUtil.okPost(url, jsonParam.toString());
		JSONObject obj = JSONObject.parseObject(resp);
		token = obj.getString("component_access_token");

		return token;
	}

	/**
	 * 获取预授权码
	 * 
	 */
	public String getPreAuthCode(String token, String appId) throws IOException {

		String obtainURL = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode";
		String url = obtainURL + "?component_access_token=" + token;
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("component_appid", appId);
		String code = "";
		String resp = HttpClientUtil.okPost(url, jsonParam.toString());
		JSONObject obj = JSONObject.parseObject(resp);
		code = obj.getString("pre_auth_code");
		return code;
	}

	public String getAuthURL(String redirectUrl) throws Exception {
		String appId = Global.getConfig("weixin.component_appid");
		String appsecret = Global.getConfig("weixin.component_appsecret");
		String verify_ticket = Global.getConfig("weixin.component_verify_ticket");
		String token = getCompTokenByTicket(appId, appsecret, verify_ticket);
		String preAuthCode = getPreAuthCode(token, appId);
		String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=" + appId + "&pre_auth_code="
				+ preAuthCode + "&redirect_uri=" + redirectUrl + "&auth_type=1";
		return url;
	}

	/**
	 * 获取第三方授权authorizer_access_token、授权刷新Token
	 * 
	 */
	public AjaxJson getAccessTokenByCode(String authCode) throws Exception {
		AjaxJson result = new AjaxJson();
		String appId = Global.getConfig("weixin.component_appid");
		String appsecret = Global.getConfig("weixin.component_appsecret");
		String verify_ticket = Global.getConfig("weixin.component_verify_ticket");
		String token = getCompTokenByTicket(appId, appsecret, verify_ticket);
		if (token != null) {
			String obtainURL = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth";
			String url = obtainURL + "?component_access_token=" + token;
			logger.info("getAccessTokenByCode url:", url);
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("component_appid", appId); //光明
			jsonParam.put("authorization_code", authCode);
			String resp = HttpClientUtil.okPost(url, jsonParam.toString());
			logger.info("getAccessTokenByCode resp:", resp);
			JSONObject obj = JSONObject.parseObject(resp);
			JSONObject authorization_info = obj.getJSONObject("authorization_info");
			String authAppid = authorization_info.getString("authorizer_appid");
			logger.info("getAccessTokenByCode authorize-->authAppid：", authAppid);
			String accessToken = authorization_info.getString("authorizer_access_token");
			logger.info("getAccessTokenByCode authorize-->accessToken：", accessToken);
			String refreshToken = authorization_info.getString("authorizer_refresh_token");
			logger.info("getAccessTokenByCode authorize-->refreshToken：", refreshToken);
			Global.modifyConfig("weixin.refreshToken", refreshToken);
			result.setSuccess(true);
			result.setMsg("已获取您的授权，感谢支持！");
		}
		return result;

	}

	/**
	 * 授权刷新Token
	 * 
	 */
	public String getAccessTokenByRefreshToken() throws Exception {
		
		String authorizer_refresh_token = Global.getConfig("weixin.authorizer_refresh_token");
		if (StringUtil.emptyToNull(authorizer_refresh_token) == null) {
			logger.info("getAccessTokenByRefreshToken authorizer_refresh_token is empty.");
			return null;
		}
		String authorizer_appid = Global.getConfig("weixin.authorizer_appid");
		String appId = Global.getConfig("weixin.component_appid");
		String appsecret = Global.getConfig("weixin.component_appsecret");
		String verify_ticket = Global.getConfig("weixin.component_verify_ticket");
		String token = getCompTokenByTicket(appId, appsecret, verify_ticket);
		String obtainURL = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token";
		String url = obtainURL + "?component_access_token=" + token;
		JSONObject jsonParam = new JSONObject();
		
		jsonParam.put("component_appid", appId);     //光明
		jsonParam.put("authorizer_appid", authorizer_appid);   //多赋
		jsonParam.put("authorizer_refresh_token", authorizer_refresh_token);
		String resp = HttpClientUtil.okPost(url, jsonParam.toString());
		JSONObject obj = JSONObject.parseObject(resp);
		logger.info("刷新token", obj);
		String accessToken = obj.getString("authorizer_access_token");
		String reshToken = obj.getString("authorizer_refresh_token");
		Global.modifyConfig("weixin.authorizer_refresh_token", reshToken);
		
		// 放入缓存中
		CacheUtils.put(AUTH_ACCESS_TOKEN_CACHE_ID, accessToken);
		CacheUtils.put(AUTH_REFRESH_ACCESS_TOKEN_CACHE_ID, reshToken);
		Date now = new Date();
		long timeout = now.getTime()/1000 + 7200 - 600; // 两个小时少10分钟, 单位秒
		CacheUtils.put(AUTH_ACCESS_TOKEN_TIMEOUT_CACHE_ID, String.valueOf(timeout));
		return accessToken;
	}

	// 获取（刷新）授权公众号或小程序的接口调用凭据
	public String getAuthorizerAccessToken() throws Exception {
		// 获取商户对象
		if (isFirst)
		{
			// 重新获取一遍
			return getAccessTokenByRefreshToken();	
		} else {
			String access_token = (String)CacheUtils.get(AUTH_ACCESS_TOKEN_CACHE_ID);
			String timeout = (String)CacheUtils.get(AUTH_ACCESS_TOKEN_TIMEOUT_CACHE_ID);
			
			// 判断是否过期
			long outTime = Long.parseLong(timeout) * 1000;
			Date now = new Date();
			long nowTime = now.getTime();
			if (nowTime > outTime) {
				//过期了
				// 重新获取一遍
				return getAccessTokenByRefreshToken();
			} else {
				return access_token;
			}	
		}
	}
}
