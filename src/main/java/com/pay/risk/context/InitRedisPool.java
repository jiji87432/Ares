/**
 *
 */
package com.pay.risk.context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.pay.risk.Constant;
import com.riskutil.redis.RedisUtil;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: InitRedisPool 此处填写需要参考的类
 * @version 2016年3月18日 下午4:26:25
 * @author zikun.liu
 */
@Component
public class InitRedisPool implements InitializingBean {
	private static final Logger logger = Logger.getLogger(InitRedisPool.class);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			logger.info(Constant.AresMsg + "初始化JedisPool开始");
			logger.info(Constant.AresJedis);
			RedisUtil.initPool(Constant.AresJedis);
			logger.info(Constant.MQJedis);
			RedisUtil.initPool(Constant.MQJedis);
			logger.info(Constant.CDLJedis);
			RedisUtil.initPool(Constant.CDLJedis);
			logger.info(Constant.ApolloJedis);
			RedisUtil.initPool(Constant.ApolloJedis);
			logger.info(Constant.PaiJedis);
			RedisUtil.initPool(Constant.PaiJedis);
			logger.info(Constant.AresMsg + "初始化JedisPool完成");
		} catch (Exception e) {
			logger.error(Constant.AresErr + "初始化JedisPool失败");
			e.printStackTrace();
		}

	}
}
