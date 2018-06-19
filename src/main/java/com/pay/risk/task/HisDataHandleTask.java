package com.pay.risk.task;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pay.orderliness.proxy.AbstractJob;
import com.pay.risk.Constant;
import com.pay.risk.constans.ONLineUtil;
import com.pay.risk.constans.OnLinePayConstants;
import com.riskrule.util.OLUtil;
import com.riskutil.redis.RedisUtil;

@Component("hisDataHandleTask")
public class HisDataHandleTask extends AbstractJob implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4051390748513558251L;

	private static final Logger logger = Logger.getLogger(HisDataHandleTask.class);

	@Override
	public void execute() {
		logger.info("HisDataHandleTask START");
		long nn = new Date().getTime();
		RedisUtil jedis = new RedisUtil(Constant.AresJedis);
		try {
			String todayStrYMD = ONLineUtil.getTodayStrYMD();
			String dateStrFull = ONLineUtil.returnDateFullStr(new Date());

			Set<String> keys = jedis.keys("*_ORDER_HIS");
			logger.info("keys length " + keys.size());
			for (String key : keys) {
				logger.info("key : " + key);
				String[] keyStr = key.split("_");
				String customerNo = keyStr[0];
				logger.info("customer_no " + customerNo);
				Map<String, Object> m2 = new HashMap<String, Object>();
				ONLineUtil.getPaymetHisData(jedis, m2, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS, "customer_order_his");
				Set<String> dayList = jedis.smembers("DAYS_ARES");
				Map<String, String> forRedis = new HashMap<String, String>();
				String sKey = ONLineUtil.geneExtKey(todayStrYMD, customerNo);
				for (String _i : dayList) {
					int k = Integer.valueOf(_i);
					int sc = OLUtil.getOrderCountByDayAreas(m2, "SUCCESS", k, dateStrFull, "N");
					int ac = OLUtil.getOrderCountByDayAreas(m2, "ALL", k, dateStrFull, "N");
					double sa = OLUtil.getOrderAmountByDayAreas(m2, "SUCCESS", k, dateStrFull, "N");
					double aa = OLUtil.getOrderAmountByDayAreas(m2, "ALL", k, dateStrFull, "N");
					double dAvg = sa / k;
					double cAvg = 0.0;;
					if (sc != 0) cAvg = sa / sc;

					forRedis.put("sc_" + k, String.valueOf(sc));
					forRedis.put("ac_" + k, String.valueOf(ac));
					forRedis.put("sa_" + k, ONLineUtil.get2point(sa));
					forRedis.put("aa_" + k, ONLineUtil.get2point(aa));
					forRedis.put("dAvg_" + k, ONLineUtil.get2point(dAvg));
					forRedis.put("cAvg_" + k, ONLineUtil.get2point(cAvg));

					logger.info("sKey " + sKey);
					logger.info("map info " + forRedis);

				}
				jedis.hmset(3600*26,sKey, forRedis);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}

		logger.info("HisDataHandleTask END use " + ((new Date().getTime()) - nn) + " ms");
	}

}
