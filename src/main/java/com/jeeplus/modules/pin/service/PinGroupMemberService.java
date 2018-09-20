/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.pin.entity.PinGroupMember;
import com.jeeplus.modules.pin.mapper.PinGroupMemberMapper;

/**
 * 拼团团成员信息Service
 * @author zzz
 * @version 2018-05-11
 */
@Service
@Transactional(readOnly = true)
public class PinGroupMemberService extends CrudService<PinGroupMemberMapper, PinGroupMember> {

	public PinGroupMember get(String id) {
		return super.get(id);
	}
	
	public List<PinGroupMember> findList(PinGroupMember pinGroupMember) {
		return super.findList(pinGroupMember);
	}
	
	public Page<PinGroupMember> findPage(Page<PinGroupMember> page, PinGroupMember pinGroupMember) {
		return super.findPage(page, pinGroupMember);
	}
	
	@Transactional(readOnly = false)
	public void save(PinGroupMember pinGroupMember) {
		super.save(pinGroupMember);
	}
	
	@Transactional(readOnly = false)
	public void delete(PinGroupMember pinGroupMember) {
		super.delete(pinGroupMember);
	}
	
}