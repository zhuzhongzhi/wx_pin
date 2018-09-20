/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.pin.entity.PinDeliverInfo;
import com.jeeplus.modules.pin.mapper.PinDeliverInfoMapper;

/**
 * 拼团递送信息Service
 * @author zzz
 * @version 2018-05-16
 */
@Service
@Transactional(readOnly = true)
public class PinDeliverInfoService extends CrudService<PinDeliverInfoMapper, PinDeliverInfo> {

	public PinDeliverInfo get(String id) {
		return super.get(id);
	}
	
	public List<PinDeliverInfo> findList(PinDeliverInfo pinDeliverInfo) {
		return super.findList(pinDeliverInfo);
	}
	
	public Page<PinDeliverInfo> findPage(Page<PinDeliverInfo> page, PinDeliverInfo pinDeliverInfo) {
		return super.findPage(page, pinDeliverInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(PinDeliverInfo pinDeliverInfo) {
		super.save(pinDeliverInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(PinDeliverInfo pinDeliverInfo) {
		super.delete(pinDeliverInfo);
	}
	
}