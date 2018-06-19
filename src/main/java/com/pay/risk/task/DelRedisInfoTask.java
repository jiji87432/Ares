/**
 *
 */
package com.pay.risk.task;

import java.io.Serializable;

import javax.annotation.Resource;

import oracle.net.aso.d;

import org.apache.log4j.Logger;


import com.pay.risk.service.DelRedisInfoService;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: DelRedisInfoTask 此处填写需要参考的类
 * @version 2015年11月26日 上午10:01:03
 * @author xiaohui.wei
 */
public class DelRedisInfoTask extends com.pay.orderliness.proxy.AbstractJob implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DelRedisInfoTask.class);
	@Resource
	private DelRedisInfoService delRedisInfoService;


	@Override
	public void execute() {

		logger.info("DelRedisInfoTask execute 定时正式启动。。。。。。");

		delRedisInfoService.delKeyNoToday();

		logger.info("DelRedisInfoTask execute 定时启动结束。。。。。。");

	}

}
