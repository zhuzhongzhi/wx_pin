/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.pin.entity.PinRule;
import com.jeeplus.modules.pin.mapper.PinRuleMapper;

/**
 * 拼团规则信息Service
 * @author zzz
 * @version 2018-05-18
 */
@Service
@Transactional(readOnly = true)
public class PinRuleService extends CrudService<PinRuleMapper, PinRule> {

	public PinRule get(String id) {
		return super.get(id);
	}
	
	public List<PinRule> findList(PinRule pinRule) {
		return super.findList(pinRule);
	}
	
	public Page<PinRule> findPage(Page<PinRule> page, PinRule pinRule) {
		return super.findPage(page, pinRule);
	}
	
	@Transactional(readOnly = false)
	public void save(PinRule pinRule) {
		super.save(pinRule);
	}
	
	@Transactional(readOnly = false)
	public void delete(PinRule pinRule) {
		super.delete(pinRule);
	}
	
}