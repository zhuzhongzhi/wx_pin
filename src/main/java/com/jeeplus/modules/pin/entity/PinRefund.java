/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 退款管理Entity
 * @author zzz
 * @version 2018-05-23
 */
public class PinRefund extends DataEntity<PinRefund> {
	
	private static final long serialVersionUID = 1L;
	private String groupId;		// 团id
	private String memberId;		// 会员id
	private String transactionId;		// 微信订单号
	private String outTradeNo;		// 商户订单号
	private String refundId;		// 微信退款单号
	private String outRefundNo;		// 商户退款单号
	private String totalFee;		// 订单金额
	private String refundFee;		// 申请退款金额
	private String settlementTotalFee;		// 应结订单金额
	private String settlementRefundFee;		// 退款金额
	private String refundStatus;		// 退款状态
	private String successTime;		// 退款成功时间
	private String refundRecvAccout;		// 退款入账账户
	private String refundAccount;		// 退款资金来源
	private String refundRequestSource;		// 退款发起来源
	
	public PinRefund() {
		super();
	}

	public PinRefund(String id){
		super(id);
	}

	@ExcelField(title="团id", align=2, sort=1)
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@ExcelField(title="会员id", align=2, sort=2)
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	@ExcelField(title="微信订单号", align=2, sort=3)
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	@ExcelField(title="商户订单号", align=2, sort=4)
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	
	@ExcelField(title="微信退款单号", align=2, sort=5)
	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}
	
	@ExcelField(title="商户退款单号", align=2, sort=6)
	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}
	
	@ExcelField(title="订单金额", align=2, sort=7)
	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	
	@ExcelField(title="申请退款金额", align=2, sort=8)
	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}
	
	@ExcelField(title="应结订单金额", align=2, sort=9)
	public String getSettlementTotalFee() {
		return settlementTotalFee;
	}

	public void setSettlementTotalFee(String settlementTotalFee) {
		this.settlementTotalFee = settlementTotalFee;
	}
	
	@ExcelField(title="退款金额", align=2, sort=10)
	public String getSettlementRefundFee() {
		return settlementRefundFee;
	}

	public void setSettlementRefundFee(String settlementRefundFee) {
		this.settlementRefundFee = settlementRefundFee;
	}
	
	@ExcelField(title="退款状态", align=2, sort=11)
	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	
	@ExcelField(title="退款成功时间", align=2, sort=12)
	public String getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(String successTime) {
		this.successTime = successTime;
	}
	
	@ExcelField(title="退款入账账户", align=2, sort=13)
	public String getRefundRecvAccout() {
		return refundRecvAccout;
	}

	public void setRefundRecvAccout(String refundRecvAccout) {
		this.refundRecvAccout = refundRecvAccout;
	}
	
	@ExcelField(title="退款资金来源", align=2, sort=14)
	public String getRefundAccount() {
		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}
	
	@ExcelField(title="退款发起来源", align=2, sort=15)
	public String getRefundRequestSource() {
		return refundRequestSource;
	}

	public void setRefundRequestSource(String refundRequestSource) {
		this.refundRequestSource = refundRequestSource;
	}
	
}