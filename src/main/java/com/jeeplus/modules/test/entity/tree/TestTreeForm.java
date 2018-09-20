/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.entity.tree;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 组织机构Entity
 * 
 * @author liugf
 * @version 2017-06-11
 */
public class TestTreeForm extends TreeEntity<TestTreeForm> {

	private static final long serialVersionUID = 1L;

	public TestTreeForm() {
		super();
	}

	public TestTreeForm(String id) {
		super(id);
	}

	public TestTreeForm getParent() {
		return parent;
	}

	@Override
	public void setParent(TestTreeForm parent) {
		this.parent = parent;

	}

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}