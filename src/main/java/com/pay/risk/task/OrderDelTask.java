/**
 *
 */
package com.pay.risk.task;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pay.orderliness.proxy.AbstractJob;
import com.pay.risk.service.OrderDelService;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OrderDelTask 此处填写需要参考的类
 * @version 2015年10月29日 下午7:55:35
 * @author xiaohui.wei
 */
@Component("orderDelTask")
public class OrderDelTask extends AbstractJob implements Serializable {
	private static final Logger logger = Logger.getLogger(OrderDelTask.class);

	@Resource
	private OrderDelService orderDelService;

	@Override
	public void execute() {
		logger.info("OrderDelTask start......");

		orderDelService.delOrderRule();
		orderDelService.delCustomerRule();
		orderDelService.delMerchantHistory();
		orderDelService.delMerchantHisMonth();
		orderDelService.delGatherData();

		logger.info("OrderDelTask end......");
	}
}
