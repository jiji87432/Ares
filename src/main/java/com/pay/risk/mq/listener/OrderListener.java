/*
 * 文 件 名:  OrderListener.java
 * 版    权:  支付有限公司. Copyright 2011-2015,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  zikun.liu
 * 修改时间:  2015-11-10
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.pay.risk.mq.listener;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.astrotrain.client.ATMessage;
import com.pay.astrotrain.client.ConcurrentlyMessageListener;
import com.pay.astrotrain.client.message.StringMessage;
import com.pay.risk.mq.service.OrderDataService;

/**
 * 商户订单MQ数据信息监听服务
 * <p>
 * @author zikun.liu
 * @version [V1.0, 2015-11-10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service("orderListener")
public class OrderListener implements ConcurrentlyMessageListener {
    /***/
    private static final Logger logger = LogManager.getLogger(OrderListener.class);

    /** 订单交易MQ数据报文处理服务接口 */
    @Autowired
    private OrderDataService orderDataService;

    /**
     * <重载方法一句话功能简述>
     * <功能详细描述>
     * @param messages
     * @return
     */

    @Override
    public void onMessage(List<ATMessage> messages) {
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        /*
         * 订单交易MQ数据报文转换
         */
        if (messages != null && !messages.isEmpty()) {

            for (ATMessage message : messages) {
                StringMessage stringMessage = (StringMessage) message;
                JSONObject json = JSONObject.fromObject(stringMessage.getMsg());
                jsonList.add(json);
                // logger.info("Ares msg order:" + json.toString());
            }

            /* 数据处理 */
            orderDataService.handleOrdertBatch(jsonList);

        }

    }

}
