/**
 *
 */
package com.pay.risk.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pay.orderliness.proxy.AbstractJob;
import com.pay.risk.constans.ONLineUtil;
import com.pay.risk.service.OlOrderService;
import com.pay.risk.service.OlPaymentService;
import com.pay.risk.service.OrderService;
import com.pay.risk.service.PaymentService;
import com.riskrule.bean.RuleObj;

/**
 * @Description: 日基础信息沉淀
 * @see: DataJobOne 此处填写需要参考的类
 * @version 2015年02月05日 下午6:08:27
 * @author zikun.liu
 */
@Component("dataJobOne")
public class DataJobOne extends AbstractJob implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2089436490054304339L;
	private static final Logger logger = Logger.getLogger(DataJobOne.class);

	@Resource
	private OrderService orderService;

	@Resource
	private PaymentService paymentService;

	@Resource
	private OlOrderService olOrderService;

	@Resource
	private OlPaymentService olPaymentService;

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public OlOrderService getOlOrderService() {
		return olOrderService;
	}

	public void setOlOrderService(OlOrderService olOrderService) {
		this.olOrderService = olOrderService;
	}

	public OlPaymentService getOlPaymentService() {
		return olPaymentService;
	}

	public void setOlPaymentService(OlPaymentService olPaymentService) {
		this.olPaymentService = olPaymentService;
	}

	@Override
	public void execute() {
		logger.info("DataJobOne");
		try {
			long x = new Date().getTime();
			int z = 0;
			// for(int i = 1;i<=30;i++){
			BufferedReader br = new BufferedReader(new InputStreamReader(DataJobOne.class.getClassLoader().getResourceAsStream("json1.txt")));
			String s = null;

			while ((s = br.readLine()) != null) {
				z++;

				// logger.info(s);
				RuleObj obj = this.orderService.parseStr2RuleObj(s);
				// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				// String time = sdf.format(new Date());
				/*
				 * String date = String.valueOf(i);
				 * if(i<10){
				 * date = "0"+i;
				 * }
				 * String createTime = "2015-07-"+date+" "+time;
				 */
				// TODO
				Map<String, Object> m1 = obj.getRuleDetail();

				this.orderService.setCalcInfo(obj);

				String createTime = String.valueOf(m1.get("create_time"));

				m1.put("create_time", createTime);
				Map<String, String> map = (Map<String, String>) m1.get("redisInfo");
				map.put("create_time", createTime);
				// new OlOrderSaveThread(olOrderService, map).start();

				Map<String, Object> mn = new HashMap<String, Object>();
				Set<String> set = map.keySet();
				for (String _s : set) {
					String _v = map.get(_s);
					if (_s != null && "create_time".equals(_s)) {

						mn.put("order_createTime", ONLineUtil.returnDateFull(_v));

					} else if (_s != null && "amount".equals(_s)) {

						mn.put("amount", Double.parseDouble(_v));

					} else {
						mn.put(_s, _v);
					}
				}

				this.olOrderService.saveOlOrder(mn);
				// new OlOrderSaveThread(olOrderService, map);
				this.orderService.setData2Cache(obj);
				// new OlOrderHisCacheThread(orderService, obj);
				obj = null;
				mn = null;
				map = null;
				m1 = null;
				if (z % 500 == 0) logger.info(z);

			}

			br.close();
			logger.info(new Date().getTime() - x);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
