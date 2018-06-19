/**
 *
 */
package com.pay.risk.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.risk.Constant;
import com.pay.risk.entity.OlrOrderDim;
import com.riskutil.redis.RedisUtil;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrSaveInRedisThread 此处填写需要参考的类
 * @version 2015年8月13日 上午11:49:36
 * @author zikun.liu
 */
@Service()
public class OlrSaveInRedisThread implements Runnable {

	private static final Logger logger = Logger.getLogger(OlrSaveInRedisThread.class);

	private OlrOrderDim olrOrderDim;

	/**
	 * @return the olrOrderDim
	 */
	public OlrOrderDim getOlrOrderDim() {
		return olrOrderDim;
	}

	/**
	 * @param olrOrderDim the olrOrderDim to set
	 */
	public void setOlrOrderDim(OlrOrderDim olrOrderDim) {
		this.olrOrderDim = olrOrderDim;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
		try {
			/** 订单记录 */
			aresJedis.sadd("ONLINE_ORDER_" + sdf.format(date), olrOrderDim.getOrderId());
			/** 订单明细 */
			// 商户编号
			aresJedis.hset(olrOrderDim.getOrderId(), "customer_no", olrOrderDim.getCustomerNo());
			// 交易金额
			aresJedis.hset(olrOrderDim.getOrderId(), "amount", String.valueOf(olrOrderDim.getAmount()));
			// 交易时间
			aresJedis.hset(olrOrderDim.getOrderId(), "create_time", String.valueOf(olrOrderDim.getCompleteTime()));
			// 业务类型
			aresJedis.hset(olrOrderDim.getOrderId(), "bus_type", olrOrderDim.getBusType());
			// 订单状态(第三步完成之后放入)
			aresJedis.hset(olrOrderDim.getOrderId(), "result", olrOrderDim.getResult());

			/** 商户历史交易信息 */
			// 总交易金额
			Double sumAmount = olrOrderDim.getAmount();
			String amount = aresJedis.hget(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_1");
			if (amount != null) {
				sumAmount += Double.valueOf(amount);
			}
			aresJedis.hset(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_1", String.valueOf(sumAmount));
			// 总交易笔数
			Integer countNum = 1;
			String num = aresJedis.hget(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_2");
			if (num != null) {
				countNum += Integer.valueOf(num);
			}
			aresJedis.hset(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_2", String.valueOf(countNum));
			// 相同类型下总交易金额
			Double sumTypeAmount = olrOrderDim.getAmount();
			String typeAmount = aresJedis.hget(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_" + olrOrderDim.getBusType() + "_1");
			if (typeAmount != null) {
				sumTypeAmount += Double.valueOf(typeAmount);
			}
			aresJedis.hset(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_" + olrOrderDim.getBusType() + "_1", String.valueOf(sumTypeAmount));
			// 相同类型下的总交易笔数
			Integer countTypeNum = 1;
			String typeNum = aresJedis.hget(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_" + olrOrderDim.getBusType() + "_2");
			if (typeNum != null) {
				countTypeNum += Integer.valueOf(typeNum);
			}
			aresJedis.hset(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_" + olrOrderDim.getBusType() + "_2", String.valueOf(countTypeNum));

			// 判断订单状态是否为成功
			if ("SUCCESS".equals(aresJedis.hget(olrOrderDim.getOrderId(), "result"))) {
				// 成功总交易金额
				Double sumSuccessAmount = olrOrderDim.getAmount();
				String successAmount = aresJedis.hget(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_3");
				if (successAmount != null) {
					sumSuccessAmount += Double.valueOf(successAmount);
				}
				aresJedis.hset(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_3", String.valueOf(sumSuccessAmount));
				// 成功总交易笔数
				Integer countSuccessNum = 1;
				String succcessNum = aresJedis.hget(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_4");
				if (succcessNum != null) {
					countSuccessNum += Integer.valueOf(succcessNum);
				}
				aresJedis.hset(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_4", String.valueOf(countSuccessNum));
				// 相同类型下成功总交易金额
				Double sumSuccessTypeAmount = olrOrderDim.getAmount();
				String successTypeAmount = aresJedis.hget(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_" + olrOrderDim.getBusType() + "_3");
				if (successTypeAmount != null) {
					sumSuccessTypeAmount += Double.valueOf(successTypeAmount);
				}
				aresJedis.hset(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_" + olrOrderDim.getBusType() + "_3",
						String.valueOf(sumSuccessTypeAmount));
				// 相同类型下成功总交易笔数
				Integer countSuccessTypeNum = 1;
				String successTypeNum = aresJedis.hget(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_" + olrOrderDim.getBusType() + "_4");
				if (successTypeNum != null) {
					countSuccessTypeNum += Integer.valueOf(successTypeNum);
				}
				aresJedis.hset(olrOrderDim.getCustomerNo() + "_ORDER_HIS", sdf.format(date) + "_" + olrOrderDim.getBusType() + "_4",
						String.valueOf(countSuccessTypeNum));
			}
		} catch (Exception e) {
			logger.error("Ares err Thread OlrSaveInRedisThread msg:" + e.getMessage());
		} finally {
			aresJedis.close();
		}
	}
}
