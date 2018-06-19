/**
 *
 */
package com.pay.risk.service;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OrderDelService 此处填写需要参考的类
 * @version 2015年10月28日 上午10:34:34
 * @author xiaohui.wei
 */
public interface OrderDelService {
	public void delOrderRule();

	public void delCustomerRule();

	public void delMerchantHistory();

	public void delMerchantHisMonth();

	public void delGatherData();
}
