package com.jeeplus.modules.sys.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;

@Service
public class CheckSignUtil {

	public boolean checkSign(HttpServletRequest request, String bodyString, String appid) {

		String sign = request.getParameter("sign");
		if (!StringUtils.isEmpty(sign)) {
			String version = request.getParameter("version");
			Logger logger = LoggerFactory.getLogger(getClass());
			logger.info("URI:[" + request.getRequestURI() + "].");
			logger.info("body:[" + bodyString + "].");
			logger.info("sign:[" + sign + "].");

			String key = Global.getConfig(appid);
			String validsign = SignUtil.getSign(appid, key, version, bodyString);

			logger.info("Verify appid:[" + appid + "],key:[" + key + "], sign:[" + validsign + "].");
			if (validsign.equals(sign)) {
				return true;
			}
		}

		return false;
	}
}
