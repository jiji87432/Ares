package com.pay.risk.thread;

import com.pay.risk.service.OrderService;
import com.pay.risk.service.PaymentService;
import com.riskrule.bean.RuleObj;

public class OlPaymentHisCacheThread extends Thread {
	private PaymentService paymentService;

	private RuleObj ruleObj;



	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public RuleObj getRuleObj() {
		return ruleObj;
	}

	public void setRuleObj(RuleObj ruleObj) {
		this.ruleObj = ruleObj;
	}

	public OlPaymentHisCacheThread(PaymentService paymentService, RuleObj ruleObj) {
		super();
		this.paymentService = paymentService;
		this.ruleObj = ruleObj;
	}

	@Override
	public void run() {
		this.paymentService.setData2Cache(ruleObj);
	}


}
