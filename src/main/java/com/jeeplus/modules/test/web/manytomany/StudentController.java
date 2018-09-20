/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.web.manytomany;

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
import com.jeeplus.modules.test.entity.manytomany.Student;
import com.jeeplus.modules.test.service.manytomany.StudentService;

/**
 * 学生Controller
 * 
 * @author lgf
 * @version 2017-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/test/manytomany/student")
public class StudentController extends BaseController {

	@Autowired
	private StudentService studentService;

	@ModelAttribute
	public Student get(@RequestParam(required = false) String id) {
		Student entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = studentService.get(id);
		}
		if (entity == null) {
			entity = new Student();
		}
		return entity;
	}

	/**
	 * 学生列表页面
	 */
	@RequiresPermissions("test:manytomany:student:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "modules/test/manytomany/studentList";
	}

	/**
	 * 学生列表数据
	 */
	@ResponseBody
	@RequiresPermissions("test:manytomany:student:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Student student, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<Student> page = studentService.findPage(new Page<Student>(request, response), student);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑学生表单页面
	 */
	@RequiresPermissions(value = { "test:manytomany:student:view", "test:manytomany:student:add",
			"test:manytomany:student:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Student student, Model model) {
		model.addAttribute("student", student);
		if (StringUtils.isBlank(student.getId())) {// 如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/test/manytomany/studentForm";
	}

	/**
	 * 保存学生
	 */
	@RequiresPermissions(value = { "test:manytomany:student:add",
			"test:manytomany:student:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(Student student, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, student)) {
			return form(student, model);
		}
		// 新增或编辑表单保存
		studentService.save(student);// 保存
		addMessage(redirectAttributes, "保存学生成功");
		return "redirect:" + Global.getAdminPath() + "/test/manytomany/student/?repage";
	}

	/**
	 * 删除学生
	 */
	@ResponseBody
	@RequiresPermissions("test:manytomany:student:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Student student, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		studentService.delete(student);
		j.setMsg("删除学生成功");
		return j;
	}

	/**
	 * 批量删除学生
	 */
	@ResponseBody
	@RequiresPermissions("test:manytomany:student:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			studentService.delete(studentService.get(id));
		}
		j.setMsg("删除学生成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("test:manytomany:student:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(Student student, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "学生" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Student> page = studentService.findPage(new Page<Student>(request, response, -1), student);
			new ExportExcel("学生", Student.class).setDataList(page.getList()).write(response, fileName).dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出学生记录失败！失败信息：" + e.getMessage());
		}
		return j;
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("test:manytomany:student:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Student> list = ei.getDataList(Student.class);
			for (Student student : list) {
				try {
					studentService.save(student);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条学生记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条学生记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学生失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/test/manytomany/student/?repage";
	}

	/**
	 * 下载导入学生数据模板
	 */
	@RequiresPermissions("test:manytomany:student:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "学生数据导入模板.xlsx";
			List<Student> list = Lists.newArrayList();
			new ExportExcel("学生数据", Student.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/test/manytomany/student/?repage";
	}

}