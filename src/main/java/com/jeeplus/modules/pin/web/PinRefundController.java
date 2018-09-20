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
import com.jeeplus.modules.pin.entity.PinRefund;
import com.jeeplus.modules.pin.service.PinRefundService;

/**
 * 退款管理Controller
 * @author zzz
 * @version 2018-05-23
 */
@Controller
@RequestMapping(value = "${adminPath}/pin/pinRefund")
public class PinRefundController extends BaseController {

	@Autowired
	private PinRefundService pinRefundService;
	
	@ModelAttribute
	public PinRefund get(@RequestParam(required=false) String id) {
		PinRefund entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pinRefundService.get(id);
		}
		if (entity == null){
			entity = new PinRefund();
		}
		return entity;
	}
	
	/**
	 * 退款管理列表页面
	 */
	@RequiresPermissions("pin:pinRefund:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/pin/pinRefundList";
	}
	
		/**
	 * 退款管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinRefund:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PinRefund pinRefund, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PinRefund> page = pinRefundService.findPage(new Page<PinRefund>(request, response), pinRefund); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑退款管理表单页面
	 */
	@RequiresPermissions(value={"pin:pinRefund:view","pin:pinRefund:add","pin:pinRefund:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PinRefund pinRefund, Model model) {
		model.addAttribute("pinRefund", pinRefund);
		if(StringUtils.isBlank(pinRefund.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/pin/pinRefundForm";
	}

	/**
	 * 保存退款管理
	 */
	@RequiresPermissions(value={"pin:pinRefund:add","pin:pinRefund:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PinRefund pinRefund, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, pinRefund)){
			return form(pinRefund, model);
		}
		//新增或编辑表单保存
		pinRefundService.save(pinRefund);//保存
		addMessage(redirectAttributes, "保存退款管理成功");
		return "redirect:"+Global.getAdminPath()+"/pin/pinRefund/?repage";
	}
	
	/**
	 * 删除退款管理
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinRefund:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PinRefund pinRefund, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		pinRefundService.delete(pinRefund);
		j.setMsg("删除退款管理成功");
		return j;
	}
	
	/**
	 * 批量删除退款管理
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinRefund:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			pinRefundService.delete(pinRefundService.get(id));
		}
		j.setMsg("删除退款管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinRefund:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PinRefund pinRefund, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "退款管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PinRefund> page = pinRefundService.findPage(new Page<PinRefund>(request, response, -1), pinRefund);
    		new ExportExcel("退款管理", PinRefund.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出退款管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pin:pinRefund:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PinRefund> list = ei.getDataList(PinRefund.class);
			for (PinRefund pinRefund : list){
				try{
					pinRefundService.save(pinRefund);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条退款管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条退款管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入退款管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinRefund/?repage";
    }
	
	/**
	 * 下载导入退款管理数据模板
	 */
	@RequiresPermissions("pin:pinRefund:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "退款管理数据导入模板.xlsx";
    		List<PinRefund> list = Lists.newArrayList(); 
    		new ExportExcel("退款管理数据", PinRefund.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinRefund/?repage";
    }

}