/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.mapper.validation;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.test.entity.validation.TestValidation;

/**
 * 测试校验功能MAPPER接口
 * 
 * @author lgf
 * @version 2017-06-12
 */
@MyBatisMapper
public interface TestValidationMapper extends BaseMapper<TestValidation> {

}