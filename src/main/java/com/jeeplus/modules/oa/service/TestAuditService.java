/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.oa.service;

import java.util.Map;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.modules.oa.entity.TestAudit;
import com.jeeplus.modules.oa.mapper.TestAuditMapper;

/**
 * 审批Service
 * 
 * @author jeeplus
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class TestAuditService extends CrudService<TestAuditMapper, TestAudit> {

	@Autowired
	private ActTaskService actTaskService;

	@Autowired
	private IdentityService identityService;

	public TestAudit getByProcInsId(String procInsId) {
		return mapper.getByProcInsId(procInsId);
	}

	public Page<TestAudit> findPage(Page<TestAudit> page, TestAudit testAudit) {
		testAudit.setPage(page);
		page.setList(mapper.findList(testAudit));
		return page;
	}

	/**
	 * 审核新增或编辑
	 * 
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void save(TestAudit testAudit) {

		// 申请发起
		if (StringUtils.isBlank(testAudit.getId())) {
			testAudit.preInsert();
			mapper.insert(testAudit);

			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(testAudit.getCurrentUser().getLoginName());
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_TEST_AUDIT[0], ActUtils.PD_TEST_AUDIT[1], testAudit.getId(),
					testAudit.getContent());

		}

		// 重新编辑申请
		else {
			testAudit.preUpdate();
			mapper.update(testAudit);

			testAudit.getAct().setComment(
					("yes".equals(testAudit.getAct().getFlag()) ? "[重申] " : "[销毁] ") + testAudit.getAct().getComment());

			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(testAudit.getAct().getFlag()) ? "1" : "0");
			actTaskService.complete(testAudit.getAct().getTaskId(), testAudit.getAct().getProcInsId(),
					testAudit.getAct().getComment(), testAudit.getContent(), vars);
		}
	}

	/**
	 * 审核审批保存
	 * 
	 * @param testAudit
	 */
	@Transactional(readOnly = false)
	public void auditSave(TestAudit testAudit) {

		// 设置意见
		testAudit.getAct().setComment(
				("yes".equals(testAudit.getAct().getFlag()) ? "[同意] " : "[驳回] ") + testAudit.getAct().getComment());

		testAudit.preUpdate();

		// 对不同环节的业务逻辑进行操作
		String taskDefKey = testAudit.getAct().getTaskDefKey();

		// 审核环节
		if ("audit".equals(taskDefKey)) {

		} else if ("audit2".equals(taskDefKey)) {
			testAudit.setHrText(testAudit.getAct().getComment());
			mapper.updateHrText(testAudit);
		} else if ("audit3".equals(taskDefKey)) {
			testAudit.setLeadText(testAudit.getAct().getComment());
			mapper.updateLeadText(testAudit);
		} else if ("audit4".equals(taskDefKey)) {
			testAudit.setMainLeadText(testAudit.getAct().getComment());
			mapper.updateMainLeadText(testAudit);
		} else if ("apply_end".equals(taskDefKey)) {

		}

		// 未知环节，直接返回
		else {
			return;
		}

		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("pass", "yes".equals(testAudit.getAct().getFlag()) ? "1" : "0");
		actTaskService.complete(testAudit.getAct().getTaskId(), testAudit.getAct().getProcInsId(),
				testAudit.getAct().getComment(), vars);

	}

}
