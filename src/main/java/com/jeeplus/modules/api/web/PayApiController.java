package com.jeeplus.modules.api.web;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.api.util.HttpRequestUtil;
import com.jeeplus.modules.api.util.IDPoolService;
import com.jeeplus.modules.api.util.MemberServiceUtil;
import com.jeeplus.modules.api.util.ProductServiceUtil;
import com.jeeplus.modules.api.util.WeixinServiceUtil;
import com.jeeplus.modules.pin.entity.PinMember;
import com.jeeplus.modules.pin.entity.PinOrder;
import com.jeeplus.modules.pin.entity.PinRefund;
import com.jeeplus.modules.pin.service.PinOrderService;
import com.jeeplus.modules.pin.service.PinRefundService;
import com.jeeplus.modules.sys.entity.Log;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.LogUtils;
import com.jeeplus.modules.sys.utils.MD5;
import com.jeeplus.modules.sys.utils.SignUtils;
import com.jeeplus.modules.weixin.entity.UnifiedOrderRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fusheng on 17/11/30.
 */
@Controller
@RequestMapping(value = "/payApi")
public class PayApiController extends BaseController {

	@Autowired
	private WeixinServiceUtil weixinServiceUtil;
	
	@Autowired
	private IDPoolService idPoolService;

	@Autowired
	private MemberServiceUtil memberServiceUtil;
	
	@Autowired
	private PinOrderService pinOrderService;
	
	@Autowired
    PinRefundService pinRefundService;
	
	/**
     * 页面取消订单
     * 
     */
    @RequestMapping(value = "/cancleOrder", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson cancleOrder(
            String orderId,
            HttpServletRequest request, HttpServletResponse response) {
    		logger.info("cancleOrder orderId:" + orderId);
	    AjaxJson result = new AjaxJson();
	    PinOrder pinOrder = pinOrderService.get(orderId);
	    if (pinOrder == null) {
	    		logger.info("cancleOrder 订单信息不存在 orderId:" + orderId);
	    		result.setSuccess(false);
	    		result.setErrorCode("1007");
	    		result.setMsg("订单信息不存在");
	    }
	    
	    pinOrder.setStatus("3");
	    pinOrder.setEndTime(new Date());
	    pinOrderService.save(pinOrder);
	    
	    result.setSuccess(true);
		result.setErrorCode("0");
		result.setMsg("取消订单成功");
		result.put("PinOrder", pinOrder);
		
	    return result;
    }
	/**
     * 微信统一下单接口，
     * 得到的结果用于发起微信JS支付接口
     */
    @RequestMapping(value = "/appUnifiedorder", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson appUnifiedorder(
            Integer totalFee,
            String memberId,
            String groupId,
            HttpServletRequest request, HttpServletResponse response) {
    	    logger.info("unifiedorder memberId:" + memberId + ",totalFee:" + totalFee);
    	    AjaxJson result = new AjaxJson();
    	    
    	    PinMember member = memberServiceUtil.getMemberbyId(memberId);
    	    if (member == null) {
    	    		logger.info("unifiedorder memberId:" + memberId + " can not find member in DB.");
    	    		result.setSuccess(false);
    	    		result.setErrorCode("1005");
    	    		result.setMsg("memberId:" + memberId + " can not find member in DB.");
    	    		return result;
    	    }
    	    PinOrder  pinOrder = new PinOrder();
    	    pinOrder.setGroupId(groupId);
    	    pinOrder.setMemberId(memberId);
    	    pinOrder.setFee(String.valueOf(totalFee));
    	    pinOrder.setStatus("0");
    	    pinOrder.setCreateTime(new Date());
    	    pinOrderService.save(pinOrder);
    	    
    	    result.put("PinOrder", pinOrder);
    	    
    	    logger.info("unifiedorder create order:" + pinOrder.getId());
    	    
        try {
            UnifiedOrderRequest preOrderReq = new UnifiedOrderRequest();
            // 小程序appid
            preOrderReq.setAppid(Global.getConfig("weixin.little_appid"));
            // 商户id
            preOrderReq.setMch_id(Global.getConfig("weixin.mchid"));
            // 支付回调地址
            preOrderReq.setNotify_url(Global.getConfig("systemdomain") + "/payApi/paynotify");
            
            // 订单描述
            preOrderReq.setBody("光明牛奶");
            
            // 网页支付提交用户端ip
            preOrderReq.setSpbill_create_ip(request.getRemoteAddr());
            
            // 我们系统的订单号
            preOrderReq.setOut_trade_no(pinOrder.getId());
            
            // 总费用，单位：分
            preOrderReq.setTotal_fee(totalFee);
            
            // openid
            preOrderReq.setOpenid(member.getOpenid());

            HashMap<String, String> map = weixinServiceUtil.createUnifiedOrder(preOrderReq);
            
            result.setSuccess(true);
	    		result.setErrorCode("0");
	    		result.setMsg("memberId:" + memberId + " can not find member in DB.");
	    		result.put("PayInfo", map);

        } catch (Exception ex) {
            ex.printStackTrace();
            result.setSuccess(false);
	    		result.setErrorCode("1006");
	    		result.setMsg("createUnifiedOrder Exception:" + ex.getMessage());
        }
        
        return result;
    }
    
    /**
     * 微信支付异步通知
     */
    @RequestMapping(value = "/paynotify", method = {RequestMethod.GET, RequestMethod.POST})
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        logger.info("payNotify start RemoteAddr:" + request.getRemoteAddr());
        InputStream is = null;
        // 处理通知之后的业务处理
        Boolean flag = false;
        try {
            is = request.getInputStream();
            String resultxml = HttpRequestUtil.inputStreamTOString(is);
            logger.info("payNotify resultxml:" + resultxml);

            /*
             *
             * <xml>
              <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
              <attach><![CDATA[支付测试]]></attach>
              <bank_type><![CDATA[CFT]]></bank_type>
              <fee_type><![CDATA[CNY]]></fee_type>
              <is_subscribe><![CDATA[Y]]></is_subscribe>
              <mch_id><![CDATA[10000100]]></mch_id>
              <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>
              <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>
              <out_trade_no><![CDATA[1409811653]]></out_trade_no>
              <result_code><![CDATA[SUCCESS]]></result_code>
              <return_code><![CDATA[SUCCESS]]></return_code>
              <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>
              <sub_mch_id><![CDATA[10000100]]></sub_mch_id>
              <time_end><![CDATA[20140903131540]]></time_end>
              <total_fee>1</total_fee>
              <trade_type><![CDATA[JSAPI]]></trade_type>
              <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>
            </xml>
             */
            // 获取商户id
            Document doc = DocumentHelper.parseText(resultxml);
            Element rootElement = doc.getRootElement();
            String return_code = rootElement.elementText("return_code");
            String result_code = rootElement.elementText("result_code");

            logger.info("payNotify return_code:" + return_code + ",result_code：" + result_code);

            if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
                String outOrderNo = rootElement.elementText("out_trade_no");
                String trade_no = rootElement.elementText("transaction_id");

                String key = Global.getConfig("weixin.key");
                logger.info("payNotify success：out_trade_no:" + outOrderNo + ",trade_no：" + trade_no);
                boolean signIsRight = SignUtils.checkSign(resultxml, key);
                logger.info("payNotify signIsRight:" + signIsRight);
                if (signIsRight) {
                    // 获得对象信息
	                	PinOrder  pinOrder = pinOrderService.get(outOrderNo);
	            	    pinOrder.setEndTime(new Date());
	            	    pinOrder.setTradeNo(trade_no);
	            	    pinOrder.setStatus("1"); //  已经支付完成
	            	    pinOrderService.save(pinOrder);//更新
	            	    logger.info("payNotify update order:" + pinOrder);
	            	    
	            	    // 其他等超时处理吧
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("payNotify 处理通知异常：" + e.getMessage());            
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info(e.getMessage());
            }

            PrintWriter out = null;
            // 响应微信支付服务器
            try {
                out = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (out != null) {
                if (flag) {
                    out.write(
                            "<xml>" +
                                    "<return_code><![CDATA[SUCCESS]]></return_code>" +
                                    "<return_msg><![CDATA[OK]]></return_msg>" +
                                    "</xml>");
                } else {
                    out.write(
                            "<xml>" +
                                    "<return_code><![CDATA[FAIL]]></return_code>" +
                                    "<return_msg><![CDATA[]]></return_msg>" +
                                    "</xml>");
                }

                out.flush();
                out.close();
            }
        }
        logger.info("payNotify end RemoteAddr:" + request.getRemoteAddr());
    }
    
    /**
     * 微信支付异步通知
     */
    @RequestMapping(value = "/refundnotify", method = {RequestMethod.GET, RequestMethod.POST})
    public void refundNotify(HttpServletRequest request, HttpServletResponse response) {
        logger.info("refundNotify start RemoteAddr:" + request.getRemoteAddr());
        /*<xml>
           <return_code>SUCCESS</return_code>
           <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
           <mch_id><![CDATA[10000100]]></mch_id>
           <nonce_str><![CDATA[TeqClE3i0mvn3DrK]]></nonce_str>
           <req_info><![CDATA[T87GAHG17TGAHG1TGHAHAHA1Y1CIOA9UGJH1GAHV871HAGAGQYQQPOOJMXNBCXBVNMNMAJAA]]></req_info>
        </xml>*/
        
        InputStream is = null;
        // 处理通知之后的业务处理
        Boolean flag = false;
        try {
            is = request.getInputStream();
            String resultxml = HttpRequestUtil.inputStreamTOString(is);
            logger.info("refundNotify resultxml:" + resultxml);
            Document doc = DocumentHelper.parseText(resultxml);
            Element rootElement = doc.getRootElement();
            String return_code = rootElement.elementText("return_code");

            logger.info("refundNotify return_code:" + return_code);

            if ("SUCCESS".equals(return_code)) {
                String appid = rootElement.elementText("appid");
                String mch_id = rootElement.elementText("mch_id");
                String nonce_str = rootElement.elementText("nonce_str");
                String req_info = rootElement.elementText("req_info");
                
                String key = Global.getConfig("weixin.key");
                String md5Key = MD5.MD5Encode(key).toLowerCase();
                SecretKeySpec key5 = new SecretKeySpec(md5Key.getBytes(), "AES");
                logger.info("md5 lower case key:" + md5Key);
                
                logger.info("refundNotify success：appid:" + appid + ",mch_id：" + mch_id + ",req_info:" + req_info);
                
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] content = decoder.decode(req_info);
        			
        			
        			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");  
        	        // 初始化  
        			cipher.init(Cipher.DECRYPT_MODE, key5);
        	    
        			byte[] result = cipher.doFinal(content);
        			String req_info_dec = new String(result);
        			logger.info("req_info base64 decode:" + req_info_dec);
        			/*
        			<root>
					<out_refund_no><![CDATA[refunde8b9e0f36e59499183afcce517ab664d]]></out_refund_no>
					<out_trade_no><![CDATA[e8b9e0f36e59499183afcce517ab664d]]></out_trade_no>
					<refund_account><![CDATA[REFUND_SOURCE_RECHARGE_FUNDS]]></refund_account>
					<refund_fee><![CDATA[1]]></refund_fee>
					<refund_id><![CDATA[50000606722018052704817141900]]></refund_id>
					<refund_recv_accout><![CDATA[支付用户零钱]]></refund_recv_accout>
					<refund_request_source><![CDATA[API]]></refund_request_source>
					<refund_status><![CDATA[SUCCESS]]></refund_status>
					<settlement_refund_fee><![CDATA[1]]></settlement_refund_fee>
					<settlement_total_fee><![CDATA[1]]></settlement_total_fee>
					<success_time><![CDATA[2018-05-27 20:30:05]]></success_time>
					<total_fee><![CDATA[1]]></total_fee>
					<transaction_id><![CDATA[4200000115201805272793767366]]></transaction_id>
				</root>
        			*/
        			String refundXML= req_info_dec.replace("root>", "xml>"); // 替换成xml格式后再解析
        			logger.info("req_info refundXML:" + refundXML);
        			
        		    Document info = DocumentHelper.parseText(refundXML);
                Element infoElement = info.getRootElement();
                String transaction_id = infoElement.elementText("transaction_id");
                String out_trade_no = infoElement.elementText("out_trade_no");
                String refund_id = infoElement.elementText("refund_id");
                String out_refund_no = infoElement.elementText("out_refund_no");
                String total_fee = infoElement.elementText("total_fee");
                String refund_fee = infoElement.elementText("refund_fee");
                String refund_status = infoElement.elementText("refund_status");
                String success_time = infoElement.elementText("success_time");
                String refund_recv_accout = infoElement.elementText("refund_recv_accout");
                String refund_account = infoElement.elementText("refund_account");
                String refund_request_source = infoElement.elementText("refund_request_source");
                String settlement_refund_fee = infoElement.elementText("settlement_refund_fee");
				String settlement_total_fee = infoElement.elementText("settlement_total_fee");
				
				logger.info("req_info out_trade_no:" + out_trade_no + ", out_refund_no:" + out_refund_no);
				
                PinRefund queryRefund = new PinRefund();
	    			queryRefund.setOutRefundNo(out_refund_no);
	    			List<PinRefund> refundList = pinRefundService.findList(queryRefund);
	    			if (refundList.size() == 1) {
					PinRefund r = refundList.get(0);
					logger.info("req_info PinRefund:" + r);
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
	    				
	    				// 修改order状态
	    				PinOrder queryOrder = new PinOrder();
	    				queryOrder.setId(r.getOutTradeNo());
	    				List<PinOrder> orders = pinOrderService.findList(queryOrder);
	    				if (!orders.isEmpty()) {
	    					PinOrder o = orders.get(0);
	    					
	    					// 设置状态为已经退款
	    					o.setStatus("4");
	    					pinOrderService.save(o);
	    				}
	    			}
    			
            } else {
            		// 失败
            		String return_msg = rootElement.elementText("return_msg");
            		logger.info("return_code is not success. refund failed. return_msg:" + return_msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("refundNotify 处理通知异常：" + e.getMessage());            
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info(e.getMessage());
            }

            PrintWriter out = null;
            // 响应微信支付服务器
            try {
                out = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (out != null) {
                if (flag) {
                    out.write(
                            "<xml>" +
                            		"<return_code><![CDATA[SUCCESS]]></return_code>" +
                            		"<return_msg><![CDATA[OK]]></return_msg>" +
                            "</xml>");
                } else {
                    out.write(
                            "<xml>" +
                            		"<return_code><![CDATA[FAIL]]></return_code>" +
                            		"<return_msg><![CDATA[]]></return_msg>" +
                            "</xml>");
                }

                out.flush();
                out.close();
            }
        }
        logger.info("payNotify end RemoteAddr:" + request.getRemoteAddr());
    }
}
