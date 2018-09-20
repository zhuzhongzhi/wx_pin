/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.pin.entity.PinGroup;
import com.jeeplus.modules.pin.mapper.PinGroupMapper;

/**
 * 拼团团信息Service
 * @author zzz
 * @version 2018-06-12
 */
@Service
@Transactional(readOnly = true)
public class PinGroupService extends CrudService<PinGroupMapper, PinGroup> {

	public PinGroup get(String id) {
		return super.get(id);
	}
	
	public List<PinGroup> findList(PinGroup pinGroup) {
		return super.findList(pinGroup);
	}
	
	public Page<PinGroup> findPage(Page<PinGroup> page, PinGroup pinGroup) {
		return super.findPage(page, pinGroup);
	}
	
	@Transactional(readOnly = false)
	public void save(PinGroup pinGroup) {
		super.save(pinGroup);
	}
	
	@Transactional(readOnly = false)
	public void delete(PinGroup pinGroup) {
		super.delete(pinGroup);
	}
	
}