package com.jeeplus.modules.api.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CookieUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.api.util.MemberServiceUtil;
import com.jeeplus.modules.api.util.WeixinServiceUtil;
import com.jeeplus.modules.sys.entity.Log;
import com.jeeplus.modules.sys.utils.AES;
import com.jeeplus.modules.sys.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.InvalidAlgorithmParameterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fusheng on 17/11/30.
 */
@Controller
@RequestMapping(value = "/memberApi")
public class MemberApiController extends BaseController {
	@Autowired
	private MemberServiceUtil memberServiceUtil;
	
	@Autowired
	private WeixinServiceUtil weixinServiceUtil;
	
	@Autowired
	private AES    aes;
	
	/**
	 * 会员注册接口 小程序会员注册接口
	 * 
	 * @param request
	 * @param appId
	 *            商户接口AppId
	 * @return
	 */
	@RequestMapping(value = "decodephone")
	@ResponseBody
	public AjaxJson decodephone(HttpServletRequest request, @RequestParam("appid") String appId,
			@RequestParam("encryptData") String encryptData, @RequestParam("sessionKey") String sessionKey,
			@RequestParam("iv") String iv) {
		AjaxJson result = new AjaxJson();

		try {
			Log log = LogUtils.saveLog(request, "会员注册接口");
			JSONObject map = JSONObject.parseObject(log.getBody());
			map.put("encryptData", encryptData);
			map.put("sessionKey", sessionKey);
			map.put("iv", iv);
			result = memberServiceUtil.regist(appId, map);
			return result;

		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("交易异常: " + e.getMessage());
			logger.error("交易异常: " + e.getStackTrace());
		}
		return result;
	}
	
	/**
     * 小程序根据code获取用户相关信息
     */
    @RequestMapping(value = "/appLogin")
    @ResponseBody
    public AjaxJson appLogin(
    			@RequestParam(value = "code", required = true) String code,
    			@RequestParam(value = "encryptedData", required = true) String encryptedData,
    			@RequestParam(value = "iv", required = true) String iv,
            HttpServletRequest request, HttpServletResponse response) {
    		logger.info("appLogin encryptedData:" + encryptedData + ", code:" + code + ", iv:" + iv);
    		AjaxJson result = new AjaxJson();
    	    // 根据code 获取openid 小程序的
    		JSONObject obj = weixinServiceUtil.getAppOpenidByCode(code);
    	    if (obj == null) {
    	    		result.setSuccess(false);
    			result.setMsg("根据code获取小程序openid失败");
    			logger.error("根据code获取小程序openid失败");
    			return result;
    	    }
    	    
    	    String sessionKey = obj.getString("session_key");
    	    String openid = obj.getString("openid");
    	    if (openid == null) {
    	    		result.setSuccess(false);
    			result.setMsg("code信息不包含openid");
    			logger.error("code信息不包含openid");
    			return result;
	    }
    	    
    	    if (sessionKey == null) {
		    	logger.error("info not include session_key, can not decode encryptedData.");
		    	result.setSuccess(false);
    			result.setMsg("code信息不包含session_key, 无法解密encryptedData,无法得到unionId.");
    			return result;	
	    }
    	    // 解密信息获取
    		String data = null;
		try {
			data = aes.decrypt(encryptedData, sessionKey, iv);
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("解密encryptedData异常:" + e.getMessage());
			logger.error("解密encryptedData异常:" + e.getMessage());
			return result;	
		}
		logger.error("AES data:" + data);
    		if (data == null) {
    			result.setSuccess(false);
    			result.setMsg("解密encryptedData内容为null");
    			logger.error("解密encryptedData内容为null");
    			return result;
    		}
    		
    		byte[] content = Base64Utils.decodeFromString(data);
    		String userInfo = new String(content);
    		logger.info("appLogin data:" + userInfo);
    		
    		// 根据字符串解除json
    		JSONObject objData = JSON.parseObject(userInfo);
    		if (objData == null) {
    			result.setSuccess(false);
    			result.setMsg(userInfo + "不是合法的json");
    			logger.error(userInfo + "不是合法的json");
    			return result;
    		}
    		String unionId = objData.getString("unionId");
    		if (unionId == null) {
    			result.setSuccess(false);
    			result.setMsg(userInfo + "里面没有包含unionId");
    			logger.error(userInfo + "里面没有包含unionId");
    			return result;
    		}
    		
    		// 根据unionid获取用户信息
    		return memberServiceUtil.appLogin(openid, unionId, objData, request, response);
    }

}
