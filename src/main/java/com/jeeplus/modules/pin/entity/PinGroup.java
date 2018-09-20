/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 拼团团信息Entity
 * @author zzz
 * @version 2018-06-12
 */
public class PinGroup extends DataEntity<PinGroup> {
	
	private static final long serialVersionUID = 1L;
	private String masterId;		// 团长id
	private Date createTime;		// 创建时间
	private String member;		// 当前团员数量
	private String total;		// 团员总数
	private String status;		// 状态 0 进行中 1 已经成功 2 已经过期
	private String expire;		// 过期时间，必须在此时间内完成拼团，单位秒
	private Date endTime;		// 结束时间
	private String ruleId;		// 拼团规则id
	private String productId;		// 团对应的产品
	private String wxacode;		// 二维码
	private String productName;		// 产品名称
	
	public static final String STATUS_WAIT = "0";
	public static final String STATUS_OK = "1";
	public static final String STATUS_EXPIRE = "2";
	public static final String STATUS_REFUND = "3";
	
	public static final String STATUS_WAIT_DES = "进行中";
	public static final String STATUS_OK_DES = "已经成功";
	public static final String STATUS_EXPIRE_DES = "已经过期";
	public static final String STATUS_REFUND_DES = "已经退款";
	
	public PinGroup() {
		super();
		this.setIdType(IDTYPE_USER);
	}

	public PinGroup(String id){
		super(id);
		this.setIdType(IDTYPE_USER);
	}

	@ExcelField(title="团长id", align=2, sort=1)
	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="创建时间", align=2, sort=2)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@ExcelField(title="当前团员数量", align=2, sort=3)
	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}
	
	@ExcelField(title="团员总数", align=2, sort=4)
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	@ExcelField(title="状态 0 进行中 1 已经成功 2 已经过期", align=2, sort=5)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="过期时间，必须在此时间内完成拼团，单位秒", align=2, sort=6)
	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=7)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@ExcelField(title="拼团规则id", align=2, sort=8)
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	
	@ExcelField(title="团对应的产品", align=2, sort=9)
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	@ExcelField(title="二维码", align=2, sort=10)
	public String getWxacode() {
		return wxacode;
	}

	public void setWxacode(String wxacode) {
		this.wxacode = wxacode;
	}
	
	@ExcelField(title="产品名称", align=2, sort=11)
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}