package com.jeeplus.modules.api.util;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.pin.entity.PinGroupMember;
import com.jeeplus.modules.pin.entity.PinMember;
import com.jeeplus.modules.pin.entity.PinOrder;
import com.jeeplus.modules.pin.entity.PinProduct;
import com.jeeplus.modules.pin.entity.PinRefund;
import com.jeeplus.modules.pin.entity.PinRule;
import com.jeeplus.modules.pin.entity.PinDeliverInfo;
import com.jeeplus.modules.pin.entity.PinGroup;
import com.jeeplus.modules.pin.entity.PinGroupInfo;
import com.jeeplus.modules.pin.service.PinDeliverInfoService;
import com.jeeplus.modules.pin.service.PinGroupMemberService;
import com.jeeplus.modules.pin.service.PinGroupService;
import com.jeeplus.modules.pin.service.PinMemberService;
import com.jeeplus.modules.pin.service.PinOrderService;
import com.jeeplus.modules.pin.service.PinProductService;
import com.jeeplus.modules.pin.service.PinRefundService;
import com.jeeplus.modules.pin.service.PinRuleService;
import com.jeeplus.modules.sys.utils.AES;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by fusheng on 17/11/30.
 */
@Service
public class GroupServiceUtil {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AES aes;
	
	@Autowired
	private PinGroupMemberService pinGroupMemberService;
	
	@Autowired
	private PinProductService pinProductService;
	
	@Autowired
	private PinMemberService pinMemberService;
	
	@Autowired
	private PinGroupService pinGroupService;
	
	@Autowired
	private PinRuleService pinRuleService;
	
	@Autowired
	private WeixinServiceUtil weixinServiceUtil;
	
	@Autowired
	private PinDeliverInfoService pinDeliverInfoService;
	
	@Autowired
	private PinOrderService pinOrderService;
	
	@Autowired
	private PinRefundService pinRefundService;
	
	@Autowired
	private IDPoolService idPoolService;
	
	public AjaxJson getMyGroup(String memberId, 
             				   String productId) {
		
		logger.info("getMyGroup productId:" + productId + ", memberId:" + memberId);
		AjaxJson result = new AjaxJson();
		
		PinGroupMember relation = new PinGroupMember();
		relation.setMemberId(memberId);
		
		List<PinGroupInfo> groupList = new ArrayList<PinGroupInfo>();
		List<PinGroupMember> groups = pinGroupMemberService.findList(relation);
		for (PinGroupMember r:groups) {
			PinGroupInfo info = getPinGroupInfo(r.getGroupId());
			if (info == null) {
				logger.info("getMyGroup group:" + r.getGroupId() + " can not get PinGroupInfo.");
			} else {
				groupList.add(info);
			}
		}
		
		result.setSuccess(true);
		result.setErrorCode("0");
		result.setMsg("查询成功");
		result.put("PinGroups", groupList);
		return result;
	}
	
	public AjaxJson getCanJoinGroup(String memberId, String productId) {
		logger.info("getCanJoinGroup productId:" + productId + ", memberId:" + memberId);
		AjaxJson result = new AjaxJson();
		PinGroup query = new PinGroup();
		query.setProductId(productId);
		query.setStatus("0");		
		List<PinGroup> groupList = pinGroupService.findList(query);
		
		List<PinGroupInfo> resultList = new ArrayList<PinGroupInfo>();
		for (PinGroup g:groupList) {
			PinGroupInfo info = getPinGroupInfo(g.getId());
			if (info == null) {
				logger.info("getCanJoinGroup group:" + g.getId() + " can not get PinGroupInfo.");
			} else {
				resultList.add(info);
			}
		}
		result.setSuccess(true);
		result.setErrorCode("0");
		result.setMsg("查询成功");
		result.put("PinGroups", resultList);
		logger.info("getCanJoinGroup success group size:" + resultList.size());
		return result;
	}
	
	public AjaxJson joinGroup(String orderId,
			                  String memberId,
            					  String groupId,
            					  String recvName,
            					  String recvPhone,
            					  String province,
            					  String city,
            					  String district,
            					  String address,
            					  String deliverType) {
		logger.info("joinGroup memberId:" + memberId + ", groupId:" + groupId);
		// 进来之前应该是已经成功付款了
		AjaxJson result = new AjaxJson();
		// 防止出现团超员的情况
		boolean actResult = false;
		PinGroup group = null;
		synchronized(this)
		{
			group = pinGroupService.get(groupId);
			if (group.getMasterId().equals(memberId)) {
				logger.info("joinGroup master id:" + group.getMasterId() + " is same as memberId.");
				result.setSuccess(false);
				result.setErrorCode("10002");
				result.setMsg("你是团主，不能再次加入这个团。");
			} else {
				Integer member = Integer.valueOf(group.getMember());
				Integer total = Integer.valueOf(group.getTotal());
				if (member >= total) {
					logger.info("joinGroup groupId:" + groupId + ", member:" + member + ",total:" + total + " group is full.");
					result.setSuccess(false);
					result.setErrorCode("19002");
					result.setMsg("您手慢了，团已经满了。");
				} else {
					member += 1;
					group.setMember(Integer.toString(member));
					if (member.equals(total))
					{
						group.setEndTime(new Date());
						group.setStatus(PinGroup.STATUS_OK);
						// 需要通知发货了
						logger.info("joinGroup group:" + group.getId() + " is complete.");
					}
					pinGroupService.save(group);
					actResult = true;
				}
			}
		}
				
		if (!actResult) {
			// 用户退款
			logger.info("joinGroup group:" + groupId + " failed. refund for user order.");
			
			// 查询订单
			PinOrder order = pinOrderService.get(orderId);
			// 只对付款成功的订单进行退款
			if (order.getStatus().equals("1")) {
				PinRefund refund = new PinRefund();
				refund.setGroupId(groupId);
				refund.setMemberId(order.getMemberId());
				refund.setOutTradeNo(order.getId()); //商户订单号
				refund.setTransactionId(order.getTradeNo());//微信订单号
				refund.setTotalFee(order.getFee()); //订单金额
				refund.setRefundFee(order.getFee()); //申请退款金额
				String outRefundNo = "refund" + order.getId(); //商户退款单号
				refund.setOutRefundNo(outRefundNo);
				refund.setRefundStatus("begin refund"); //退款状态
			
				// 保存退款信息
				pinRefundService.save(refund);
				Integer fee = Integer.valueOf(order.getFee());
				weixinServiceUtil.refund(order.getId(), outRefundNo, fee, fee);
				
				// 订单设置为已经退款
				order.setStatus("4");
				order.setGroupId(groupId);
				pinOrderService.save(order);
				
				//  删除递送信息
				// 这里还没有加入递送，所以不用删除
			}
			return result;
		}
		
		result.setSuccess(true);
		result.setErrorCode("0");
		result.setMsg("加入成功");
		result.put("PinGroup", group); // 用了刷新团信息，新加入了一人
		
		// 更新order信息
		PinOrder order = pinOrderService.get(orderId);
		order.setGroupId(group.getId());
		pinOrderService.save(order);
				
		// 加入关系
		PinGroupMember relation = new PinGroupMember();
		relation.setMemberId(memberId);
		relation.setGroupId(groupId);
		pinGroupMemberService.save(relation);
		logger.info("joinGroup add PinGroupMember.");

		//添加递送信息
		PinDeliverInfo pinDeliverInfo = new PinDeliverInfo();
		pinDeliverInfo.setCreateTime(new Date());
		pinDeliverInfo.setDeliverType(deliverType);
		pinDeliverInfo.setRecvPhone(recvPhone);
		pinDeliverInfo.setRecvName(recvName);
		pinDeliverInfo.setProvince(province);
		pinDeliverInfo.setCity(city);
		pinDeliverInfo.setDistrict(district);
		pinDeliverInfo.setAddress(address);
		pinDeliverInfo.setStatus("0");
		pinDeliverInfo.setGroupId(groupId);
		pinDeliverInfo.setMemberId(memberId);
		pinDeliverInfoService.save(pinDeliverInfo);
		
		result.put("PinDeliverInfo", pinDeliverInfo); // 递送信息
		
		logger.info("joinGroup PinDeliverInfo:" + pinDeliverInfo);
		
		// 组团成功，推送模版消息
		if (PinGroup.STATUS_OK.equals(group.getStatus())) {
			
			logger.info("joinGroup group status:" + group.getStatus() + ", group is complete! Send template message.");
			
			PinProduct pinProduct = pinProductService.get(group.getProductId());
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			// 查询团所有成员
			PinGroupMember qGM = new PinGroupMember();
			qGM.setGroupId(groupId);
			List<PinGroupMember> mList = pinGroupMemberService.findList(qGM);
			for (PinGroupMember m:mList) {
				PinMember pinMember = pinMemberService.get(m.getMemberId());
				
				String formId = "";
				// 查询这个成员的订单
				PinOrder moQuery = new PinOrder();
				moQuery.setGroupId(groupId);
				moQuery.setMemberId(pinMember.getId());
				List<PinOrder> moList = pinOrderService.findList(moQuery);
				if ((moList != null) && (!moList.isEmpty())) {
					PinOrder mo = moList.get(0);
					formId = (String) CacheUtils.get("PREPAYID" + mo.getId());
					CacheUtils.remove("PREPAYID" + mo.getId());//用户就删除
				}
				
				String openid = pinMember.getOpenid();
				String templateId = "wkg57CVPslqpP0tM75sQuSynx_obSCxx3sj8fMFVwGI";  
				//商品名称
				String data1 = pinProduct.getName(); 

				//成团时间
				String data2 = sf.format(group.getEndTime()); 
				
				//友情提醒
				String data3 = "友情提醒"; 
				
				//配送方式
				String data4= "团长收货";
				PinDeliverInfo qDI = new PinDeliverInfo();
				qDI.setGroupId(groupId);
				qDI.setMemberId(m.getMemberId());
				List<PinDeliverInfo> dInfoList = pinDeliverInfoService.findList(qDI);
				if ((dInfoList != null) && (!dInfoList.isEmpty())) {
					PinDeliverInfo dInfo = dInfoList.get(0);
					if (dInfo.getDeliverType().equals("1")) {
						data4= "自己收货";
					}
				}

				// 给团的成员发送模版消息
				weixinServiceUtil.sendAppTemplateMsg(openid, templateId, formId, data1, data2, data3, data4);
			}
			
		}
		
		return result;
	}
	
	public PinGroupInfo getPinGroupInfo(String groupId) {
		PinGroupInfo result = new PinGroupInfo();
		
		PinGroup group = pinGroupService.get(groupId);
		if (group == null) {
			return null;
		}
		
		/*if (group.getWxacode() == null) {
			String wxacode = weixinServiceUtil.getWXACode(group.getId(), Global.getConfig("group.little_app_path"));
			group.setWxacode(wxacode);
			pinGroupService.save(group);
		}*/
		// 设置团信息
		result.setPinGroup(group);
		
		// 查询所以团员信息
		PinGroupMember pm = new PinGroupMember();
		pm.setGroupId(groupId);
		List<PinGroupMember> listPm = pinGroupMemberService.findList(pm);
		if ((listPm == null) || (listPm.isEmpty())) {
			// 这个团，没有团员， 不正常，至少应该有团主
		}
		
		Base64.Decoder coder = Base64.getDecoder();
		
		List<PinMember> memberList = new ArrayList<PinMember>();
		for (PinGroupMember p:listPm) {
			logger.info("getGroupInfo  MemberId:" + p.getMemberId());
			PinMember m = pinMemberService.get(p.getMemberId());
			if (m != null) {
				//  解一下base64给前台
				String nameM = m.getName();
				byte[] content = coder.decode(nameM.getBytes());
				String nickName = new String(content);
				m.setName(nickName);
				memberList.add(m);
			}
		}
		
		// 设置成员
		result.setPinMembers(memberList);
		
		logger.info("getGroupInfo  membersize:" + memberList.size() + ", group member value:" + group.getMember());
		
		PinProduct product = pinProductService.get(group.getProductId());
		
		// 设置产品信息
		result.setPinProduct(product);
		
		PinRule rule = pinRuleService.get(group.getRuleId());
		
		// 设置规则
		result.setPinRule(rule);
		
		logger.info("getGroupInfo product:" + product + ", rule:" + rule);
		
		return result;
	}
	
	public AjaxJson getGroupInfo(String groupId) {
		logger.info("getGroupInfo  groupId:" + groupId);
		AjaxJson result = new AjaxJson();
		
		PinGroupInfo groupIndo = getPinGroupInfo(groupId);
		if (groupIndo == null) {
			result.setSuccess(false);
			result.setErrorCode("1005");
			result.setMsg("团信息不存在");
		} else {
			result.setSuccess(true);
			result.setErrorCode("0");
			result.setMsg("查询成功");
			result.put("PinGroupInfo", groupIndo); 
		}
		return result;
	}
	
	
	public AjaxJson createGroup(String orderId,
			                    String memberId,
            					    String ruleId,
            					    String recvName,
            					    String recvPhone,
            					    String province,
            					    String city,
            					    String district,
            					    String address,
            					    String deliverType) {
		logger.info("createGroup memberId:" + memberId + ", ruleId:" + ruleId);
		// 进来之前应该是已经成功付款了
		AjaxJson result = new AjaxJson();
		PinRule rule = pinRuleService.get(ruleId);
		PinGroup group = new PinGroup();
		group.setMasterId(memberId);
		group.setMember("1");
		group.setTotal(rule.getMembers());
		group.setExpire(rule.getExpire());
		group.setStatus(PinGroup.STATUS_WAIT);
		group.setCreateTime(new Date());
		group.setRuleId(ruleId);
		group.setProductId(rule.getProductId());
		
		PinProduct pinProduct = pinProductService.get(rule.getProductId());
		group.setProductName(pinProduct.getName());
		pinGroupService.save(group);
		
		// 更新order信息
		PinOrder order = pinOrderService.get(orderId);
		order.setGroupId(group.getId());
		pinOrderService.save(order);
		
		//String wxacode = weixinServiceUtil.getWXACode(group.getId(), Global.getConfig("group.little_app_path"));
		//logger.info("createGroup save group id:" + group.getId() + ", wxacode:" + wxacode);
		//group.setWxacode(wxacode);
		//pinGroupService.save(group);
		
		PinGroupMember relation = new PinGroupMember();
		relation.setMemberId(memberId);
		relation.setGroupId(group.getId());
		pinGroupMemberService.save(relation);
		
		logger.info("createGroup save group member memberId:" + memberId);
		
		result.setSuccess(true);
		result.setErrorCode("0");
		result.setMsg("组团成功");
		result.put("PinGroup", group); // 用了显示团信息
		
		logger.info("createGroup success pin group.");
		
		//添加递送信息
		PinDeliverInfo pinDeliverInfo = new PinDeliverInfo();
		pinDeliverInfo.setCreateTime(new Date());
		pinDeliverInfo.setDeliverType(deliverType);
		pinDeliverInfo.setRecvPhone(recvPhone);
		pinDeliverInfo.setRecvName(recvName);
		pinDeliverInfo.setProvince(province);
		pinDeliverInfo.setCity(city);
		pinDeliverInfo.setDistrict(district);
		pinDeliverInfo.setAddress(address);
		pinDeliverInfo.setStatus("0");
		pinDeliverInfo.setGroupId(group.getId());
		pinDeliverInfo.setMemberId(memberId);
		pinDeliverInfoService.save(pinDeliverInfo);
		
		result.put("PinDeliverInfo", pinDeliverInfo); // 递送信息
		
		logger.info("createGroup PinDeliverInfo:" + pinDeliverInfo);
		return result;
	}
}
