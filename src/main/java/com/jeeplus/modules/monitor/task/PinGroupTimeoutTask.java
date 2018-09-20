package com.jeeplus.modules.monitor.task;


import javax.servlet.ServletContext;

import org.quartz.DisallowConcurrentExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jeeplus.modules.monitor.entity.Task;
import com.jeeplus.modules.pin.service.PinGroupService;


@DisallowConcurrentExecution
public class PinGroupTimeoutTask extends Task {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    PinGroupService pinGroupService;
	
	@Override
	public void run() {
		logger.info("PinGroupTimeoutTask run begin*******************************************");
		ServletContext scontext = JobContext.getInstance().getContext();  
		if (scontext == null) {
			logger.info("PinGroupTimeoutTask scontext is null=================================");
			return;
		}
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(scontext);  
		pinGroupService = (PinGroupService)ctx.getBean("pinGroupService");        
        
		// 查询所有状态是0进行中的团，如果已经过期了，则设置结束时间，同时将状态修改为2
		String sql = "update pin_group set status=2, end_time=NOW() where (status=0) AND ((NOW()-interval expire second) > create_time)";
		pinGroupService.executeUpdateSql(sql);
		
		logger.info("PinGroupTimeoutTask run end*******************************************");
	}

}
