package com.jeeplus.modules.api.util;

import java.util.Random;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.modules.sys.utils.SignUtil;

public class CharaterUtils {

	/**
	 * 根据指定长度生成字母和数字的随机数 0~9的ASCII为48~57 A~Z的ASCII为65~90 a~z的ASCII为97~122
	 **/
	public static String createRandomCharData(int length) {
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();// 随机用以下三个随机生成器
		Random randdata = new Random();
		int data = 0;
		for (int i = 0; i < length; i++) {
			int index = rand.nextInt(3);
			// 目的是随机选择生成数字，大小写字母
			switch (index) {
			case 0:
				data = randdata.nextInt(10);// 仅仅会生成0~9
				sb.append(data);
				break;
			case 1:
				data = randdata.nextInt(26) + 65;// 保证只会产生65~90之间的整数
				sb.append((char) data);
				break;
			case 2:
				data = randdata.nextInt(26) + 97;// 保证只会产生97~122之间的整数
				sb.append((char) data);
				break;
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {

		JSONObject postData = new JSONObject();
		postData.put("file_type", "GIFTCARD_ACTIVE");
		postData.put("work_date", "20180102");
		postData.put("rand_num", "BlV1M5v8uP6GNNZF");
		String obj = postData.toJSONString();
		System.out.println(obj);
		String sign = SignUtil.getSign("W6570452", "8GcebViuYwMbkO6kvJKK0GHZHmutduGF", "1.0.0", obj);
		System.out.println(sign);
		System.out.println(createRandomCharData(16));
	}
}
