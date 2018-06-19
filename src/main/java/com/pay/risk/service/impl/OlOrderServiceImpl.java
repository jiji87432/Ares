/**
 *
 */
package com.pay.risk.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.risk.dao.OlOrderDao;
import com.pay.risk.service.OlOrderService;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlOrderServiceImpl 此处填写需要参考的类
 * @version 2015年8月24日 下午3:54:49
 * @author zikun.liu
 */
@Service("olOrderService")
public class OlOrderServiceImpl implements OlOrderService {

	private static final Logger logger = Logger.getLogger(OlOrderServiceImpl.class);

	@Resource
	private OlOrderDao olOrderDao;

	/**
	 * 存入线上订单数据
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void saveOlOrder(Map<String, Object> map, List<String> rules, String orderId) {
		try {
			olOrderDao.saveOlOrder(map);

			for (String rule : rules) {
				Map<String, Object> ruleMap = new HashMap<String, Object>();
				ruleMap.put("order_id", orderId);
				ruleMap.put("rule", rule);
				ruleMap.put("type", "order");
				olOrderDao.saveOrderRuleDetail(ruleMap);
			}
		} catch (Exception e) {
			logger.error("Ares err ServiceImpl saveOlOrder msg:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 存入线上订单数据
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void saveOlOrder(Map<String, Object> map) {
		try {
			olOrderDao.saveOlOrder(map);

		} catch (Exception e) {
			logger.error("Ares err ServiceImpl saveOlOrder msg:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 根据订单id修改订单状态
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void updateStatusByOrderId(Map<String, Object> map) {
		try {
			olOrderDao.updateStatusByOrderId(map);
		} catch (Exception e) {
			logger.error("Ares err ServiceImpl updateStatusByOrderId msg:" + e.getMessage());
			e.printStackTrace();
		}
	}

}
