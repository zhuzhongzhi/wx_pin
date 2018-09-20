/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.mapper.treetable;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.test.entity.treetable.CarKind;

/**
 * 车系MAPPER接口
 * 
 * @author lgf
 * @version 2017-10-16
 */
@MyBatisMapper
public interface CarKindMapper extends TreeMapper<CarKind> {

}