/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.pin.entity.PinProduct;
import com.jeeplus.modules.pin.mapper.PinProductMapper;

/**
 * 拼团产品信息Service
 * @author zzz
 * @version 2018-07-26
 */
@Service
@Transactional(readOnly = true)
public class PinProductService extends CrudService<PinProductMapper, PinProduct> {

	public PinProduct get(String id) {
		return super.get(id);
	}
	
	public List<PinProduct> findList(PinProduct pinProduct) {
		return super.findList(pinProduct);
	}
	
	public Page<PinProduct> findPage(Page<PinProduct> page, PinProduct pinProduct) {
		return super.findPage(page, pinProduct);
	}
	
	@Transactional(readOnly = false)
	public void save(PinProduct pinProduct) {
		super.save(pinProduct);
	}
	
	@Transactional(readOnly = false)
	public void delete(PinProduct pinProduct) {
		super.delete(pinProduct);
	}
	
}