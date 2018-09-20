/**
 * Copyright &copy; 2015-2020 <a href="http://www.doforsys.com/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.entity.one;

import com.jeeplus.modules.sys.entity.Office;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 请假表单Entity
 * 
 * @author lgf
 * @version 2017-06-11
 */
public class LeaveForm extends DataEntity<LeaveForm> {

	private static final long serialVersionUID = 1L;
	private Office office; // 归属部门
	private User tuser; // 员工
	private String area; // 归属区域
	private Date beginDate; // 请假开始日期
	private Date endDate; // 请假结束日期
	private Date beginBeginDate; // 开始 请假开始日期
	private Date endBeginDate; // 结束 请假开始日期

	public LeaveForm() {
		super();
	}

	public LeaveForm(String id) {
		super(id);
	}

	@NotNull(message = "归属部门不能为空")
	@ExcelField(title = "归属部门", fieldType = Office.class, value = "office.name", align = 2, sort = 1)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@NotNull(message = "员工不能为空")
	@ExcelField(title = "员工", fieldType = User.class, value = "tuser.name", align = 2, sort = 2)
	public User getTuser() {
		return tuser;
	}

	public void setTuser(User tuser) {
		this.tuser = tuser;
	}

	@ExcelField(title = "归属区域", align = 2, sort = 3)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title = "请假开始日期", align = 2, sort = 4)
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title = "请假结束日期", align = 2, sort = 5)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getBeginBeginDate() {
		return beginBeginDate;
	}

	public void setBeginBeginDate(Date beginBeginDate) {
		this.beginBeginDate = beginBeginDate;
	}

	public Date getEndBeginDate() {
		return endBeginDate;
	}

	public void setEndBeginDate(Date endBeginDate) {
		this.endBeginDate = endBeginDate;
	}

}