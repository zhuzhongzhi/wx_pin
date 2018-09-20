/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.service.validation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.entity.validation.TestValidation;
import com.jeeplus.modules.test.mapper.validation.TestValidationMapper;

/**
 * 测试校验功能Service
 * 
 * @author lgf
 * @version 2017-06-12
 */
@Service
@Transactional(readOnly = true)
public class TestValidationService extends CrudService<TestValidationMapper, TestValidation> {

	public TestValidation get(String id) {
		return super.get(id);
	}

	public List<TestValidation> findList(TestValidation testValidation) {
		return super.findList(testValidation);
	}

	public Page<TestValidation> findPage(Page<TestValidation> page, TestValidation testValidation) {
		return super.findPage(page, testValidation);
	}

	@Transactional(readOnly = false)
	public void save(TestValidation testValidation) {
		super.save(testValidation);
	}

	@Transactional(readOnly = false)
	public void delete(TestValidation testValidation) {
		super.delete(testValidation);
	}

}