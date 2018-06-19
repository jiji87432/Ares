/**
 *
 */
package com.pay.risk.util;

import java.util.Set;

import org.apache.log4j.Logger;

import com.pay.risk.Constant;
import com.riskutil.redis.RedisUtil;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: PaymentDelUtil 此处填写需要参考的类
 * @version 2015年11月4日 下午1:55:52
 * @author xiaohui.wei
 */
public class PaymentDelUtil {
	private static final Logger logger = Logger.getLogger(PaymentDelUtil.class);

	/**
	 * @Description set 的value 作为另一个set集合的key
	 * @param keyVar
	 * @param ArrLenVar 从右往左数 日期在keyVar中第几个 从1开始数
	 * @param dayVar 删除多少天之外的
	 * @see 需要参考的类或方法
	 *      ssss_asdf_222_sdfgsd_asdf 2 = 5 - 3
	 */
	public static void delPaymentCommon(String keyVar, int arrLenVar, int dayVar) {
		logger.info("PaymentDelUtil delPaymentCommon method start keyVar =" + keyVar + ",arrLenVar=" + arrLenVar + ",dayVar=" + dayVar);
		RedisUtil jedis = new RedisUtil(Constant.AresJedis);
		try {
			String dFlag = jedis.get("DEL_FALG");

			/* 得到所有的符合条件的key */
			Set<String> keys = jedis.keys(keyVar);
			logger.info("交易所有的keys的长度" + keys.size());
			for (String key : keys) {
				String[] keyArr = key.split("_");
				if (keyArr != null && keyArr.length > 0) {

					logger.info("所有交易的key:" + key + ",日期:" + keyArr[keyArr.length - arrLenVar] + ",该日期是否符合删除的条件:"
							+ DateUtils.compareDate(keyArr[keyArr.length - arrLenVar], dayVar));
					/* 判断key是否符合删除的条件 */
					if (DateUtils.compareDate(keyArr[keyArr.length - arrLenVar], dayVar)) {

						/* 删除key */
						Set<String> keyValues = jedis.smembers(key);
						logger.info("符合条件key下关联的要删除key的长度：" + keyValues.size());
						for (String keyValue : keyValues) {
							logger.info("符合条件key下关联要删除的key：" + keyValue);
							if (dFlag != null && "Y".equals(dFlag)) {
								jedis.del(keyValue);
							}
						}
						logger.info("符合删除条件的key=" + key + ",日期=" + keyArr[keyArr.length - arrLenVar]);
						if (dFlag != null && "Y".equals(dFlag)) {
							jedis.del(key);
						}

					}
				}
			}

		} catch (Exception e) {
			logger.info("Ares class = PaymentDelServiceImpl,method = delPaymentCommon err msg：" + e.getMessage());
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		logger.info("PaymentDelUtil  delPaymentCommon method end...");
	}

	/**
	 * @Description hash结构
	 * @param keyVar
	 * @param arrLenVar 从左往右数 日期在域中地几个 从0开始数
	 * @param dayVar 删除多少天之外的
	 * @see 需要参考的类或方法
	 */
	public static void delPaymentCommon_hash(String keyVar, int arrLenVar, int dayVar) {
		logger.info("PaymentDelUtil delPaymentCommon_hash method start...");
		RedisUtil jedis = new RedisUtil(Constant.AresJedis);
		try {
			String dFlag = jedis.get("DEL_FALG");

			/* 得到所有符合条件的key */
			Set<String> keys = jedis.keys(keyVar);
			logger.info("PaymentDelUtil delPaymentCommon_hash所有符合条件的key的长度" + keys.size());
			for (String key : keys) {

				/* 得到符合条件key下的域 */
				Set<String> fields = jedis.hkeys(key);
				for (String field : fields) {

					String[] fieldArr = field.split("_");
					if (fieldArr != null && fieldArr.length > 0) {

						/* 判断域的日期是否符合条件 */
						logger.info("PaymentDelUtil delPaymentCommon_hash符合条件的key" + key + "，域：" + field + ",是否符合条件："
								+ DateUtils.compareDate(fieldArr[arrLenVar], dayVar));
						if (DateUtils.compareDate(fieldArr[arrLenVar], dayVar)) {

							/* 删除符合条件的域 */
							logger.info("PaymentDelUtil delPaymentCommon_hash要删除key:" + key + ",域:" + field);
							if (null != dFlag && "Y".equals(dFlag)) {
								jedis.hdel(key, field);
							}

						}
					}
				}

			}

		} catch (Exception e) {
			logger.info("Ares class = PaymentDelServiceImpl,method = delPaymentCommon err msg：" + e.getMessage());
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		logger.info("PaymentDelUtil delPaymentCommon_hash method end...");
	}

	/**
	 * @Description 删除几月之外的数据 hash结构数据 日期在redis的key上
	 * @param matchesStr
	 * @param offset 从左往右数 日期在域中地几个 从0开始数
	 * @param dateSize 删除多少月之外的
	 * @see 需要参考的类或方法
	 */
	public static void delCommonMonth_hash(String keyVar, int arrLenVar, int dayVar) {
		logger.info("PaymentDelUtil delCommonMonth_hash method start...");
		RedisUtil jedis = new RedisUtil(Constant.AresJedis);
		try {
			String dFlag = jedis.get("DEL_FALG");

			/* 得到所有符合条件的key */
			Set<String> keys = jedis.keys(keyVar);
			logger.info("PaymentDelUtil delCommonMonth_hash所有符合条件的key的长度" + keys.size());
			for (String key : keys) {
				String[] fieldArr = key.split("_");
				if (fieldArr != null && fieldArr.length > 0) {

					/* 判断域的日期是否符合条件 */
					logger.info("PaymentDelUtil delCommonMonth_hash符合条件的key" + key + "，是否符合条件：" + DateUtils.compareDateMonth(fieldArr[arrLenVar], dayVar));
					if (DateUtils.compareDateMonth(fieldArr[arrLenVar], dayVar)) {

						/* 删除符合条件的域 */
						logger.info("PaymentDelUtil delCommonMonth_hash要删除key:" + key);
						if (null != dFlag && "Y".equals(dFlag)) {
							jedis.del(key);
						}

					}
				}
			}

		} catch (Exception e) {
			logger.info("Ares class = PaymentDelServiceImpl,method = delCommonMonth_hash err msg：" + e.getMessage());
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		logger.info("PaymentDelUtil delCommonMonth_hash method end...");
	}

	/**
	 * @Description 删除几天之外的数据 hash结构数据 日期在redis的key上
	 * @param matchesStr
	 * @param arrLenVar 从左往右数 日期在域中地几个 从0开始数
	 * @param dayVar 删除几天之外的数据
	 * @see 需要参考的类或方法
	 */
	public static void delRedisBeforeDay_Hash(String keyVar, int arrLenVar, int dayVar) {
		logger.info("PaymentDelUtil delRedisBeforeDay_Hash method start...");
		RedisUtil jedis = new RedisUtil(Constant.AresJedis);
		try {
			String dFlag = jedis.get("DEL_FALG");

			/* 得到所有符合条件的key */
			Set<String> keys = jedis.keys(keyVar);
			logger.info("PaymentDelUtil delRedisBeforeDay_Hash所有符合条件的key的长度" + keys.size());
			for (String key : keys) {
				String[] fieldArr = key.split("_");
				if (fieldArr != null && fieldArr.length > 0) {

					/* 判断域的日期是否符合条件 */
					logger.info("PaymentDelUtil delRedisBeforeDay_Hash符合条件的key" + key + "，是否符合条件：" + DateUtils.compareDate(fieldArr[arrLenVar], dayVar));
					if (DateUtils.compareDate(fieldArr[arrLenVar], dayVar)) {

						/* 删除符合条件的域 */
						logger.info("PaymentDelUtil delRedisBeforeDay_Hash要删除key:" + key);
						if (null != dFlag && "Y".equals(dFlag)) {
							jedis.del(key);
						}

					}
				}
			}

		} catch (Exception e) {
			logger.info("Ares class = PaymentDelServiceImpl,method = delRedisBeforeDay_Hash err msg：" + e.getMessage());
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		logger.info("PaymentDelUtil delRedisBeforeDay_Hash method end...");
	}
}
