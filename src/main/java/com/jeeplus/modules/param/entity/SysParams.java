/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.param.entity;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 系统参数Entity
 * 
 * @author ysisl
 * @version 2017-11-25
 */
public class SysParams extends DataEntity<SysParams> {

	private static final long serialVersionUID = 1L;
	private String type; // 参数类型
	private String name; // 参数功能
	private String code; // 参数代码
	private String value; // 参数键值
	private String remark; // 备注信息

	public SysParams() {
		super();
	}

	public SysParams(String id) {
		super(id);
	}

	@ExcelField(title = "参数类型", dictType = "param_type", align = 2, sort = 5)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ExcelField(title = "参数功能", align = 2, sort = 7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title = "参数代码", align = 2, sort = 8)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ExcelField(title = "参数键值", align = 2, sort = 9)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ExcelField(title = "备注信息", align = 2, sort = 10)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}