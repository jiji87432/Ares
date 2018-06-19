/**
 *
 */
package com.pay.risk.util;

import net.sf.json.JSONObject;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OrderFilterUtil 此处填写需要参考的类
 * @version 2016年4月27日 上午9:56:13
 * @author xiaohua.fan
 */
public class OrderFilterUtil {

	public static boolean orderDataFilter(JSONObject json) {
		try {
			if (json != null) {
				Object obj = json.get("pan");
				if (obj != null) {
					String pan = (String) obj;
					if ("0001110000000000".equals(pan) || "0002220000000000".equals(pan)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
