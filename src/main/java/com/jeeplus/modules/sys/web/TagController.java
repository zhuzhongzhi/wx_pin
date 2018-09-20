/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeeplus.core.web.BaseController;

/**
 * 标签Controller
 * 
 * @author jeeplus
 * @version 2016-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/tag")
public class TagController extends BaseController {

	/**
	 * 树结构选择标签（treeselect.tag）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeselect")
	public String treeselect(HttpServletRequest request, Model model) {
		model.addAttribute("url", request.getParameter("url")); // 树结构数据URL
		model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
		model.addAttribute("isAll", request.getParameter("isAll")); // 是否读取全部数据，不进行权限过滤
		model.addAttribute("allowSearch", request.getParameter("allowSearch")); // 是否显示查找
		return "modules/common/tagTreeselect";
	}

	/**
	 * 图标选择标签（iconselect.tag）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "iconselect")
	public String iconselect(HttpServletRequest request, Model model) {
		model.addAttribute("value", request.getParameter("value"));
		return "modules/common/tagIconselect";
	}

	/**
	 * 选择所属类型
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "gridselect")
	public String gridselect(String url, String fieldLabels, String fieldKeys, String searchLabels, String searchKeys,
			String isMultiSelected, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
			searchLabels = URLDecoder.decode(searchLabels, "UTF-8");
			searchKeys = URLDecoder.decode(searchKeys, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("isMultiSelected", isMultiSelected);
		model.addAttribute("fieldLabels", fieldLabels.split("\\|"));
		model.addAttribute("fieldKeys", fieldKeys.split("\\|"));
		model.addAttribute("url", url);
		model.addAttribute("searchLabels", searchLabels.split("\\|"));
		model.addAttribute("searchKeys", searchKeys.split("\\|"));
		return "modules/common/gridselect";
	}

}
