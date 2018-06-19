/**
 *
 */
package com.pay.risk.service;

import net.sf.json.JSONObject;

import com.pay.risk.entity.OlrCustomerBean;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrReturnBeanService 此处填写需要参考的类
 * @version 2015年8月13日 上午11:47:14
 * @author zikun.liu
 */
public interface OlrReturnBeanService {

	/**
	 * 根据传过来的json组装成相应的Bean并返回
	 * @Description  一句话描述方法用法
	 * @param json
	 * @return
	 * @see 需要参考的类或方法
	 */
	public OlrCustomerBean getCustomerBeanByJson(JSONObject json);
}
