/**
 *
 */
package com.pay.risk.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pay.orderliness.proxy.AbstractJob;
import com.pay.risk.service.OrderService;
import com.pay.risk.service.PaymentService;

/**
 * @Description: 日基础信息沉淀
 * @see: DataJobThree 此处填写需要参考的类
 * @version 2015年02月05日 下午6:08:27
 * @author zikun.liu
 */
@Component("dataJobThree")
public class DataJobThree extends AbstractJob implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2089436490054304339L;
	private static final Logger logger = Logger.getLogger(DataJobThree.class);

	@Resource
	private OrderService orderService;
	@Resource
	private PaymentService paymentService;



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



	@Override
	public void execute() {

		logger.info("DataJobThree");
		try {
			long x = new Date().getTime();
			int z = 0;
			// for(int i = 1;i<=30;i++){
			BufferedReader br = new BufferedReader(new InputStreamReader(
					DataJobOne.class.getClassLoader().getResourceAsStream(
							"json3.txt")));
			String s = null;

			while ((s = br.readLine()) != null) {
				z++;
				this.orderService.saveOnResult(s);
				this.paymentService.saveOnResult(s);
				if(z % 500 ==0)
					logger.info(z);
			}

			br.close();
			logger.info(new Date().getTime() - x);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
