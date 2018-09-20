/**
 * There are <a href="http://www.doforsys.com/">jeeplus</a> code generation
 */
package com.jeeplus.modules.oa.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.oa.entity.Leave;

/**
 * 请假MAPPER接口
 * 
 * @author liuj
 * @version 2016-8-23
 */
@MyBatisMapper
public interface LeaveMapper extends BaseMapper<Leave> {

	/**
	 * 更新流程实例ID
	 * 
	 * @param leave
	 * @return
	 */
	public int updateProcessInstanceId(Leave leave);

	/**
	 * 更新实际开始结束时间
	 * 
	 * @param leave
	 * @return
	 */
	public int updateRealityTime(Leave leave);

}
