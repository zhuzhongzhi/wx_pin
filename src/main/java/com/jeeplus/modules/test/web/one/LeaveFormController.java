/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.web.one;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.test.entity.one.LeaveForm;
import com.jeeplus.modules.test.service.one.LeaveFormService;

/**
 * 请假表单Controller
 * 
 * @author lgf
 * @version 2017-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/test/one/leaveForm")
public class LeaveFormController extends BaseController {

	@Autowired
	private LeaveFormService leaveFormService;

	@ModelAttribute
	public LeaveForm get(@RequestParam(required = false) String id) {
		LeaveForm entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = leaveFormService.get(id);
		}
		if (entity == null) {
			entity = new LeaveForm();
		}
		return entity;
	}

	/**
	 * 请假表单列表页面
	 */
	@RequiresPermissions("test:one:leaveForm:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "modules/test/one/leaveFormList";
	}

	/**
	 * 请假表单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("test:one:leaveForm:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(LeaveForm leaveForm, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<LeaveForm> page = leaveFormService.findPage(new Page<LeaveForm>(request, response), leaveForm);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑请假表单表单页面
	 */
	@RequiresPermissions(value = { "test:one:leaveForm:view", "test:one:leaveForm:add",
			"test:one:leaveForm:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(LeaveForm leaveForm, Model model) {
		model.addAttribute("leaveForm", leaveForm);
		if (StringUtils.isBlank(leaveForm.getId())) {// 如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/test/one/leaveFormForm";
	}

	/**
	 * 保存请假表单
	 */
	@RequiresPermissions(value = { "test:one:leaveForm:add", "test:one:leaveForm:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(LeaveForm leaveForm, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, leaveForm)) {
			return form(leaveForm, model);
		}
		// 新增或编辑表单保存
		leaveFormService.save(leaveForm);// 保存
		addMessage(redirectAttributes, "保存请假表单成功");
		return "redirect:" + Global.getAdminPath() + "/test/one/leaveForm/?repage";
	}

	/**
	 * 删除请假表单
	 */
	@ResponseBody
	@RequiresPermissions("test:one:leaveForm:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(LeaveForm leaveForm, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		leaveFormService.delete(leaveForm);
		j.setMsg("删除请假表单成功");
		return j;
	}

	/**
	 * 批量删除请假表单
	 */
	@ResponseBody
	@RequiresPermissions("test:one:leaveForm:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			leaveFormService.delete(leaveFormService.get(id));
		}
		j.setMsg("删除请假表单成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("test:one:leaveForm:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(LeaveForm leaveForm, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "请假表单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<LeaveForm> page = leaveFormService.findPage(new Page<LeaveForm>(request, response, -1), leaveForm);
			new ExportExcel("请假表单", LeaveForm.class).setDataList(page.getList()).write(response, fileName).dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出请假表单记录失败！失败信息：" + e.getMessage());
		}
		return j;
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("test:one:leaveForm:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<LeaveForm> list = ei.getDataList(LeaveForm.class);
			for (LeaveForm leaveForm : list) {
				try {
					leaveFormService.save(leaveForm);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条请假表单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条请假表单记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入请假表单失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/test/one/leaveForm/?repage";
	}

	/**
	 * 下载导入请假表单数据模板
	 */
	@RequiresPermissions("test:one:leaveForm:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "请假表单数据导入模板.xlsx";
			List<LeaveForm> list = Lists.newArrayList();
			new ExportExcel("请假表单数据", LeaveForm.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/test/one/leaveForm/?repage";
	}

}