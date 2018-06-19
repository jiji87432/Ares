package com.pay.risk.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.pay.risk.constans.ONLineUtil;
import com.pay.risk.constans.OnLinePayConstants;
import com.pay.risk.service.OlOrderService;

public class OlOrderSaveThread extends Thread {

	private OlOrderService olOrderService;

	private Map<String , String> map;



	public OlOrderSaveThread(OlOrderService olOrderService,
			Map<String, String> map) {
		this.olOrderService = olOrderService;
		this.map = map;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public OlOrderService getOlOrderService() {
		return olOrderService;
	}

	public void setOlOrderService(OlOrderService olOrderService) {
		this.olOrderService = olOrderService;
	}

	@Override
	public void run() {

		Map<String , Object> m1 = new HashMap<String, Object>();
		Set<String> set = map.keySet();
		for(String s : set){
			String _v = map.get(s);
			if(s!= null && "create_time".equals(s)){

				m1.put("order_createTime", ONLineUtil.returnDateFull(_v));

			}else if(s!= null && "amount".equals(s)){

				m1.put("amount", Double.parseDouble(_v));

			}else{
				m1.put(s, _v);
			}
		}

		this.olOrderService.saveOlOrder(m1);
	}


}
