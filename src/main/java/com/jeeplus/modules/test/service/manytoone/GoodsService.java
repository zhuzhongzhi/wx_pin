/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.service.manytoone;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.entity.manytoone.Goods;
import com.jeeplus.modules.test.mapper.manytoone.GoodsMapper;

/**
 * 商品Service
 * 
 * @author liugf
 * @version 2017-06-12
 */
@Service
@Transactional(readOnly = true)
public class GoodsService extends CrudService<GoodsMapper, Goods> {

	public Goods get(String id) {
		return super.get(id);
	}

	public List<Goods> findList(Goods goods) {
		return super.findList(goods);
	}

	public Page<Goods> findPage(Page<Goods> page, Goods goods) {
		return super.findPage(page, goods);
	}

	@Transactional(readOnly = false)
	public void save(Goods goods) {
		super.save(goods);
	}

	@Transactional(readOnly = false)
	public void delete(Goods goods) {
		super.delete(goods);
	}

}