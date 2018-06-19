/**
 *
 */
package com.pay.risk.task;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pay.orderliness.proxy.AbstractJob;
import com.pay.risk.ares.interfaces.BaseRuleCalcService;


/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: DelRedisDate 此处填写需要参考的类
 * @version 2016年1月28日 上午9:35:58
 * @author xiaohua.fan
 */
@Component("delRedisDateTask")
public class DelRedisDateTask extends AbstractJob implements Serializable {

	private static final Logger logger = Logger.getLogger(DelRedisDateTask.class);

	@Resource
	private BaseRuleCalcService cdRuleService;

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		logger.info("DelRedisDate execute method start...");

		cdRuleService.deleteData();

		logger.info("DelRedisDate execute method end...");

	}

}
