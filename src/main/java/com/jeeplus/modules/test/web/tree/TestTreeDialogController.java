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
import com.jeeplus.modules.test.entity.tree.TestTreeDialog;
import com.jeeplus.modules.test.service.tree.TestTreeDialogService;

/**
 * 组织机构Controller
 * 
 * @author liugf
 * @version 2017-06-19
 */
@Controller
@RequestMapping(value = "${adminPath}/test/tree/testTreeDialog")
public class TestTreeDialogController extends BaseController {

	@Autowired
	private TestTreeDialogService testTreeDialogService;

	@ModelAttribute
	public TestTreeDialog get(@RequestParam(required = false) String id) {
		TestTreeDialog entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = testTreeDialogService.get(id);
		}
		if (entity == null) {
			entity = new TestTreeDialog();
		}
		return entity;
	}

	/**
	 * 机构列表页面
	 */
	@RequiresPermissions("test:tree:testTreeDialog:list")
	@RequestMapping(value = { "list", "" })
	public String list(TestTreeDialog testTreeDialog, HttpServletRequest request, HttpServletResponse response,
			Model model) {

		return "modules/test/tree/testTreeDialogList";
	}

	/**
	 * 查看，增加，编辑机构表单页面
	 */
	@RequiresPermissions(value = { "test:tree:testTreeDialog:view", "test:tree:testTreeDialog:add",
			"test:tree:testTreeDialog:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(TestTreeDialog testTreeDialog, Model model) {
		if (testTreeDialog.getParent() != null && StringUtils.isNotBlank(testTreeDialog.getParent().getId())) {
			testTreeDialog.setParent(testTreeDialogService.get(testTreeDialog.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(testTreeDialog.getId())) {
				TestTreeDialog testTreeDialogChild = new TestTreeDialog();
				testTreeDialogChild.setParent(new TestTreeDialog(testTreeDialog.getParent().getId()));
				List<TestTreeDialog> list = testTreeDialogService.findList(testTreeDialog);
				if (list.size() > 0) {
					testTreeDialog.setSort(list.get(list.size() - 1).getSort());
					if (testTreeDialog.getSort() != null) {
						testTreeDialog.setSort(testTreeDialog.getSort() + 30);
					}
				}
			}
		}
		if (testTreeDialog.getSort() == null) {
			testTreeDialog.setSort(30);
		}
		model.addAttribute("testTreeDialog", testTreeDialog);
		return "modules/test/tree/testTreeDialogForm";
	}

	/**
	 * 保存机构
	 */
	@ResponseBody
	@RequiresPermissions(value = { "test:tree:testTreeDialog:add",
			"test:tree:testTreeDialog:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(TestTreeDialog testTreeDialog, Model model, RedirectAttributes redirectAttributes)
			throws Exception {
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, testTreeDialog)) {
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}

		// 新增或编辑表单保存
		testTreeDialogService.save(testTreeDialog);// 保存
		j.setSuccess(true);
		j.put("testTreeDialog", testTreeDialog);
		j.setMsg("保存机构成功");
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<TestTreeDialog> getChildren(String parentId) {
		if ("-1".equals(parentId)) {// 如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
		return testTreeDialogService.getChildren(parentId);
	}

	/**
	 * 删除机构
	 */
	@ResponseBody
	@RequiresPermissions("test:tree:testTreeDialog:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(TestTreeDialog testTreeDialog, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		testTreeDialogService.delete(testTreeDialog);
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
		List<TestTreeDialog> list = testTreeDialogService.findList(new TestTreeDialog());
		for (int i = 0; i < list.size(); i++) {
			TestTreeDialog e = list.get(i);
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