/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.pin.entity.PinRefund;
import com.jeeplus.modules.pin.mapper.PinRefundMapper;

/**
 * 退款管理Service
 * @author zzz
 * @version 2018-05-23
 */
@Service
@Transactional(readOnly = true)
public class PinRefundService extends CrudService<PinRefundMapper, PinRefund> {

	public PinRefund get(String id) {
		return super.get(id);
	}
	
	public List<PinRefund> findList(PinRefund pinRefund) {
		return super.findList(pinRefund);
	}
	
	public Page<PinRefund> findPage(Page<PinRefund> page, PinRefund pinRefund) {
		return super.findPage(page, pinRefund);
	}
	
	@Transactional(readOnly = false)
	public void save(PinRefund pinRefund) {
		super.save(pinRefund);
	}
	
	@Transactional(readOnly = false)
	public void delete(PinRefund pinRefund) {
		super.delete(pinRefund);
	}
	
}