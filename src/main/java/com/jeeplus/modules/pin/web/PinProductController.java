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
import com.jeeplus.modules.pin.entity.PinProduct;
import com.jeeplus.modules.pin.service.PinProductService;

/**
 * 拼团产品信息Controller
 * @author zzz
 * @version 2018-07-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pin/pinProduct")
public class PinProductController extends BaseController {

	@Autowired
	private PinProductService pinProductService;
	
	@ModelAttribute
	public PinProduct get(@RequestParam(required=false) String id) {
		PinProduct entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pinProductService.get(id);
		}
		if (entity == null){
			entity = new PinProduct();
		}
		return entity;
	}
	
	/**
	 * 产品信息列表页面
	 */
	@RequiresPermissions("pin:pinProduct:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/pin/pinProductList";
	}
	
		/**
	 * 产品信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinProduct:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PinProduct pinProduct, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PinProduct> page = pinProductService.findPage(new Page<PinProduct>(request, response), pinProduct); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑产品信息表单页面
	 */
	@RequiresPermissions(value={"pin:pinProduct:view","pin:pinProduct:add","pin:pinProduct:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PinProduct pinProduct, Model model) {
		model.addAttribute("pinProduct", pinProduct);
		if(StringUtils.isBlank(pinProduct.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/pin/pinProductForm";
	}

	/**
	 * 保存产品信息
	 */
	@RequiresPermissions(value={"pin:pinProduct:add","pin:pinProduct:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PinProduct pinProduct, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, pinProduct)){
			return form(pinProduct, model);
		}
		//新增或编辑表单保存
		pinProductService.save(pinProduct);//保存
		addMessage(redirectAttributes, "保存产品信息成功");
		return "redirect:"+Global.getAdminPath()+"/pin/pinProduct/?repage";
	}
	
	/**
	 * 删除产品信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinProduct:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PinProduct pinProduct, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		pinProductService.delete(pinProduct);
		j.setMsg("删除产品信息成功");
		return j;
	}
	
	/**
	 * 批量删除产品信息
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinProduct:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			pinProductService.delete(pinProductService.get(id));
		}
		j.setMsg("删除产品信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinProduct:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PinProduct pinProduct, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "产品信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PinProduct> page = pinProductService.findPage(new Page<PinProduct>(request, response, -1), pinProduct);
    		new ExportExcel("产品信息", PinProduct.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出产品信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pin:pinProduct:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PinProduct> list = ei.getDataList(PinProduct.class);
			for (PinProduct pinProduct : list){
				try{
					pinProductService.save(pinProduct);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条产品信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条产品信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入产品信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinProduct/?repage";
    }
	
	/**
	 * 下载导入产品信息数据模板
	 */
	@RequiresPermissions("pin:pinProduct:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品信息数据导入模板.xlsx";
    		List<PinProduct> list = Lists.newArrayList(); 
    		new ExportExcel("产品信息数据", PinProduct.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinProduct/?repage";
    }

}