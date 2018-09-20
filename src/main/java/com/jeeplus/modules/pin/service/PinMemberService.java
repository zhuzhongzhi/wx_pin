/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.pin.entity.PinMember;
import com.jeeplus.modules.pin.mapper.PinMemberMapper;

/**
 * 拼团会员信息Service
 * @author zzz
 * @version 2018-05-10
 */
@Service
@Transactional(readOnly = true)
public class PinMemberService extends CrudService<PinMemberMapper, PinMember> {

	public PinMember get(String id) {
		return super.get(id);
	}
	
	public List<PinMember> findList(PinMember pinMember) {
		return super.findList(pinMember);
	}
	
	public Page<PinMember> findPage(Page<PinMember> page, PinMember pinMember) {
		return super.findPage(page, pinMember);
	}
	
	@Transactional(readOnly = false)
	public void save(PinMember pinMember) {
		super.save(pinMember);
	}
	
	@Transactional(readOnly = false)
	public void delete(PinMember pinMember) {
		super.delete(pinMember);
	}
	
}