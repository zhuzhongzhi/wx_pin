/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.entity.grid;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 洲Entity
 * 
 * @author lgf
 * @version 2017-06-12
 */
public class TestContinent extends DataEntity<TestContinent> {

	private static final long serialVersionUID = 1L;
	private String name; // 洲名

	public TestContinent() {
		super();
	}

	public TestContinent(String id) {
		super(id);
	}

	@ExcelField(title = "洲名", align = 2, sort = 1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}