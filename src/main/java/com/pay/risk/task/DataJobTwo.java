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
import com.pay.risk.Constant;
import com.pay.risk.constans.ONLineUtil;
import com.pay.risk.service.OlPaymentService;
import com.pay.risk.service.PaymentService;
import com.riskrule.bean.RuleObj;
import com.riskutil.redis.RedisUtil;

/**
 * @Description: 日基础信息沉淀
 * @see: DataJobTwo 此处填写需要参考的类
 * @version 2015年02月05日 下午6:08:27
 * @author zikun.liu
 */
@Component("dataJobTwo")
public class DataJobTwo extends AbstractJob implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2089436490054304339L;
	private static final Logger logger = Logger.getLogger(DataJobTwo.class);

	@Resource
	private PaymentService paymentService;

	@Resource
	private OlPaymentService olPaymentService;

	public OlPaymentService getOlPaymentService() {
		return olPaymentService;
	}

	public void setOlPaymentService(OlPaymentService olPaymentService) {
		this.olPaymentService = olPaymentService;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@Override
	public void execute() {
		RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
		logger.info("DataJobTwo");
		try {
			long x = new Date().getTime();
			int z = 0;
			// for(int i = 1;i<=30;i++){
			BufferedReader br = new BufferedReader(new InputStreamReader(DataJobOne.class.getClassLoader().getResourceAsStream("json2.txt")));
			String s = null;

			while ((s = br.readLine()) != null) {
				z++;

				// logger.info(s);
				RuleObj obj = this.paymentService.parseStr2RuleObj(s);

				Map<String, Object> m1 = obj.getRuleDetail();

				String createTime = String.valueOf(m1.get("create_time"));

				// TODO 上线去掉***********************

				String orderId = String.valueOf(m1.get("order_id"));

				Map<String, String> mapOrder = aresJedis.hgetAll(orderId);

				if (createTime == null) {
					createTime = mapOrder.get("create_time");
					if (createTime == null) createTime = "2015-07-11 11:12:14";
				}
				// TODO 上线去掉结束***************************************
				m1.put("create_time", createTime);
				Map<String, String> map = (Map<String, String>) m1.get("redisInfo");
				map.put("create_time", createTime);

				this.paymentService.setCalcInfo(obj);
				Map<String, Object> mn = new HashMap<String, Object>();
				Set<String> set = map.keySet();
				for (String _s : set) {
					String _v = map.get(_s);
					if (_s != null && "create_time".equals(_s)) {

						mn.put("payment_createTime", ONLineUtil.returnDateFull(_v));

					} else if (_s != null && "amount".equals(_s)) {

						mn.put("amount", Double.parseDouble(_v));

					} else {
						mn.put(_s, _v);
					}
				}
				// new OlOrderSaveThread(olOrderService, map).start();
				this.olPaymentService.saveOlPayment(mn);
				this.paymentService.setData2Cache(obj);
				if (z % 500 == 0) logger.info(z);

			}

			br.close();
			logger.info(new Date().getTime() - x);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			aresJedis.close();
		}

	}
}
