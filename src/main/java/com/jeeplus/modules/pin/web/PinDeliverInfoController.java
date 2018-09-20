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
import com.jeeplus.modules.pin.entity.PinDeliverInfo;
import com.jeeplus.modules.pin.entity.PinGroup;
import com.jeeplus.modules.pin.service.PinDeliverInfoService;
import com.jeeplus.modules.pin.service.PinGroupService;

/**
 * 拼团递送信息Controller
 * @author zzz
 * @version 2018-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/pin/pinDeliverInfo")
public class PinDeliverInfoController extends BaseController {

	@Autowired
	private PinDeliverInfoService pinDeliverInfoService;
	
	@Autowired
	private PinGroupService pinGroupService;
	
	@ModelAttribute
	public PinDeliverInfo get(@RequestParam(required=false) String id) {
		PinDeliverInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pinDeliverInfoService.get(id);
		}
		if (entity == null){
			entity = new PinDeliverInfo();
		}
		return entity;
	}
	
	/**
	 * 递送信息列表页面
	 */
	@RequiresPermissions("pin:pinDeliverInfo:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/pin/pinDeliverInfoList";
	}
	
		/**
	 * 递送信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinDeliverInfo:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PinDeliverInfo pinDeliverInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PinDeliverInfo> page = pinDeliverInfoService.findPage(new Page<PinDeliverInfo>(request, response), pinDeliverInfo); 
		List<PinDeliverInfo> dList = page.getList();
		for (PinDeliverInfo d:dList) {
			if (d.getDeliverType().equals(PinDeliverInfo.TYPE_MASTER)) {
				d.setDeliverType(PinDeliverInfo.TYPE_MASTER_DES);
			} else if (d.getDeliverType().equals(PinDeliverInfo.TYPE_OWNER)) {
				d.setDeliverType(PinDeliverInfo.TYPE_OWNER_DES);
			}
			
			PinGroup g = pinGroupService.get(d.getGroupId());
			if (g.getStatus().equals(PinGroup.STATUS_WAIT))
			{
				d.setGroupStatus(PinGroup.STATUS_WAIT_DES);
			} else if (g.getStatus().equals(PinGroup.STATUS_OK)) {
				d.setGroupStatus(PinGroup.STATUS_OK_DES);
			} else if (g.getStatus().equals(PinGroup.STATUS_EXPIRE)) {
				d.setGroupStatus(PinGroup.STATUS_EXPIRE_DES);
			} else if (g.getStatus().equals(PinGroup.STATUS_REFUND)) {
				d.setGroupStatus(PinGroup.STATUS_REFUND_DES);
			} 
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑递送信息表单页面
	 */
	@RequiresPermissions(value={"pin:pinDeliverInfo:view","pin:pinDeliverInfo:add","pin:pinDeliverInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PinDeliverInfo pinDeliverInfo, Model model) {
		model.addAttribute("pinDeliverInfo", pinDeliverInfo);
		if(StringUtils.isBlank(pinDeliverInfo.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/pin/pinDeliverInfoForm";
	}

	/**
	 * 保存递送信息
	 */
	@RequiresPermissions(value={"pin:pinDeliverInfo:add","pin:pinDeliverInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PinDeliverInfo pinDeliverInfo, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, pinDeliverInfo)){
			return form(pinDeliverInfo, model);
		}
		if (pinDeliverInfo.getDeliverType().equals(PinDeliverInfo.TYPE_MASTER_DES)) {
			pinDeliverInfo.setDeliverType(PinDeliverInfo.TYPE_MASTER);
		} else if (pinDeliverInfo.getDeliverType().equals(PinDeliverInfo.TYPE_OWNER_DES)) {
			pinDeliverInfo.setDeliverType(PinDeliverInfo.TYPE_OWNER);
		}
		
		//新增或编辑表单保存
		pinDeliverInfoService.save(pinDeliverInfo);//保存
		addMessage(redirectAttributes, "保存递送信息成功");
		return "redirect:"+Global.getAdminPath()+"/pin/pinDeliverInfo/?repage";
	}
	
	/**
	 * 删除递送信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinDeliverInfo:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PinDeliverInfo pinDeliverInfo, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		pinDeliverInfoService.delete(pinDeliverInfo);
		j.setMsg("删除递送信息成功");
		return j;
	}
	
	/**
	 * 批量删除递送信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinDeliverInfo:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			pinDeliverInfoService.delete(pinDeliverInfoService.get(id));
		}
		j.setMsg("删除递送信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinDeliverInfo:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PinDeliverInfo pinDeliverInfo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "递送信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PinDeliverInfo> page = pinDeliverInfoService.findPage(new Page<PinDeliverInfo>(request, response, -1), pinDeliverInfo);
    		new ExportExcel("递送信息", PinDeliverInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出递送信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pin:pinDeliverInfo:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PinDeliverInfo> list = ei.getDataList(PinDeliverInfo.class);
			for (PinDeliverInfo pinDeliverInfo : list){
				try{
					pinDeliverInfoService.save(pinDeliverInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条递送信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条递送信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入递送信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinDeliverInfo/?repage";
    }
	
	/**
	 * 下载导入递送信息数据模板
	 */
	@RequiresPermissions("pin:pinDeliverInfo:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "递送信息数据导入模板.xlsx";
    		List<PinDeliverInfo> list = Lists.newArrayList(); 
    		new ExportExcel("递送信息数据", PinDeliverInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinDeliverInfo/?repage";
    }

}