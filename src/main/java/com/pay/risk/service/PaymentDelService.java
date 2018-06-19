/**
 *
 */
package com.pay.risk.service;

/**
 * @Description: 删除redis中关于交易的部分信息
 * @see: PaymentDelService 此处填写需要参考的类
 * @version 2015年11月4日 下午4:35:26
 * @author xiaohui.wei
 */
public interface PaymentDelService {
	public void delPaymentSet();

	public void delPaymentHash();

	public void delRedisBeforeMonth_Hash();

	public void delRedisBeforeDay_Hash();
}
