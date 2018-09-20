package com.jeeplus.modules.sys.interceptor;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.security.Digests;
import com.jeeplus.modules.sys.utils.ParamUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ysisl on 17/11/23.
 */
public class AccessTokenVerifyInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
			throws Exception {
		
		return true;
		/* 光明拼团目前不用进行接口校验
		boolean verified = false;

		String appid = httpServletRequest.getParameter("appid");
		if (StringUtils.isEmpty(appid)) {
			AjaxJson result = new AjaxJson();
			result.setSuccess(false);
			result.setErrorCode("10003");
			result.setMsg("缺少参数，请提供appid!");
			httpServletResponse.setContentType("application/json");
			httpServletResponse.setCharacterEncoding("UTF-8");
			httpServletResponse.getWriter().append(result.getJsonStr());
			verified = false;
			return verified;
		}

		String sign = httpServletRequest.getParameter("sign");
		Logger logger = LoggerFactory.getLogger(getClass());
		String accessToken = httpServletRequest.getParameter("token");
		if (!StringUtils.isEmpty(accessToken)) {
			String iv = httpServletRequest.getParameter("iv");
			iv = StringUtils.isEmpty(iv) ? "" : iv;
			String key = ParamUtils.getParamByKey("global_interface_saltkey");
			String validToken = Digests.string2MD5(appid + iv + key);
			logger.info("Verify appid:[" + appid + "],key:[" + key + "], token:[" + validToken + "].");
			if (validToken.equals(accessToken)) {
				verified = true;
			} else {
				AjaxJson result = new AjaxJson();
				result.setSuccess(false);
				result.setErrorCode("10004");
				result.setMsg("token校验失败");
				httpServletResponse.setContentType("application/json");
				httpServletResponse.setCharacterEncoding("UTF-8");
				httpServletResponse.getWriter().append(result.getJsonStr());
				verified = false;
			}
		} else if (!StringUtils.isEmpty(sign)) {
			// 先放过，到具体的程序里面进行校验
			verified = true;
		} else {
			AjaxJson result = new AjaxJson();
			result.setSuccess(false);
			result.setErrorCode("10003");
			result.setMsg("缺少参数，请提供sign或者token!");
			httpServletResponse.setContentType("application/json");
			httpServletResponse.setCharacterEncoding("UTF-8");
			httpServletResponse.getWriter().append(result.getJsonStr());
			verified = false;
		}

		return verified;*/
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {

	}
}
