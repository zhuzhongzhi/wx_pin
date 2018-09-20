/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.service.one;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.test.entity.one.LeaveDialog;
import com.jeeplus.modules.test.mapper.one.LeaveDialogMapper;

/**
 * 请假表单Service
 * 
 * @author lgf
 * @version 2017-06-11
 */
@Service
@Transactional(readOnly = true)
public class LeaveDialogService extends CrudService<LeaveDialogMapper, LeaveDialog> {

	public LeaveDialog get(String id) {
		return super.get(id);
	}

	public List<LeaveDialog> findList(LeaveDialog leaveDialog) {
		return super.findList(leaveDialog);
	}

	public Page<LeaveDialog> findPage(Page<LeaveDialog> page, LeaveDialog leaveDialog) {
		return super.findPage(page, leaveDialog);
	}

	@Transactional(readOnly = false)
	public void save(LeaveDialog leaveDialog) {
		super.save(leaveDialog);
	}

	@Transactional(readOnly = false)
	public void delete(LeaveDialog leaveDialog) {
		super.delete(leaveDialog);
	}

}