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
import com.jeeplus.modules.pin.entity.PinOrder;
import com.jeeplus.modules.pin.service.PinOrderService;

/**
 * 拼团订单信息Controller
 * @author zzz
 * @version 2018-05-15
 */
@Controller
@RequestMapping(value = "${adminPath}/pin/pinOrder")
public class PinOrderController extends BaseController {

	@Autowired
	private PinOrderService pinOrderService;
	
	@ModelAttribute
	public PinOrder get(@RequestParam(required=false) String id) {
		PinOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pinOrderService.get(id);
		}
		if (entity == null){
			entity = new PinOrder();
		}
		return entity;
	}
	
	/**
	 * 订单管理列表页面
	 */
	@RequiresPermissions("pin:pinOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/pin/pinOrderList";
	}
	
		/**
	 * 订单管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinOrder:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PinOrder pinOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PinOrder> page = pinOrderService.findPage(new Page<PinOrder>(request, response), pinOrder); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑订单管理表单页面
	 */
	@RequiresPermissions(value={"pin:pinOrder:view","pin:pinOrder:add","pin:pinOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PinOrder pinOrder, Model model) {
		model.addAttribute("pinOrder", pinOrder);
		if(StringUtils.isBlank(pinOrder.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/pin/pinOrderForm";
	}

	/**
	 * 保存订单管理
	 */
	@RequiresPermissions(value={"pin:pinOrder:add","pin:pinOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PinOrder pinOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, pinOrder)){
			return form(pinOrder, model);
		}
		//新增或编辑表单保存
		pinOrderService.save(pinOrder);//保存
		addMessage(redirectAttributes, "保存订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/pin/pinOrder/?repage";
	}
	
	/**
	 * 删除订单管理
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinOrder:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PinOrder pinOrder, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		pinOrderService.delete(pinOrder);
		j.setMsg("删除订单管理成功");
		return j;
	}
	
	/**
	 * 批量删除订单管理
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinOrder:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			pinOrderService.delete(pinOrderService.get(id));
		}
		j.setMsg("删除订单管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinOrder:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PinOrder pinOrder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "订单管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PinOrder> page = pinOrderService.findPage(new Page<PinOrder>(request, response, -1), pinOrder);
    		new ExportExcel("订单管理", PinOrder.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出订单管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pin:pinOrder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PinOrder> list = ei.getDataList(PinOrder.class);
			for (PinOrder pinOrder : list){
				try{
					pinOrderService.save(pinOrder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条订单管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入订单管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinOrder/?repage";
    }
	
	/**
	 * 下载导入订单管理数据模板
	 */
	@RequiresPermissions("pin:pinOrder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单管理数据导入模板.xlsx";
    		List<PinOrder> list = Lists.newArrayList(); 
    		new ExportExcel("订单管理数据", PinOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinOrder/?repage";
    }

}