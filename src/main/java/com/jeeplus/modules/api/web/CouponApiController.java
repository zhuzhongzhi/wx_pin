package com.jeeplus.modules.api.web;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.api.util.CouponServiceUtil;
import com.jeeplus.modules.api.util.IDPoolService;
import com.jeeplus.modules.api.util.MemberServiceUtil;
import com.jeeplus.modules.api.util.ProductServiceUtil;
import com.jeeplus.modules.api.util.WeixinServiceUtil;
import com.jeeplus.modules.pin.entity.PinMember;
import com.jeeplus.modules.pin.service.PinMemberService;
import com.jeeplus.modules.sys.entity.Log;
import com.jeeplus.modules.sys.utils.LogUtils;
import com.jeeplus.modules.sys.utils.SHA256Util;
import com.jeeplus.modules.sys.utils.SignUtil;
import com.jeeplus.modules.sys.utils.SignUtils;
import com.jeeplus.modules.weixin.entity.CardExt;
import com.jeeplus.modules.weixin.entity.SendCouponRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fusheng on 17/11/30.
 */
@Controller
@RequestMapping(value = "/CouponApi")
public class CouponApiController extends BaseController {

	@Autowired
	private CouponServiceUtil productServiceUtil;
	
	@Autowired
	private WeixinServiceUtil weixinServiceUtil;
	
	@Autowired
	private PinMemberService pinMemberService;
	
	@Autowired
	private IDPoolService idPoolService;
	
	/**
	 * 查询满足条件的的面值最大的优惠券
	 * 
	 * @param request
	 * @param appId
	 *            商户接口AppId
	 * @return
	 */
	@RequestMapping(value = "getCouponInfo")
	@ResponseBody
	public AjaxJson getCouponInfo(HttpServletRequest request,
								 @RequestParam("price") Integer price,
								 @RequestParam("productId") String productId) {
		
		return productServiceUtil.getCouponInfo(price, productId);
	}
	
	/**
	 * 发放优惠券
	 * 
	 * @param request
	 * @param stockId 批次
	 * @param memberId 会员id       
	 * @return
	 */
	@RequestMapping(value = "sendCoupon")
	@ResponseBody
	public AjaxJson sendCoupon(HttpServletRequest request,
							  @RequestParam("stockId") String stockId,
							  @RequestParam("memberId") String memberId) {
		AjaxJson result = new AjaxJson();
		
		PinMember member = pinMemberService.get(memberId);
		if (member == null) {
			result.setSuccess(false);
			result.setErrorCode("1010");
			result.setMsg("用户不存在 memberId:" + memberId);
			return result;
		}
		
		SendCouponRequest preReqParamsModel = new SendCouponRequest();
		// 小程序appid
		preReqParamsModel.setAppid(Global.getConfig("weixin.little_appid"));
		// 商户id
		preReqParamsModel.setMch_id(Global.getConfig("weixin.mchid"));
		// openid
		preReqParamsModel.setOpenid(member.getOpenid());
		preReqParamsModel.setOpenid_count("1");
		preReqParamsModel.setPartner_trade_no(idPoolService.generateOrderId());
		preReqParamsModel.setCoupon_stock_id(stockId);
		preReqParamsModel.setOp_user_id(Global.getConfig("weixin.mchid"));
		preReqParamsModel.setVersion("1.0");
		preReqParamsModel.setType("XML");
				
		try {
			String resultinfo = weixinServiceUtil.sendCouponToWX(preReqParamsModel);
			if (resultinfo == null) {
				result.setSuccess(false);
				result.setErrorCode("10011");
				result.setMsg("优惠券发送失败");
			} else {
				result.setSuccess(true);
				result.setErrorCode("0");
				result.setMsg("优惠券发送成功");
				result.put("CouponInfo", resultinfo);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			result.setSuccess(false);
			result.setErrorCode("1009");
			result.setMsg("sendCouponToWX Exception:" + e.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 优惠券扩展参数
	 * 
	 * @param request
	 * @param memberId 会员id       
	 * @return
	 */
	@RequestMapping(value = "getCardExt")
	@ResponseBody
	public AjaxJson getCardExt(HttpServletRequest request,
			                  @RequestParam("cardId") String cardId,
			                  @RequestParam("groupId") String groupId,
							  @RequestParam("memberId") String memberId) {
		AjaxJson result = new AjaxJson();
		logger.info("getCardExt cardId:" + cardId + ", groupId:" + groupId + ", memberId:" + memberId);
		
		PinMember member = pinMemberService.get(memberId);
		if (member == null) {
			result.setSuccess(false);
			result.setErrorCode("1010");
			result.setMsg("用户不存在 memberId:" + memberId);
			return result;
		}
		
		CardExt cardExt = new CardExt();
		cardExt.setCode(WeixinServiceUtil.create_code());
		cardExt.setOpenid(member.getOpenid());
		cardExt.setNonce_str(WeixinServiceUtil.create_nonce_str());
		cardExt.setTimestamp(WeixinServiceUtil.create_timestamp());
		Map<String, String> map = new TreeMap<String, String>();
		map.put(cardExt.getCode(),"code");
		map.put(cardExt.getOpenid(),"openid");
		map.put(cardExt.getTimestamp(), "timestamp");
		map.put(cardExt.getNonce_str(), "nonce_str");
		map.put(cardId, "card_id");
		String api_ticket = WeixinServiceUtil.getTicketFromWeixin();
		map.put(api_ticket, "api_ticket");
		Set<Map.Entry<String, String>> set = map.entrySet();
		StringBuffer sb = new StringBuffer();
		Iterator<Map.Entry<String, String>> it = set.iterator();
		sb.append((it.next()).getKey());
		// 取出排序后的参数，逐一连接起来
		for (Iterator<Map.Entry<String, String>> item = it; item.hasNext();) {
			Map.Entry<String, String> me = item.next();
			sb.append("" + me.getKey());
		}
		String signTemp =  sb.toString();
		
		try {  
            //指定sha1算法  
            MessageDigest digest = MessageDigest.getInstance("SHA-1");  
            digest.update(signTemp.getBytes());  
            //获取字节数组  
            byte messageDigest[] = digest.digest();  
            // Create Hex String  
            StringBuffer hexString = new StringBuffer();  
            // 字节数组转换为 十六进制 数  
            for (int i = 0; i < messageDigest.length; i++) {  
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);  
                if (shaHex.length() < 2) {  
                    hexString.append(0);  
                }  
                hexString.append(shaHex);  
            }  
            String sign = hexString.toString().toLowerCase();  
            cardExt.setSignature(sign);
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
            logger.info("getCardExt NoSuchAlgorithmException:" + e.getMessage());
        } 
		
		logger.info("getCardExt signTemp:"+ signTemp + ", api_ticket:" + api_ticket + ", Signature:" + cardExt.getSignature());
		result.setSuccess(true);
		result.setErrorCode("0");
		result.setMsg("操作成功");
		result.put("cardExt", cardExt);
		logger.info("getCardExt cardExt:" + cardExt);
		
		return result;
	}
}
