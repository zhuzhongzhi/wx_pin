/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.param.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.utils.ParamUtils;
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
import com.jeeplus.modules.param.entity.SysParams;
import com.jeeplus.modules.param.service.SysParamsService;

/**
 * 系统参数Controller
 * 
 * @author ysisl
 * @version 2017-11-25
 */
@Controller
@RequestMapping(value = "${adminPath}/param/sysParams")
public class SysParamsController extends BaseController {

	@Autowired
	private SysParamsService sysParamsService;

	@ModelAttribute
	public SysParams get(@RequestParam(required = false) String id) {
		SysParams entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = sysParamsService.get(id);
		}
		if (entity == null) {
			entity = new SysParams();
		}
		return entity;
	}

	/**
	 * 系统参数列表页面
	 */
	@RequiresPermissions("param:sysParams:list")
	@RequestMapping(value = { "list", "" })
	public String list() {
		return "modules/param/sysParamsList";
	}

	/**
	 * 系统参数列表数据
	 */
	@ResponseBody
	@RequiresPermissions("param:sysParams:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SysParams sysParams, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<SysParams> page = sysParamsService.findPage(new Page<SysParams>(request, response), sysParams);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑系统参数表单页面
	 */
	@RequiresPermissions(value = { "param:sysParams:view", "param:sysParams:add",
			"param:sysParams:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(SysParams sysParams, Model model) {
		model.addAttribute("sysParams", sysParams);
		return "modules/param/sysParamsForm";
	}

	/**
	 * 保存系统参数
	 */
	@ResponseBody
	@RequiresPermissions(value = { "param:sysParams:add", "param:sysParams:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SysParams sysParams, Model model, RedirectAttributes redirectAttributes) throws Exception {
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, sysParams)) {
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		sysParamsService.save(sysParams);// 新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存系统参数成功");
		return j;
	}

	/**
	 * 删除系统参数
	 */
	@ResponseBody
	@RequiresPermissions("param:sysParams:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SysParams sysParams, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		sysParamsService.delete(sysParams);
		j.setMsg("删除系统参数成功");
		return j;
	}

	/**
	 * 批量删除系统参数
	 */
	@ResponseBody
	@RequiresPermissions("param:sysParams:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			sysParamsService.delete(sysParamsService.get(id));
		}
		j.setMsg("删除系统参数成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("param:sysParams:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public AjaxJson exportFile(SysParams sysParams, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "系统参数" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<SysParams> page = sysParamsService.findPage(new Page<SysParams>(request, response, -1), sysParams);
			new ExportExcel("系统参数", SysParams.class).setDataList(page.getList()).write(response, fileName).dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出系统参数记录失败！失败信息：" + e.getMessage());
		}
		return j;
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("param:sysParams:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SysParams> list = ei.getDataList(SysParams.class);
			for (SysParams sysParams : list) {
				try {
					sysParamsService.save(sysParams);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条系统参数记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条系统参数记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入系统参数失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/param/sysParams/?repage";
	}

	/**
	 * 下载导入系统参数数据模板
	 */
	@RequiresPermissions("param:sysParams:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "系统参数数据导入模板.xlsx";
			List<SysParams> list = Lists.newArrayList();
			new ExportExcel("系统参数数据", SysParams.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/param/sysParams/?repage";
	}

}