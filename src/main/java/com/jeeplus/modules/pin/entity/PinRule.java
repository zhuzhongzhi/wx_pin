/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 拼团规则信息Entity
 * @author zzz
 * @version 2018-05-18
 */
public class PinRule extends DataEntity<PinRule> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 团名称
	private String members;		// 团需要成员个数
	private String price;		// 团购价
	private String expire;		// 过期时间，必须在此时间内完成拼团，单位秒
	private String description;		// 拼团规则描述
	private String productId;		// 团对应的产品id
	private String masterFreight;		// 团长运费,默认免费0
	private String memberFreight;		// 团员运费, 团员自己收货时需付
	
	public PinRule() {
		super();
	}

	public PinRule(String id){
		super(id);
	}

	@ExcelField(title="团名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="团需要成员个数", align=2, sort=2)
	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}
	
	@ExcelField(title="团购价", align=2, sort=3)
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	@ExcelField(title="过期时间，必须在此时间内完成拼团，单位秒", align=2, sort=4)
	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}
	
	@ExcelField(title="拼团规则描述", align=2, sort=5)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@ExcelField(title="团对应的产品id", align=2, sort=6)
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	@ExcelField(title="团长运费,默认免费0", align=2, sort=7)
	public String getMasterFreight() {
		return masterFreight;
	}

	public void setMasterFreight(String masterFreight) {
		this.masterFreight = masterFreight;
	}
	
	@ExcelField(title="团员运费, 团员自己收货时需付", align=2, sort=8)
	public String getMemberFreight() {
		return memberFreight;
	}

	public void setMemberFreight(String memberFreight) {
		this.memberFreight = memberFreight;
	}
	
}