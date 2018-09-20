package com.jeeplus.modules.api.web;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.api.util.GroupServiceUtil;
import com.jeeplus.modules.api.util.MemberServiceUtil;
import com.jeeplus.modules.api.util.ProductServiceUtil;
import com.jeeplus.modules.api.util.WeixinServiceUtil;
import com.jeeplus.modules.pin.entity.PinGroup;
import com.jeeplus.modules.pin.entity.PinGroupInfo;
import com.jeeplus.modules.pin.service.PinGroupService;
import com.jeeplus.modules.sys.entity.Log;
import com.jeeplus.modules.sys.utils.LogUtils;

import freemarker.template.utility.StringUtil;

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
@RequestMapping(value = "/groupApi")
public class GroupApiController extends BaseController {

	@Autowired
	private GroupServiceUtil groupServiceUtil;
	
	@Autowired
	private WeixinServiceUtil weixinServiceUtil;
	
	@Autowired
	private PinGroupService pinGroupService;
	/**
	 * 查询我参与的团信息
	 * 
	 * @param request
	 * @param memberId
	 *            用户ID
	 * @param productId
	 *            团的产品ID
	 * @return
	 */
	@RequestMapping(value = "getMyGroup")
	@ResponseBody
	public AjaxJson getMyGroup(HttpServletRequest request, 
			                  @RequestParam("memberId") String memberId, 
			                  @RequestParam("productId") String productId) {
		
		return groupServiceUtil.getMyGroup(memberId, productId);
	}
	
	/**
	 * 查询我可以参与的团信息
	 * 
	 * @param request
	 * @param productId
	 *            团的产品ID
	 * @return
	 */
	@RequestMapping(value = "getCanJoinGroup")
	@ResponseBody
	public AjaxJson getCanJoinGroup(HttpServletRequest request, 
			                        @RequestParam("memberId") String memberId,
			                        @RequestParam("productId") String productId) {
		
		return groupServiceUtil.getCanJoinGroup(memberId, productId);
	}
	
	/**
	 * 查询指定id的团信息
	 * 
	 * @param request
	 * @param productId
	 *            团的产品ID
	 * @return
	 */
	@RequestMapping(value = "getGroupInfo")
	@ResponseBody
	public AjaxJson getGroupInfo(HttpServletRequest request,
			                        @RequestParam("groupId") String groupId) {
		
		return groupServiceUtil.getGroupInfo(groupId);
	}
	
	/**
	 * 加入某个团
	 * 
	 * @param request
	 * @param memberId
	 *            用户ID
	 * @param groupId
	 *            团ID
	 * @return
	 */
	@RequestMapping(value = "joinGroup")
	@ResponseBody
	public AjaxJson joinGroup(HttpServletRequest request, 
			                 @RequestParam("orderId") String orderId,
							 @RequestParam("memberId") String memberId,
			                 @RequestParam("groupId") String groupId,
			                 @RequestParam("recvName") String recvName,
			                  @RequestParam("recvPhone") String recvPhone,
			                  @RequestParam("province") String province,
			                  @RequestParam("city") String city,
			                  @RequestParam("district") String district,
			                  @RequestParam("address") String address,
			                  @RequestParam("deliverType") String deliverType) {
		
		return groupServiceUtil.joinGroup(orderId, memberId, groupId, recvName, recvPhone, province, city, district, address, deliverType);
	}
	
	/**
	 * 开团
	 * 
	 * @param request
	 * @param memberId
	 *            用户ID
	 * @param ruleId
	 *            团规则ID
	 * @return
	 */
		@RequestMapping(value = "createGroup")
		@ResponseBody
		public AjaxJson createGroup(HttpServletRequest request, 
				                  @RequestParam("orderId") String orderId,
								  @RequestParam("memberId") String memberId,
				                  @RequestParam("ruleId") String ruleId,
				                  @RequestParam("recvName") String recvName,
				                  @RequestParam("recvPhone") String recvPhone,
				                  @RequestParam("province") String province,
				                  @RequestParam("city") String city,
				                  @RequestParam("district") String district,
				                  @RequestParam("address") String address,
				                  @RequestParam("deliverType") String deliverType) {
		
		return groupServiceUtil.createGroup(orderId, memberId, ruleId, recvName, recvPhone, province, city, district, address, deliverType);
	}
	
	/**
	 * 分享团二维码
	 * 
	 * @param request
	 * @param memberId
	 *            用户ID
	 * @param memberId
	 *            分享人id
	 * @return
	 */
	@RequestMapping(value = "share")
	@ResponseBody
	public AjaxJson share(HttpServletRequest request, 
					      @RequestParam("groupId") String groupId,
			              @RequestParam("memberId") String memberId) {
		AjaxJson result = new AjaxJson();
		String sence = "groupId=" + groupId;
		if (groupId.length() > 32) {
			result.setSuccess(false);
			result.setErrorCode("10011");
			result.setMsg("error sence:[" + groupId + "] length > 32.");
			return result;
		}
		
		String wxacode = null;
		PinGroup group = pinGroupService.get(groupId);
		if (group == null) {
			result.setSuccess(false);
			result.setErrorCode("10011");
			result.setMsg("团" + groupId + "不存在");
			return result;
		}
		wxacode = group.getWxacode();
		if ((wxacode != null) && (!wxacode.equals("")))   {
			logger.info("share sence:" + sence + ", db wxacode:" + wxacode);
		} else {
			wxacode = weixinServiceUtil.getWXACode(groupId, Global.getConfig("group.little_app_path"));
			logger.info("share sence:" + sence + ", wxacode:" + wxacode);
			group.setWxacode(wxacode);
			pinGroupService.save(group);
		}
	    
	    result.setSuccess(true);
		result.setErrorCode("0");
		result.setMsg("二维码成功");
		result.put("wxacode", wxacode); 
		
	    return result;
	}
			                  
}
