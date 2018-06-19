/**
 *
 */
package com.pay.risk.service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlPaymentService 此处填写需要参考的类
 * @version 2015年8月24日 下午3:51:56
 * @author zikun.liu
 */
public interface OlPaymentService {

	/**
	 * 存入线上订单流水数据
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void saveOlPayment(Map<String, Object> map);

	/**
	 * 根据订单和流水id修改流水状态
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void updateStatusByOrderAndPaymentId(Map<String, Object> map);

	/**
	 * @Description 一句话描述方法用法
	 * @param mn
	 * @param rules
	 * @param paymentId
	 * @see 需要参考的类或方法
	 */
	public void saveOlPayment(Map<String, Object> mn, List<String> rules, String paymentId);

}
