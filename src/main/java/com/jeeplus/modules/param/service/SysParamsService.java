/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.param.service;

import java.util.List;

import com.jeeplus.modules.sys.utils.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.param.entity.SysParams;
import com.jeeplus.modules.param.mapper.SysParamsMapper;

/**
 * 系统参数Service
 * 
 * @author ysisl
 * @version 2017-11-25
 */
@Service
@Transactional(readOnly = true)
public class SysParamsService extends CrudService<SysParamsMapper, SysParams> {

	public SysParams get(String id) {
		return super.get(id);
	}

	public List<SysParams> findList(SysParams sysParams) {
		return super.findList(sysParams);
	}

	public Page<SysParams> findPage(Page<SysParams> page, SysParams sysParams) {
		return super.findPage(page, sysParams);
	}

	@Transactional(readOnly = false)
	public void save(SysParams sysParams) {
		super.save(sysParams);
		ParamUtils.clearCache(); // 清除缓存
		ParamUtils.getParamByKey(""); // 构建缓存
	}

	@Transactional(readOnly = false)
	public void delete(SysParams sysParams) {
		super.delete(sysParams);
	}

}