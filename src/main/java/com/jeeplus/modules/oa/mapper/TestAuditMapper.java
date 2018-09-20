/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.oa.entity.TestAudit;

/**
 * 审批MAPPER接口
 * 
 * @author jeeplus
 * @version 2017-05-16
 */
@MyBatisMapper
public interface TestAuditMapper extends BaseMapper<TestAudit> {

	public TestAudit getByProcInsId(String procInsId);

	public int updateInsId(TestAudit testAudit);

	public int updateHrText(TestAudit testAudit);

	public int updateLeadText(TestAudit testAudit);

	public int updateMainLeadText(TestAudit testAudit);

}
