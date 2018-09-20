/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 拼团团信息Entity
 * @author zzz
 * @version 2018-05-10
 */
public class PinGroupInfo{
	private PinGroup pinGroup;		        // 团
	private List<PinMember> pinMembers;		// 成员 
	private PinProduct pinProduct;	        // 产品
	private PinRule pinRule;                 // 规则
	
	public PinGroupInfo() {
		pinGroup = null;
		pinMembers = null;
		pinProduct = null;
		pinRule = null;
	}


	public PinGroup getPinGroup() {
		return pinGroup;
	}

	public void setPinGroup(PinGroup pinGroup) {
		this.pinGroup = pinGroup;
	}
	
	public List<PinMember> getPinMembers() {
		return pinMembers;
	}

	public void setPinMembers(List<PinMember> pinMembers) {
		this.pinMembers = pinMembers;
	}
	
	public PinProduct getPinProduct() {
		return pinProduct;
	}

	public void setPinProduct(PinProduct pinProduct) {
		this.pinProduct = pinProduct;
	}
	
	public PinRule getPinRule() {
		return pinRule;
	}

	public void setPinRule(PinRule pinRule) {
		this.pinRule = pinRule;
	}
	
}