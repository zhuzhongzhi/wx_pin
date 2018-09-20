package com.jeeplus.modules.sys.utils;

import java.util.*;

public class SignUtil {
	/**
	 * 
	 * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
	 * 实现步骤: <br>
	 * 
	 * @param paraMap
	 *            要排序的Map对象
	 * @param urlEncode
	 *            是否需要URLENCODE
	 * @param keyToLower
	 *            是否需要将Key转换为全小写 true:key转化成小写，false:不转化
	 * @return
	 */
	public static String buildSign(Map<String, String> map, String key) {
		Set<Map.Entry<String, String>> set = map.entrySet();
		StringBuffer sb = new StringBuffer();
		Iterator<Map.Entry<String, String>> it = set.iterator();
		sb.append((it.next()).getValue());
		// 取出排序后的参数，逐一连接起来
		for (Iterator<Map.Entry<String, String>> item = it; item.hasNext();) {
			Map.Entry<String, String> me = item.next();
			sb.append("" + me.getValue());
		}
		sb.append(key);
		return sb.toString();
	}

	/**
	 * 根据签名算法获取Sign值，算法如下： 取所有url中的请求参数字段值及POST请求报文的jsonString(整个post
	 * body作为一个string) 按ASCII 码从小到大 排序(字典序)后,使用签名的格式(即 value1&value2)拼接成字符串string1
	 * 在string1最后拼接上KEY (微众提供的 签名KEY)得到stringSignTemp字符串
	 * 并对stringSignTemp进行sha256运算,得到sign值signValue
	 * 
	 * @param appid
	 *            微众商户接口Appid
	 * @param version
	 *            微众接口版本
	 * @param bodyStr
	 *            推送内容
	 * @return
	 */
	public static String getSign(String appid, String key, String version, String bodyStr) {
		// appid = "W4770821";
		// version = "1.0.0";
		// key = "cMciHirZms349YTgXEZCAeAYw0wwBHlf";
		// bodyStr = "{\"randNum\": \"1234567890123451\", \"unionid\":
		// \"o2aJxwD4TlQKKbarsEAwSrTQ9igw\", \"userId\": \"1109000001742\", \"phone\":
		// \"18273712869\"}";
		// Map<String, String> map = new TreeMap<String, String>();
		// map.put("appid",appid);
		// map.put("version",version);
		// map.put("body",bodyStr);
		// String signTemp = buildSign(map,key);
		// return SHA256Util.getSHA256StrJava(signTemp);
		List<String> forSignList = new ArrayList<String>();
		forSignList.add(appid);
		forSignList.add(version);
		forSignList.add(bodyStr);

		forSignList.removeAll(Collections.singleton(null));
		Collections.sort(forSignList);

		StringBuilder sb = new StringBuilder();
		for (String s : forSignList) {
			sb.append(s);
		}
		sb.append(key);

		System.out.println("sb-->" + sb);
		System.out.println("sb.toString()-->" + SHA256Util.getSHA256StrJava(sb.toString()));
		return SHA256Util.getSHA256StrJava(sb.toString());

	}
}
