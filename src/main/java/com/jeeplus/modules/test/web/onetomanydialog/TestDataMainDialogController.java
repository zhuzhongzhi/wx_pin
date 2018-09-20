/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.web.onetomanydialog;

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
import com.jeeplus.modules.test.entity.onetomanydialog.TestDataMainDialog;
import com.jeeplus.modules.test.service.onetomanydialog.TestDataMainDialogService;

/**
 * 票务代理Controller
 * 
 * @author liugf
 * @version 2017-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/test/onetomanydialog/testDataMainDialog")
public class TestDataMainDialogController extends BaseController {

	@Autowired
	private TestDataMainDialogService testDataMainDialogService;

	@ModelAttribute
	public TestDataMainDialog get(@RequestParam(required = false) String id) {
		TestDataMainDialog entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = testDataMainDialogService.get(id);
		}
		if (entity == null) {
			entity = new TestDataMainDialog();
		}
		return entity;
	}

	/**
	 * 票务代理列表页面
	 */
	@RequiresPermissions("test:onetomanydialog:testDataMainDialog:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "modules/test/onetomanydialog/testDataMainDialogList";
	}

	/**
	 * 票务代理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("test:onetomanydialog:testDataMainDialog:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(TestDataMainDialog testDataMainDialog, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<TestDataMainDialog> page = testDataMainDialogService
				.findPage(new Page<TestDataMainDialog>(request, response), testDataMainDialog);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑票务代理表单页面
	 */
	@RequiresPermissions(value = { "test:onetomanydialog:testDataMainDialog:view",
			"test:onetomanydialog:testDataMainDialog:add",
			"test:onetomanydialog:testDataMainDialog:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(TestDataMainDialog testDataMainDialog, Model model) {
		model.addAttribute("testDataMainDialog", testDataMainDialog);
		return "modules/test/onetomanydialog/testDataMainDialogForm";
	}

	/**
	 * 保存票务代理
	 */
	@ResponseBody
	@RequiresPermissions(value = { "test:onetomanydialog:testDataMainDialog:add",
			"test:onetomanydialog:testDataMainDialog:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TestDataMainDialog testDataMainDialog, Model model, RedirectAttributes redirectAttributes)
			throws Exception {
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, testDataMainDialog)) {
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		// 新增或编辑表单保存
		testDataMainDialogService.save(testDataMainDialog);// 保存
		j.setSuccess(true);
		j.setMsg("保存票务代理成功");
		return j;
	}

	/**
	 * 删除票务代理
	 */
	@ResponseBody
	@RequiresPermissions("test:onetomanydialog:testDataMainDialog:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TestDataMainDialog testDataMainDialog, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		testDataMainDialogService.delete(testDataMainDialog);
		j.setMsg("删除票务代理成功");
		return j;
	}

	/**
	 * 批量删除票务代理
	 */
	@ResponseBody
	@RequiresPermissions("test:onetomanydialog:testDataMainDialog:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			testDataMainDialogService.delete(testDataMainDialogService.get(id));
		}
		j.setMsg("删除票务代理成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("test:onetomanydialog:testDataMainDialog:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(TestDataMainDialog testDataMainDialog, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "票务代理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<TestDataMainDialog> page = testDataMainDialogService
					.findPage(new Page<TestDataMainDialog>(request, response, -1), testDataMainDialog);
			new ExportExcel("票务代理", TestDataMainDialog.class).setDataList(page.getList()).write(response, fileName)
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
	public TestDataMainDialog detail(String id) {
		return testDataMainDialogService.get(id);
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("test:onetomanydialog:testDataMainDialog:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TestDataMainDialog> list = ei.getDataList(TestDataMainDialog.class);
			for (TestDataMainDialog testDataMainDialog : list) {
				try {
					testDataMainDialogService.save(testDataMainDialog);
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
		return "redirect:" + Global.getAdminPath() + "/test/onetomanydialog/testDataMainDialog/?repage";
	}

	/**
	 * 下载导入票务代理数据模板
	 */
	@RequiresPermissions("test:onetomanydialog:testDataMainDialog:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "票务代理数据导入模板.xlsx";
			List<TestDataMainDialog> list = Lists.newArrayList();
			new ExportExcel("票务代理数据", TestDataMainDialog.class, 1).setDataList(list).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/test/onetomanydialog/testDataMainDialog/?repage";
	}

}