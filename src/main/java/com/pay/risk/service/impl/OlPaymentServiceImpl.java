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

import com.pay.risk.dao.OlPaymentDao;
import com.pay.risk.service.OlPaymentService;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlPaymentServiceImpl 此处填写需要参考的类
 * @version 2015年8月24日 下午3:55:04
 * @author zikun.liu
 */
@Service("olPaymentService")
public class OlPaymentServiceImpl implements OlPaymentService {

	private static final Logger logger = Logger.getLogger(OlPaymentServiceImpl.class);

	@Resource
	private OlPaymentDao olPaymentDao;

	/**
	 * 存入线上订单流水数据
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void saveOlPayment(Map<String, Object> map, List<String> rules, String paymentId) {
		try {
			olPaymentDao.saveOlPayment(map);
			for (String rule : rules) {
				Map<String, Object> ruleMap = new HashMap<String, Object>();
				ruleMap.put("payment_id", paymentId);
				ruleMap.put("rule", rule);
				ruleMap.put("type", "payment");
				olPaymentDao.saveOrderRuleDetail(ruleMap);
			}
		} catch (Exception e) {
			logger.error("Ares err ServiceImpl saveOlPayment msg:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 存入线上订单流水数据
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	@Override
	public void saveOlPayment(Map<String, Object> map) {
		try {
			olPaymentDao.saveOlPayment(map);
		} catch (Exception e) {
			logger.error("Ares err ServiceImpl saveOlPayment msg:" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 根据订单和流水id修改流水状态
	 * @Description 一句话描述方法用法
	 * @param Code
	 * @return
	 * @see 需要参考的类或方法
	 */
	public void updateStatusByOrderAndPaymentId(Map<String, Object> map) {
		try {
			olPaymentDao.updateStatusByOrderAndPaymentId(map);
		} catch (Exception e) {
			logger.error("Ares err ServiceImpl updateStatusByOrderAndPaymentId msg:" + e.getMessage());
			e.printStackTrace();
		}
	}

}
