package com.jeeplus.modules.api.util;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CookieUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.pin.entity.PinMember;
import com.jeeplus.modules.pin.service.PinMemberService;
import com.jeeplus.modules.sys.utils.AES;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by fusheng on 17/11/30.
 */
@Service
public class MemberServiceUtil {
	
	public final static int COOKEI_TIMEOUT = 60*60*24*30; //30天
	public final static String COOKEI_NAME = "PIN_MEMBER_ID";
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AES aes;
	
	@Autowired
	private PinMemberService pinMemberService;

	@Autowired
	private IDPoolService idPoolService;
	
	/**
	 * 会员注册接口 小程序会员注册接口
	 * 
	 * @param appId
	 *            商户接口AppId
	 * @param map
	 *            前端传过来的Request Body ，Json格式
	 * @return
	 * @throws IOException
	 * @throws InvalidAlgorithmParameterException
	 */
	public AjaxJson regist(String appId, JSONObject map) {
		AjaxJson result = new AjaxJson();

		String encryptData = map.getString("encryptData");
		String sessionKey = map.getString("sessionKey");
		String iv = map.getString("iv");
		// 用户信息 调用api/下面的AES类进行解码
		String resMember = null;
		try {
			resMember = aes.decrypt(encryptData, sessionKey, iv);
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!StringUtils.isEmpty(resMember)) {
			
			// 将字符串转换为JSONObject
			JSONObject jsonObjMap = JSONObject.parseObject(resMember);
			
			// 存到Member
			PinMember pm = new PinMember();
			pm.setId(idPoolService.generateMemberId());
			pm.setUnionid(jsonObjMap.getString("unionid"));
			pm.setName(jsonObjMap.getString("name"));
			pm.setSex(jsonObjMap.getString("sex"));
			pm.setHeadimgurl(jsonObjMap.getString("headimgurl"));
			pm.setStatus("0");
			
			pinMemberService.save(pm);
			result.setSuccess(true);
			result.setErrorCode("0");
			result.setMsg("新增用户成功");
			result.put("id", pm.getId());

			
		} else {
			result.setSuccess(false);
			result.setErrorCode("405001");
			result.setMsg(
					"encryptData, sessionKey, iv不存在或未解码成功或Token失效.【" + encryptData + "" + sessionKey + "" + iv + "】");

		}

		return result;
	}
	
	public PinMember getMemberFromCookie(HttpServletRequest request) {
		String memberId = CookieUtils.getCookie(request, COOKEI_NAME);
		if (memberId != null) {
			PinMember member = pinMemberService.get(memberId);
			if (member != null) {
				logger.info("PinMember id:" + memberId + ", name" + member.getName());
				return member;
			} else {
				logger.info("PinMember id:" + memberId + ", not exist in db.");
			}
		}
		
		return null;
	}
	
	public PinMember getMemberbyId(String memberId) {
		PinMember member = pinMemberService.get(memberId);
		if (member != null) {
			logger.info("PinMember id:" + memberId + ", name" + member.getName());
			return member;
		} else {
			logger.info("PinMember id:" + memberId + ", not exist in db.");
		}
		
		return null;
	}
	
	public AjaxJson appLogin(String openid, String unionId, JSONObject objData,
			HttpServletRequest request, HttpServletResponse response) {
		AjaxJson result = new AjaxJson();
		
		Base64.Encoder coder = Base64.getEncoder();
		String oraName = objData.getString("nickName");
        byte[] content = coder.encode(oraName.getBytes());
        String nickName = new String(content);
        
		PinMember query = new PinMember();
		query.setUnionid(unionId);
		List<PinMember> members = pinMemberService.findList(query);
		if ((members == null) || (members.isEmpty())) {
			logger.info("can not find member info with unionId:" + unionId);
			
			// 新建用户
			//query.setId(idPoolService.generateMemberId());
			
			query.setUnionid(unionId);
			query.setName(nickName);
			query.setSex(objData.getString("gender"));
			query.setOpenid(openid);
			query.setHeadimgurl(objData.getString("avatarUrl"));
			query.setStatus("0");
			query.setCreateTime(new Date());
			
			pinMemberService.save(query);		
			logger.info("new member:" + query.getId() + " info with unionId:" + unionId);
		} else {
			query = members.get(0);
			query.setUnionid(unionId);
			query.setName(nickName);
			query.setSex(objData.getString("gender"));
			query.setOpenid(openid);
			query.setHeadimgurl(objData.getString("avatarUrl"));
			if (query.getCreateTime() == null) {
				query.setCreateTime(new Date());
			}
			//query.setStatus("0");
			pinMemberService.save(query);
			logger.info("find member:" + query.getId() + " info with unionId:" + unionId);
		}
		
		// 重新设置cookie
		CookieUtils.setCookie(response, COOKEI_NAME, query.getId(), COOKEI_TIMEOUT);
		
		result.setSuccess(true);
		result.setErrorCode("0");
		result.setMsg("用户登录成功");
		query.setName(oraName); //给前端原始的
		result.put("PinMember", query);	
		return result;
	}
}
