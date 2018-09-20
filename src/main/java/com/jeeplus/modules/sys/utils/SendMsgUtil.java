package com.jeeplus.modules.sys.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.utils.ParamUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ysisl on 17/11/30.
 */
@Service
public class SendMsgUtil {

	public static final String EVENT_CODE_REGIST = "MSGSVR_REGIST";
	public static final String CACHE_TYPE = "MSGSVR";

	public String sendValidateMsg(String phone, String eventCode, String tplId) throws IOException {
		String key = ParamUtils.getParamByKey("juhe_api_key");
		String validateCode = generateCode(eventCode, phone);
		String url = "http://v.juhe.cn/sms/send?mobile=" + phone + "&tpl_id=" + tplId + "&tpl_value=%23code%23%3D"
				+ validateCode + "&key=" + key;

		// 发送手机号验证码
		String response = HttpClientUtil.okGet(url);
		JSONObject result = JSON.parseObject(response);
		if (!result.get("error_code").toString().equals("0"))
			return result.get("reason").toString();
		else
			return validateCode;
	}

	/**
	 * 自动生成随机验证码，并缓存
	 * 
	 * @param eventCode
	 * @param phone
	 * @return
	 */
	private String generateCode(String eventCode, String phone) {
		Object cacheCode = CacheUtils.get(SendMsgUtil.CACHE_TYPE + eventCode + phone);
		if (cacheCode != null)
			return cacheCode.toString();

		int min = 1;
		int max = 999999;
		int randNum = min + (int) (Math.random() * ((max - min) + 1));
		String randNumStr = String.valueOf(randNum);
		CacheUtils.put(SendMsgUtil.CACHE_TYPE + eventCode + phone, randNumStr); // 缓存Code
		return randNumStr;
	}

	/**
	 * 按缓存的事件和手机号 获取验证码，并清除验证码
	 * 
	 * @param eventCode
	 * @param phone
	 * @return
	 */
	public Boolean ValidateCode(String eventCode, String phone, String code) {
		Object actualCode = CacheUtils.get(SendMsgUtil.CACHE_TYPE + eventCode + phone);

		if (actualCode != null && code.equals(actualCode)) {
			CacheUtils.remove(SendMsgUtil.CACHE_TYPE + eventCode + phone);
			return true;
		} else
			return false;

	}
}
