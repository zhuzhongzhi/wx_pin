package com.jeeplus.modules.api.util;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.pin.entity.PinMember;
import com.jeeplus.modules.pin.entity.PinProduct;
import com.jeeplus.modules.pin.entity.PinRule;
import com.jeeplus.modules.pin.service.PinMemberService;
import com.jeeplus.modules.pin.service.PinProductService;
import com.jeeplus.modules.pin.service.PinRuleService;
import com.jeeplus.modules.sys.utils.AES;

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
public class ProductServiceUtil {

	@Autowired
	private AES aes;
	
	@Autowired
	private PinProductService pinProductService;
	
	@Autowired
	private PinRuleService pinRuleService;
	
	@Autowired
	private IDPoolService idPoolService;
	
	/**
	 * 拼团小程序查询当前售卖的产品以及组团规则
	 * @return
	 * @throws IOException
	 * @throws InvalidAlgorithmParameterException
	 */
	public AjaxJson getProductInfo() {
		AjaxJson result = new AjaxJson();
		PinProduct queryProduct = new PinProduct();
		queryProduct.setStatus("0"); //查询当前售卖的产品
		List<PinProduct> products = pinProductService.findList(queryProduct);
		if (products.isEmpty()) {
			result.setSuccess(false);
			result.setErrorCode("10001");
			result.setMsg("没有售卖的产品");
		}
		PinProduct current = products.get(0);
		result.put("PinProduct", current);
		
		// 查询该产品的拼团规则
		PinRule queryRule = new PinRule();
		queryRule.setProductId(current.getId());
		List<PinRule> rules = pinRuleService.findList(queryRule);
		if (rules.isEmpty()) {
			result.setSuccess(false);
			result.setErrorCode("10002");
			result.setMsg("产品没有拼团规则");
		}
		result.put("PinRule", rules);
		result.setSuccess(true);
		result.setErrorCode("0");
		result.setMsg("查询成功");
		return result;
	}
}
