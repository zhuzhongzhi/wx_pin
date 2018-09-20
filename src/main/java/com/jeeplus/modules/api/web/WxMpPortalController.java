/**
 * Copyright © 2017 com.cn. All rights reserved.
 *
 * @Title: WxMpPortalController.java
 * @Prject: coffee-web
 * @Package: com.company.coffee.controller.weixin
 * @Description: WxMpPortalController
 * @author: lijunfeng
 * @date: 2017/9/11 22:41
 * @version: V1.0
 */
package com.jeeplus.modules.api.web;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.jeeplus.common.utils.CookieUtils;
import com.jeeplus.modules.api.util.IDPoolService;
import com.jeeplus.modules.api.util.MemberServiceUtil;
import com.jeeplus.modules.api.util.ProductServiceUtil;
import com.jeeplus.modules.api.util.WeixinServiceUtil;
import com.jeeplus.modules.pin.entity.PinMember;
import com.jeeplus.modules.pin.service.PinMemberService;
import com.jeeplus.modules.weixin.entity.WxUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

/**
 * 微信事件推送
 *
 * @ClassName: WxMpPortalController
 * @Description: WxMpPortalController
 * @author:
 * @date: 2017/9/30 22:41
 */
@RestController
@RequestMapping("/weixin/portal")
public class WxMpPortalController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
	private WeixinServiceUtil weixinServiceUtil;
    @Autowired
	private PinMemberService pinMemberService;
    @Autowired
	private IDPoolService idPoolService;
   
    public String getRequestParams(HttpServletRequest request) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        return null;
    }
    
    private void eventHandle(Element rootElement, HttpServletResponse response) {
    		String Event = rootElement.elementText("Event");
    		switch(Event) {
    			case "subscribe":
    				onSubscribe(rootElement, response);
    				break;
    			case "unsubscribe":
    				onUnSubscribe(rootElement, response);
    				break;
    			case "SCAN":
    				onScan(rootElement, response);
    				break;
    			case "LOCATION":
    				onLocation(rootElement, response);
    				break;
    			case "CLICK":
    				onClick(rootElement, response);
    				break;
    			case "VIEW":
    				onView(rootElement, response);
    				break;
    		}
    }
    
    private void onSubscribe(Element rootElement, HttpServletResponse response) {
    		String openid = rootElement.elementText("FromUserName");
        String CreateTime = rootElement.elementText("CreateTime");
        
        WxUser user = weixinServiceUtil.getWxUserInfo(openid);
        if (user != null) {
	        	PinMember query = new PinMember();
	    		query.setUnionid(user.getUnionid());
	    		List<PinMember> members = pinMemberService.findList(query);
	    		if ((members == null) || (members.isEmpty())) {
	    			logger.info("can not find member info with unionId:" + user.getUnionid());
	    			
	    			// 新建用户
	    			query.setId(idPoolService.generateMemberId());
	    			query.setName(user.getNickname());
	    			query.setSex(user.getSex());
	    			query.setHeadimgurl(user.getHeadimgurl());
	    			query.setStatus("0");
	    			
	    			pinMemberService.save(query);		
	    			logger.info("new member:" + query.getId() + " info with unionId:" + user.getUnionid());
	    		} else {
	    			query = members.get(0);
	    			logger.info("find member:" + query.getId() + " info with unionId:" + user.getUnionid());
	    		}  
	    		
	    		// 重新设置cookie
	    		CookieUtils.setCookie(response, MemberServiceUtil.COOKEI_NAME, query.getId(), MemberServiceUtil.COOKEI_TIMEOUT);
        } else {
        		logger.info("can not find weixin user info with openid:" + openid);
        }
    }
    
    private void onUnSubscribe(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void onScan(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void onLocation(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void onClick(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void onView(Element rootElement, HttpServletResponse response) {
    	
    }

    private void textHandle(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void imageHandle(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void voiceHandle(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void videoHandle(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void shortvideoHandle(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void musicHandle(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void newsHandle(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void locationHandle(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void linkHandle(Element rootElement, HttpServletResponse response) {
    	
    }
    
    private void close(HttpServletResponse response, String echostr) {
    		if (!StringUtils.isEmpty(echostr)) {
            PrintWriter out = null;
			try {
				out = response.getWriter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            out.write(echostr);
            out.flush();
            out.close();
        }
    }

    /**
     * 微信事件推送
     *
     * @param request
     * @param response
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public void eventNotify(String signature, String timestamp, String nonce, String echostr, HttpServletRequest
            request, HttpServletResponse response) {
        try {
            logger.info("eventNotify signature:" + signature + ",timestamp:" + timestamp + ",nonce:" + nonce + ",echostr:" + echostr);
            String xml = getRequestParams(request);
            logger.info("eventNotify xml:" + xml);
            
            if (!StringUtils.isEmpty(xml)) {
                Document doc = DocumentHelper.parseText(xml);
                Element rootElement = doc.getRootElement();
                String MsgType = rootElement.elementText("MsgType").toLowerCase();
                switch (MsgType) {
	                case "event":
	                		eventHandle(rootElement, response);
	                		break;
	                case "text":
                			textHandle(rootElement, response);
                			break;
	                case "image":
	            			imageHandle(rootElement, response);
	            			break;
	                case "voice":
	                		voiceHandle(rootElement, response);
	                		break;
	                case "video":
	                		videoHandle(rootElement, response);
	                		break;
	                case "shortvideo":
	                		shortvideoHandle(rootElement, response);
	                		break;
	                case "location":
	                		locationHandle(rootElement, response);
	                		break;
	                case "link":
	                		linkHandle(rootElement, response);
	                		break;
	                case "music":
	                		musicHandle(rootElement, response);
	                		break;
	                case "news":
	                		newsHandle(rootElement, response);
	                		break;
	            		default:
	            			logger.info("unknown msg type:" + MsgType);
	            			break;
                }
            }         
        } catch (Exception e) {
        		e.printStackTrace();
        		logger.info("Exception:" + e.toString());
        }
        close(response, echostr);
    }

    
    
}
