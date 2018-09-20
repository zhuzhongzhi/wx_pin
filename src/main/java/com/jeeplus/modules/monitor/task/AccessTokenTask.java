package com.jeeplus.modules.monitor.task;

import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeeplus.common.config.Global;
import com.jeeplus.modules.api.util.AuthTokenUtil;
import com.jeeplus.modules.api.util.WeixinServiceUtil;
import com.jeeplus.modules.monitor.entity.Task;
import com.jeeplus.modules.pin.service.PinGroupService;
import com.jeeplus.modules.pin.service.PinMemberService;

@DisallowConcurrentExecution
public class AccessTokenTask extends Task {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	WeixinServiceUtil weixinServiceUtil;
	
	@Autowired
	AuthTokenUtil authTokenUtil;
	
	@Override
	public void run() {
		logger.info("AccessTokenTask run begin");
		String app_token = weixinServiceUtil.getAppToken();
		logger.info("AccessTokenTask little app access token:" + app_token);
		String access_token = null;
		if (Global.isAuthMode()) {
			access_token = weixinServiceUtil.getGMToken();
		} else {
			access_token = weixinServiceUtil.getToken();
		}
		logger.info("AccessTokenTask weixin app access token:" + access_token);
		
		try {
			String comptoken =  authTokenUtil.getAccessTokenByRefreshToken();
			logger.info("AccessTokenTask comptoken:" + comptoken);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("AccessTokenTask run end");
	}

}
