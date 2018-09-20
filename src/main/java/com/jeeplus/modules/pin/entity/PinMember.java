/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 拼团会员信息Entity
 * @author zzz
 * @version 2018-05-10
 */
public class PinMember extends DataEntity<PinMember> {
	
	private static final long serialVersionUID = 1L;
	private Date createTime;		// 创建时间
	private String unionid;		// 用户微信unionid
	private String openid;		// 用户微信openid
	private String name;		// 用户名，取微信名
	private String sex;		// 性别
	private String headimgurl;		// 头像地址
	private String status;		// 用户状态
	
	public PinMember() {
		super();
	}

	public PinMember(String id){
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
	
	@ExcelField(title="用户微信unionid", align=2, sort=2)
	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
	@ExcelField(title="用户微信openid", align=2, sort=3)
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	@ExcelField(title="用户名，取微信名", align=2, sort=4)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="性别", align=2, sort=5)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@ExcelField(title="头像地址", align=2, sort=6)
	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	
	@ExcelField(title="用户状态", align=2, sort=7)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}