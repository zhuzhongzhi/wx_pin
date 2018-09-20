/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 拼团订单信息Entity
 * @author zzz
 * @version 2018-05-15
 */
public class PinOrder extends DataEntity<PinOrder> {
	
	private static final long serialVersionUID = 1L;
	private Date createTime;		// 创建时间
	private Date endTime;		// 完成时间
	private String groupId;		// 团id
	private String memberId;		// 用户id
	private String fee;		// 订单金额
	private String status;		// 支付状态 0 进行中 1 支付完成 2 支付失败 3 已经取消 4 已经退款
	private String tradeNo;		// 微信交易流水
	
	public PinOrder() {
		super();
	}

	public PinOrder(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="创建时间", align=2, sort=1)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="完成时间", align=2, sort=2)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@ExcelField(title="团id", align=2, sort=3)
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@ExcelField(title="用户id", align=2, sort=4)
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	@ExcelField(title="订单金额", align=2, sort=5)
	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
	
	@ExcelField(title="支付状态 0 进行中 1 支付完成 2 支付失败 3 已经取消 4 已经退款", align=2, sort=6)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="微信交易流水", align=2, sort=7)
	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	
}