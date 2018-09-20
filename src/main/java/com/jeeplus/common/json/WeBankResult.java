package com.jeeplus.common.json;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeeplus.core.mapper.JsonMapper;

public class WeBankResult {
	private String code = "0";// 错误代码
	private String msg = "操作成功";// 提示信息
	private String responseTime = "";

	public WeBankResult() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		responseTime = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
	}

	private LinkedHashMap<String, Object> body = new LinkedHashMap<String, Object>();// 封装json的map

	public LinkedHashMap<String, Object> getBody() {
		return body;
	}

	public void setBody(LinkedHashMap<String, Object> body) {
		this.body = body;
	}

	public void put(String key, Object value) {// 向json中添加属性，在js中访问，请调用data.map.key
		body.put(key, value);
	}

	public void remove(String key) {
		body.remove(key);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {// 向json中添加属性，在js中访问，请调用data.msg
		this.msg = msg;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {// 向json中添加属性，在js中访问，请调用data.msg
		this.responseTime = responseTime;
	}

	@JsonIgnore // 返回对象时忽略此属性
	public String getJsonStr() {// 返回json字符串数组，将访问msg和key的方式统一化，都使用data.key的方式直接访问。

		String json = JsonMapper.getInstance().toJson(this);
		return json;
	}

	public void setCode(String errorCode) {
		this.code = errorCode;
	}

	public String getCode() {
		return code;
	}
}
