/**
 *
 */
package com.pay.risk.util;

import java.util.Collection;
import java.util.Map;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: StringUtil 此处填写需要参考的类
 * @version 2015年12月28日 下午3:06:42
 * @author xiaohui.wei
 */
public class StringUtil {

	/**
	 * 将Object转化成String类型
	 */
	public static String parseObjectToString(Object obj) {
		String str = "";
		if (obj != null) {
			str = obj.toString();
		}
		return str;
	}

	public static String getT0Key(String str1, String str2, String str3) {
		return str1 + "_" + str2 + "_" + str3;
	}

	/*
	 * 判断属性为不为空
	 */
	public static boolean isEmpty(Object obj) {

		if (null == obj) return true;
		if ("".equals(obj.toString().trim())) return true;
		if ("null".equals(obj.toString().trim())) return true;
		if (obj.toString().length() == 0) return true;
		return (obj instanceof Collection<?>) ? ((Collection<?>) obj).isEmpty() : (obj instanceof Map ? ((Map<?, ?>) obj).isEmpty() : false);

	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static boolean isStrEmpty(String str) {
		if (null == str) return true;
		if ("".equals(str.trim())) return true;
		if ("null".equals(str.trim())) return true;
		return false;
	}

	public static boolean isStrNotEmpty(String str) {
		return !isStrEmpty(str);
	}

	public static boolean isMapEmpty(Map<?, ?> map) {
		return map == null ? true : map.isEmpty();
	}

	public static boolean isMapNotEmpty(Map<?, ?> map) {
		return !isMapEmpty(map);
	}

	public static boolean isCollectionEmpty(Collection<?> collection) {
		return collection == null ? true : collection.isEmpty();
	}

	public static boolean isCollectionNotEmpty(Collection<?> collection) {
		return !isCollectionEmpty(collection);
	}

	/**
	 * @Description 解析简单字符串数据
	 * @param str
	 * @param label
	 * @param index
	 * @return
	 * @see 需要参考的类或方法
	 */
	public static String analyString(String str, String label, int index) {
		try {
			String[] strings = str.split(label);
			return strings[index];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isEmptyWithTrim(String str) {
		return (str == null) || (str.trim().length() == 0);
	}
}
