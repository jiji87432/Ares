/*
 * 文 件 名:  OrderDataService.java
 * 版    权:  支付有限公司. Copyright 2011-2015,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  zikun.liu
 * 修改时间:  2015-11-10
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.pay.risk.mq.service;

import java.util.List;

import net.sf.json.JSONObject;

/**
 * 订单交易MQ数据报文处理服务接口
 * <p>
 * @author zikun.liu
 * @version [V1.0, 2015-11-10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface OrderDataService {
    /**
     * 处理订单交易MQ数据报文实体
     * <p>
     * @param orderPaymentDataList
     * @return
     * @see [类、类#方法、类#成员]
     */
    public void handleOrdertBatch(List<JSONObject> jsonList);

}
