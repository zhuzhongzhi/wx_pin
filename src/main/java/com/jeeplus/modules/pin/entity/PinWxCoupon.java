/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 拼团微信优惠券Entity
 * @author zzz
 * @version 2018-05-12
 */
public class PinWxCoupon extends DataEntity<PinWxCoupon> {
	
	private static final long serialVersionUID = 1L;
	private String stockId;		// 微信代金券批次号
	private String faceValue;		// 面值
	private String effectType;		// 用户领券之后，券的生效方式 0 立即生效 1 第二天生效
	private Date startTime;		// 有效期开始时间
	private Date endTime;		// 有效期结束时间
	private String condition;		// 使用条件， 满多少元可用。0表示不限制
	private String description;		// 说明
	
	public PinWxCoupon() {
		super();
	}

	public PinWxCoupon(String id){
		super(id);
	}

	@ExcelField(title="微信代金券批次号", align=2, sort=1)
	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	
	@ExcelField(title="面值", align=2, sort=2)
	public String getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(String faceValue) {
		this.faceValue = faceValue;
	}
	
	@ExcelField(title="用户领券之后，券的生效方式 0 立即生效 1 第二天生效", align=2, sort=3)
	public String getEffectType() {
		return effectType;
	}

	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="有效期开始时间", align=2, sort=4)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="有效期结束时间", align=2, sort=5)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@ExcelField(title="使用条件， 满多少元可用。0表示不限制", align=2, sort=6)
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@ExcelField(title="说明", align=2, sort=7)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}