/**
 *
 */
package com.pay.risk.task;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pay.orderliness.proxy.AbstractJob;
import com.pay.risk.service.PaymentDelService;

/**
 * @Description: 删除redis中关于交易的部分信息
 * @see: PaymentDelTask 此处填写需要参考的类
 * @version 2015年11月4日 下午4:36:42
 * @author xiaohui.wei
 */
@Component("paymentDelTask")
public class PaymentDelTask extends AbstractJob implements Serializable {

	private static final Logger logger = Logger.getLogger(PaymentDelTask.class);

	@Resource
	private PaymentDelService paymentDelService;

	@Override
	public void execute() {
		logger.info("PaymentDelTask execute method start...");

		paymentDelService.delPaymentSet();
		paymentDelService.delPaymentHash();

		logger.info("PaymentDelTask execute method end...");

	}

}
