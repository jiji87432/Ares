package com.pay.risk.thread;

import com.pay.risk.service.OrderService;
import com.riskrule.bean.RuleObj;

public class OlOrderHisCacheThread extends Thread {
	private OrderService orderService;

	private RuleObj ruleObj;

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public RuleObj getRuleObj() {
		return ruleObj;
	}

	public void setRuleObj(RuleObj ruleObj) {
		this.ruleObj = ruleObj;
	}

	public OlOrderHisCacheThread(OrderService orderService, RuleObj ruleObj) {
		super();
		this.orderService = orderService;
		this.ruleObj = ruleObj;
	}

	@Override
	public void run() {
		this.orderService.setData2Cache(ruleObj);
	}


}
