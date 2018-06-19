package com.pay.risk.mq.consumer;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.pay.astrotrain.client.ConcurrentlyMessageListener;
import com.pay.astrotrain.client.Listener;
import com.pay.astrotrain.client.consumer.DefaultATPushConsumer;
import com.pay.common.util.PropertyUtil;

/**
 * 消费者批量消费示例
 *
 * <pre>
 * 需要在 ClassPath 路径下准备 astrotrain-consumer.properties 具体配置信息参考 src/main/resources下的配置
 * </pre>
 * @author jiang.li
 */
public class BatchConsumer implements InitializingBean {

	private static final Logger logger = LogManager.getLogger(BatchConsumer.class);

	private DefaultATPushConsumer atPushConsumer;

	/***/
	private List<ConcurrentlyMessageListener> messageListeners;
	/***/
	private static String[] registTopic = null;

	/**
	 * 获取 messageListeners
	 * @return 返回 messageListeners
	 */
	public List<ConcurrentlyMessageListener> getMessageListeners() {
		return messageListeners;
	}

	/**
	 * 设置 messageListeners
	 * @param 对messageListeners进行赋值
	 */
	public void setMessageListeners(List<ConcurrentlyMessageListener> messageListeners) {
		this.messageListeners = messageListeners;
	}

	/**
	 * 获取 atPushConsumer
	 * @return 返回 atPushConsumer
	 */
	public DefaultATPushConsumer getAtPushConsumer() {
		return atPushConsumer;
	}

	/**
	 * 设置 atPushConsumer
	 * @param 对atPushConsumer进行赋值
	 */
	public void setAtPushConsumer(DefaultATPushConsumer atPushConsumer) {
		this.atPushConsumer = atPushConsumer;
	}

	static {
		PropertyUtil propertyUtil = PropertyUtil.getInstance("astrotrain-consumer");
		registTopic = propertyUtil.getProperty("consumer.regist.topic").split(";");
	}

	/**
	 * 监听作为单例使用
	 */
	private BatchConsumer() {

	}

	public void subscribe() {
		if (messageListeners != null && messageListeners.size() > 0 && registTopic != null && registTopic.length == messageListeners.size()) {
			for (int i = 0; i < messageListeners.size(); i++) {
				try {
					logger.info("Ares msg listener:" + messageListeners.get(i));
					logger.info("Ares msg topic:" + registTopic[i]);
					// 订阅必须在start之前
					this.atPushConsumer.subscribe(registTopic[i], (Listener) messageListeners.get(i));
				} catch (MQClientException e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.error("Ares err 订阅主题与注册的监听器不对应！");
			logger.error("Ares err 监听器 === " + messageListeners);
			logger.error("Ares err 订阅主题 === " + registTopic);
		}
	}

	public void start() {
		if (this.atPushConsumer != null) {
			try {
				this.atPushConsumer.start();
				// System.in.read();//按任意键退出
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void shutdown() {
		if (this.atPushConsumer != null) {
			this.atPushConsumer.shutdown();
		}
	}

	/**
	 * 初始化订阅
	 * @see [类、类#方法、类#成员]
	 */
	private void init() {
		logger.info("MQ消息消费端初始化开始");
		this.subscribe();
		this.start();
		logger.info("MQ消息消费端初始化完成");
	}

	/**
	 * 属性全部注入后启动
	 * @throws Exception
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

}