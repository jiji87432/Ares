/**
 *
 */
package com.pay.risk.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pay.ares.remote.service.CommonRiskServiceFacade;
import com.pay.ares.remote.service.ONLineRiskServiceFacade;
import com.pay.risk.service.OlOrderService;
import com.pay.risk.service.OlPaymentService;
import com.pay.risk.service.OrderService;
import com.pay.risk.service.PaymentService;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: ExeProcessAction 此处填写需要参考的类
 * @version 2015年8月27日 上午9:43:42
 * @author ximin.yi
 */
@Controller
public class ExeProcessAction {
	private static final Logger logger = Logger.getLogger(ExeProcessAction.class);

	private static final String mCalcCodeM3 = "M00003_t0";

	private static final String mCalcCodeM4 = "M00004_t0";

	// @Resource
	// private OrderService orderService;
	//
	// @Resource
	// private ONLineRiskServiceFacade onLineRiskServiceFacade;
	//
	// @Resource
	// private CommonRiskServiceFacade commonRiskServiceFacade;
	//
	// @Resource
	// private PaymentService paymentService;
	//
	// @Resource
	// private OlOrderService olOrderService;
	//
	// @Resource
	// private OlPaymentService olPaymentService;
	//
	// /**
	// * @Description 功能测试页面跳转
	// * @return
	// * @see 需要参考的类或方法
	// */
	// @RequestMapping("toExeProcessQuery")
	// public String toExeProcess() {
	// return "toExeProcessQuery";
	// }
	//
	// /**
	// * @Description 执行测试方法
	// * @return
	// * @see 需要参考的类或方法
	// */
	// @RequestMapping(value = "executeProcess", produces = "text/html;charset=UTF-8")
	// @ResponseBody
	// public String executeProcess(String param1, String param2, String param3) {
	//
	// String result = this.commonRiskServiceFacade.getCommonResult(param1);
	// logger.info(result);
	// return result;
	//
	// }

}
