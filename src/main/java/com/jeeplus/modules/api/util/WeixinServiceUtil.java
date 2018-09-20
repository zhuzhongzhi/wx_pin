package com.jeeplus.modules.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.modules.weixin.entity.CouponDetailRequest;
import com.jeeplus.modules.weixin.entity.RefundRequest;
import com.jeeplus.modules.weixin.entity.SendCouponRequest;
import com.jeeplus.modules.weixin.entity.UnifiedOrderRequest;
import com.jeeplus.modules.weixin.entity.WxUser;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.pin.entity.PinMember;
import com.jeeplus.modules.pin.entity.PinProduct;
import com.jeeplus.modules.pin.entity.PinRefund;
import com.jeeplus.modules.pin.entity.PinRule;
import com.jeeplus.modules.pin.entity.PinToken;
import com.jeeplus.modules.pin.entity.PinTokenExpire;
import com.jeeplus.modules.pin.service.PinMemberService;
import com.jeeplus.modules.pin.service.PinProductService;
import com.jeeplus.modules.pin.service.PinRefundService;
import com.jeeplus.modules.pin.service.PinRuleService;
import com.jeeplus.modules.sys.utils.AES;
import com.jeeplus.modules.sys.utils.HttpClientUtil;
import com.jeeplus.modules.sys.utils.SignUtils;
import com.jeeplus.modules.tools.utils.MultiDBUtils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by fusheng on 17/11/30.
 */
@Service
public class WeixinServiceUtil {
	protected static Logger logger = LoggerFactory.getLogger(WeixinServiceUtil.class);
	//小程序access token缓存中的id
	public static final String APP_ACCESS_TOKEN_CACHE_ID = "PIN_APP_ACCESS_TOKEN_ID";
	//小程序access token缓存过期时间的id
	public static final String APP_ACCESS_TOKEN_TIMEOUT_CACHE_ID = "PIN_APP_ACCESS_TOKEN_TIMEOUT_ID";
	
	//gmaccess token缓存中的id
	public static final String GM_ACCESS_TOKEN_CACHE_ID = "PIN_GM_ACCESS_TOKEN_ID";
	//gmaccess token缓存过期时间的id
	public static final String GM_ACCESS_TOKEN_TIMEOUT_CACHE_ID = "PIN_GM_ACCESS_TOKEN_TIMEOUT_ID";
		
	//公众号access token缓存中的id
	public static final String ACCESS_TOKEN_CACHE_ID = "PIN_ACCESS_TOKEN_ID";
	//公众号access token缓存过期时间的id
	public static final String ACCESS_TOKEN_TIMEOUT_CACHE_ID = "PIN_ACCESS_TOKEN_TIMEOUT_ID";
	// 两个小时差10分钟 (60*60*2 -60*10)*1000 = 6600000
	public static final long TOKEN_TIMEOUT = 6600000;
	
	public static final String WX_SENDCOUPON = "https://api.mch.weixin.qq.com/mmpaymkttransfers/send_coupon";
    public static final String WX_COUPONDETAIL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/querycouponsinfo";
    // 微信消息推送
    private static final String WX_MSG = " https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
    // 微信小程序模版消息推送
    private static final String WX_TEMPLATE_MSG = " https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";
    
    // 微信支付统一下单
    public static final String WX_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    
	// 微信获取用户信息
    private static final String WX_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?lang=zh_CN";
    // 微信获取access
    public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    // 微信获取openid和sessionkey
    public static final String GET_OPENID_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=";
    // 微信退款
 	public static final String WX_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";
 	// 微信退款查询
 	public static final String WX_REFUND_QUERY = "https://api.mch.weixin.qq.com/pay/refundquery";
    // 获取ticket
 	public static final String GET_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=";
 	// 获取小程序码
 	public static final String GET_WXACODE_B ="https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";
 	
 	private static String ACCESS_TOKEN;     //微信公众号的access_token
    private static String APP_ACCESS_TOKEN; //小程序access_token
    private static String GM_ACCESS_TOKEN;  //光明access_token
    
    @Autowired
    PinRefundService pinRefundService;
    
	// 获取token
    public static synchronized String getTokenFromWeixin() {
        String turl = String.format(
                "%s?grant_type=client_credential&appid=%s&secret=%s", GET_TOKEN_URL,
                Global.getConfig("weixin.appid"), Global.getConfig("weixin.secret"));
        String access_token = null;
        logger.info("getTokenFromWeixin url：" + turl);
        String response = null;
		try {
			response = HttpClientUtil.okGet(turl);
			logger.info("getTokenFromWeixin response：" + response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getTokenFromWeixin http okGet Exception:" + e.getMessage());
		}
		if (response != null) {
			JSONObject result = JSON.parseObject(response);
			if (result.get("errcode") != null) {
				return null;
			} else {
				access_token = result.get("access_token").toString();
			}
		} else {
			logger.error("getTokenFromWeixin response is null.");
			return null;
		}

        return access_token;
    }
    
    public static synchronized String getAppTokenFromWeixin() {
        String turl = String.format(
                "%s?grant_type=client_credential&appid=%s&secret=%s", GET_TOKEN_URL,
                Global.getConfig("weixin.little_appid"), Global.getConfig("weixin.little_secret"));
        String app_access_token = null;
        logger.info("getAppTokenFromWeixin url：" + turl);
        String response = null;
		try {
			response = HttpClientUtil.okGet(turl);
			logger.info("getAppTokenFromWeixin response：" + response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getAppTokenFromWeixin http okGet Exception:" + e.getMessage());
		}
		if (response != null) {
			JSONObject result = JSON.parseObject(response);
			if (result.get("errcode") != null) {
				return null;
			} else {
				app_access_token = result.get("access_token").toString();
			}
		} else {
			logger.error("getAppTokenFromWeixin response is null.");
			return null;
		}

        return app_access_token;
    }
    
    public static synchronized String getAppTicketFromWeixin() {
        String turl = GET_TICKET + getAppToken() + "&type=wx_card";
       
        logger.info("getAppTicketFromWeixin url：" + turl);
        String ticket = null;
        String response = null;
		try {
			response = HttpClientUtil.okGet(turl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getAppTicketFromWeixin http okGet Exception:" + e.getMessage());
		}
		if (response != null) {
			JSONObject result = JSON.parseObject(response);
			ticket = result.get("ticket").toString();
		} else {
			logger.error("getAppTicketFromWeixin response is null.");
			return null;
		}

        return ticket;
    }
    
    public static synchronized String getTicketFromWeixin() {
    		String access_token = null;
    		if (Global.isAuthMode()) {
    			access_token = getGMToken();
    		} else {
    			access_token = getToken();
    		}
    		if (access_token == null) {
    			logger.info("getTicketFromWeixin access_token：" + access_token);
    			return null;
    		}
        String turl = GET_TICKET + access_token + "&type=wx_card";
       
        logger.info("getTicketFromWeixin url：" + turl);
        String ticket = null;
        String response = null;
		try {
			response = HttpClientUtil.okGet(turl);
			logger.info("getTicketFromWeixin response：" + response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getTicketFromWeixin http okGet Exception:" + e.getMessage());
		}
		if (response != null) {
			JSONObject result = JSON.parseObject(response);
			try
			{
				ticket = result.get("ticket").toString();
			} catch(Exception e) {
				logger.error("getTicketFromWeixin get ticket Exception:" + e.getMessage());
				ticket = null;
			}
		} else {
			logger.error("getTicketFromWeixin response is null.");
			return null;
		}

        return ticket;
    }
    
    public static String getToken() {
    		String access_token = null;
    		String timeout = null;
    		if (ACCESS_TOKEN != null) {
    			access_token = (String)CacheUtils.get(ACCESS_TOKEN_CACHE_ID);
    			timeout = (String)CacheUtils.get(ACCESS_TOKEN_TIMEOUT_CACHE_ID);
    			ACCESS_TOKEN = access_token;
    		}
    		if ((access_token != null) && (!access_token.equals("")) && (timeout != null) && (!timeout.equals(""))) {
    			// 判断是否过期
    			long outTime = Long.parseLong(timeout) * 1000;
    			Date now = new Date();
    			long nowTime = now.getTime();
    			if (nowTime > outTime) {
    				// 过期, 重新获取一个
    				access_token = getTokenFromWeixin();
    				if (access_token == null) {
    					//try again
    					access_token = getTokenFromWeixin();
    				}
    				if (access_token != null) {
    					// 超时时间设定为2小时差10分钟
    					timeout = String.valueOf(nowTime + (60*60*2 -60*10)*1000);
    					CacheUtils.put(ACCESS_TOKEN_CACHE_ID, access_token);
    					CacheUtils.put(ACCESS_TOKEN_TIMEOUT_CACHE_ID, timeout);
    				} else {
    					CacheUtils.put(ACCESS_TOKEN_CACHE_ID, "");
    					CacheUtils.put(ACCESS_TOKEN_TIMEOUT_CACHE_ID, nowTime);
    				}
    			}
    		} else {
    			// 不存在，则重新获取
    			access_token = getTokenFromWeixin();
			if (access_token == null) {
				//try again
				access_token = getTokenFromWeixin();
			}
			Date now = new Date();
			long nowTime = now.getTime();
			if (access_token != null) {
				// 超时时间设定为2小时差10分钟
				timeout = String.valueOf(nowTime + (60*60*2 -60*10)*1000);
				CacheUtils.put(ACCESS_TOKEN_CACHE_ID, access_token);
				CacheUtils.put(ACCESS_TOKEN_TIMEOUT_CACHE_ID, timeout);
			} else {
				CacheUtils.put(ACCESS_TOKEN_CACHE_ID, "");
				CacheUtils.put(ACCESS_TOKEN_TIMEOUT_CACHE_ID, nowTime);
			}
    		}
    		
    		logger.info("getToken access_token:" + access_token);
    		return access_token;
    }
    
    public static String getAppToken() {
    	String app_access_token = null;
		String timeout = null;
		if (APP_ACCESS_TOKEN != null) {
			app_access_token = (String)CacheUtils.get(APP_ACCESS_TOKEN_CACHE_ID);
			timeout = (String)CacheUtils.get(APP_ACCESS_TOKEN_TIMEOUT_CACHE_ID);
			APP_ACCESS_TOKEN = app_access_token;
		}
		if ((app_access_token != null) && (!app_access_token.equals("")) && (timeout != null) && (!timeout.equals(""))) {
			// 判断是否过期
			long outTime = Long.parseLong(timeout) * 1000;
			Date now = new Date();
			long nowTime = now.getTime();
			if (nowTime > outTime) {
				// 过期, 重新获取一个
				app_access_token = getAppTokenFromWeixin();
				if (app_access_token == null) {
					//try again
					app_access_token = getAppTokenFromWeixin();
				}
				if (app_access_token != null) {
					// 超时时间设定为2小时差10分钟
					timeout = String.valueOf(nowTime + (60*60*2 -60*10)*1000);
					CacheUtils.put(APP_ACCESS_TOKEN_CACHE_ID, app_access_token);
					CacheUtils.put(APP_ACCESS_TOKEN_TIMEOUT_CACHE_ID, timeout);
				}
			}
		} else {
			// 不存在，则重新获取
			app_access_token = getAppTokenFromWeixin();
			if (app_access_token == null) {
				//try again
				app_access_token = getAppTokenFromWeixin();
			}
			if (app_access_token != null) {
				// 超时时间设定为2小时差10分钟
				Date now = new Date();
					long nowTime = now.getTime();
				timeout = String.valueOf(nowTime + (60*60*2 -60*10)*1000);
				CacheUtils.put(APP_ACCESS_TOKEN_CACHE_ID, app_access_token);
				CacheUtils.put(APP_ACCESS_TOKEN_TIMEOUT_CACHE_ID, timeout);
			}
		}
		
		logger.info("getAppToken access_token:" + app_access_token);
		return app_access_token;
    }

    public static String getGMToken() {
    		String gm_access_token = null;
		String timeout = null;
		if (GM_ACCESS_TOKEN != null) {
			gm_access_token = (String)CacheUtils.get(GM_ACCESS_TOKEN_CACHE_ID);
			timeout = (String)CacheUtils.get(GM_ACCESS_TOKEN_TIMEOUT_CACHE_ID);
			GM_ACCESS_TOKEN = gm_access_token;
		}
		if ((gm_access_token != null) && (!gm_access_token.equals("")) && (timeout != null) && (!timeout.equals(""))) {
			// 判断是否过期
			long outTime = Long.parseLong(timeout) * 1000;
			Date now = new Date();
			long nowTime = now.getTime();
			if (nowTime > outTime) {
				// 过期, 重新获取一个
				PinTokenExpire te = getGmTokenFromCard();
				if (te.getAccessToken().equals("NONE")) {
					//try again
					try {
						Thread.sleep(3*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					te = getGmTokenFromCard();
				}
				if (!te.getAccessToken().equals("NONE")) {
					gm_access_token = te.getAccessToken();
					timeout = String.valueOf(te.getExpiredDate().getTime());
					CacheUtils.put(GM_ACCESS_TOKEN_CACHE_ID, gm_access_token);
					CacheUtils.put(GM_ACCESS_TOKEN_TIMEOUT_CACHE_ID, timeout);
				}
			}
		} else {
			// 不存在，则重新获取
			PinTokenExpire te = getGmTokenFromCard();
			if (te.getAccessToken().equals("NONE")) {
				//try again
				try {
					Thread.sleep(3*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				te = getGmTokenFromCard();
			}
			if (!te.getAccessToken().equals("NONE")) {
				gm_access_token = te.getAccessToken();
				timeout = String.valueOf(te.getExpiredDate().getTime());
				CacheUtils.put(GM_ACCESS_TOKEN_CACHE_ID, gm_access_token);
				CacheUtils.put(GM_ACCESS_TOKEN_TIMEOUT_CACHE_ID, timeout);
			}
		}
		
		logger.info("getGMToken access_token:" + gm_access_token);
		return gm_access_token;
    }
    
    public static PinTokenExpire getGmTokenFromCard() {
    		PinTokenExpire result = new PinTokenExpire();
    		result.setAccessToken("NONE");
    		String appid = Global.getConfig("weixin.appid");
    		// 从礼品卡库中获取
    		MultiDBUtils md = MultiDBUtils.get("card");
    		String sql = "select inf_app_id as appid, access_token as accessToken, token_expired_date as tokenExpiredDate from card_account where inf_app_id='"+ appid + "' ";
    		logger.info("getGmTokenFromCard sql:" + sql);
    		List<PinToken> list = md.queryList(sql, PinToken.class, null);
    		if ((list != null) && (!list.isEmpty())) {
    			PinToken token = list.get(0);
    			String accessToken = token.getAccessToken();
    			// 查看是否过期
    			String expire = token.getTokenExpiredDate();
    			logger.info("getGmTokenFromCard accessToken:"+ accessToken + ",expire:" + expire);
    			
    			Date expireDate = null;
    			if (expire.length() == 23) {
	    			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 
	    			try {
					expireDate = format.parse(expire);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.info("getGmTokenFromCard expireDate:" + expire + " to date exception:" + e.getMessage());
					
					// 通知礼品卡平台重新取
					doTokenExpire(appid);
					
					return result;
				} 
    			} else {
    				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	    			try {
					expireDate = format.parse(expire);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.info("getGmTokenFromCard expireDate:" + expire + " to date exception:" + e.getMessage());
					
					// 通知礼品卡平台重新取
					doTokenExpire(appid);
					return result;
				} 
    			}
    			
    			//expireDate 在比较的时候需要加上2小时
    			long exp = expireDate.getTime() + 7200*1000;
    			Date expDate = new Date(exp);
    			Date now = new Date();
    			
    			if (now.after(expDate)) {
    				doTokenExpire(appid);
    				// access token返回null 让外面决定再次调用
    			} else {
	    			result.setAccessToken(accessToken);
	    			result.setExpiredDate(expDate);
    			}
    		} else {
    			doTokenExpire(appid);
    		}
    		return result;
    }
    public static void doTokenExpire(String appid) {
    		//  过期了， 需要通知礼品卡更新token,  通过查询订单接口，变相的促进礼品卡平台更新token
		String turl = "https://prod.doforsys.net/etiquette/api/queryOrder?sign=xxx&appid=" + appid;
        String json = "{\"cardCode\":\""+ create_nonce_str() + "\"}";
		String response = null;
		try {
			response = HttpClientUtil.okPost(turl, json);
			logger.info("doTokenExpire response：" + response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("doTokenExpire http okPost Exception:" + e.getMessage());
		}
		
		logger.info("doTokenExpire turl：" + turl + ", response:" + response);	
    }
    
	public static WxUser getWxUserInfo(String openid) {
		String access_token = null;
		if (Global.isAuthMode()) {
			access_token = getGMToken();
		} else {
			access_token = getToken();
		}
        String url = WX_USERINFO + "&access_token=" + access_token + "&openid=" + openid;
        logger.info("getWxUserInfo：" + url);
        
        String response = null;
		try {
			response = HttpClientUtil.okGet(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getWxUserInfo http okGet Exception:" + e.getMessage());
		}
		if (response != null) {
			WxUser resp = JSON.parseObject(response, WxUser.class);
	        return resp;
		} else {
			logger.error("getWxUserInfo failed.");
			return null;
		}
    }
	
	public static WxUser getAppWxUserInfo(String openid) {
        String url = WX_USERINFO + "&access_token=" + getAppToken() + "&openid=" + openid;
        logger.info("getAppWxUserInfo：" + url);
        
        String response = null;
		try {
			response = HttpClientUtil.okGet(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getAppWxUserInfo http okGet Exception:" + e.getMessage());
		}
		if (response != null) {
			WxUser resp = JSON.parseObject(response, WxUser.class);
	        return resp;
		} else {
			logger.error("getAppWxUserInfo failed.");
			return null;
		}
    }
	
	//获取小程序的openid
    public static JSONObject getAppOpenidByCode(String code) {

        String url = GET_OPENID_URL +
        		Global.getConfig("weixin.little_appid") + "&secret=" + 
        		Global.getConfig("weixin.little_secret") +
        		"&js_code="+ code + "&grant_type=authorization_code";        
        logger.info("getAppOpenidByCode，code：" + code + ",url:" + url);
        String response = null;
		try {
			response = HttpClientUtil.okGet(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("getAppOpenidByCode http okGet Exception:" + e.getMessage());
		}
		if (response != null) {
			logger.info("getAppOpenidByCode，response：" + response);
			JSONObject result = JSON.parseObject(response);
	        return result;
		} else {
			logger.error("getAppOpenidByCode failed.");
			return null;
		}
    }
    
    /**
     * 发送小程序模版消息
     *
     * @param openid
     * @param text
     */
    public static void sendAppTemplateMsg(String openid, String templateId, String formId, String data1, String data2, String data3, String data4) {
        String msgTemplate = "{\"touser\": \"OPENID\",\"template_id\": \"TEMPLATE_ID\",\"form_id\": \"FORMID\",\"data\": {\"keyword1\": {\"value\": \"keyword1DATA\"},\"keyword2\": {\"value\": \"keyword2DATA\"},\"keyword3\": {\"value\": \"keyword3DATA\"} ,\"keyword4\": {\"value\": \"keyword4DATA\"}}}";
        
        msgTemplate = msgTemplate.replace("OPENID", openid);
        msgTemplate = msgTemplate.replace("TEMPLATE_ID", templateId);
        msgTemplate = msgTemplate.replace("FORMID", formId);
        msgTemplate = msgTemplate.replace("keyword1DATA", data1);
        msgTemplate = msgTemplate.replace("keyword2DATA", data2);
        msgTemplate = msgTemplate.replace("keyword3DATA", data3);
        msgTemplate = msgTemplate.replace("keyword4DATA", data4);

        logger.info("sendAppTemplateMsg msgTemplate:" + msgTemplate);
        String access_token = getAppToken();
		
        String url = WX_TEMPLATE_MSG + access_token;
        
        
        String response = null;
    		try {
    			response = HttpClientUtil.okPost(url, msgTemplate);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			logger.error("sendAppTemplateMsg http okGet Exception:" + e.getMessage());
    		}
    		
    		logger.error("sendAppTemplateMsg weixin response:" + response);
    }
    
    /**
     * 发送文本消息
     *
     * @param openid
     * @param text
     */
    public static void sendTextMsg(String openid, String text) {
        String msgTemplate = "{" + "\"touser\":\"{openid}\"," + "\"msgtype\":\"text\"," + "\"text\":" + "{" + "\"content\":\"{text}\"" + "}" + "}";

        msgTemplate = msgTemplate.replace("{openid}", openid);
        msgTemplate = msgTemplate.replace("{text}", text);

        logger.info("发送文本消息请求：" + msgTemplate);
        String access_token = null;
		if (Global.isAuthMode()) {
			access_token = getGMToken();
		} else {
			access_token = getToken();
		}
        String url = WX_MSG + access_token;
        
        
        String response = null;
    		try {
    			response = HttpClientUtil.okPost(url, msgTemplate);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			logger.error("getAppOpenidByCode http okGet Exception:" + e.getMessage());
    		}
    		
    		logger.error("sendTextMsg weixin response:" + response);
    }
    
    /**
     * 发送链接消息
     *
     * @param openid
     * @param title
     * @param desc
     * @param url
     * @param picUrl
     */
    public static void sendLinkMsg(String openid, String title, String desc, String url, String picUrl) {
        
        /*
        {
            "touser":"OPENID",
            "msgtype":"news",
            "news":{
                "articles": [
                 {
                     "title":"Happy Day",
                     "description":"Is Really A Happy Day",
                     "url":"URL",
                     "picurl":"PIC_URL"
                 },
                 {
                     "title":"Happy Day",
                     "description":"Is Really A Happy Day",
                     "url":"URL",
                     "picurl":"PIC_URL"
                 }
                 ]
            }
        }
        */
        
        String msgTemplate = "{\"touser\":\"{OPENID}\",\"msgtype\":\"news\",\"news\":{\"articles\": [{\"title\":\"{TITLE}\",\"description\":\"{DESC}\",\"url\":\"{URL}\",\"picurl\":\"{PIC_URL}\"}]}}";

        msgTemplate = msgTemplate.replace("{OPENID}", openid);
        msgTemplate = msgTemplate.replace("{TITLE}", title);
        msgTemplate = msgTemplate.replace("{DESC}", desc);
        msgTemplate = msgTemplate.replace("{URL}", url);
        msgTemplate = msgTemplate.replace("{PIC_URL}", picUrl);

        logger.info("发送图文消息请求：" + msgTemplate);
        String access_token = null;
		if (Global.isAuthMode()) {
			access_token = getGMToken();
		} else {
			access_token = getToken();
		}
        String wxUrl = WX_MSG + access_token;
        
        String response = null;
    		try {
    			response = HttpClientUtil.okPost(wxUrl, msgTemplate);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			logger.error("getAppOpenidByCode http okGet Exception:" + e.getMessage());
    		}
    		
    		logger.error("sendTextMsg weixin response:" + response);
    }
    
    public static String create_nonce_str() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
    
    public static String create_code() {
    		return "2" + Long.toString(System.currentTimeMillis()) + UUID.randomUUID().toString().substring(0, 6);
	}
    
    // 代金券详情H5
  	public String couponDetail(CouponDetailRequest preReqParamsModel) throws Exception {

  		String apiKey = Global.getConfig("weixin.key");
  		preReqParamsModel.setNonce_str(create_nonce_str());
  		
  		// 设置签名
  		String sign = SignUtils.genereateSign(preReqParamsModel, apiKey);
  		preReqParamsModel.setSign(sign);

  		String detailUri = WX_COUPONDETAIL + "?stock_id=" + preReqParamsModel.getStock_id() + "&coupon_id="
  				+ preReqParamsModel.getCoupon_id() + "&openid=" + preReqParamsModel.getOpenid() + "&appid="
  				+ preReqParamsModel.getAppid() + "&mch_id=" + preReqParamsModel.getMch_id() + "&nonce_str="
  				+ preReqParamsModel.getNonce_str() + "&sign=" + preReqParamsModel.getSign() + "#wechat_redirect";
  		logger.info("couponDetail h5 URL：" + detailUri);

  		return detailUri;
  	}
 	 	
     // 发送代金券
  	public String sendCouponToWX(SendCouponRequest preReqParamsModel) throws Exception {
  		String result = null;
  		
  		String apiKey = Global.getConfig("weixin.key");
  		preReqParamsModel.setNonce_str(create_nonce_str());
  		// 设置签名
  		String sign = SignUtils.genereateSign(preReqParamsModel, apiKey);
  		preReqParamsModel.setSign(sign);
  		//log.info("代金券请求JSON：" + JSON.toJSONString(preReqParamsModel));
  		String requestXml = SignUtils.genereateXML(preReqParamsModel);
  		logger.info("sendCouponToWX requestXml:" + requestXml);

  		String mchid = Global.getConfig("weixin.mchid");
  		String p12path = Global.class.getResource("/properties/apiclient_cert-prd.p12").getPath();
  		p12path = URLDecoder.decode(p12path, "UTF-8");
  		logger.info("sendCouponToWX p12path：" + p12path);
  		KeyStore keyStore = KeyStore.getInstance("PKCS12");
  		FileInputStream instream = new FileInputStream(new File(p12path));
  		//log.info("FileInputStream：" + instream);
  		try {
  			keyStore.load(instream, mchid.toCharArray());
  		} finally {
  			instream.close();
  		}
  		//log.info("KeyStore：" + keyStore);
  		// Trust own CA and all self-signed certs
  		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchid.toCharArray()).build();
  		// Allow TLSv1 protocol only
  		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
  				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
  		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
  		HttpPost httpPost = new HttpPost(WX_SENDCOUPON);
  		httpPost.setHeader("Content-Type", "text/xml; charset=UTF-8");  
  		//log.info("httpPost：" + httpPost);
  		StringEntity ent = new StringEntity(requestXml);
  		httpPost.setEntity(ent);
  		CloseableHttpResponse response = httpclient.execute(httpPost);
  		//log.info("httpclient.execute response：" + response);
  		try {
  			HttpEntity entity = response.getEntity();
  			if (entity != null) {
  				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
  				String text;
  				StringBuffer sb = new StringBuffer();
  				while ((text = bufferedReader.readLine()) != null) {
  					sb.append(text);
  				}
  				String res = sb.toString();
  				logger.info("sendCouponToWX res:" + res);

  				Document doc = null;
  				try {
  					doc = DocumentHelper.parseText(res);
  				} catch(DocumentException e) {
  					logger.info("sendCouponToWX parseText res:[" + res + "], DocumentException:" + e.getMessage());
  				}
  				Element rootElement = doc.getRootElement();
  				String return_code = rootElement.elementText("return_code");
  				String return_msg = rootElement.elementText("return_msg");

  				// 通信成功标识
  				if ("SUCCESS".equals(return_code)) {
  					// 交易成功标识
  					String result_code = rootElement.elementText("result_code");
  					if ("SUCCESS".equals(result_code)) {
  						String coupon_id = rootElement.elementText("coupon_id");
  						String openid = rootElement.elementText("openid");
  						String coupon_stock_id = rootElement.elementText("coupon_stock_id");

  						CouponDetailRequest preOrderReq = new CouponDetailRequest();
  						// 小程序appid
  						preOrderReq.setAppid(Global.getConfig("weixin.little_appid"));
  						// 商户id
  						preOrderReq.setMch_id(Global.getConfig("weixin.mchid"));
  						// openid
  						preOrderReq.setOpenid(openid);
  						preOrderReq.setCoupon_id(coupon_id);
  						preOrderReq.setStock_id(coupon_stock_id);
  						String h5Uri = couponDetail(preOrderReq);
  						result = "{\"coupon_id\":\"" + coupon_id + "\", \"h5Uri\":\"" + h5Uri + "\"}";
  						logger.info("sendCouponToWX result:" + result);
  						
  						
  					} else {
  						// 交易失败
  						logger.info("sendCouponToWX result_code:" + result_code + ", err_code:" + rootElement.elementText("err_code") + ", err_code_des"
  								+ rootElement.elementText("err_code_des"));
  					}
  				} else {
  					logger.info("sendCouponToWX return_code:" + return_code + ", return_msg:"+ return_msg);
  				}
  			}
  			EntityUtils.consume(entity);
  		} finally {
  			response.close();
  		}
  		
  		return result;
  	}
  	
  	public void sendWxCoupon(String openId, String orderId, String stockId) {
  		logger.error("sendWxCoupon:" + openId + ":" + orderId + ":" + stockId);
  		SendCouponRequest preOrderReq = new SendCouponRequest();
 		// 公众号appid
 		preOrderReq.setAppid(Global.getConfig("weixin.appid"));
 		// 商户id
 		preOrderReq.setMch_id(Global.getConfig("weixin.mchid"));
 		// openid
 		preOrderReq.setOpenid(openId);
 		preOrderReq.setOpenid_count("1");
 		preOrderReq.setPartner_trade_no(orderId);
 		preOrderReq.setCoupon_stock_id(stockId);
 		preOrderReq.setOp_user_id(preOrderReq.getMch_id());
 		preOrderReq.setVersion("1.0");
 		preOrderReq.setType("XML");
 		try {
 			sendCouponToWX(preOrderReq);
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			logger.error("sendCouponToWX exception:" + e.getStackTrace());
 		}
  	}
  	
  	public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
  	
  	/**
     * 统一下单,返回json的String
     * Description: <br>
     *
     * @see
     */
    public static HashMap<String, String> createUnifiedOrder(UnifiedOrderRequest preReqParamsModel) throws Exception {

        String apiKey = Global.getConfig("weixin.key");
        preReqParamsModel.setNonce_str(create_nonce_str());
        // 设置签名
        String sign = SignUtils.genereateSign(preReqParamsModel, apiKey);
        preReqParamsModel.setSign(sign);
        logger.info("UnifiedOrderRequest preReqParamsModel:" + JSON.toJSONString(preReqParamsModel));
        String requestXml = SignUtils.genereateXML(preReqParamsModel);
        logger.info("UnifiedOrderRequest requestXml:" + requestXml);
        String resultXml = HttpRequestUtil.postByStream(WX_UNIFIEDORDER + "?showwxpaytitle=1", requestXml, null);
        logger.info("UnifiedOrderRequest resultXml:" + resultXml);
        Document doc = DocumentHelper.parseText(resultXml);
        Element rootElement = doc.getRootElement();
        String return_code = rootElement.elementText("return_code");
        String return_msg = rootElement.elementText("return_msg");

        // 通信成功标识
        if ("SUCCESS".equals(return_code)) {
            // 交易成功标识
            String result_code = rootElement.elementText("result_code");
            if ("SUCCESS".equals(result_code)) {
                // 此处要验证微信返回的签名是否正确，如果不正确代表被人劫包篡改了
                boolean signIsRignt = SignUtils.checkSign(resultXml, apiKey);
                if (signIsRignt) {
                    // 统一订单请求成功
                    String prepay_id = rootElement.elementText("prepay_id");

                    // 生成签名
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("appId", preReqParamsModel.getAppid());
                    String nonceStr = create_nonce_str();
                    map.put("nonceStr", nonceStr);
                    map.put("package", "prepay_id=" + prepay_id);
                    map.put("signType", "MD5");
                    String timeStamp = create_timestamp();
                    map.put("timeStamp", timeStamp);
                    String paySign = SignUtils.genereateSign(map, apiKey);
                    map.put("paySign", paySign);
                    map.put("out_trade_no", preReqParamsModel.getOut_trade_no());
                    
                    // 先按照订单将prepay_id放到缓存中， 后面发模版消息需要使用
                    CacheUtils.put("PREPAYID" + preReqParamsModel.getOut_trade_no() , prepay_id);
                    
                    return map;
                } else {
                    // 检验签名错误
                		logger.info("UnifiedOrderRequest checkSign failed.");
                    throw new Exception("通信交易成功，验证签名失败");
                }
            } else {
                // 交易失败
            		logger.info("UnifiedOrderRequest result_code:" + result_code);
                throw new Exception("通信成功，交易失败：" + rootElement.elementText("err_code") + ":" + rootElement.elementText("err_code_des"));
            }
        } else {
        		logger.info("UnifiedOrderRequest return_code:" + return_code + ", return_msg:"+ return_msg);
            throw new Exception("通信失败" + return_msg);
        }
    }
    
    // 需要的参数appid,mch_id,nonce_str,out_refund_no,out_trade_no,refund_fee,total_fee,transaction_id,sign
 	// 退款接口
 	public String refund(String orderID, String refundID, Integer totalFee, Integer refundFee) {
 		
 		logger.info("refund orderID:" + orderID + ", refundID:" + refundID);
 		
 		
 		try {
 			RefundRequest refundReq = new RefundRequest();

 			String appid = Global.getConfig("weixin.little_appid");
 			String mchid = Global.getConfig("weixin.mchid");

 			refundReq.setAppid(appid);
 			refundReq.setMch_id(mchid);
 			refundReq.setOut_trade_no(orderID);  // 商户订单号
 			refundReq.setOut_refund_no(refundID); // 商户退款单号
 			refundReq.setTotal_fee(totalFee);
 			refundReq.setRefund_fee(refundFee);
 			refundReq.setNotify_url(Global.getConfig("systemdomain") + "/payApi/refundnotify");
 			
 			// 获取沙盒签名
 			createRefund(refundReq);

 			String p12path = Global.class.getResource("/properties/apiclient_cert-prd.p12").getPath();
 			p12path = URLDecoder.decode(p12path, "UTF-8");
 			KeyStore keyStore = KeyStore.getInstance("PKCS12");
 			FileInputStream instream = new FileInputStream(new File(p12path));
 			try {
 				keyStore.load(instream, mchid.toCharArray());
 			} finally {
 				instream.close();
 			}
 			// Trust own CA and all self-signed certs
 			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchid.toCharArray()).build();
 			// Allow TLSv1 protocol only
 			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
 					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
 			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
 			HttpPost httpPost = new HttpPost(WX_REFUND);
 			String requestXml = SignUtils.genereateXML(refundReq);
 			logger.info("refund requestXml:" + requestXml);
 			// log.info("企业支付请求：" + requestXml);
 			StringEntity ent = new StringEntity(requestXml);
 			httpPost.setEntity(ent);
 			CloseableHttpResponse response = httpclient.execute(httpPost);
 			String responseXml = SignUtils.genereateXML(response);
 			logger.info("refund responseXml:" + responseXml);
 			Document doc = DocumentHelper.parseText(responseXml);
	        Element rootElement = doc.getRootElement();
	        String return_code = rootElement.elementText("return_code");
	        String return_msg = rootElement.elementText("return_msg");
	        if ("SUCCESS".equals(return_code)) {
	        		String result_code = rootElement.elementText("result_code");
	        		if ("SUCCESS".equals(result_code)) {
	        			// 退款成功, 更新退款信息
	        			String out_refund_no = rootElement.elementText("out_refund_no");
	        			if (!out_refund_no.equals(refundID))
	        			{
	        				// 退款单号不一致
	        				logger.error("refund out_refund_no:" + out_refund_no + " not equal refundID:" + refundID);
	        			}
	        			PinRefund queryRefund = new PinRefund();
	        			queryRefund.setOutRefundNo(refundID);
	        			List<PinRefund> refundList = pinRefundService.findList(queryRefund);
	        			if (refundList.size() == 1) {
						PinRefund r = refundList.get(0);
						String transaction_id = rootElement.elementText("transaction_id");
						//String out_trade_no = rootElement.elementText("out_trade_no");
						String refund_id = rootElement.elementText("refund_id");
						String total_fee = rootElement.elementText("total_fee");
						String refund_fee = rootElement.elementText("refund_fee");
						String settlement_refund_fee = rootElement.elementText("settlement_refund_fee");
						String settlement_total_fee = rootElement.elementText("settlement_total_fee");
						String refund_status = rootElement.elementText("refund_status");
						String success_time = rootElement.elementText("success_time");
						String refund_recv_accout = rootElement.elementText("refund_recv_accout");
						String refund_account = rootElement.elementText("refund_account");
						String refund_request_source = rootElement.elementText("refund_request_source");
	        				r.setSettlementRefundFee(settlement_refund_fee);
	        				r.setSettlementTotalFee(settlement_total_fee);
	        				r.setRefundAccount(refund_account);
	        				r.setRefundFee(refund_fee);
	        				r.setTotalFee(total_fee);
	        				r.setRefundRecvAccout(refund_recv_accout);
	        				r.setRefundRequestSource(refund_request_source);
	        				r.setSuccessTime(success_time);
	        				r.setRefundStatus(refund_status);
	        				r.setRefundId(refund_id);
	        				r.setTransactionId(transaction_id);
	        				
	        				pinRefundService.save(r);
	        				return "SUCCESS";
	        			}
	        		}
	        }
	        
	        // 退款失败
	        response.close();
	        return "RAILED";
	        
 			/*String res = null;
 			try {
 				HttpEntity entity = response.getEntity();
 				if (entity != null) {
 					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
 					String text;
 					StringBuffer sb = new StringBuffer();
 					while ((text = bufferedReader.readLine()) != null) {
 						sb.append(text);
 					}
 					res = sb.toString();
 					logger.info("refund res:" + res);
 				}
 				EntityUtils.consume(entity);
 			} finally {
 				response.close();
 			}*/
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 			logger.info("refund Exception:", e.getMessage());
 			return "RAILED";
 		}
 	}

 	// 获取微信退款签名
 	private void createRefund(RefundRequest refundReq) throws Exception {

 		String sign = "";
 		// 获取签名
 		String refundKey = Global.getConfig("weixin.key");

 		String nonce_str = create_nonce_str();
 		refundReq.setNonce_str(nonce_str);

 		sign = SignUtils.genereateSign(refundReq, refundKey);
 		refundReq.setSign(sign);
 		logger.info("createRefund sign:" + sign);
 		logger.info("createRefund refundReq:" + JSON.toJSONString(refundReq));
 		String requestXml = SignUtils.genereateXML(refundReq);
 		logger.info("createRefund requestXml:" + requestXml);
 	}

 	// 需要的参数appid,mch_id,nonce_str,sign,out_refund_no
 	// 退款查询接口
 	public String refundQuery(String orderID, String transID) {
 		try {
 			RefundRequest refundQueryReq = new RefundRequest();

 			String key = Global.getConfig("weixin.key");
 			String appid = Global.getConfig("weixin.little_appid");
 			String mchid = Global.getConfig("weixin.mchid");

 			refundQueryReq.setAppid(appid);
 			refundQueryReq.setMch_id(mchid);
 			refundQueryReq.setOut_trade_no(orderID); // 商户订单号
 			// out_refund_no":"149150677220180208165118","out_trade_no":"149150677220180208132813
 			refundQueryReq.setOut_refund_no(transID); // 商户退款单号

 			// 查询退款
 			return queryRefund(refundQueryReq);
 			
 		} catch (Exception ex) {
 			ex.printStackTrace();
 			logger.error("refundQuery Exception:" + ex.getMessage());
 		}
 		
 		return null;
 	}

 	// 获取退款查询沙盒签名
 	private String queryRefund(RefundRequest refundQueryReq) throws Exception {

 		String sign = "";
 		// 通过退款获取签名
 		String refundQueryKey = Global.getConfig("weixin.key");

 		String nonce_str = create_nonce_str();
 		refundQueryReq.setNonce_str(nonce_str);

 		sign = SignUtils.genereateSign(refundQueryReq, refundQueryKey);
 		refundQueryReq.setSign(sign);
 		logger.info("createQueryRefund sign:" + sign);
 		logger.info("createQueryRefund refundQueryReq:" + JSON.toJSONString(refundQueryReq));
 		String requestXml = SignUtils.genereateXML(refundQueryReq);
 		logger.info("createQueryRefund requestXml:" + requestXml);
 		String resultXml = HttpRequestUtil.postByStream(WX_REFUND_QUERY, requestXml, null);
 		logger.info("createQueryRefund resultXml:" + resultXml);
 		
 		return resultXml;
 	}
 	
 	public String getWXACode(String scene, String page) {
 		String url = GET_WXACODE_B + getAppToken();
 		String json = "{\"scene\":\"" + scene + "\",\"page\":\"" + page + "\", \"is_hyaline\":true}";
 		
 		String base = Global.getUserfilesBaseDir();
 		String userPath = "static/" + scene + ".jpeg";
 		logger.info("getWXACode url：" + url + ", json:"  + json + ", path:" + userPath);
 		
 		String bigPath = base + "static/" + "groupback.jpeg";
 		String smallPath = base + userPath;
 		
        String response = HttpClientUtil.postHttp(url, json, smallPath);
        String middlePath = base + "static/" + scene + "m.jpeg";
        logger.info("getWXACode smallPath：" + smallPath + ", middlePath:"  + middlePath);
        scale(smallPath, middlePath, 120, 120);
        String overlapPath = base + "static/" + scene + "g.jpeg";
        overlapImage(bigPath, middlePath, overlapPath);
        String codePath = Global.getConfig("systemdomain") + "/static/" + scene + "g.jpeg";
        
		logger.info("getWXACode response:" + overlapPath);
 		return codePath;
 	}
 	
 	public static final void overlapImage(String bigPath, String smallPath, String newPath) {
 	    try {
 	      BufferedImage big = ImageIO.read(new File(bigPath));
 	      BufferedImage small = ImageIO.read(new File(smallPath));
 	      Graphics2D g = big.createGraphics();
 	      int x = 381;
 	      int y = 842;
 	      g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
 	      g.dispose();
 	      ImageIO.write(big, "jpeg", new File(newPath));
 	    } catch (Exception e) {
 	      e.printStackTrace();
 	    }
 	  }
 	
 	/** 
 	 * 图片伸缩，不破坏图片 
 	 *  
 	 * @param srcFile 原图片路径 
 	 * @param dstFile 目标图片路径 
 	 * @param dstWidth 目标宽度 
 	 * @param dstHeight 目标高度 
 	 * @date 2013-11-1 
 	 */  
 	public static void scale(String srcFile, String dstFile, int dstWidth, int dstHeight) {
 	    try {  
 	        ImageInputStream iis = ImageIO.createImageInputStream(new File(srcFile));  
 	  
 	        Iterator<ImageReader> iterator = ImageIO.getImageReaders(iis);  
 	  
 	        ImageReader reader = (ImageReader) iterator.next();  
 	  
 	        reader.setInput(iis, true);  
 	  
 	        BufferedImage source = reader.read(0);  
 	  
 	        BufferedImage tag = new BufferedImage(dstWidth, dstHeight, source.getType());  
 	        tag.getGraphics().drawImage(source, 0, 0, dstWidth, dstHeight, null);  
 	        File file = new File(dstFile);  
 	        ImageIO.write(tag, reader.getFormatName(), file);  
 	    } catch (Exception e) {  
 	        e.printStackTrace();  
 	    }  
 	} 
}
