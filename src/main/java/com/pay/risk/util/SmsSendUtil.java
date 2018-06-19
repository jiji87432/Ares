package com.pay.risk.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pay.camel.client.api.impl.CamelClientApiImpl;
import com.pay.camel.remote.bean.CamelResponse;
import com.pay.camel.remote.bean.Goods;
import com.pay.camel.remote.bean.MessageLevel;
import com.pay.camel.remote.bean.MessageSendType;
import com.pay.camel.remote.bean.MessageType;
import com.pay.camel.remote.bean.MessgReceiver;
import com.pay.common.util.PropertyUtil;



public class SmsSendUtil {

	private static final Log log = LogFactory.getLog(SmsSendUtil.class);

	// 获取短信平台的主机和端口
	private static PropertyUtil propertyUtil = PropertyUtil.getInstance("system");
	private static String host = propertyUtil.getProperty("camel.server.host");
	private static int port = Integer.valueOf(propertyUtil.getProperty("camel.server.port"));
	private static String appCode = propertyUtil.getProperty("camel.appCode");
	private static String token = propertyUtil.getProperty("camel.token");

	private static CamelClientApiImpl api = CamelClientApiImpl.getInstance();
	static {
		api.setHost(host);
		api.setPort(port);
	}

	// public static void send(String phoneNo, String content) {
	//
	// send(phoneNo, content, null);
	//
	// }

	public static void send2(String phoneNo, String content) {

		try {
			/*
			 * SmsBean bean = new SmsBean(phoneNo, content);
			 * bean.setToken(token);
			 * bean.setAppCode(appCode);
			 * SmsResponse response = api.send(bean, false);
			 */

			Goods goods = new Goods();
			goods.setMessgType(MessageType.NOTE);
			goods.setMessgLevel(MessageLevel.INFO);
			goods.setAppCode(appCode);
			goods.setBusiType("风控_短信");
			goods.setToken(token);
			goods.setContent(content);
			List<MessageSendType> messgSendTypes = new ArrayList<MessageSendType>();
			messgSendTypes.add(MessageSendType.SMS);
			goods.setMessgSendTypes(messgSendTypes);
			MessgReceiver messgReceiver = new MessgReceiver();
			goods.setMessgReceiver(messgReceiver);
			messgReceiver.setPhone(phoneNo);
			CamelResponse result = api.send(goods);

			log.info("Message sended result {" + result + "}");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("send2 method is error!");
		}
	}

	/**
	 * @Description 一句话描述方法用法
	 * @param phoneNo
	 * @param content
	 * @param sendLevel 短信级别 默认NORMAL，业务短信通道，单通道发送 WARN 单通道发送 ERROR 双短信通道发送 FATAL 双短信通道+手机APP推送
	 * @see 需要参考的类或方法
	 */
	// public static void send(String phoneNo, String content, SmsSendLevel sendLevel) {
	//
	// // 使用手机号码和内容构建短信bean，多个号码','分隔，最大不要超过500个
	//
	// SmsBean bean = new SmsBean(phoneNo, content);
	//
	// if (null == sendLevel) {
	// bean.setLevel(SmsSendLevel.NORMAL);
	// } else {
	// bean.setLevel(sendLevel);
	// }
	//
	// try {
	//
	// // 异步的方式发送短信，默认
	// api.send(bean, false);// 这里不要求捕获异常，在连接不到消息平台是会抛出异常!
	//
	// log.info("短信发送日志：------------" + new Date() + "-----发送短信给" + phoneNo);
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	//
	// }
	//
	// }
}
