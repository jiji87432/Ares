/**
 *
 */
package com.pay.risk.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.risk.Constant;
import com.pay.risk.entity.OlrCustomerBean;
import com.pay.risk.service.OlrReturnBeanService;
import com.riskutil.redis.RedisUtil;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrReturnBeanServiceImpl 此处填写需要参考的类
 * @version 2015年8月13日 上午11:49:36
 * @author zikun.liu
 */
@Service("olrReturnBeanService")
public class OlrReturnBeanServiceImpl implements OlrReturnBeanService {

	private static final Logger logger = Logger.getLogger(OlrReturnBeanServiceImpl.class);

	/**
	 * 根据传过来的json组装成相应的Bean并返回
	 * @Description 一句话描述方法用法
	 * @param json
	 * @return
	 * @see 需要参考的类或方法
	 */
	public OlrCustomerBean getCustomerBeanByJson(JSONObject json) {
		RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
		try {
			OlrCustomerBean olrCustomerBean = new OlrCustomerBean();
			// 商户编号
			olrCustomerBean.setCustomerNo(String.valueOf(json.get("customer_no")));
			// 订单号
			olrCustomerBean.setOrderId(String.valueOf(json.get("order_id")));
			// 交易金额
			olrCustomerBean.setAmount(Double.valueOf(String.valueOf(json.get("amount"))));
			// 业务类型
			olrCustomerBean.setBusType(String.valueOf(json.get("bus_type")));
			// 商户限额
			Map<String, String> limitAmount = aresJedis.hgetAll("customer_" + String.valueOf(json.get("customer_no")) + "_limit");
			olrCustomerBean.setLimitAmount(limitAmount);
			// 历史交易明细
			Map<String, String> historyAmount = aresJedis.hgetAll(String.valueOf(json.get("customer_no")) + "ORDER_HIS");
			olrCustomerBean.setHistoryAmount(historyAmount);
			// 商户是否在黑名单中
			String isBlack = "N";
			if (aresJedis.hget("customer_no_blacklist", String.valueOf(json.get("customer_no"))) != null) {
				isBlack = aresJedis.hget("customer_no_blacklist", String.valueOf(json.get("customer_no")));
			}
			olrCustomerBean.setIsBlack(isBlack);
			// 商户基础信息
			Map<String, String> basicInformation = new HashMap<String, String>();
			String onlineOrganizationCode = "nodata";
			if (aresJedis.hget(String.valueOf(json.get("customer_no")), "ONLINE_ORGANIZATION_CODE") != null) {
				onlineOrganizationCode = aresJedis.hget(String.valueOf(json.get("customer_no")), "ONLINE_ORGANIZATION_CODE");
			}
			basicInformation.put("organizationCode", onlineOrganizationCode);
			String OnlineOpenTime = "nodata";
			if (aresJedis.hget(String.valueOf(json.get("customer_no")), "ONLINE_OPEN_TIME") != null) {
				OnlineOpenTime = aresJedis.hget(String.valueOf(json.get("customer_no")), "ONLINE_OPEN_TIME");
			}
			basicInformation.put("open_time", OnlineOpenTime);
			String onlineAgentId = "nodata";
			if (aresJedis.hget(String.valueOf(json.get("customer_no")), "ONLINE_AGENT_ID") != null) {
				onlineAgentId = aresJedis.hget(String.valueOf(json.get("customer_no")), "ONLINE_AGENT_ID");
			}
			basicInformation.put("anent_no", onlineAgentId);
			olrCustomerBean.setBasicInformation(basicInformation);
			// 规则命中情况
			List<String> list = new ArrayList<String>();
			olrCustomerBean.setRuleRecord(list);
			return olrCustomerBean;
		} catch (Exception e) {
			logger.error("Ares err ServiceImpl returnCustomerBeanByJson msg:" + e.getMessage());
			return null;
		} finally {
			aresJedis.close();
		}

	}

}
