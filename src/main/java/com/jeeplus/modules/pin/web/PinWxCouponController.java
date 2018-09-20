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
import com.jeeplus.modules.pin.entity.PinWxCoupon;
import com.jeeplus.modules.pin.service.PinWxCouponService;

/**
 * 拼团微信优惠券Controller
 * @author zzz
 * @version 2018-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/pin/pinWxCoupon")
public class PinWxCouponController extends BaseController {

	@Autowired
	private PinWxCouponService pinWxCouponService;
	
	@ModelAttribute
	public PinWxCoupon get(@RequestParam(required=false) String id) {
		PinWxCoupon entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = pinWxCouponService.get(id);
		}
		if (entity == null){
			entity = new PinWxCoupon();
		}
		return entity;
	}
	
	/**
	 * 优惠券管理列表页面
	 */
	@RequiresPermissions("pin:pinWxCoupon:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/pin/pinWxCouponList";
	}
	
		/**
	 * 优惠券管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinWxCoupon:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PinWxCoupon pinWxCoupon, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PinWxCoupon> page = pinWxCouponService.findPage(new Page<PinWxCoupon>(request, response), pinWxCoupon); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑优惠券管理表单页面
	 */
	@RequiresPermissions(value={"pin:pinWxCoupon:view","pin:pinWxCoupon:add","pin:pinWxCoupon:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PinWxCoupon pinWxCoupon, Model model) {
		model.addAttribute("pinWxCoupon", pinWxCoupon);
		if(StringUtils.isBlank(pinWxCoupon.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/pin/pinWxCouponForm";
	}

	/**
	 * 保存优惠券管理
	 */
	@RequiresPermissions(value={"pin:pinWxCoupon:add","pin:pinWxCoupon:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PinWxCoupon pinWxCoupon, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, pinWxCoupon)){
			return form(pinWxCoupon, model);
		}
		//新增或编辑表单保存
		pinWxCouponService.save(pinWxCoupon);//保存
		addMessage(redirectAttributes, "保存优惠券管理成功");
		return "redirect:"+Global.getAdminPath()+"/pin/pinWxCoupon/?repage";
	}
	
	/**
	 * 删除优惠券管理
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinWxCoupon:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PinWxCoupon pinWxCoupon, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		pinWxCouponService.delete(pinWxCoupon);
		j.setMsg("删除优惠券管理成功");
		return j;
	}
	
	/**
	 * 批量删除优惠券管理
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinWxCoupon:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			pinWxCouponService.delete(pinWxCouponService.get(id));
		}
		j.setMsg("删除优惠券管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("pin:pinWxCoupon:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(PinWxCoupon pinWxCoupon, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "优惠券管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PinWxCoupon> page = pinWxCouponService.findPage(new Page<PinWxCoupon>(request, response, -1), pinWxCoupon);
    		new ExportExcel("优惠券管理", PinWxCoupon.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出优惠券管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pin:pinWxCoupon:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PinWxCoupon> list = ei.getDataList(PinWxCoupon.class);
			for (PinWxCoupon pinWxCoupon : list){
				try{
					pinWxCouponService.save(pinWxCoupon);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条优惠券管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条优惠券管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入优惠券管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinWxCoupon/?repage";
    }
	
	/**
	 * 下载导入优惠券管理数据模板
	 */
	@RequiresPermissions("pin:pinWxCoupon:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "优惠券管理数据导入模板.xlsx";
    		List<PinWxCoupon> list = Lists.newArrayList(); 
    		new ExportExcel("优惠券管理数据", PinWxCoupon.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pin/pinWxCoupon/?repage";
    }

}