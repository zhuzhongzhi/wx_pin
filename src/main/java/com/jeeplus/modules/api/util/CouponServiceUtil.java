package com.jeeplus.modules.api.util;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.pin.entity.PinMember;
import com.jeeplus.modules.pin.entity.PinProduct;
import com.jeeplus.modules.pin.entity.PinRule;
import com.jeeplus.modules.pin.entity.PinWxCoupon;
import com.jeeplus.modules.pin.service.PinMemberService;
import com.jeeplus.modules.pin.service.PinProductService;
import com.jeeplus.modules.pin.service.PinRuleService;
import com.jeeplus.modules.pin.service.PinWxCouponService;
import com.jeeplus.modules.sys.utils.AES;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fusheng on 17/11/30.
 */
@Service
public class CouponServiceUtil {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AES aes;
	
	@Autowired
	private PinWxCouponService pinWxCouponService;
	
	
	/**
	 * 查询满足条件的的面值最大的优惠券
	 * @return
	 * @throws IOException
	 * @throws InvalidAlgorithmParameterException
	 */
	public AjaxJson getCouponInfo(Integer price, String productId) {
		AjaxJson result = new AjaxJson();
		logger.info("getCouponInfo price:" + price + ", productId:" + productId);
		
		String sql = "select a.id as \"id\" from pin_wx_coupon a where (a.condition<=" + price + " OR a.condition=0) AND (NOW() between a.start_time and a.end_time) ORDER BY a.face_value DESC limit 1";
		List<Object> coupons = pinWxCouponService.executeSelectSql(sql);
		if ((coupons == null) || (coupons.isEmpty())) {
			result.setSuccess(false);
			result.setErrorCode("1004");
			result.setMsg("没有合适的优惠券");
		}
		
		String couponId = (String)coupons.get(0);
		logger.info("getCouponInfo o:" + couponId);
		PinWxCoupon coupon = pinWxCouponService.get(couponId);
		result.setSuccess(true);
		result.setErrorCode("0");
		result.setMsg("查询成功");
		result.put("PinWxCoupon", coupon);
		
		//logger.info("getCouponInfo PinWxCoupon id:" + cp.getId());
		
		return result;
	}
}
