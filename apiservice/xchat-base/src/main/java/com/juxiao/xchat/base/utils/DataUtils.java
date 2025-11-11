package com.juxiao.xchat.base.utils;

import org.apache.commons.lang.StringUtils;

import org.apache.commons.lang.RandomStringUtils;

import java.util.regex.Pattern;

/**
 * @class: DataUtils
 * @description: 这里用一句话描述这个类的作用
 * @author: chenjunsheng
 * @date 2018年7月28日
 */
public class DataUtils {
	private static final Pattern PATTERN = Pattern.compile("[^0-9]");

	/**
	 * 提取字符串中的数字
	 * 
	 * @param string
	 * @return
	 */
	public static String getNumer(String string) {
		return PATTERN.matcher(string).replaceAll("").trim();
	}

	/**
	 * 根据数据库主键生成随机号码
	 *
	 * @param id
	 * @return
	 */
	public static String randomNmberById(String id) {
		StringBuilder builder = new StringBuilder(id.substring(0, id.length() - 3));
		builder.append(RandomStringUtils.randomNumeric(3));
		builder.append(id.substring(id.length() - 3));
		return builder.toString();
	}


	/**
	 * 去除html标签和js代码
	 */
	public static String escapeHtml(String content) {
		if (StringUtils.isBlank(content)) {
			return "";
		}

		return content.replaceAll("<((\"[^\"]*\"|'[^']*'|[^'\">])*)>", "〈$1〉");
	}

	public static String unescapeHtml(String content) {
		if (StringUtils.isBlank(content)) {
			return "";
		}

		return content.replaceAll("〈((\"[^\"]*\"|'[^']*'|[^'\"〉])*)〉", "<$1>");
	}

}
