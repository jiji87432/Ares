/**
 *
 */
package com.pay.risk.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.risk.bean.PaymentDelBean;
import com.pay.risk.service.DelRedisInfoService;
import com.pay.risk.util.DelRedisInfoUtil;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: DelRedisInfoServiceImpl 此处填写需要参考的类
 * @version 2015年11月12日 下午3:08:44
 * @author xiaohui.wei
 */
@Service("delRedisInfoService")
public class DelRedisInfoServiceImpl implements DelRedisInfoService{
	private static final Logger logger = Logger.getLogger(DelRedisInfoServiceImpl.class);

	public void delKeyNoToday(){
		long x = new Date().getTime();
		logger.info("DelRedisInfoServiceImpl delKeyNoToday method start...");

		List<PaymentDelBean> lists = new ArrayList<PaymentDelBean>();
		lists.add(new PaymentDelBean("*_init_extid_list", 0));
		lists.add(new PaymentDelBean("*_success_extid_list", 0));
		lists.add(new PaymentDelBean("*_repal_extid_list", 0));
		lists.add(new PaymentDelBean("*_initcount", 0));
		lists.add(new PaymentDelBean("*_initamount", 0));

		lists.add(new PaymentDelBean("*_successcount", 0));
		lists.add(new PaymentDelBean("*_successamount", 0));
		lists.add(new PaymentDelBean("*_repalcount", 0));
		lists.add(new PaymentDelBean("*_repalamount", 0));
		lists.add(new PaymentDelBean("*_mostamount", 0));

		lists.add(new PaymentDelBean("*_inipan", 0));
		lists.add(new PaymentDelBean("*_posrequest_list", 0));
		lists.add(new PaymentDelBean("*_purchase_count", 0));
		lists.add(new PaymentDelBean("*_hl_rcode_count", 0));
		lists.add(new PaymentDelBean("*_hl_rcode_cardcount", 0));

		lists.add(new PaymentDelBean("*_ll_rcode_count", 0));
		lists.add(new PaymentDelBean("*_void_count", 0));
		lists.add(new PaymentDelBean("*_void_amount", 0));
		lists.add(new PaymentDelBean("*_ll_rcode_amount", 0));

		lists.add(new PaymentDelBean("*_DATE_TRANSLASTORDER", 0));
		lists.add(new PaymentDelBean("*_DATE_CALCSLASTORDER", 0));
		lists.add(new PaymentDelBean("*_date_customerriskrecords", 0));


		for(PaymentDelBean pay : lists){
			DelRedisInfoUtil.delKeyInfo(pay.getMatchesStr(),pay.getFrontIndex());
		}

		logger.info("DelRedisInfoServiceImpl delKeyNoToday method start...删除所有的key总共use======" + (new Date().getTime() - x) + "ms=========");

	}
}
