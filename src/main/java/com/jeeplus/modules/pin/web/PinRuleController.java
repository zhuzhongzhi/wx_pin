/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pin.web;

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
import com.jeeplus.modules.pin.entity.PinRule;
import com.jeeplus.modules.pin.service.PinRuleService;

/**
 * 拼团规则信息Controller
 * @author zzz
 * @version 2018-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/pin/pinRule")
public class PinRuleController extends BaseController {

	@Autowired
	private PinRuleService pinRuleService;
	
	@ModelAttribute
	public PinRule get(@RequestParam(required=false) String id) {
		PinRule entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pinRuleService.get(id);
		}
		if (entity == null){
			entity = new PinRule();
		}
		return entity;
	}
	
	/**
	 * 规则信息列表页面
	 */
	@RequiresPermissions("pin:pinRule:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/pin/pinRuleList";
	}
	
		/**
	 * 规则信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinRule:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PinRule pinRule, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PinRule> page = pinRuleService.findPage(new Page<PinRule>(request, response), pinRule); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑规则信息表单页面
	 */
	@RequiresPermissions(value={"pin:pinRule:view","pin:pinRule:add","pin:pinRule:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PinRule pinRule, Model model) {
		model.addAttribute("pinRule", pinRule);
		if(StringUtils.isBlank(pinRule.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/pin/pinRuleForm";
	}

	/**
	 * 保存规则信息
	 */
	@RequiresPermissions(value={"pin:pinRule:add","pin:pinRule:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PinRule pinRule, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, pinRule)){
			return form(pinRule, model);
		}
		//新增或编辑表单保存
		pinRuleService.save(pinRule);//保存
		addMessage(redirectAttributes, "保存规则信息成功");
		return "redirect:"+Global.getAdminPath()+"/pin/pinRule/?repage";
	}
	
	/**
	 * 删除规则信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinRule:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PinRule pinRule, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		pinRuleService.delete(pinRule);
		j.setMsg("删除规则信息成功");
		return j;
	}
	
	/**
	 * 批量删除规则信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinRule:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			pinRuleService.delete(pinRuleService.get(id));
		}
		j.setMsg("删除规则信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinRule:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PinRule pinRule, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "规则信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PinRule> page = pinRuleService.findPage(new Page<PinRule>(request, response, -1), pinRule);
    		new ExportExcel("规则信息", PinRule.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出规则信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pin:pinRule:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PinRule> list = ei.getDataList(PinRule.class);
			for (PinRule pinRule : list){
				try{
					pinRuleService.save(pinRule);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条规则信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条规则信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入规则信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinRule/?repage";
    }
	
	/**
	 * 下载导入规则信息数据模板
	 */
	@RequiresPermissions("pin:pinRule:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "规则信息数据导入模板.xlsx";
    		List<PinRule> list = Lists.newArrayList(); 
    		new ExportExcel("规则信息数据", PinRule.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinRule/?repage";
    }

}