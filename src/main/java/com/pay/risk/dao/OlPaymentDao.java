/**
 *
 */
package com.pay.risk.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlPaymentDao 此处填写需要参考的类
 * @version 2015年8月24日 下午5:12:13
 * @author zikun.liu
 */
public interface OlPaymentDao {

	/**
	 * 存入线上订单流水数据
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void saveOlPayment(@Param("queryParams") Map<String, Object> map);

	/**
	 * 根据订单和流水id修改流水状态
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void updateStatusByOrderAndPaymentId(@Param("queryParams") Map<String, Object> map);

	/**
	 * @Description 一句话描述方法用法
	 * @param ruleMap
	 * @see 需要参考的类或方法
	 */
	public void saveOrderRuleDetail(@Param("queryParams") Map<String, Object> ruleMap);

}
