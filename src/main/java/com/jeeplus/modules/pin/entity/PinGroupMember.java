/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 拼团团成员信息Entity
 * @author zzz
 * @version 2018-05-11
 */
public class PinGroupMember extends DataEntity<PinGroupMember> {
	
	private static final long serialVersionUID = 1L;
	private String groupId;		// 团id
	private String memberId;		// 用户id
	
	public PinGroupMember() {
		super();
	}

	public PinGroupMember(String id){
		super(id);
	}

	@ExcelField(title="团id", align=2, sort=0)
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@ExcelField(title="用户id", align=2, sort=1)
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
}