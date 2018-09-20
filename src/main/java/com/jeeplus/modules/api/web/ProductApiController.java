package com.jeeplus.modules.api.web;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.api.util.ProductServiceUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fusheng on 17/11/30.
 */
@Controller
@RequestMapping(value = "/productApi")
public class ProductApiController extends BaseController {

	@Autowired
	private ProductServiceUtil productServiceUtil;

	/**
	 * 拼团小程序查询拼团的产品相关信息，以及拼团的规则等信息
	 * 
	 * @param request
	 * @param appId
	 *            商户接口AppId
	 * @return
	 */
	@RequestMapping(value = "getProductInfo")
	@ResponseBody
	public AjaxJson getProductInfo(HttpServletRequest request) {
		
		return productServiceUtil.getProductInfo();
	}

}
