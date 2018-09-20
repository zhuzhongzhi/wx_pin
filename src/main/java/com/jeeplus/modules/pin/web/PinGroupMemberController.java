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
import com.jeeplus.modules.pin.entity.PinGroupMember;
import com.jeeplus.modules.pin.service.PinGroupMemberService;

/**
 * 拼团团成员信息Controller
 * @author zzz
 * @version 2018-05-11
 */
@Controller
@RequestMapping(value = "${adminPath}/pin/pinGroupMember")
public class PinGroupMemberController extends BaseController {

	@Autowired
	private PinGroupMemberService pinGroupMemberService;
	
	@ModelAttribute
	public PinGroupMember get(@RequestParam(required=false) String id) {
		PinGroupMember entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pinGroupMemberService.get(id);
		}
		if (entity == null){
			entity = new PinGroupMember();
		}
		return entity;
	}
	
	/**
	 * 团成员信息列表页面
	 */
	@RequiresPermissions("pin:pinGroupMember:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/pin/pinGroupMemberList";
	}
	
		/**
	 * 团成员信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinGroupMember:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PinGroupMember pinGroupMember, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PinGroupMember> page = pinGroupMemberService.findPage(new Page<PinGroupMember>(request, response), pinGroupMember); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑团成员信息表单页面
	 */
	@RequiresPermissions(value={"pin:pinGroupMember:view","pin:pinGroupMember:add","pin:pinGroupMember:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PinGroupMember pinGroupMember, Model model) {
		model.addAttribute("pinGroupMember", pinGroupMember);
		if(StringUtils.isBlank(pinGroupMember.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/pin/pinGroupMemberForm";
	}

	/**
	 * 保存团成员信息
	 */
	@RequiresPermissions(value={"pin:pinGroupMember:add","pin:pinGroupMember:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PinGroupMember pinGroupMember, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, pinGroupMember)){
			return form(pinGroupMember, model);
		}
		//新增或编辑表单保存
		pinGroupMemberService.save(pinGroupMember);//保存
		addMessage(redirectAttributes, "保存团成员信息成功");
		return "redirect:"+Global.getAdminPath()+"/pin/pinGroupMember/?repage";
	}
	
	/**
	 * 删除团成员信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinGroupMember:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PinGroupMember pinGroupMember, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		pinGroupMemberService.delete(pinGroupMember);
		j.setMsg("删除团成员信息成功");
		return j;
	}
	
	/**
	 * 批量删除团成员信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinGroupMember:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			pinGroupMemberService.delete(pinGroupMemberService.get(id));
		}
		j.setMsg("删除团成员信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinGroupMember:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PinGroupMember pinGroupMember, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "团成员信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PinGroupMember> page = pinGroupMemberService.findPage(new Page<PinGroupMember>(request, response, -1), pinGroupMember);
    		new ExportExcel("团成员信息", PinGroupMember.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出团成员信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pin:pinGroupMember:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PinGroupMember> list = ei.getDataList(PinGroupMember.class);
			for (PinGroupMember pinGroupMember : list){
				try{
					pinGroupMemberService.save(pinGroupMember);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条团成员信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条团成员信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入团成员信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinGroupMember/?repage";
    }
	
	/**
	 * 下载导入团成员信息数据模板
	 */
	@RequiresPermissions("pin:pinGroupMember:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "团成员信息数据导入模板.xlsx";
    		List<PinGroupMember> list = Lists.newArrayList(); 
    		new ExportExcel("团成员信息数据", PinGroupMember.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinGroupMember/?repage";
    }

}