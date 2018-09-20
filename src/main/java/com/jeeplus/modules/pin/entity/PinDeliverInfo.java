/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 拼团递送信息Entity
 * @author zzz
 * @version 2018-05-16
 */
public class PinDeliverInfo extends DataEntity<PinDeliverInfo> {
	
	private static final long serialVersionUID = 1L;
	private String groupId;		// 团id
	private String logisticsCodes;		// 物流编码
	private Date createTime;		// 创建时间
	private String deliverType;		// 递送方式 0 团长收货 1 自己收货
	private String province;		// 省
	private String city;		// 市
	private String district;		// 区
	private String address;		// 详细地址
	private String recvName;		// 接收人名字
	private String status;		// 递送状态 0 进行中 1 已经送达
	private String recvPhone;		// 接收人手机
	private String memberId;		// 会员id
	
	private String groupStatus;		// 团状态
	
	public static final String TYPE_MASTER = "0";
	public static final String TYPE_OWNER = "1";
	
	public static final String TYPE_MASTER_DES = "团长收货";
	public static final String TYPE_OWNER_DES = "自己收货";
	
	public PinDeliverInfo() {
		super();
	}

	public PinDeliverInfo(String id){
		super(id);
	}

	@ExcelField(title="团id", align=2, sort=1)
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@ExcelField(title="物流编码", align=2, sort=2)
	public String getLogisticsCodes() {
		return logisticsCodes;
	}

	public void setLogisticsCodes(String logisticsCodes) {
		this.logisticsCodes = logisticsCodes;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="创建时间", align=2, sort=3)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@ExcelField(title="递送方式 0 团长收货 1 自己收货", align=2, sort=4)
	public String getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}
	
	@ExcelField(title="省", align=2, sort=5)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@ExcelField(title="市", align=2, sort=6)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@ExcelField(title="区", align=2, sort=7)
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	
	@ExcelField(title="详细地址", align=2, sort=8)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="接收人名字", align=2, sort=9)
	public String getRecvName() {
		return recvName;
	}

	public void setRecvName(String recvName) {
		this.recvName = recvName;
	}
	
	@ExcelField(title="递送状态 0 进行中 1 已经送达", align=2, sort=10)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="接收人手机", align=2, sort=11)
	public String getRecvPhone() {
		return recvPhone;
	}

	public void setRecvPhone(String recvPhone) {
		this.recvPhone = recvPhone;
	}
	
	@ExcelField(title="会员id", align=2, sort=12)
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	@ExcelField(title="团状态", align=2, sort=13)
	public String getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(String groupStatus) {
		this.groupStatus = groupStatus;
	}
}