/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.web.onetomanyform;

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
import com.jeeplus.modules.test.entity.onetomanyform.TestDataMainForm;
import com.jeeplus.modules.test.service.onetomanyform.TestDataMainFormService;

/**
 * 票务代理Controller
 * 
 * @author liugf
 * @version 2017-06-19
 */
@Controller
@RequestMapping(value = "${adminPath}/test/onetomanyform/testDataMainForm")
public class TestDataMainFormController extends BaseController {

	@Autowired
	private TestDataMainFormService testDataMainFormService;

	@ModelAttribute
	public TestDataMainForm get(@RequestParam(required = false) String id) {
		TestDataMainForm entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = testDataMainFormService.get(id);
		}
		if (entity == null) {
			entity = new TestDataMainForm();
		}
		return entity;
	}

	/**
	 * 票务代理列表页面
	 */
	@RequiresPermissions("test:onetomanyform:testDataMainForm:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "modules/test/onetomanyform/testDataMainFormList";
	}

	/**
	 * 票务代理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("test:onetomanyform:testDataMainForm:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TestDataMainForm testDataMainForm, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<TestDataMainForm> page = testDataMainFormService.findPage(new Page<TestDataMainForm>(request, response),
				testDataMainForm);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑票务代理表单页面
	 */
	@RequiresPermissions(value = { "test:onetomanyform:testDataMainForm:view",
			"test:onetomanyform:testDataMainForm:add",
			"test:onetomanyform:testDataMainForm:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(TestDataMainForm testDataMainForm, Model model) {
		model.addAttribute("testDataMainForm", testDataMainForm);
		if (StringUtils.isBlank(testDataMainForm.getId())) {// 如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/test/onetomanyform/testDataMainFormForm";
	}

	/**
	 * 保存票务代理
	 */
	@RequiresPermissions(value = { "test:onetomanyform:testDataMainForm:add",
			"test:onetomanyform:testDataMainForm:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(TestDataMainForm testDataMainForm, Model model, RedirectAttributes redirectAttributes)
			throws Exception {
		if (!beanValidator(model, testDataMainForm)) {
			return form(testDataMainForm, model);
		}
		// 新增或编辑表单保存
		testDataMainFormService.save(testDataMainForm);// 保存
		addMessage(redirectAttributes, "保存票务代理成功");
		return "redirect:" + Global.getAdminPath() + "/test/onetomanyform/testDataMainForm/?repage";
	}

	/**
	 * 删除票务代理
	 */
	@ResponseBody
	@RequiresPermissions("test:onetomanyform:testDataMainForm:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TestDataMainForm testDataMainForm, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		testDataMainFormService.delete(testDataMainForm);
		j.setMsg("删除票务代理成功");
		return j;
	}

	/**
	 * 批量删除票务代理
	 */
	@ResponseBody
	@RequiresPermissions("test:onetomanyform:testDataMainForm:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			testDataMainFormService.delete(testDataMainFormService.get(id));
		}
		j.setMsg("删除票务代理成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("test:onetomanyform:testDataMainForm:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(TestDataMainForm testDataMainForm, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "票务代理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<TestDataMainForm> page = testDataMainFormService
					.findPage(new Page<TestDataMainForm>(request, response, -1), testDataMainForm);
			new ExportExcel("票务代理", TestDataMainForm.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出票务代理记录失败！失败信息：" + e.getMessage());
		}
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "detail")
	public TestDataMainForm detail(String id) {
		return testDataMainFormService.get(id);
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("test:onetomanyform:testDataMainForm:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestDataMainForm> list = ei.getDataList(TestDataMainForm.class);
			for (TestDataMainForm testDataMainForm : list) {
				try {
					testDataMainFormService.save(testDataMainForm);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条票务代理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条票务代理记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入票务代理失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/test/onetomanyform/testDataMainForm/?repage";
	}

	/**
	 * 下载导入票务代理数据模板
	 */
	@RequiresPermissions("test:onetomanyform:testDataMainForm:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "票务代理数据导入模板.xlsx";
			List<TestDataMainForm> list = Lists.newArrayList();
			new ExportExcel("票务代理数据", TestDataMainForm.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/test/onetomanyform/testDataMainForm/?repage";
	}

}