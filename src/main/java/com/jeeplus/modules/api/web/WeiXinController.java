package com.jeeplus.modules.api.web;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.api.util.AuthTokenUtil;
import com.jeeplus.modules.api.util.WeixinServiceUtil;
import com.jeeplus.modules.api.util.xml2Map;
import com.jeeplus.modules.sys.entity.Log;
import com.jeeplus.modules.sys.utils.LogUtils;
import com.jeeplus.modules.sys.utils.ParamUtils;


/**
 * Created by ysisl on 17/11/22.
 */
@Controller
@RequestMapping(value = "/wx")
public class WeiXinController extends BaseController {
	@Autowired
	private AuthTokenUtil authTokenUtil;
	@Autowired
	private WeixinServiceUtil weixinServiceUtil;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "verifyTicketNotify")
	@ResponseBody
	public String verifyTicketNotify(HttpServletRequest request) {
		Log log = LogUtils.saveLog(request, "微信verifyTicket通知接口");

		String xmltext = log.getBody();
		Map<String, String> map = xml2Map.XmlToMap(xmltext);
		logger.info(xmltext);
		String infoType = map.containsKey("InfoType") ? map.get("InfoType").toString() : "";
		String ticket = map.containsKey("ComponentVerifyTicket") ? map.get("ComponentVerifyTicket").toString() : "";
		if (infoType.equals("component_verify_ticket")) {
			try {
				Global.modifyConfig("weixin.component_verify_ticket", ticket);
				return "success";
			} catch (Exception e) {
				logger.error("已获取Ticket：" + ticket + "| 发生异常: " + e.getStackTrace());
			}
		}
		return "faild";

		/*
		 * //消息类型 String infoType = map.get("InfoType").toString(); switch
		 * (InfoType.valueOf(infoType)) { //授权成功，可以获得授权码；授权码也可以在流程图⑤中获得，所以可以忽略 case
		 * authorized: break; //取消授权，可以删除本地保存的已授权公众号 case unauthorized: String appId =
		 * map.get("AuthorizerAppid"); //todo 删除本地授权的公众号 break;
		 * //更新授权，可以更新授权方令牌authorizer_access_token，刷新令牌authorizer_refresh_token，权限集列表等
		 * case updateauthorized: //todo 更新令牌等 break;
		 * //推送ticket，妥善保存ticket，用于获取component_access_token case
		 * component_verify_ticket: String ticket = map.get("ComponentVerifyTicket");
		 * //存储ticket break; default: break; }
		 */

	}
	
	//授权之后回调url
	@RequestMapping(value = "authorize")
	@ResponseBody
	public AjaxJson authorize(HttpServletRequest request) {

		AjaxJson result = new AjaxJson();
		String authCode = request.getParameter("auth_code");
		if (authCode == null) {
			logger.error("授权码为空");
			result.setSuccess(false);
			result.setMsg("获取授权异常：授权码为空");
			return result;
		}
		try {
			// 获取预授权码
			result = authTokenUtil.getAccessTokenByCode(authCode);
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
			result.setSuccess(false);
			result.setMsg("获取授权异常：" + e.getMessage());
		}
		return result;
	}
}
