/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.entity.manytoone;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 商品类型Entity
 * 
 * @author lf
 * @version 2017-06-11
 */
public class Category extends TreeEntity<Category> {

	private static final long serialVersionUID = 1L;

	public Category() {
		super();
	}

	public Category(String id) {
		super(id);
	}

	public Category getParent() {
		return parent;
	}

	@Override
	public void setParent(Category parent) {
		this.parent = parent;

	}

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}