/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 拼团产品信息Entity
 * @author zzz
 * @version 2018-07-26
 */
public class PinProduct extends DataEntity<PinProduct> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 产品名称
	private String description;		// 产品简介
	private String specifications;		// 产品规格，数量，容量等描述
	private String mallPrice;		// 商城价
	private String minPrice;		// 拼团最低价
	private String maxPrice;		// 拼团最高价
	private String picUrl;		// 产品图片url
	private Date createTime;		// 创建时间
	private String status;		// 产品状态 0 当前售卖产品 1 其他产品
	private String iconUrl;		// 产品图标url
	private String detailUrl;		// 产品详情url
	
	public PinProduct() {
		super();
	}

	public PinProduct(String id){
		super(id);
	}

	@ExcelField(title="产品名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="产品简介", align=2, sort=2)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@ExcelField(title="产品规格，数量，容量等描述", align=2, sort=3)
	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	
	@ExcelField(title="商城价", align=2, sort=4)
	public String getMallPrice() {
		return mallPrice;
	}

	public void setMallPrice(String mallPrice) {
		this.mallPrice = mallPrice;
	}
	
	@ExcelField(title="拼团最低价", align=2, sort=5)
	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}
	
	@ExcelField(title="拼团最高价", align=2, sort=6)
	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	@ExcelField(title="产品图片url", align=2, sort=7)
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="创建时间", align=2, sort=8)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@ExcelField(title="产品状态 0 当前售卖产品 1 其他产品", align=2, sort=9)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="产品图标url", align=2, sort=10)
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	@ExcelField(title="产品详情url", align=2, sort=11)
	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	
}