/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.pin.entity.PinWxCoupon;
import com.jeeplus.modules.pin.mapper.PinWxCouponMapper;

/**
 * 拼团微信优惠券Service
 * @author zzz
 * @version 2018-05-12
 */
@Service
@Transactional(readOnly = true)
public class PinWxCouponService extends CrudService<PinWxCouponMapper, PinWxCoupon> {

	public PinWxCoupon get(String id) {
		return super.get(id);
	}
	
	public List<PinWxCoupon> findList(PinWxCoupon pinWxCoupon) {
		return super.findList(pinWxCoupon);
	}
	
	public Page<PinWxCoupon> findPage(Page<PinWxCoupon> page, PinWxCoupon pinWxCoupon) {
		return super.findPage(page, pinWxCoupon);
	}
	
	@Transactional(readOnly = false)
	public void save(PinWxCoupon pinWxCoupon) {
		super.save(pinWxCoupon);
	}
	
	@Transactional(readOnly = false)
	public void delete(PinWxCoupon pinWxCoupon) {
		super.delete(pinWxCoupon);
	}
	
}