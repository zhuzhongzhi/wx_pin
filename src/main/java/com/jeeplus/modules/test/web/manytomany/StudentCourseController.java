/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.web.manytomany;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.jeeplus.modules.test.entity.manytomany.StudentCourse;
import com.jeeplus.modules.test.service.manytomany.StudentCourseService;

/**
 * 学生课程记录Controller
 * 
 * @author lgf
 * @version 2017-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/test/manytomany/studentCourse")
public class StudentCourseController extends BaseController {

	@Autowired
	private StudentCourseService studentCourseService;

	@ModelAttribute
	public StudentCourse get(@RequestParam(required = false) String id) {
		StudentCourse entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = studentCourseService.get(id);
		}
		if (entity == null) {
			entity = new StudentCourse();
		}
		return entity;
	}

	/**
	 * 学生课程记录列表页面
	 */
	@RequiresPermissions("test:manytomany:studentCourse:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "modules/test/manytomany/studentCourseList";
	}

	/**
	 * 学生课程记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("test:manytomany:studentCourse:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(StudentCourse studentCourse, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<StudentCourse> page = studentCourseService.findPage(new Page<StudentCourse>(request, response),
				studentCourse);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑学生课程记录表单页面
	 */
	@RequiresPermissions(value = { "test:manytomany:studentCourse:view", "test:manytomany:studentCourse:add",
			"test:manytomany:studentCourse:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(StudentCourse studentCourse, Model model) {
		model.addAttribute("studentCourse", studentCourse);
		if (StringUtils.isBlank(studentCourse.getId())) {// 如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/test/manytomany/studentCourseForm";
	}

	/**
	 * 保存学生课程记录
	 */
	@RequiresPermissions(value = { "test:manytomany:studentCourse:add",
			"test:manytomany:studentCourse:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(StudentCourse studentCourse, Model model, RedirectAttributes redirectAttributes)
			throws Exception {
		if (!beanValidator(model, studentCourse)) {
			return form(studentCourse, model);
		}
		// 新增或编辑表单保存
		studentCourseService.save(studentCourse);// 保存
		addMessage(redirectAttributes, "保存学生课程记录成功");
		return "redirect:" + Global.getAdminPath() + "/test/manytomany/studentCourse/?repage";
	}

	/**
	 * 删除学生课程记录
	 */
	@ResponseBody
	@RequiresPermissions("test:manytomany:studentCourse:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(StudentCourse studentCourse, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		studentCourseService.delete(studentCourse);
		j.setMsg("删除学生课程记录成功");
		return j;
	}

	/**
	 * 批量删除学生课程记录
	 */
	@ResponseBody
	@RequiresPermissions("test:manytomany:studentCourse:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			studentCourseService.delete(studentCourseService.get(id));
		}
		j.setMsg("删除学生课程记录成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("test:manytomany:studentCourse:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(StudentCourse studentCourse, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "学生课程记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<StudentCourse> page = studentCourseService.findPage(new Page<StudentCourse>(request, response, -1),
					studentCourse);
			new ExportExcel("学生课程记录", StudentCourse.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出学生课程记录记录失败！失败信息：" + e.getMessage());
		}
		return j;
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("test:manytomany:studentCourse:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<StudentCourse> list = ei.getDataList(StudentCourse.class);
			for (StudentCourse studentCourse : list) {
				try {
					studentCourseService.save(studentCourse);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条学生课程记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条学生课程记录记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学生课程记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/test/manytomany/studentCourse/?repage";
	}

	/**
	 * 下载导入学生课程记录数据模板
	 */
	@RequiresPermissions("test:manytomany:studentCourse:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "学生课程记录数据导入模板.xlsx";
			List<StudentCourse> list = Lists.newArrayList();
			new ExportExcel("学生课程记录数据", StudentCourse.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/test/manytomany/studentCourse/?repage";
	}

}