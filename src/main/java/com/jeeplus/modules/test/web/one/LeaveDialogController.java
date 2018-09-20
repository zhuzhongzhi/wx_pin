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
import com.jeeplus.modules.test.entity.one.LeaveDialog;
import com.jeeplus.modules.test.service.one.LeaveDialogService;

/**
 * 请假表单Controller
 * 
 * @author lgf
 * @version 2017-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/test/one/leaveDialog")
public class LeaveDialogController extends BaseController {

	@Autowired
	private LeaveDialogService leaveDialogService;

	@ModelAttribute
	public LeaveDialog get(@RequestParam(required = false) String id) {
		LeaveDialog entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = leaveDialogService.get(id);
		}
		if (entity == null) {
			entity = new LeaveDialog();
		}
		return entity;
	}

	/**
	 * 请假表单列表页面
	 */
	@RequiresPermissions("test:one:leaveDialog:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "modules/test/one/leaveDialogList";
	}

	/**
	 * 请假表单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("test:one:leaveDialog:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(LeaveDialog leaveDialog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<LeaveDialog> page = leaveDialogService.findPage(new Page<LeaveDialog>(request, response), leaveDialog);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑请假表单表单页面
	 */
	@RequiresPermissions(value = { "test:one:leaveDialog:view", "test:one:leaveDialog:add",
			"test:one:leaveDialog:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(LeaveDialog leaveDialog, Model model) {
		model.addAttribute("leaveDialog", leaveDialog);
		return "modules/test/one/leaveDialogForm";
	}

	/**
	 * 保存请假表单
	 */
	@ResponseBody
	@RequiresPermissions(value = { "test:one:leaveDialog:add", "test:one:leaveDialog:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(LeaveDialog leaveDialog, Model model, RedirectAttributes redirectAttributes) throws Exception {
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, leaveDialog)) {
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		leaveDialogService.save(leaveDialog);// 新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存请假表单成功");
		return j;
	}

	/**
	 * 删除请假表单
	 */
	@ResponseBody
	@RequiresPermissions("test:one:leaveDialog:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(LeaveDialog leaveDialog, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		leaveDialogService.delete(leaveDialog);
		j.setMsg("删除请假表单成功");
		return j;
	}

	/**
	 * 批量删除请假表单
	 */
	@ResponseBody
	@RequiresPermissions("test:one:leaveDialog:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			leaveDialogService.delete(leaveDialogService.get(id));
		}
		j.setMsg("删除请假表单成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("test:one:leaveDialog:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(LeaveDialog leaveDialog, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "请假表单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<LeaveDialog> page = leaveDialogService.findPage(new Page<LeaveDialog>(request, response, -1),
					leaveDialog);
			new ExportExcel("请假表单", LeaveDialog.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	@RequiresPermissions("test:one:leaveDialog:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<LeaveDialog> list = ei.getDataList(LeaveDialog.class);
			for (LeaveDialog leaveDialog : list) {
				try {
					leaveDialogService.save(leaveDialog);
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
		return "redirect:" + Global.getAdminPath() + "/test/one/leaveDialog/?repage";
	}

	/**
	 * 下载导入请假表单数据模板
	 */
	@RequiresPermissions("test:one:leaveDialog:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "请假表单数据导入模板.xlsx";
			List<LeaveDialog> list = Lists.newArrayList();
			new ExportExcel("请假表单数据", LeaveDialog.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/test/one/leaveDialog/?repage";
	}

}