package com.juxiao.xchat.base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

import java.util.Arrays;
import java.util.List;

/**
 * 对象工具类
 * 
 * @class: PojoUtils
 * @author: chenjunsheng
 * @date 2018年4月26日
 */
public final class PojoUtils {
	private PojoUtils() {
	}

	/**
	 * 把一个Bean对象转换成为key1=value1&key2=value2格式
	 * 
	 * @author: chenjunsheng
	 * @date 2018年4月26日
	 * @param object
	 * @param ignoreFields
	 * @return
	 */
	public static String keyValuePair(Object object, String... ignoreFields) {
		if (object == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		String text;
		if (ignoreFields != null && ignoreFields.length > 0) {
			final List<String> list = Arrays.asList(ignoreFields);
			text = JSON.toJSONString(object, (PropertyPreFilter) (serializer, object1, name) -> !list.contains(name));
		} else {
			text = JSON.toJSONString(object);
		}

		JSONObject jsonObj = JSONObject.parseObject(text);
		Object[] keys = jsonObj.keySet().toArray();
		if (keys == null || keys.length == 0) {
			return null;
		}

		Arrays.sort(keys);
		for (int i = 0; i < keys.length; i++) {
			builder.append(keys[i]).append("=").append(jsonObj.get(keys[i])).append("&");
		}

		if (builder.length() > 0) {
			builder.setLength(builder.length() - 1);
		}
		return builder.toString();
	}

}
