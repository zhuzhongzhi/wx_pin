/**
 * Copyright © 2017 com.cn. All rights reserved.
 *
 * @Title: OrderIDPoolService.java
 * @Prject: coffee-web
 * @Package: com.company.coffee.service
 * @Description: OrderIDPoolService
 * @author: lijunfeng
 * @date: 2017/9/16 15:36
 * @version: V1.0
 */
package com.jeeplus.modules.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jeeplus.modules.pin.service.PinMemberService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: OrderIDPoolService
 * @Description: OrderIDPoolService
 * @author: lijunfeng
 * @date 2017/9/16 15:36
 */
@Service
public class IDPoolService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
    private static SimpleDateFormat sdfCNT;
    private static Integer MemberId;
    private static Integer GroupId;
    
    @Autowired
    PinMemberService pinMemberService;
        
    public IDPoolService (){
        sdfCNT = new SimpleDateFormat("yyyyMMddHHmmss");
        MemberId = null;
        GroupId = null;
    }


    /**
     * 订单编号规则: 日期+递增序号
     * @return
     */
    public synchronized String generateMemberId() {
    		if (MemberId == null) {
    			List<Object> ids = pinMemberService.executeSelectSql("select id from pin_member order by id desc LIMIT 1");
    			if (ids.isEmpty()) {
    				MemberId = 0;
    			} else {
    				String idStr = (String) ids.get(0);
    				int length = idStr.length();
    				//取后面2位 20180502595915
	    	    		String value = idStr.substring(length - 2, length - 1);
	    	    		MemberId =  Integer.valueOf(value) % 100 + 1;
    			}
    			
    			logger.info("current max member id:" + MemberId);
    		}
    		
    		StringBuilder sb = new StringBuilder();
    		sb.append(sdfCNT.format(new Date()));
    		sb.append(MemberId);
    		MemberId += 1;
    		logger.info("generateMemberId member id:" + sb.toString());
        return sb.toString();
    }
    
    public synchronized String generateGroupId() {
		if (GroupId == null) {
			List<Object> ids = pinMemberService.executeSelectSql("select id from pin_group order by id desc LIMIT 1");
			if (ids.isEmpty()) {
				GroupId = 0;
			} else {
				String idStr = (String) ids.get(0);
				int length = idStr.length();
				//取后面2位 20180502595915
	    	    		String value = idStr.substring(length - 2, length - 1);
	    	    		GroupId =  Integer.valueOf(value) % 100 + 1;
			}
			
			logger.info("current max group id:" + GroupId);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(sdfCNT.format(new Date()));
		sb.append(GroupId);
		GroupId += 1;
		logger.info("generateGroupId group id:" + sb.toString());
		return sb.toString();
    }
    
    public synchronized String generateOrderId() {
    		String orderid = Long.toString(System.currentTimeMillis() / 1000) + UUID.randomUUID().toString().substring(0, 8);
    		logger.info("generateOrderId orderid:" + orderid);
    		return orderid;
    }
    
}
