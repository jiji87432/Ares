/**
 *
 */
package com.pay.risk.dao;

import com.pay.risk.entity.OlrModel;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrDealFlowDao 此处填写需要参考的类
 * @version 2015年8月12日 下午5:12:13
 * @author zikun.liu
 */
public interface OlrModelDao {

	/**
	 * 根据code查询模型
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public OlrModel getModelByCode(String code);

}
