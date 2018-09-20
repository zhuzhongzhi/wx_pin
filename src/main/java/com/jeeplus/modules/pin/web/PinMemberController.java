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
import com.jeeplus.modules.pin.entity.PinMember;
import com.jeeplus.modules.pin.service.PinMemberService;

/**
 * 拼团会员信息Controller
 * @author zzz
 * @version 2018-05-10
 */
@Controller
@RequestMapping(value = "${adminPath}/pin/pinMember")
public class PinMemberController extends BaseController {

	@Autowired
	private PinMemberService pinMemberService;
	
	@ModelAttribute
	public PinMember get(@RequestParam(required=false) String id) {
		PinMember entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pinMemberService.get(id);
		}
		if (entity == null){
			entity = new PinMember();
		}
		return entity;
	}
	
	/**
	 * 会员信息列表页面
	 */
	@RequiresPermissions("pin:pinMember:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/pin/pinMemberList";
	}
	
		/**
	 * 会员信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinMember:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PinMember pinMember, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PinMember> page = pinMemberService.findPage(new Page<PinMember>(request, response), pinMember); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑会员信息表单页面
	 */
	@RequiresPermissions(value={"pin:pinMember:view","pin:pinMember:add","pin:pinMember:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PinMember pinMember, Model model) {
		model.addAttribute("pinMember", pinMember);
		if(StringUtils.isBlank(pinMember.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/pin/pinMemberForm";
	}

	/**
	 * 保存会员信息
	 */
	@RequiresPermissions(value={"pin:pinMember:add","pin:pinMember:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PinMember pinMember, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, pinMember)){
			return form(pinMember, model);
		}
		//新增或编辑表单保存
		pinMemberService.save(pinMember);//保存
		addMessage(redirectAttributes, "保存会员信息成功");
		return "redirect:"+Global.getAdminPath()+"/pin/pinMember/?repage";
	}
	
	/**
	 * 删除会员信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinMember:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PinMember pinMember, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		pinMemberService.delete(pinMember);
		j.setMsg("删除会员信息成功");
		return j;
	}
	
	/**
	 * 批量删除会员信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinMember:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			pinMemberService.delete(pinMemberService.get(id));
		}
		j.setMsg("删除会员信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinMember:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PinMember pinMember, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "会员信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PinMember> page = pinMemberService.findPage(new Page<PinMember>(request, response, -1), pinMember);
    		new ExportExcel("会员信息", PinMember.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出会员信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pin:pinMember:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PinMember> list = ei.getDataList(PinMember.class);
			for (PinMember pinMember : list){
				try{
					pinMemberService.save(pinMember);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条会员信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条会员信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入会员信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinMember/?repage";
    }
	
	/**
	 * 下载导入会员信息数据模板
	 */
	@RequiresPermissions("pin:pinMember:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "会员信息数据导入模板.xlsx";
    		List<PinMember> list = Lists.newArrayList(); 
    		new ExportExcel("会员信息数据", PinMember.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinMember/?repage";
    }

}