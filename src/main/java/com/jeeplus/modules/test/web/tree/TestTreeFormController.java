/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.web.tree;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.config.Global;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.test.entity.tree.TestTreeForm;
import com.jeeplus.modules.test.service.tree.TestTreeFormService;

/**
 * 组织机构Controller
 * 
 * @author liugf
 * @version 2017-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/test/tree/testTreeForm")
public class TestTreeFormController extends BaseController {

	@Autowired
	private TestTreeFormService testTreeFormService;

	@ModelAttribute
	public TestTreeForm get(@RequestParam(required = false) String id) {
		TestTreeForm entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = testTreeFormService.get(id);
		}
		if (entity == null) {
			entity = new TestTreeForm();
		}
		return entity;
	}

	/**
	 * 机构列表页面
	 */
	@RequiresPermissions("test:tree:testTreeForm:list")
	@RequestMapping(value = { "list", "" })
	public String list(TestTreeForm testTreeForm, @ModelAttribute("parentIds") String parentIds,
			HttpServletRequest request, HttpServletResponse response, Model model) {

		if (StringUtils.isNotBlank(parentIds)) {
			model.addAttribute("parentIds", parentIds);
		}

		return "modules/test/tree/testTreeFormList";
	}

	/**
	 * 查看，增加，编辑机构表单页面
	 */
	@RequiresPermissions(value = { "test:tree:testTreeForm:view", "test:tree:testTreeForm:add",
			"test:tree:testTreeForm:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(TestTreeForm testTreeForm, Model model) {
		if (testTreeForm.getParent() != null && StringUtils.isNotBlank(testTreeForm.getParent().getId())) {
			testTreeForm.setParent(testTreeFormService.get(testTreeForm.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(testTreeForm.getId())) {
				TestTreeForm testTreeFormChild = new TestTreeForm();
				testTreeFormChild.setParent(new TestTreeForm(testTreeForm.getParent().getId()));
				List<TestTreeForm> list = testTreeFormService.findList(testTreeForm);
				if (list.size() > 0) {
					testTreeForm.setSort(list.get(list.size() - 1).getSort());
					if (testTreeForm.getSort() != null) {
						testTreeForm.setSort(testTreeForm.getSort() + 30);
					}
				}
			}
		}
		if (testTreeForm.getSort() == null) {
			testTreeForm.setSort(30);
		}
		model.addAttribute("testTreeForm", testTreeForm);
		return "modules/test/tree/testTreeFormForm";
	}

	/**
	 * 保存机构
	 */
	@RequiresPermissions(value = { "test:tree:testTreeForm:add", "test:tree:testTreeForm:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(TestTreeForm testTreeForm, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, testTreeForm)) {
			return form(testTreeForm, model);
		}

		// 新增或编辑表单保存
		testTreeFormService.save(testTreeForm);// 保存
		redirectAttributes.addFlashAttribute("parentIds", testTreeForm.getParentIds());
		addMessage(redirectAttributes, "保存机构成功");
		return "redirect:" + Global.getAdminPath() + "/test/tree/testTreeForm/?repage";
	}

	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<TestTreeForm> getChildren(String parentId) {
		if ("-1".equals(parentId)) {// 如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return testTreeFormService.getChildren(parentId);
	}

	/**
	 * 删除机构
	 */
	@ResponseBody
	@RequiresPermissions("test:tree:testTreeForm:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TestTreeForm testTreeForm, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		testTreeFormService.delete(testTreeForm);
		j.setSuccess(true);
		j.setMsg("删除机构成功");
		return j;
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<TestTreeForm> list = testTreeFormService.findList(new TestTreeForm());
		for (int i = 0; i < list.size(); i++) {
			TestTreeForm e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("text", e.getName());
				if (StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())) {
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				} else {
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

}