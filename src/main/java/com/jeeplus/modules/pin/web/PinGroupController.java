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
import com.jeeplus.modules.pin.entity.PinGroup;
import com.jeeplus.modules.pin.service.PinGroupService;

/**
 * 拼团团信息Controller
 * @author zzz
 * @version 2018-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/pin/pinGroup")
public class PinGroupController extends BaseController {

	@Autowired
	private PinGroupService pinGroupService;
	
	@ModelAttribute
	public PinGroup get(@RequestParam(required=false) String id) {
		PinGroup entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pinGroupService.get(id);
		}
		if (entity == null){
			entity = new PinGroup();
		}
		return entity;
	}
	
	/**
	 * 团信息列表页面
	 */
	@RequiresPermissions("pin:pinGroup:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/pin/pinGroupList";
	}
	
		/**
	 * 团信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinGroup:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PinGroup pinGroup, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PinGroup> page = pinGroupService.findPage(new Page<PinGroup>(request, response), pinGroup); 
		List<PinGroup> gList = page.getList();
		for (PinGroup g:gList) {
			if (g.getStatus().equals(PinGroup.STATUS_WAIT))
			{
				g.setStatus(PinGroup.STATUS_WAIT_DES);
			} else if (g.getStatus().equals(PinGroup.STATUS_OK)) {
				g.setStatus(PinGroup.STATUS_OK_DES);
			} else if (g.getStatus().equals(PinGroup.STATUS_EXPIRE)) {
				g.setStatus(PinGroup.STATUS_EXPIRE_DES);
			} else if (g.getStatus().equals(PinGroup.STATUS_REFUND)) {
				g.setStatus(PinGroup.STATUS_REFUND_DES);
			} 
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑团信息表单页面
	 */
	@RequiresPermissions(value={"pin:pinGroup:view","pin:pinGroup:add","pin:pinGroup:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PinGroup pinGroup, Model model) {
		model.addAttribute("pinGroup", pinGroup);
		if(StringUtils.isBlank(pinGroup.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/pin/pinGroupForm";
	}

	/**
	 * 保存团信息
	 */
	@RequiresPermissions(value={"pin:pinGroup:add","pin:pinGroup:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PinGroup pinGroup, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, pinGroup)){
			return form(pinGroup, model);
		}
		//新增或编辑表单保存
		if (pinGroup.getStatus().equals(PinGroup.STATUS_WAIT_DES))
		{
			pinGroup.setStatus(PinGroup.STATUS_WAIT);
		} else if (pinGroup.getStatus().equals(PinGroup.STATUS_OK_DES)) {
			pinGroup.setStatus(PinGroup.STATUS_OK);
		} else if (pinGroup.getStatus().equals(PinGroup.STATUS_EXPIRE_DES)) {
			pinGroup.setStatus(PinGroup.STATUS_EXPIRE);
		} else if (pinGroup.getStatus().equals(PinGroup.STATUS_REFUND_DES)) {
			pinGroup.setStatus(PinGroup.STATUS_REFUND);
		} 
		pinGroupService.save(pinGroup);//保存
		addMessage(redirectAttributes, "保存团信息成功");
		return "redirect:"+Global.getAdminPath()+"/pin/pinGroup/?repage";
	}
	
	/**
	 * 删除团信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinGroup:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PinGroup pinGroup, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		pinGroupService.delete(pinGroup);
		j.setMsg("删除团信息成功");
		return j;
	}
	
	/**
	 * 批量删除团信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinGroup:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			pinGroupService.delete(pinGroupService.get(id));
		}
		j.setMsg("删除团信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinGroup:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PinGroup pinGroup, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "团信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PinGroup> page = pinGroupService.findPage(new Page<PinGroup>(request, response, -1), pinGroup);
    		new ExportExcel("团信息", PinGroup.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出团信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pin:pinGroup:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PinGroup> list = ei.getDataList(PinGroup.class);
			for (PinGroup pinGroup : list){
				try{
					pinGroupService.save(pinGroup);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条团信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条团信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入团信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinGroup/?repage";
    }
	
	/**
	 * 下载导入团信息数据模板
	 */
	@RequiresPermissions("pin:pinGroup:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "团信息数据导入模板.xlsx";
    		List<PinGroup> list = Lists.newArrayList(); 
    		new ExportExcel("团信息数据", PinGroup.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinGroup/?repage";
    }

}