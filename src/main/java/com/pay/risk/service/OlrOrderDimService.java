/**
 *
 */
package com.pay.risk.service;

import com.pay.risk.entity.OlrOrderDim;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrOrderDim 此处填写需要参考的类
 * @version 2015年8月13日 上午11:43:52
 * @author ximin.yi
 */
public interface OlrOrderDimService {
	/**
	 * @Description 新增订单维度
	 * @param olrOrderDim
	 * @see 需要参考的类或方法
	 */
	public void saveOlrOrderDim(OlrOrderDim olrOrderDim);

	/**
	 * @Description 更新订单维度
	 * @param olrOrderDim
	 * @see 需要参考的类或方法
	 */
	public void updateOlrOrderDim(OlrOrderDim olrOrderDim);

	/**
	 * @Description 根据ID查询获得订单维度
	 * @param id
	 * @return
	 * @see 需要参考的类或方法
	 */
	public OlrOrderDim getOlrOrderDimById(Long id);

	/**
	 * @Description 根据ID删除订单维度
	 * @param id
	 * @see 需要参考的类或方法
	 */
	public void deleteOlrOrderDimById(Long id);

}
