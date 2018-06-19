/**
 *
 */
package com.pay.risk.service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlOrderService 此处填写需要参考的类
 * @version 2015年8月24日 下午3:51:39
 * @author zikun.liu
 */
public interface OlOrderService {

	/**
	 * 存入线上订单数据
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void saveOlOrder(Map<String, Object> map, List<String> rules, String orderId);

	/**
	 * 根据订单id修改订单状态
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void updateStatusByOrderId(Map<String, Object> map);

	/**
	 * @Description 一句话描述方法用法
	 * @param mn
	 * @see 需要参考的类或方法
	 */
	public void saveOlOrder(Map<String, Object> mn);

}
