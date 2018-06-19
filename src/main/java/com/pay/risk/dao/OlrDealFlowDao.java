/**
 *
 */
package com.pay.risk.dao;

import com.pay.risk.entity.OlrDealFlow;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrDealFlowDao 此处填写需要参考的类
 * @version 2015年8月12日 下午5:12:13
 * @author ximin.yi
 */
public interface OlrDealFlowDao {

	/**
	 * @Description 保存新增交易流水
	 * @param olrDealFlow
	 * @see 需要参考的类或方法
	 */
	public void saveOlrDealFlow(OlrDealFlow olrDealFlow);

	/**
	 * @Description 更新交易流水
	 * @param olrDealFlow
	 * @see 需要参考的类或方法
	 */
	public void updateOlrDealFlow(OlrDealFlow olrDealFlow);

	/**
	 * @Description 根据ID查询获得交易流水
	 * @param id
	 * @return
	 * @see 需要参考的类或方法
	 */
	public OlrDealFlow getOlrDealFlowById(Long id);

	/**
	 * @Description 根据ID删除交易流水
	 * @param id
	 * @see 需要参考的类或方法
	 */
	public void deleteOlrDealFlowById(Long id);
}
