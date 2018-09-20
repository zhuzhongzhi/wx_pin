/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.pin.entity.PinOrder;
import com.jeeplus.modules.pin.mapper.PinOrderMapper;

/**
 * 拼团订单信息Service
 * @author zzz
 * @version 2018-05-15
 */
@Service
@Transactional(readOnly = true)
public class PinOrderService extends CrudService<PinOrderMapper, PinOrder> {

	public PinOrder get(String id) {
		return super.get(id);
	}
	
	public List<PinOrder> findList(PinOrder pinOrder) {
		return super.findList(pinOrder);
	}
	
	public Page<PinOrder> findPage(Page<PinOrder> page, PinOrder pinOrder) {
		return super.findPage(page, pinOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(PinOrder pinOrder) {
		super.save(pinOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(PinOrder pinOrder) {
		super.delete(pinOrder);
	}
	
}