/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 机构MAPPER接口
 * 
 * @author jeeplus
 * @version 2017-05-16
 */
@MyBatisMapper
public interface OfficeMapper extends TreeMapper<Office> {

	public Office getByCode(String code);

	public List<Office> getChildrenById(@Param("parentId") String parentId, @Param("id") String id);

}
