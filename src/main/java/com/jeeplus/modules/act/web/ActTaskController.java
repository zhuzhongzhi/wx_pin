/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.act.web;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeeplus.common.json.AjaxJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.act.entity.Act;
import com.jeeplus.modules.act.service.ActTaskService;
import com.jeeplus.modules.act.utils.ActUtils;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 流程个人任务相关Controller
 * 
 * @author jeeplus
 * @version 2016-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/act/task")
public class ActTaskController extends BaseController {

	@Autowired
	private ActTaskService actTaskService;

	/**
	 * 获取待办列表
	 * 
	 * @return
	 */
	@RequestMapping(value = { "todo", "" })
	public String todoList(Act act, HttpServletResponse response, Model model) throws Exception {
		return "modules/act/actTaskTodoList";
	}

	@ResponseBody
	@RequestMapping(value = "todo/data")
	public Map<String, Object> todoListData(Act act, HttpServletResponse response, Model model) throws Exception {
		List<HashMap<String, String>> list = actTaskService.todoList(act);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", list.size());
		return map;
	}

	/**
	 * 获取已办任务
	 * 
	 * @return
	 */
	@RequestMapping(value = "historic")
	public String historicList(Act act, HttpServletRequest request, HttpServletResponse response, Model model)
			throws Exception {
		return "modules/act/actTaskHistoricList";
	}

	@ResponseBody
	@RequestMapping(value = "historic/data")
	public Map<String, Object> historicListData(Act act, HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		Page<HashMap<String, String>> page = actTaskService
				.historicList(new Page<HashMap<String, String>>(request, response), act);
		return getBootstrapData(page);
	}

	/**
	 * 获取流转历史列表
	 * 
	 * @param startAct
	 *            开始活动节点名称
	 * @param endAct
	 *            结束活动节点名称
	 */
	@RequestMapping(value = "histoicFlow")
	public String histoicFlow(Act act, String startAct, String endAct, Model model) {
		if (StringUtils.isNotBlank(act.getProcInsId())) {
			List<Act> histoicFlowList = actTaskService.histoicFlowList(act.getProcInsId(), startAct, endAct);
			model.addAttribute("histoicFlowList", histoicFlowList);
		}
		return "modules/act/actTaskHistoricFlow";
	}

	/**
	 * 获取流程流向图
	 * 
	 * @param startAct
	 *            开始活动节点名称
	 * @param endAct
	 *            结束活动节点名称
	 */
	@RequestMapping(value = "flowChart")
	public String flowChart(Act act, String startAct, String endAct, Model model) {
		if (StringUtils.isNotBlank(act.getProcInsId())) {
			List<Act> histoicFlowList = actTaskService.histoicFlowList(act.getProcInsId(), startAct, endAct);
			model.addAttribute("histoicFlowList", histoicFlowList);
		}
		return "modules/act/actTaskFlowChart";
	}

	/**
	 * 获取流程列表
	 * 
	 * @param category
	 *            流程分类
	 */
	@RequestMapping(value = "process")
	public String processList(String category, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Object[]> page = new Page<Object[]>(request, response);
		page = actTaskService.processList(page, category);
		model.addAttribute("page", page);
		model.addAttribute("category", category);
		return "modules/act/actTaskProcessList";
	}

	/**
	 * 获取流程表单
	 */
	@RequestMapping(value = "form")
	public String form(Act act, HttpServletRequest request, Model model) {
		// 获取流程XML上的表单KEY
		String formKey = actTaskService.getFormKey(act.getProcDefId(), act.getTaskDefKey());
		// 获取流程实例对象
		if (act.getProcInsId() != null) {
			if (actTaskService.getProcIns(act.getProcInsId()) != null) {
				act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
			} else {
				act.setFinishedProcIns(actTaskService.getFinishedProcIns(act.getProcInsId()));
			}

		}
		return "redirect:" + ActUtils.getFormUrl(formKey, act);

		// 传递参数到视图
		// String formUrl = ActUtils.getFormUrl(formKey, act);
		// model.addAttribute("act", act);
		// model.addAttribute("formUrl", formUrl);
		// return "modules/act/actTaskForm";
	}

	/**
	 * 启动流程
	 */
	@RequestMapping(value = "start")
	@ResponseBody
	public String start(Act act, String table, String id, Model model) throws Exception {
		actTaskService.startProcess(act.getProcDefKey(), act.getBusinessId(), act.getBusinessTable(), act.getTitle());
		return "true";// adminPath + "/act/task";
	}

	/**
	 * 签收任务
	 */
	@RequestMapping(value = "claim")
	@ResponseBody
	public String claim(Act act) {
		String userId = UserUtils.getUser().getLoginName();// ObjectUtils.toString(UserUtils.getUser().getId());
		actTaskService.claim(act.getTaskId(), userId);
		return "true";// adminPath + "/act/task";
	}

	/**
	 * 完成任务 vars.keys=flag,pass vars.values=1,true vars.types=S,B @see
	 * com.jeeplus.jeeplus.modules.act.utils.PropertyType
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	public String complete(Act act) {
		actTaskService.complete(act.getTaskId(), act.getProcInsId(), act.getComment(), act.getVars().getVariableMap());
		return "true";// adminPath + "/act/task";
	}

	/**
	 * 读取带跟踪的图片
	 */
	@RequestMapping(value = "trace/photo/{procDefId}/{execId}")
	public void tracePhoto(@PathVariable("procDefId") String procDefId, @PathVariable("execId") String execId,
			HttpServletResponse response) throws Exception {
		InputStream imageStream = actTaskService.tracePhoto(procDefId, execId);

		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}

	/**
	 * 输出跟踪流程信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "trace/info/{proInsId}")
	public List<Map<String, Object>> traceInfo(@PathVariable("proInsId") String proInsId) throws Exception {
		List<Map<String, Object>> activityInfos = actTaskService.traceProcess(proInsId);
		return activityInfos;
	}

	/**
	 * 显示流程图
	 * 
	 * @RequestMapping(value = "processPic") public void processPic(String
	 *                       procDefId, HttpServletResponse response) throws
	 *                       Exception { ProcessDefinition procDef =
	 *                       repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
	 *                       String diagramResourceName =
	 *                       procDef.getDiagramResourceName(); InputStream
	 *                       imageStream =
	 *                       repositoryService.getResourceAsStream(procDef.getDeploymentId(),
	 *                       diagramResourceName); byte[] b = new byte[1024]; int
	 *                       len = -1; while ((len = imageStream.read(b, 0, 1024))
	 *                       != -1) { response.getOutputStream().write(b, 0, len); }
	 *                       }
	 */

	/**
	 * 获取跟踪信息
	 * 
	 * @RequestMapping(value = "processMap") public String processMap(String
	 *                       procDefId, String proInstId, Model model) throws
	 *                       Exception { List<ActivityImpl> actImpls = new
	 *                       ArrayList<ActivityImpl>(); ProcessDefinition
	 *                       processDefinition = repositoryService
	 *                       .createProcessDefinitionQuery().processDefinitionId(procDefId)
	 *                       .singleResult(); ProcessDefinitionImpl pdImpl =
	 *                       (ProcessDefinitionImpl) processDefinition; String
	 *                       processDefinitionId = pdImpl.getId();// 流程标识
	 *                       ProcessDefinitionEntity def = (ProcessDefinitionEntity)
	 *                       ((RepositoryServiceImpl) repositoryService)
	 *                       .getDeployedProcessDefinition(processDefinitionId);
	 *                       List<ActivityImpl> activitiList =
	 *                       def.getActivities();// 获得当前任务的所有节点 List<String>
	 *                       activeActivityIds =
	 *                       runtimeService.getActiveActivityIds(proInstId); for
	 *                       (String activeId : activeActivityIds) { for
	 *                       (ActivityImpl activityImpl : activitiList) { String id
	 *                       = activityImpl.getId(); if (activityImpl.isScope()) {
	 *                       if (activityImpl.getActivities().size() > 1) {
	 *                       List<ActivityImpl> subAcList = activityImpl
	 *                       .getActivities(); for (ActivityImpl subActImpl :
	 *                       subAcList) { String subid = subActImpl.getId();
	 *                       System.out.println("subImpl:" + subid); if
	 *                       (activeId.equals(subid)) {// 获得执行到那个节点
	 *                       actImpls.add(subActImpl); break; } } } } if
	 *                       (activeId.equals(id)) {// 获得执行到那个节点
	 *                       actImpls.add(activityImpl); System.out.println(id); } }
	 *                       } model.addAttribute("procDefId", procDefId);
	 *                       model.addAttribute("proInstId", proInstId);
	 *                       model.addAttribute("actImpls", actImpls); return
	 *                       "modules/act/actTaskMap"; }
	 */

	/**
	 * 删除任务
	 * 
	 * @param taskId
	 *            流程实例ID
	 * @param reason
	 *            删除原因
	 */
	@ResponseBody
	@RequiresPermissions("act:process:edit")
	@RequestMapping(value = "deleteTask")
	public AjaxJson deleteTask(String taskId, String reason, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		if (StringUtils.isBlank(reason)) {
			j.setSuccess(false);
			j.setMsg("请填写删除原因");
		} else {
			actTaskService.deleteTask(taskId, reason);
			j.setSuccess(true);
			j.setMsg("删除任务成功，任务ID=" + taskId);
		}
		return j;
	}
}
