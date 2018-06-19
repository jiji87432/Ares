package com.pay.risk.thread;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.pay.risk.service.OlPaymentService;

public class OlPaymentSaveThread extends Thread {

	private static final Logger logger = LogManager.getLogger(OlPaymentSaveThread.class);

	private OlPaymentService olPaymentService;

	private Map<String , String> map;

	private Map<String , Object> mn;

	private String paymentId;

	private List<String> rules;

	public OlPaymentSaveThread(OlPaymentService olPaymentService,
			Map<String, String> map) {
		this.olPaymentService = olPaymentService;
		this.map = map;
	}

	public OlPaymentSaveThread(OlPaymentService olPaymentService2,
			Map<String, Object> mn, List<String> rules, String paymentId) {
		this.olPaymentService = olPaymentService2;
		this.mn = mn;
		this.paymentId = paymentId;
		this.rules = rules;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}



	public OlPaymentService getOlPaymentService() {
		return olPaymentService;
	}

	public void setOlPaymentService(OlPaymentService olPaymentService) {
		this.olPaymentService = olPaymentService;
	}

	public Map<String, Object> getMn() {
		return mn;
	}

	public void setMn(Map<String, Object> mn) {
		this.mn = mn;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public List<String> getRules() {
		return rules;
	}

	public void setRules(List<String> rules) {
		this.rules = rules;
	}

	@Override
	public void run() {
		long c = new Date().getTime();
		logger.info("save payment DB start " + this.paymentId);
		this.olPaymentService.saveOlPayment(mn,rules , paymentId);
		logger.info("save payment DB end use "+(new Date().getTime() - c)+" ms " + this.paymentId);
	}


}
