package com.jeeplus.modules.sys.utils;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.base.ExceptionUtil;
import com.jeeplus.modules.param.entity.SysParams;
import com.jeeplus.modules.param.mapper.SysParamsMapper;
import com.jeeplus.common.utils.CacheUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数表工具类 Created by ysisl on 17/11/25.
 */
public class ParamUtils {

	public static final String CACHE_SYS_PARMAS = "sysParamsMap";

	private static SysParamsMapper paramsMap = SpringContextHolder.getBean(SysParamsMapper.class);

	/**
	 * 从缓存中获取系统参数值
	 * 
	 * @param key
	 * @return
	 */
	public static String getParamByKey(String key) {

		Map<String, String> maps = (Map<String, String>) CacheUtils.get(CACHE_SYS_PARMAS);
		if (maps == null) {
			maps = new HashMap<String, String>();
			for (SysParams param : paramsMap.findAllList(new SysParams())) {
				maps.put(param.getCode(), param.getValue());
			}
			CacheUtils.put(CACHE_SYS_PARMAS, maps);
			return maps.get(key);
		} else {
			return maps.get(key);
		}
	}

	/**
	 * 清除缓存
	 * 
	 * @return
	 */
	public static void clearCache() {
		CacheUtils.remove(CACHE_SYS_PARMAS);
	}

	/**
	 * 设置键值
	 * 
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void setParamByKey(String key, String value) throws Exception {

		SysParams param = paramsMap.findUniqueByProperty("code", key);
		if (param != null) {
			param.setValue(value);
			paramsMap.update(param); // 保存到数据库
			Map<String, String> maps = (Map<String, String>) CacheUtils.get(CACHE_SYS_PARMAS);
			if (maps != null)
				maps.put(key, value); // 刷新最新值到内存
			else
				getParamByKey(key); // 重新加载到内存

		} else
			throw new Exception("未找到对应的键名【" + key + "】");
	}

	public static String getParamHard(String key) {
		SysParams param = paramsMap.findUniqueByProperty("code", key);
		if (param != null)
			return param.getValue();
		else
			return null;
	}

}
