/**
 *
 */
package com.pay.risk.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.risk.Constant;
import com.pay.risk.service.OrderDelService;
import com.riskutil.redis.RedisUtil;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OrderDelServiceImpl 此处填写需要参考的类
 * @version 2015年10月26日 下午3:47:16
 * @author xiaohui.wei
 */
@Service("orderDelService")
public class OrderDelServiceImpl implements OrderDelService {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
	private static final Logger logger = Logger.getLogger(OrderDelServiceImpl.class);

	// 当天 订单（删除范围：15天外）
	public void delOrderRule() {
		long x = new Date().getTime();
		logger.info("delOrderRule方法开始....");
		RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
		try {
			String dFlag = aresJedis.get("DEL_FALG");
			String delDateKey = aresJedis.get("delDateKey");
			Set<String> keys = null;
			if (delDateKey == null) {
				keys = aresJedis.keys("ON_ORDER_*");
			} else {
				keys = aresJedis.keys("ON_ORDER_*" + delDateKey + "*");
			}
			logger.info("订单的所有key" + keys);

			for (String key : keys) {
				String[] keyStr = key.split("_");
				if (null != keyStr && keyStr.length > 0) {
					// Date keyDate = sdf.parse(keyStr[2]);
					// 判断当前日期是否在当天的15天外 在 true 不是 false
					logger.info("订单创建的时间keyStr[2]" + keyStr[2]);
					logger.info("当前日期是否在当天的15天外或则大于今天的日期" + compareDate(keyStr[2], 15));
					if (compareDate(keyStr[2], 17)) {
						Set<String> keys1 = aresJedis.smembers(key);
						logger.info("每个订单key对应的所有成员的长度 ：" + keys1.size());
						for (String key1 : keys1) {
							logger.info("delOrderRule删除的key ：" + key + "值：" + key1);
							if (dFlag != null && "Y".equals(dFlag)) {
								aresJedis.del(key1);
							}
						}
						logger.info("删除的key02 ：" + key);
						if (dFlag != null && "Y".equals(dFlag)) {
							aresJedis.del(key);
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error("Ares err delOrderRule msg:" + e.getMessage());
			e.printStackTrace();
		} finally {
			aresJedis.close();
		}
		logger.info("delOrderRule方法结束.... use " + (new Date().getTime() - x) + " ms ");

	}

	public boolean compareDate(String dateStr, int num) {
		try {

			try {
				Integer.parseInt(dateStr);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("该日期dateStr有非数字：" + dateStr);
				return false;
			}

			if (dateStr != null) {
				// 校验日期
				Date keyDate = sdf.parse(dateStr);

				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				// 将当前时间的时分秒设置成0，防止过滤掉今天的在这之前的用户
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				// 计算几天之前的日期
				cal.add(Calendar.DATE, -num);
				Date date2 = cal.getTime();
				if (keyDate.getTime() < date2.getTime() || keyDate.getTime() > date.getTime()) {
					return true;
				}
			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}

	public boolean compareDateMonth(String dateStr, int num) {
		logger.info("compareDateMonth 方法。。。");
		try {
			try {
				Integer.parseInt(dateStr);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("该月份有非数字" + dateStr);
				return false;
			}

			if (dateStr != null) {
				// 判断时间区间
				Date fieldDate = sdf2.parse(dateStr);
				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				// 将当前时间的时分秒设置成0，防止过滤掉今天的在这之前的用户
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				// 计算几个月之前的日期
				cal.add(Calendar.MONTH, -num);
				Date date2 = cal.getTime();
				if (fieldDate.getTime() < date2.getTime() || fieldDate.getTime() > date.getTime()) {
					return true;
				}

			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}

	// 当天商户交易 （删除范围：15天外）
	public void delCustomerRule() {
		long x = new Date().getTime();
		logger.info("delCustomerRule方法开始....");
		RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
		try {
			String delDateKey = aresJedis.get("delDateKey");
			Set<String> keys = null;
			if (delDateKey == null) {
				keys = aresJedis.keys("*_*_CUSTOMER_ORDERDATE");
			} else {
				keys = aresJedis.keys("*" + delDateKey + "*_CUSTOMER_ORDERDATE");
			}
			String dFlag = aresJedis.get("DEL_FALG");
			logger.info("得到商户交易的所有key" + keys);
			for (String key : keys) {
				String[] keyStr = key.split("_");
				if (null != keyStr && keyStr.length > 0) {
					// 判断当前日期是否在当天的15天外 在 true 不是 false
					logger.info("订单创建的时间keyStr[1]" + keyStr[1]);
					logger.info("商户交易日期是否在当天的15天外或则大于今天的日期" + compareDate(keyStr[1], 15));
					if (compareDate(keyStr[1], 17)) {

						Set<String> keys1 = aresJedis.smembers(key);
						logger.info("商户交易的所有订单Id length" + keys1.size());
						for (String key1 : keys1) {
							logger.info("delCustomerRule删除key03" + key + " 值：" + key1);
							if (dFlag != null && "Y".equals(dFlag)) {
								aresJedis.del(key1);
							}
						}

						logger.info("删除key04" + key);
						if (dFlag != null && "Y".equals(dFlag)) {
							aresJedis.del(key);
						}

					}

				}

			}
		} catch (Exception e) {
			logger.error("Ares err delCustomerRule msg:" + e.getMessage());
			e.printStackTrace();
		} finally {
			aresJedis.close();
		}
		logger.info("delCustomerRule方法结束....use" + (new Date().getTime() - x) + "ms");
	}

	// 商户交易历史
	public void delMerchantHistory() {
		long x = new Date().getTime();
		logger.info("delCustomerRule方法开始....");
		RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
		try {
			// 1得到所有符合条件的key
			Set<String> keys = aresJedis.keys("*_ORDER_HIS");
			String dFlag = aresJedis.get("DEL_FALG");
			logger.info("商户交易历史的所有keys" + keys);
			for (String key : keys) {
				// 2 得到key下有所得域
				Set<String> s1 = aresJedis.hkeys(key);
				for (String mapKey : s1) {
					String[] fieldArr = mapKey.split("_");
					if (null != fieldArr && fieldArr.length > 0) {
						logger.info("商户交易历史的日期" + fieldArr[0]);
						logger.info("商户交易历史的日期是否在30天以外或则大于今天" + compareDate(fieldArr[0], 30));
						if (compareDate(fieldArr[0], 32)) {
							// 3 删除每个key下符合条件的域
							logger.info("删除每个key下符合条件的域key:" + key + " filed :" + mapKey);
							if (dFlag != null && "Y".equals(dFlag)) {
								aresJedis.hdel(key, mapKey);
							}

						}
					}
				}

			}

		} catch (Exception e) {
			logger.error("Ares err delMerchantHistory msg:" + e.getMessage());
			e.printStackTrace();
		} finally {
			aresJedis.close();
		}
		logger.info("delCustomerRule方法结束....use" + (new Date().getTime() - x) + "ms");
	}

	// 商户交易历史月表
	public void delMerchantHisMonth() {
		long x = new Date().getTime();
		logger.info("delMerchantHisMonth方法的开始.....");
		RedisUtil jedis = new RedisUtil(Constant.AresJedis);
		try {
			// 1得到有所符合条件的key
			String dFlag = jedis.get("DEL_FALG");
			Set<String> keys = jedis.keys("*_ORDER_HIS_MONTH");
			logger.info("得到商户交易历史月表所有符合条件的keys" + keys);
			for (String key : keys) {
				Set<String> fields = jedis.hkeys(key);
				for (String field : fields) {

					String[] fieldArr = field.split("_");
					if (null != fieldArr && fieldArr.length > 0) {
						logger.info("商户交易历史月表的日期" + fieldArr[0]);
						logger.info("判断商户交易历史月表是否在3个月以外且这个月以后" + compareDateMonth(fieldArr[0], 3));
						if (compareDateMonth(fieldArr[0], 5)) {
							// 3 删除每个key下符合条件的域
							logger.info("delMerchantHisMonth删除的每个key下的域:" + key + "filed" + field);
							if (dFlag != null && "Y".equals(dFlag)) {
								jedis.hdel(key, field);
							}
						}
					}

				}
			}
		} catch (Exception e) {
			logger.error("Ares err delMerchantHisMonth msg:" + e.getMessage());
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		logger.info("delMerchantHisMonth方法的结束.....use" + (new Date().getTime() - x) + "ms");
	}

	/**
	 * 删除汇总数据
	 */
	@Override
	public void delGatherData() {
		long x = new Date().getTime();
		logger.info("delGatherData method start...");
		RedisUtil jedis = new RedisUtil(Constant.AresJedis);
		try {
			String dFlag = jedis.get("DEL_FALG");
			String delDateKey = jedis.get("delDateKey");
			Set<String> keys = null;
			if (delDateKey == null) {
				/* 1 匹配数据库中所有的key 8615206261_20151030_CUSTOMER_ORDERHIS_EXT */
				keys = jedis.keys("*_*_CUSTOMER_ORDERHIS_EXT");
			} else {
				keys = jedis.keys("*" + delDateKey + "*_CUSTOMER_ORDERHIS_EXT");
			}
			logger.info("delGatherData 数据库中所有匹配的方法key：" + keys + " lenght: " + keys.size());
			for (String key : keys) {
				String[] keyArr = key.split("_");
				if (null != keyArr && keyArr.length > 0) {
					logger.info("delGatherData 所有匹配的key：" + key + " 对应日期:" + keyArr[1]);
					/* 2 找到符合条件的key */
					if (compareDate(keyArr[1], 4)) {
						logger.info("delGatherData 删除的key：" + key + " 对应日期：" + keyArr[1]);
						/* 3 删除key */
						if (dFlag != null && "Y".equals(dFlag)) {
							jedis.del(key);
						}

					}
				}

			}

		} catch (Exception e) {
			logger.error("Ares delGatherData method err msg:" + e.getMessage());
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		logger.info("delGatherData method end...use" + (new Date().getTime() - x) + "ms");
	}
}
