/*
 * 文 件 名:  OrderDataServiceImpl.java
 * 版    权:  支付有限公司. Copyright 2011-2015,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  zikun.liu
 * 修改时间:  2015-11-10
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.pay.risk.mq.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.athena.remote.service.MerchantVerificationFacede;
import com.pay.risk.Constant;
import com.pay.risk.ares.cdl.constant.RedisConstants;
import com.pay.risk.ares.cdl.util.CDUtil;
import com.pay.risk.ares.secnodcash.SecondCashConstant;
import com.pay.risk.mq.service.OrderDataService;
import com.pay.risk.remote.service.SecondCashServiceFacade;
import com.pay.risk.util.DateUtils;
import com.pay.risk.util.OrderFilterUtil;
import com.pay.risk.util.StringUtil;
import com.riskutil.redis.RedisUtil;

/**
 * 订单交易MQ数据报文解析、操作服务类
 * <p>
 * 针对订单交易MQ数据报文处理。
 * @author zikun.liu
 * @version [版本号, 2015-11-10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service("orderDataService")
public class OrderDataServiceImpl implements OrderDataService {

    protected static final Logger logger = LogManager.getLogger(OrderDataServiceImpl.class);

    @Resource
    private MerchantVerificationFacede merchantVerificationFacede;

    @Resource
    private SecondCashServiceFacade secondCashServiceFacade;

    /**
     * MQ处理订单消息
     */
    @Override
    public void handleOrdertBatch(List<JSONObject> jsonList) {
        RedisUtil mqJedis = new RedisUtil(Constant.MQJedis);
        logger.info(Constant.AresMsg + " mq:" + jsonList);
        try {
            String _mf = mqJedis.get("MQ_LOG_FLAG");
            if (jsonList != null && !jsonList.isEmpty()) {
                // 订单状态：失败;成功;撤销
                String str = "INIT;SUCCESS;SETTLED;REPEAL;REVERSALED;AUTHORIZED";
                Map<String, JSONObject> map = new HashMap<String, JSONObject>();
                Date startTime = new Date();
                for (JSONObject json : jsonList) {

                    if (_mf != null && "Y".equals(_mf)) logger.info(Constant.AresMsg + "mq:" + json);
                    // 判断json中是否有以下值
                    if (json.get("createTime") == null || json.get("pan") == null || json.get("customerNo") == null || json.get("amount") == null
                            || json.get("status") == null || json.get("externalId") == null || json.get("orderOptimistic") == null || json.get("cuOpenTime") == null) {

                        continue;
                    }
                    if ("null".equals(String.valueOf(json.get("pan"))) || "null".equals(String.valueOf(json.get("customerNo")))
                            || "null".equals(String.valueOf(json.get("amount"))) || "null".equals(String.valueOf(json.get("createTime")))
                            || "null".equals(String.valueOf(json.get("status"))) || "null".equals(String.valueOf(json.get("externalId")))
                            || "null".equals(String.valueOf(json.get("orderOptimistic"))) || "null".equals(String.valueOf(json.get("cuOpenTime")))) {

                        continue;
                    }

                    if (OrderFilterUtil.orderDataFilter(json)) {
                        continue;
                    }

                    Calendar c1 = new GregorianCalendar();
                    c1.set(Calendar.HOUR_OF_DAY, 0);
                    c1.set(Calendar.MINUTE, 0);
                    c1.set(Calendar.SECOND, 0);
                    // 获得当日开始时间
                    Long dayStartTime = c1.getTime().getTime();
                    Long createTime = Long.valueOf(String.valueOf(json.get("createTime")));

                    // 判断本次订单状态是否为以上四种之一且订单时间是当日
                    if (str.contains(String.valueOf(json.get("status"))) && (createTime >= dayStartTime)) {

                        // 获得参考号
                        String externalId = String.valueOf(json.get("externalId"));
                        // 查看当前记录是否已存在
                        if (map.get(externalId) != null) {
                            // 获得已存在版本号
                            Integer oldOptimistic = Integer.valueOf(String.valueOf(map.get(externalId).get("orderOptimistic")));
                            // 获得当前版本号
                            Integer nowOptimistic = Integer.valueOf(String.valueOf(json.get("orderOptimistic")));
                            // 如果版本号大于已存在版本号则替换记录
                            if (nowOptimistic > oldOptimistic) {
                                map.put(externalId, json);
                            }
                            // 释放资源
                            oldOptimistic = null;
                            nowOptimistic = null;
                        } else {
                            // 不存在则记录
                            map.put(externalId, json);
                        }
                    }
                }

                if (map != null && map.size() > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String date = sdf.format(new Date());
                    Integer dseconds = (26 - (Integer.valueOf(new SimpleDateFormat("HH").format(startTime)))) * 3600;
                    for (String key : map.keySet()) {
                        // 订单状态
                        String status = String.valueOf(map.get(key).get("status"));
                        // 商户编号
                        String customerNo = String.valueOf(map.get(key).get("customerNo"));
                        // 交易金额
                        Double amount = Double.valueOf(String.valueOf(map.get(key).get("amount")));
                        // 交易卡号
                        String pan = String.valueOf(map.get(key).get("pan"));
                        // 历史信息综合KEY
                        String cHisKey = date + "_" + customerNo + SecondCashConstant.SUFFIX_ORDER_HIS;
                        // 商户开通距今天数
                        Integer openDays = (int) ((new Date().getTime() - Long.valueOf(String.valueOf(map.get(key).get("cuOpenTime")))) / (1000 * 60 * 60 * 24));
                        String cOpenTimes = String.valueOf(map.get(key).get("cuOpenTime"));// 商户开通的时间
                        // 是否芯片卡
                        Boolean complexCardFlag = false;
                        // 所属银行
                        String bank = String.valueOf(map.get(key).get("issuer"));
                        // 卡类型
                        String cardType = String.valueOf(map.get(key).get("cardType"));
                        // 交易时间 TODO
                        String transTime = String.valueOf(map.get(key).get("createTime"));
                        // agentId
                        String agentId = "";
                        try {
                            agentId = String.valueOf(map.get(key).get("agentId"));
                        } catch (Exception e3) {
                            // TODO Auto-generated catch block
                            e3.printStackTrace();
                        }

                        if (map.get(key) != null && map.get(key).get("complexCard") != null) {
                            String ccf = String.valueOf(map.get(key).get("complexCard"));
                            if ("Y".equals(ccf)) complexCardFlag = true;
                        }
                        try {
                            if (map.get(key) != null && map.get(key).get("mcc") != null) {
                                String mcc = String.valueOf(map.get(key).get("mcc"));
                                if ("2003".equals(mcc) || "2004".equals(mcc) || "2005".equals(mcc) || "2006".equals(mcc)) {
                                    mqJedis.sadd(dseconds, date + "_zyt_customerset", customerNo);
                                }
                            }

                            if (map.get(key) != null && map.get(key).get("businessType") != null) {
                                String businessType = String.valueOf(map.get(key).get("businessType"));
                                if (businessType != null && !"".equals(businessType) && businessType.contains("LFB")) {
                                    mqJedis.sadd(dseconds, date + "_lfb_customerset", customerNo);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        /**
                         * 某张卡片（借记卡或贷记卡）单日在某一合伙人商户5分钟内成功交易大于等于3笔且成功金额大于等于6000元（以该卡在该商户第一笔成功交易为起始进行计算5分钟内，单卡单日单商户），
                         */
                        try {
                            int seconds = DateUtils.getSeconds();

                            String keyAgentId = date + "_" + customerNo + "_AGENT_ID";
                            mqJedis.set(seconds, keyAgentId, agentId);

                            if ("SETTLED".equals(status) || "SUCCESS".equals(status)) {
                                String nowDate = DateUtils.getTodayStr();
                                // 确保是当前当天成功的第一笔的key
                                String CusPankey = nowDate + "_CUS_PAN_CP01_5min";
                                // 单卡单商户当天第一笔成功交易的key，有效时间为5分钟
                                String min5Key = nowDate + "_" + customerNo + "_" + pan + "_5min";
                                String min5KeyCount = nowDate + "_" + customerNo + "_" + pan + "_5minCOUNT";
                                String min5KeySumAmount = nowDate + "_" + customerNo + "_" + pan + "_5minSAMOUNT";

                                if (!mqJedis.sismember(CusPankey, min5Key)) {
                                    logger.info("CusPankey:" + CusPankey + ",min5Key:" + min5Key);
                                    mqJedis.sadd(seconds, CusPankey, min5Key);
                                    mqJedis.set(300, min5Key, "Y");
                                    mqJedis.incrby(seconds, min5KeyCount, 1L);
                                    mqJedis.incrByFloat(seconds, min5KeySumAmount, amount);
                                } else {
                                    if (mqJedis.exists(min5Key)) {
                                        Long count = mqJedis.incrby(seconds, min5KeyCount, 1L);
                                        Double sumAmount = mqJedis.incrByFloat(seconds, min5KeySumAmount, amount);
                                        if (count >= 3 && sumAmount >= 6000) {
                                            logger.info("min5KeyCount:" + min5KeyCount + ",value:" + count);
                                            logger.info("min5KeySumAmount:" + min5KeySumAmount + ",value:" + sumAmount);
                                            mqJedis.hset(seconds, cHisKey, "PAN5M1", "Y");
                                        }
                                    }
                                }
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }

                        try {
                            if ("INIT".equals(status)) {
                                // 若参考号在撤销集合中不存在则进行数据操作
                                if (!mqJedis.sismember(date + SecondCashConstant.repalExist, key) && !mqJedis.sismember(date + SecondCashConstant.initExist, key)) {
                                    // logger.info(Constant.AresMsg + " A " + key);
                                    // 将参考号放入当日失败交易集合中
                                    mqJedis.sadd(dseconds, date + SecondCashConstant.initExist, key);
                                    // 该商户当日失败交易笔数加一
                                    mqJedis.incr(dseconds, date + "_" + customerNo + SecondCashConstant.initCount);
                                    // 该商户当日失败金额加上本次金额
                                    String initAmountStr = date + "_" + customerNo + SecondCashConstant.initAmount;
                                    calculate(dseconds, amount, initAmountStr, 1);
                                    // 记录该商户当日失败银行卡号
                                    mqJedis.hset(dseconds, date + "_" + customerNo + SecondCashConstant.inipan, key, pan);
                                    // 将卡号放入当日失败卡集合中
                                    mqJedis.sadd(dseconds, date + "_" + customerNo + Constant.initPan, pan);

                                }
                            } else if ("REPEAL".equals(status)) {
                                if (!mqJedis.sismember(date + SecondCashConstant.repalExist, key)) {
                                    // logger.info(Constant.AresMsg + " D " + key);
                                    // 将参考号添加进当日撤销交易集合
                                    mqJedis.sadd(dseconds, date + SecondCashConstant.repalExist, key);
                                    // 该商户当日撤销交易笔数加一
                                    mqJedis.incr(dseconds, date + "_" + customerNo + SecondCashConstant.repalCount);
                                    // 该商户当日撤销金额加本次撤销金额
                                    String repalAmountStr = date + "_" + customerNo + SecondCashConstant.repalAmount;
                                    calculate(dseconds, amount, repalAmountStr, 1);
                                    // 判断参考号是否存在失败集合中
                                    if (mqJedis.sismember(date + SecondCashConstant.initExist, key)) {
                                        // logger.info(Constant.AresMsg + " G " + key);
                                        // 将参考号从失败交易集合中删除
                                        mqJedis.srem(date + SecondCashConstant.initExist, key);
                                        // 将卡号从当日失败卡集合中删除
                                        mqJedis.srem(date + "_" + customerNo + Constant.initPan, pan);
                                        // 将此卡号从该商户当日失败卡号集合中删除
                                        mqJedis.hdel(date + "_" + customerNo + SecondCashConstant.inipan, key);
                                        // 该商户当日失败交易笔数减一
                                        mqJedis.decr(date + "_" + customerNo + SecondCashConstant.initCount);
                                        // 该商户当日失败金额减本次金额
                                        String initAmountStr = date + "_" + customerNo + SecondCashConstant.initAmount;
                                        calculate(dseconds, amount, initAmountStr, 0);
                                    }
                                    // 判断参考号在成功集合中是否存在
                                    if (mqJedis.sismember(date + SecondCashConstant.successExist, key)) {
                                        // logger.info(Constant.AresMsg + " I " + key);
                                        // 将参考号从成功交易集合中删除
                                        mqJedis.srem(date + SecondCashConstant.successExist, key);
                                        // 该商户当日成功交易笔数减一
                                        mqJedis.decr(date + "_" + customerNo + SecondCashConstant.successCount);
                                        // 该商户当日成功金额减本次金额
                                        String successAmountStr = date + "_" + customerNo + SecondCashConstant.successAmount;
                                        calculate(dseconds, amount, successAmountStr, 0);
                                        // 在该商户当日最大交易额中删除此笔交易额
                                        mqJedis.zrem(date + "_" + customerNo + SecondCashConstant.mostAmount, key);
                                    }
                                }
                            } else if ("SETTLED".equals(status) || "SUCCESS".equals(status)) {
                                // 若参考号在撤销集合中不存在则进行数据操作
                                if (!mqJedis.sismember(date + SecondCashConstant.repalExist, key) && !mqJedis.sismember(date + SecondCashConstant.successExist, key)) {
                                    // 将参考号添加进当日成功集合中
                                    // logger.info(Constant.AresMsg + " K " + key);
                                    mqJedis.sadd(dseconds, date + SecondCashConstant.successExist, key);
                                    // 该商户当日成功交易笔数加一
                                    mqJedis.incr(dseconds, date + "_" + customerNo + SecondCashConstant.successCount);
                                    // 该商户当日成功金额加本次金额
                                    String cKey = date + "_" + customerNo + SecondCashConstant.successAmount;

                                    String unComplesSuccessAmount = date + "_" + customerNo + SecondCashConstant.unComplesSuccessAmount;
                                    // cHisKey
                                    //
                                    Map<String, String> m2 = mqJedis.hgetAll(cHisKey);
                                    try {

                                        if (!complexCardFlag) {
                                            String cMaxValue = mqJedis.hget(cHisKey, "UNCOMPAMOUNT");
                                            if (m2 == null || m2.size() == 0 || cMaxValue == null || "".equals(cMaxValue)) {
                                                mqJedis.hset(dseconds, cHisKey, "UNCOMPAMOUNT", String.valueOf(amount));
                                            }
                                            if (m2 != null && m2.size() > 0 && cMaxValue != null && !"".equals(cMaxValue)) {
                                                if (amount > Double.valueOf(cMaxValue)) mqJedis.hset(dseconds, cHisKey, "UNCOMPAMOUNT", String.valueOf(amount));
                                            }

                                        }

                                    } catch (Exception e) {
                                        logger.info(e.getMessage() + "unComplesSuccessAmount ___***" + map);
                                    }

                                    try {

                                        if (cardType != null && !"".equals(cardType)) {
                                            if ("CREDIT_CARD".equals(cardType)) {
                                                String cMaxValue = mqJedis.hget(cHisKey, "CRMAAMOUT");
                                                if (m2 == null || m2.size() == 0 || cMaxValue == null || "".equals(cMaxValue)) {
                                                    mqJedis.hset(dseconds, cHisKey, "CRMAAMOUT", String.valueOf(amount));
                                                }
                                                if (m2 != null && m2.size() > 0 && cMaxValue != null && !"".equals(cMaxValue)) {
                                                    if (amount > Double.valueOf(cMaxValue)) mqJedis.hset(dseconds, cHisKey, "CRMAAMOUT", String.valueOf(amount));
                                                }

                                            }
                                        }
                                    } catch (Exception e) {
                                        logger.info(e.getMessage() + "___***" + map);
                                    }

                                    try {
                                        // 当天超第一笔成功的超过8K的交易前 交易金额
                                        if (amount >= 8000) setTodayHisMin8K(dseconds, customerNo, cKey);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    calculate(dseconds, amount, cKey, 1);
                                    calculate(dseconds, amount, unComplesSuccessAmount, 1);
                                    // 判断参考号在失败交易集合中是否存在
                                    if (mqJedis.sismember(date + SecondCashConstant.initExist, key)) {
                                        // 将参考号从失败交易集合中删除
                                        // logger.info(Constant.AresMsg + " N " + key);
                                        mqJedis.srem(date + SecondCashConstant.initExist, key);
                                        // 将卡号从当日失败卡集合中删除
                                        mqJedis.srem(date + "_" + customerNo + Constant.initPan, pan);
                                        // 将此卡号从该商户当日失败卡号集合中删除
                                        mqJedis.hdel(date + "_" + customerNo + SecondCashConstant.inipan, key);
                                        // 该商户当日失败交易笔数减一
                                        mqJedis.decr(date + "_" + customerNo + SecondCashConstant.initCount);
                                        // 该商户当日失败金额减本次金额
                                        String initAmountKey = date + "_" + customerNo + SecondCashConstant.initAmount;
                                        calculate(dseconds, amount, initAmountKey, 0);
                                    }
                                    // 将本次交易金额作为权重存入权重集合中
                                    BigDecimal bd = new BigDecimal(amount);
                                    mqJedis.zadd(dseconds, date + "_" + customerNo + SecondCashConstant.mostAmount, bd.setScale(2, BigDecimal.ROUND_HALF_UP)
                                            .doubleValue(), key);
                                    // 一个商户下只保留5个最高消费
                                    while (mqJedis.zcard(date + "_" + customerNo + SecondCashConstant.mostAmount) > 5) {
                                        mqJedis.zremrangeByRank(date + "_" + customerNo + SecondCashConstant.mostAmount, (long) 0, (long) 0);
                                    }
                                }
                            } else if ("REVERSALED".equals(status)) {
                                // 判断参考号是否存在撤销集合中
                                if (mqJedis.sismember(date + SecondCashConstant.repalExist, key)) {
                                    // logger.info(Constant.AresMsg + " G " + key);
                                    // 将参考号从失败交易集合中删除
                                    mqJedis.srem(date + SecondCashConstant.repalExist, key);
                                    // 该商户当日失败交易笔数减一
                                    mqJedis.decr(date + "_" + customerNo + SecondCashConstant.repalCount);
                                    // 该商户当日失败金额减本次金额
                                    String repalAmountStr = date + "_" + customerNo + SecondCashConstant.repalAmount;
                                    calculate(dseconds, amount, repalAmountStr, 0);
                                }
                                // 判断参考号是否存在失败集合中
                                if (mqJedis.sismember(date + SecondCashConstant.initExist, key)) {
                                    // logger.info(Constant.AresMsg + " G " + key);
                                    // 将参考号从失败交易集合中删除
                                    mqJedis.srem(date + SecondCashConstant.initExist, key);
                                    // 将卡号从当日失败卡集合中删除
                                    mqJedis.srem(date + "_" + customerNo + Constant.initPan, pan);
                                    // 将此卡号从该商户当日失败卡号集合中删除
                                    mqJedis.hdel(date + "_" + customerNo + SecondCashConstant.inipan, key);
                                    // 该商户当日失败交易笔数减一
                                    mqJedis.decr(date + "_" + customerNo + SecondCashConstant.initCount);
                                    // 该商户当日失败金额减本次金额
                                    String initAmountStr = date + "_" + customerNo + SecondCashConstant.initAmount;
                                    calculate(dseconds, amount, initAmountStr, 0);
                                }
                                // 判断参考号在成功集合中是否存在
                                if (mqJedis.sismember(date + SecondCashConstant.successExist, key)) {
                                    // logger.info(Constant.AresMsg + " I " + key);
                                    // 将参考号从成功交易集合中删除
                                    mqJedis.srem(date + SecondCashConstant.successExist, key);
                                    // 该商户当日成功交易笔数减一
                                    mqJedis.decr(date + "_" + customerNo + SecondCashConstant.successCount);
                                    // 该商户当日成功金额减本次金额
                                    String successAmountStr = date + "_" + customerNo + SecondCashConstant.successAmount;
                                    calculate(dseconds, amount, successAmountStr, 0);
                                    // 在该商户当日最大交易额中删除此笔交易额
                                    mqJedis.zrem(date + "_" + customerNo + SecondCashConstant.mostAmount, key);
                                }
                            }
                            // 记录当天最后一笔交易
                            // TODO
                            mqJedis.lpush(dseconds, date + "_" + customerNo + SecondCashConstant.SUFFIX_DATE_TRANSLASTORDER, key);
                            logger.info(Constant.AresMsg + " order success use " + (new Date().getTime() - startTime.getTime()) + " externalId:" + key + " status:"
                                    + status + " pan:" + pan + " amount:" + amount + " customerNo:" + customerNo);

                            /* t0数据汇总 */
                            Date t0StartTime = new Date();

                            // 超时时间
                            Integer overtime = 3600 * 22;
                            /*
                             * 风险案例 商户当日单笔贷记卡成功交易金额大于等于15000元，商户开通至今（不含贷记卡交易当天）成功交易只有借记卡，且交易天数大于等于3天，其中任意一张借记卡过去7日内在其他商户有过交易
                             * 单笔贷记卡金额 >= 15000
                             */
                            // String cardType = String.valueOf(map.get(key).get("cardType"));
                            String t0CustomerNo = String.valueOf(map.get(key).get("customerNo"));
                            String t0hisKey = StringUtil.getT0Key(date, t0CustomerNo, "order_his");
                            try {
                                if ("SETTLED".equals(status) || "SUCCESS".equals(status)) {
                                    if ("CREDIT_CARD".equals(cardType) && amount >= 15000) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY006403", "SP1");
                                        mqJedis.sadd(overtime, t0key, t0CustomerNo);
                                    }
                                }
                                /*
                                 * 商户开通至今（不含贷记卡交易当天）成功交易只有借记卡，且交易天数大于等于3天，其中任意一张借记卡过去7日内在其他商户有过交易
                                 */
                                String t0EXT9Flag = mqJedis.hget(t0hisKey, "EXT9");
                                if ("Y".equals(t0EXT9Flag)) {
                                    String t0key = StringUtil.getT0Key(date, "CATEGORY006403", "EXT9");
                                    mqJedis.sadd(overtime, t0key, t0CustomerNo);
                                }
                            } catch (Exception e) {
                                logger.error(Constant.AresErr + " CATEGORY006403:" + e.getMessage());
                                e.printStackTrace();
                            }

                            /*
                             * 新入网商户（开通30日内），7日内出现2张及2张以上卡片存在跨商户刷卡成功或失败交易，且此商户或其关联商户在前7日内笔均金额在2000元以下
                             */
                            try {
                                String t0EXT7Flag = mqJedis.hget(t0hisKey, "EXT7");
                                if ("Y".equals(t0EXT7Flag)) {
                                    String t0key = StringUtil.getT0Key(date, "CATEGORY006389", "EXT7");
                                    mqJedis.sadd(overtime, t0key, t0CustomerNo);
                                }

                                /*
                                 * KLB商户当日单笔成功交易金额大于等于8000元
                                 */
                                if ("SETTLED".equals(status) || "SUCCESS".equals(status)) {
                                    if (amount >= 8000 && !complexCardFlag) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY006389", "SP1");
                                        mqJedis.sadd(overtime, t0key, t0CustomerNo);
                                    }
                                }
                            } catch (Exception e) {
                                logger.error(Constant.AresErr + " CATEGORY006389:" + e.getMessage());
                                e.printStackTrace();
                            }

                            /*
                             * KLB商户当日单笔成功交易金额大于等于15000元
                             */
                            // 商户当前失败金额
                            Double initAmount = 0.0;
                            // 商户当前失败笔数
                            Integer initCount = 0;
                            // 商户当前失败卡数
                            Long initPanCount = mqJedis.scard(date + "_" + customerNo + Constant.initPan);
                            // 商户当前成功交易笔数
                            Integer successCount = 0;
                            try {
                                if ("SETTLED".equals(status) || "SUCCESS".equals(status)) {
                                    if (amount >= 15000 && !complexCardFlag) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY006387", "SP1");
                                        mqJedis.sadd(overtime, t0key, t0CustomerNo);
                                    }
                                }

                                /*
                                 * 且涉案金额≥60000（失败）且涉案笔数≥ 3且成功交易笔数≥ 1且交易失败卡片大于等于2张。
                                 */
                                String ia = mqJedis.get(date + "_" + customerNo + SecondCashConstant.initAmount);
                                if (ia != null && !"".equals(ia)) {
                                    initAmount = Double.valueOf(ia);
                                }

                                String ic = mqJedis.get(date + "_" + customerNo + SecondCashConstant.initCount);
                                if (ic != null && !"".equals(ic)) {
                                    initCount = Integer.valueOf(ic);
                                }
                                String sam = mqJedis.get(date + "_" + customerNo + SecondCashConstant.successAmount);
                                Double successAmount = 0.0;
                                if (sam != null && !"".equals(sam)) {
                                    successAmount = Double.valueOf(sam);

                                }

                                String sc = mqJedis.get(date + "_" + customerNo + SecondCashConstant.successCount);
                                if (sc != null && !"".equals(sc)) {
                                    successCount = Integer.valueOf(sc);
                                }

                                if (successCount >= 1 && initPanCount >= 2 && successAmount >= 10000.0) {
                                    String t0key = StringUtil.getT0Key(date, "CATEGORY003144", "SP2");
                                    mqJedis.sadd(overtime, t0key, customerNo);

                                }
                            } catch (Exception e) {
                                logger.error(Constant.AresErr + " CATEGORY003144:" + e.getMessage());
                                e.printStackTrace();
                            }

                            /*
                             * 交易失败卡>=2张，交易笔数>=5笔,涉案金额≥60000
                             */
                            // 商户当前撤销交易笔数
                            Integer repalCount = 0;
                            try {
                                String rc = mqJedis.get(date + "_" + customerNo + SecondCashConstant.repalCount);
                                if (rc != null && !"".equals(rc)) {
                                    repalCount = Integer.valueOf(rc);
                                }

                                // 商户当前交易笔数
                                Integer sumConut = repalCount + successCount + initCount;
                                if (sumConut >= 5 && initPanCount >= 2 && successCount >= 1) {
                                    String t0key = StringUtil.getT0Key(date, "CATEGORY003002", "SP1");
                                    mqJedis.sadd(overtime, t0key, customerNo);

                                }
                            } catch (Exception e) {
                                logger.error(Constant.AresErr + " CATEGORY003002:" + e.getMessage());
                                e.printStackTrace();
                            }

                            /*
                             * 商户开通30日内，开通后第一笔成功交易为借记卡交易，且当日单笔成功交易金额大于等于8000元
                             */
                            try {
                                String t0EXT8Flag = mqJedis.hget(t0hisKey, "EXT8");
                                if ("SETTLED".equals(status) || "SUCCESS".equals(status)) {
                                    Double m8Ksum = 0.0;
                                    String sam = mqJedis.hget(t0hisKey, "m8Ksum");
                                    if (sam != null && !"".equals(sam)) {
                                        m8Ksum = Double.valueOf(sam);
                                    }
                                    Double EXT8_AMOUNT = 0.0;
                                    String ext8am = mqJedis.hget(t0hisKey, "EXT8_AMOUNT");
                                    if (ext8am != null && !"".equals(ext8am)) {
                                        EXT8_AMOUNT = Double.valueOf(ext8am);
                                    }
                                    m8Ksum += EXT8_AMOUNT;
                                    if ("Y".equals(t0EXT8Flag) && amount >= 8000 && m8Ksum <= 200) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY006399", "SP1");
                                        mqJedis.sadd(overtime, t0key, customerNo);

                                    }
                                }
                            } catch (Exception e) {
                                logger.error(Constant.AresErr + " CATEGORY006399:" + e.getMessage());
                                e.printStackTrace();
                            }

                            /*
                             * 单笔金额大于等于15000且单笔金额大于等于前10日笔均20倍，前10日笔均金额小于等于1000元
                             */
                            // 商户当前成功交易额

                            // 单笔金额大于等于15000且该卡片在前45天内在该商户无成功交易，单笔金额大于等于前30日笔均20倍，当日成功交易金额大于等于30000，前30日笔均金额小于等于2000元，开通在3日外；
                            Double successAmount = 0.0;
                            try {

                                String front30_avg = getFront30Avg(mqJedis, t0hisKey);

                                String front45Notsu = get45Notsu(mqJedis, t0hisKey);

                                String sam = mqJedis.get(date + "_" + customerNo + SecondCashConstant.successAmount);

                                int i = 0;

                                if ("SETTLED".equals(status) || "SUCCESS".equals(status)) {
                                    if (amount >= 15000) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY003104", "SP1");
                                        mqJedis.sadd(overtime, t0key, customerNo);
                                        i++;
                                    }
                                }

                                if ("Y".equals(front45Notsu)) {
                                    String t0key1 = StringUtil.getT0Key(date, "CATEGORY003104", "SP2");
                                    mqJedis.sadd(overtime, t0key1, customerNo);
                                    i++;
                                }

                                if (front30_avg != null) {
                                    Double _30Avg = Double.valueOf(front30_avg);
                                    if (amount > (_30Avg * 20)) {
                                        String t0key1 = StringUtil.getT0Key(date, "CATEGORY003104", "SP3");
                                        mqJedis.sadd(overtime, t0key1, customerNo);
                                        i++;
                                    }
                                    if (_30Avg <= 2000) {
                                        String t0key1 = StringUtil.getT0Key(date, "CATEGORY003104", "SP4");
                                        mqJedis.sadd(overtime, t0key1, customerNo);
                                        i++;
                                    }
                                }

                                if (sam != null && !"".equals(sam)) {
                                    successAmount = Double.valueOf(sam);
                                    if (successAmount >= 30000) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY003104", "SP5");
                                        mqJedis.sadd(overtime, t0key, customerNo);
                                        i++;
                                    }
                                }

                                if (openDays > 3) {
                                    String t0key = StringUtil.getT0Key(date, "CATEGORY003104", "SP6");
                                    mqJedis.sadd(overtime, t0key, customerNo);
                                    i++;
                                }

                                if (i == 6) {
                                    mqJedis.hset(overtime, t0hisKey, "CATEGORY003104", "Y");
                                }
                                i = 0;

                                /*
                                 * if (front10_avg != null && !"".equals(front10_avg)) {
                                 * if ("SETTLED".equals(status) || "SUCCESS".equals(status)) {
                                 * Double front10Avg = Double.valueOf(front10_avg);
                                 * if (front10Avg > 0.0 && amount >= 15000 && !complexCardFlag && (amount / front10Avg >= 20) && front10Avg <= 1000) {
                                 * String t0key = StringUtil.getT0Key(date, "CATEGORY003104", "SP1");
                                 * mqJedis.sadd(overtime, t0key, customerNo);
                                 * }
                                 * }
                                 * }
                                 * /*
                                 * 当日成功交易金额大于等于30000
                                 * if (sam != null && !"".equals(sam)) {
                                 * successAmount = Double.valueOf(sam);
                                 * if (successAmount >= 30000) {
                                 * String t0key = StringUtil.getT0Key(date, "CATEGORY003104", "SP2");
                                 * mqJedis.sadd(overtime, t0key, customerNo);
                                 * }
                                 * }
                                 */

                                /*
                                 * 开通在3日外
                                 * if (openDays > 3) {
                                 * String t0key = StringUtil.getT0Key(date, "CATEGORY003104", "SP3");
                                 * mqJedis.sadd(overtime, t0key, customerNo);
                                 * }
                                 */
                            } catch (Exception e) {
                                logger.error(Constant.AresErr + " CATEGORY003104:" + e.getMessage());
                                e.printStackTrace();
                            }

                            /*
                             * 单笔成功交易金额≥10000元
                             */
                            try {
                                if ("SETTLED".equals(status) || "SUCCESS".equals(status)) {
                                    if (amount >= 10000 && !complexCardFlag) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY003003", "SP1");
                                        mqJedis.sadd(overtime, t0key, customerNo);
                                    }
                                }

                                /*
                                 * 前30日内商户出现10元以下的成功交易≥3笔且累计金额小于10000
                                 */
                                String t0EXT10Flag = mqJedis.hget(t0hisKey, "EXT10");
                                if (t0EXT10Flag != null && !"".equals(t0EXT10Flag)) {
                                    if ("Y".equals(t0EXT10Flag)) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY003003", "SP2");
                                        mqJedis.sadd(overtime, t0key, customerNo);
                                    }
                                }
                            } catch (Exception e) {
                                logger.error(Constant.AresErr + " CATEGORY003003:" + e.getMessage());
                                e.printStackTrace();
                            }

                            /*
                             * 1日内同一商户撤销成功的交易≥5
                             */
                            Double repalAmount = 0.0;
                            try {
                                if (repalCount >= 5) {
                                    String t0key = StringUtil.getT0Key(date, "CATEGORY003069", "SP1");
                                    mqJedis.sadd(overtime, t0key, customerNo);
                                }

                                /*
                                 * 总交易金额≥3000
                                 */

                                String ra = mqJedis.get(date + "_" + customerNo + SecondCashConstant.repalAmount);
                                if (ra != null && !"".equals(ra)) {
                                    repalAmount = Double.valueOf(ra);
                                }
                                // 该商户当前总交易金额
                                Double sumAmount = repalAmount + initAmount + successAmount;
                                if (sumAmount >= 3000) {
                                    String t0key = StringUtil.getT0Key(date, "CATEGORY003069", "SP2");
                                    mqJedis.sadd(overtime, t0key, customerNo);
                                }
                            } catch (Exception e) {
                                logger.error(Constant.AresErr + " CATEGORY003069:" + e.getMessage());
                                e.printStackTrace();
                            }

                            /*
                             * 乐付宝：1日内成功交易金额≥100000元
                             */
                            // try {
                            // Double ucAmount = 0.0;
                            // String ucAmountStr = mqJedis.get(date + "_" + customerNo + SecondCashConstant.unComplesSuccessAmount);
                            // if (ucAmountStr != null) {
                            // ucAmount = Double.parseDouble(ucAmountStr);
                            // }
                            // if (ucAmount >= 100000) {
                            // String t0key = StringUtil.getT0Key(date, "CATEGORY003075", "SP1");
                            // mqJedis.sadd(overtime, t0key, customerNo);
                            // }
                            //
                            // /*
                            // * 乐付宝：前30天成功交易金额≤10000元
                            // */
                            // String t0EXT11Flag = mqJedis.hget(t0hisKey, "EXT11VALUE") == null ? "0.0" : mqJedis.hget(t0hisKey, "EXT11VALUE");
                            // Double ext11 = Double.parseDouble(t0EXT11Flag);
                            // if (ext11 <= 10000.0) {
                            // String t0key = StringUtil.getT0Key(date, "CATEGORY003075", "SP2");
                            // mqJedis.sadd(overtime, t0key, customerNo);
                            // }
                            //
                            // /*
                            // * 乐付宝：商户开通在30天内
                            // */
                            // if (openDays <= 30) {
                            // String t0key = StringUtil.getT0Key(date, "CATEGORY003075", "SP3");
                            // mqJedis.sadd(overtime, t0key, customerNo);
                            // }
                            // } catch (Exception e) {
                            // logger.error(Constant.AresErr + " CATEGORY003075:" + e.getMessage());
                            // e.printStackTrace();
                            // }

                            /**
                             * 单卡单商户
                             * 商户开通30日内,1日内单卡（贷记卡）成功交易笔数≥2笔且笔均≥4500且总交易金额大于30000且该商户前10日（含当日）
                             * 交易笔数≤10笔且前10日（不含当日）笔均≤100，（无交易商户算作0） 。
                             * SET_KEY :WK20161116SET 伪卡的key
                             * cardType : CREDIT_CARD 借记卡
                             * CDOP：当天单卡currentDayOnePan
                             * CDOP：当天单卡currentDayOnePan
                             * 通过customerNo获取到该商户前十天的交易次数。
                             */
                            try {
                                /* 定义key */
                                String wkKey = "WK20161116SET";
                                String wk_map_Key = date + "_" + customerNo + "_" + pan + "_" + wkKey;
                                String wk_set_Key = date + "_" + customerNo + "_" + "WK20161116_CUS_SET";
                                String _sAmountHKey = "sumAmountKey";
                                String _countHKey = "countKey";
                                if (cardType != null && customerNo != null && status != null && "CREDIT_CARD".equals(cardType)
                                        && mqJedis.sismember(wkKey, customerNo) && ("SUCCESS".equals(status) || "SETTLED".equals(status))) {
                                    /* 计算 */
                                    Map<String, String> wkmap = mqJedis.hgetAll(wk_map_Key);
                                    if (wkmap != null) {
                                        // 取到了值
                                        if (_mf != null && "Y".equals(_mf)) logger.info(wk_map_Key + " 运算之前  map " + map);
                                        String c_sumAmount = wkmap.get(_sAmountHKey);
                                        String c_count = wkmap.get(_countHKey);
                                        if (c_sumAmount != null && c_count != null && !"".equals(c_sumAmount) && !"".equals(c_count)) {
                                            Double _sumAmount = Double.valueOf(c_sumAmount);
                                            Integer _count = Integer.valueOf(c_count);
                                            _sumAmount += amount;
                                            _count++;
                                            Double avg = _sumAmount / _count;
                                            if (_count >= 2 && avg >= 4500 && _sumAmount >= 30000) {
                                                // 将命中的卡片放入对应customerNo的 setKey中
                                                mqJedis.sadd(dseconds, wk_set_Key, pan);
                                                if (_mf != null && "Y".equals(_mf)) logger.info(wk_set_Key + " sadd" + "pan " + pan + " sumAmount " + _sumAmount
                                                        + " count " + _count + " avg " + avg);
                                            } else {
                                                // 如果交易次数的增加导致平均值不再满足条件的时候就将对应的pan移除掉。
                                                mqJedis.srem(wk_set_Key, pan);
                                                if (_mf != null && "Y".equals(_mf)) logger.info(wk_set_Key + " remove" + "pan " + pan + " sumAmount " + _sumAmount
                                                        + " count " + _count + " avg " + avg);
                                            }
                                            wkmap.put(_sAmountHKey, String.format("%.2f", _sumAmount));
                                            wkmap.put(_countHKey, _count.toString());
                                            mqJedis.hmset(dseconds, wk_map_Key, wkmap);
                                            if (_mf != null && "Y".equals(_mf)) logger.info(wk_map_Key + " 运算之后  map " + map);
                                        } else {
                                            wkmap.put(_sAmountHKey, String.format("%.2f", amount));
                                            wkmap.put(_countHKey, "1");
                                            mqJedis.hmset(dseconds, wk_map_Key, wkmap);
                                            if (_mf != null && "Y".equals(_mf)) logger.info(wk_map_Key + " 该卡当天在该商户第一笔交易 " + amount);
                                        }
                                    } else {
                                        // 没取到值 将当次交易相关信息放入缓存
                                        wkmap = new HashMap<String, String>();
                                        wkmap.put(_sAmountHKey, String.format("%.2f", amount));
                                        wkmap.put(_countHKey, "1");
                                        mqJedis.hmset(dseconds, wk_map_Key, wkmap);
                                        if (_mf != null && "Y".equals(_mf)) logger.info(wk_map_Key + " 该卡当天在该商户第一笔交易 " + amount);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            /*** 统计商户当天的第一笔交易无论成功或失败 ***/
                            try {
                                String keyFirstAmount = CDUtil.getNowTime() + "_" + customerNo + "_FIRSTAMOUNT";
                                if (!mqJedis.exists(keyFirstAmount)) {
                                    mqJedis.set(CDUtil.getSeconds(), keyFirstAmount, String.valueOf(amount));
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            /**
                             * 商户开通30日内  单张贷记卡一日内（30分钟）有过跨商户交易（两商户即可）交易，
                             * 交易成功笔数大于等于3笔，
                             * 笔均大于等于4000元且交易间隔不超过30分钟（时间以第一笔成交易向后推迟30分钟），
                             * 该张卡片当日（30分钟）交易成功金额大于等于70000元且小于等于160000元，商户开通时间相差3日内（含3日）。
                             * key 定义说明： _20161122_ncbak 新增商户卡片大额异常交易
                             */

                            try {
                                if (status != null && cardType != null && ("SUCCESS".equals(status) || "SETTLED".equals(status)) && "CREDIT_CARD".equals(cardType)) {
                                    String _k = "_20161122_ncbak"; // new customer big amount key ncbaak
                                    String psk = date + "_set_" + pan + _k; // pan set key
                                    String pmk = date + "_map_" + pan + _k; // pan map key
                                    String csk = date + "_set_" + customerNo + _k; // customerNo set key
                                    String _fttk = "fttk"; // first trans time key
                                    String _sCount = "sc"; // 30 minute success count
                                    String _stAmount = "stamount"; // 30 minute success total amount
                                    String _cioTime = "ciot"; // 30 minute customer min open time
                                    String _cxoTime = "cxot"; // 30 minute customer max open time
                                    Map<String, String> _pm = mqJedis.hgetAll(pmk);
                                    if (_pm == null || _pm.isEmpty() || _pm.get(_fttk) == null || _pm.get(_fttk).trim().equals("")) {
                                        // 当天该卡的第一笔交易
                                        _pm = new HashMap<String, String>();
                                        _pm.put(_fttk, transTime); // 记录该卡当天的最早交易时间
                                        _pm.put(_stAmount, String.format("%.2f", amount));// 该卡的交易金额
                                        _pm.put(_sCount, "1");// 交易笔数
                                        _pm.put(_cioTime, cOpenTimes);// 关联商户最早的开通时间
                                        _pm.put(_cxoTime, cOpenTimes);// 关联商户最晚的开通时间
                                        mqJedis.hmset(dseconds, pmk, _pm);
                                        mqJedis.sadd(dseconds, psk, csk); // 该卡当天交易过的商户

                                        if (_mf != null && "Y".equals(_mf)) logger.info(_k + " 该卡当天第一笔交易 " + _pm);
                                    } else {

                                        long _fTimelong = Long.valueOf(_pm.get(_fttk)); // 获取该卡当天最早一笔交易时间

                                        long _tTimelong = Long.valueOf(transTime); // 当前交易时间

                                        if ((_tTimelong - _fTimelong) < (30 * 60 * 1000)) {// 30分钟内的交易参与计算 超过30分钟的交易不计算
                                            long _cOpenTime = Long.valueOf(cOpenTimes);
                                            if (_mf != null && "Y".equals(_mf)) logger.info(_k + " 当前交易时间 " + _cOpenTime);

                                            // 30分钟内该卡交易商户的最早开通时间
                                            long _fcOpenTime = Long.valueOf(_pm.get(_cioTime)) < _cOpenTime ? Long.valueOf(_pm.get(_cioTime)) : _cOpenTime;
                                            if (_mf != null && "Y".equals(_mf)) logger.info(_k + " 关联商户最小开通时间 " + _fcOpenTime);

                                            // 30分钟内该卡交易商户的最晚开通时间
                                            long _lcOpenTime = Long.valueOf(_pm.get(_cxoTime)) > _cOpenTime ? Long.valueOf(_pm.get(_cxoTime)) : _cOpenTime;
                                            if (_mf != null && "Y".equals(_mf)) logger.info(_k + " 关联商户最大开通时间 " + _lcOpenTime);

                                            mqJedis.sadd(dseconds, psk, csk); // 记录该卡30分钟内交易成功的商户

                                            Double _30mAmount = Double.valueOf(_pm.get(_stAmount)) + amount; // 获取缓存中该卡30分钟内交易的总金额

                                            Integer _30mCount = Integer.valueOf(_pm.get(_sCount)) + 1; // 获取缓存中该卡30分钟内的总交易次数

                                            Double _30avg = _30mAmount / _30mCount;

                                            if (_mf != null && "Y".equals(_mf)) logger.info(_k + " 30分钟内  pan:" + pan + " 30mi sum Amount:" + _30mAmount
                                                    + " 30mi count:" + _30mCount + " 30mi agg:" + _30avg);

                                            // 跨商户,卡片半小时内 ：交易超过3笔,交易金额大于70000 小于160000 均值大于4000,关联商户开通时间在三天内
                                            if (mqJedis.scard(psk) >= 2 && _30mCount >= 3 && _30mAmount >= 70000 && _30mAmount <= 160000 && _30avg >= 4000
                                                    && (_lcOpenTime - _fcOpenTime) < (3 * 24 * 60 * 60 * 1000)) {

                                                mqJedis.sadd(dseconds, csk, pan);

                                                if (_mf != null && "Y".equals(_mf)) logger.info(_k + csk + " 添加 pan:" + pan);

                                            } else { // 如果当前交易的卡不满足条件,关联的商户也将不再被记录

                                                if (_mf != null && "Y".equals(_mf)) logger.info(_k + " 排除 pan:" + pan);

                                                Set<String> panSet = mqJedis.smembers(psk);

                                                if (panSet != null && !panSet.isEmpty()) { // 删除卡关联的每一个商户
                                                    for (String _csk : panSet) {
                                                        mqJedis.srem(_csk, pan);
                                                        if (_mf != null && "Y".equals(_mf)) logger.info(_k + " _srem " + _csk + ":" + pan + " panSet Size:"
                                                                + panSet.size());
                                                    }
                                                }
                                            }
                                            // 将当前交易的数据汇总之前的数据后写入缓存
                                            _pm.put(_stAmount, String.format("%.2f", _30mAmount)); // 放入交易总金额
                                            _pm.put(_sCount, _30mCount.toString()); // 放入交易次数
                                            _pm.put(_cioTime, _fcOpenTime + ""); // 放入最小开通时间
                                            _pm.put(_cxoTime, _lcOpenTime + ""); // 放入最大开通时间
                                            mqJedis.hmset(dseconds, pmk, _pm);
                                            if (_mf != null && "Y".equals(_mf)) logger.info(_k + " _pm:" + _pm);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String hisKey = date + "_" + customerNo + "_order_his";
                            // 单日单张卡（借记卡或贷记卡）在合伙人商户交易大于等于3笔（成功或失败）且前三笔笔均小于等于10000元，
                            // 商户开通30日内且不为真实商户，商户为合伙人商户，商户所属地为广西。
                            try {
                                if (agentId != null && "37449855".equals(agentId)) {
                                    String keyPanHH01 = date + "_" + pan + "_" + "HH01";
                                    if (!mqJedis.exists(keyPanHH01)) {
                                        String keyPanHH01Count = date + "_" + pan + "_" + "HH01_COUNT";
                                        String keyPanHH01Amount = date + "_" + pan + "_" + "HH01_AMOUNT";
                                        String keyPanHH01CS = date + "_" + pan + "_" + "HH01_CUSTOMERSET";
                                        long k = mqJedis.incr(dseconds, keyPanHH01Count);
                                        double d = mqJedis.incrByFloat(dseconds, keyPanHH01Amount, amount);
                                        mqJedis.sadd(dseconds, keyPanHH01CS, customerNo);
                                        if (k == 3) {
                                            double a = d / k;
                                            if (a <= 10000) {
                                                Set<String> hh01CustomerSet = mqJedis.smembers(keyPanHH01CS);
                                                for (String _s : hh01CustomerSet) {
                                                    mqJedis.hset(dseconds, date + "_" + _s + "_order_his", "HH01", "Y");
                                                }

                                                mqJedis.set(dseconds, keyPanHH01, "Y");
                                                logger.info("HH01 " + k + " " + d + " " + a + hh01CustomerSet);

                                            }
                                        }
                                    } else {
                                        mqJedis.hset(dseconds, date + "_" + customerNo + "_order_his", "HH01", "Y");
                                        logger.info("HH01 " + " customerNo " + customerNo);
                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // 某一合伙人商户开通30日以内且商户前10日内成功交易小于等于五笔，无交易算做0
                            // 该商户存在一张卡片在10分钟内有过跨合伙人商户交易，
                            // 且该卡当日成功交易笔数大于等于两笔，前两笔成功交易合计大于8000（以该卡片当日第一笔成功交易向后递推10分钟）

                            try {
                                if (agentId != null && "37449855".equals(agentId)) {
                                    // 10分钟内是否加入增量商户标志
                                    String keyPanHH02 = date + "_" + pan + "_" + "HH02";
                                    if ("SUCCESS".equals(status) || "SETTLED".equals(status)) {
                                        // 当天第一笔交易成功交易标志
                                        String keyPanHH02Frist = date + "_" + pan + "_" + "HH02_FIRST";
                                        // 当天第一笔交易成功交易后10分钟内区间标志
                                        String keyPanHH0210Mark = date + "_" + pan + "_" + "HH02_10MINMARK";
                                        // 当天第一笔交易成功交易商户标志
                                        String keyPanHH0210FirstCustomerNo = date + "_" + pan + "_" + "HH02_FIRSTCUSTOMERNO";
                                        // 当天第一笔交易成功交易后10分钟内区间内交易笔数标志
                                        String keyPanHH021TrCount = date + "_" + pan + "_" + "HH02_10MTRANSCOUNT";
                                        String keyPanHH021TrAmount = date + "_" + pan + "_" + "HH02_10MTRANSAMOUNT";
                                        if (!mqJedis.exists(keyPanHH02Frist)) {
                                            mqJedis.set(dseconds, keyPanHH02Frist, "Y");
                                            mqJedis.set(600, keyPanHH0210Mark, "Y");
                                        }

                                        if (mqJedis.exists(keyPanHH0210Mark)) {
                                            // 取上一次商户
                                            String _customerNo = mqJedis.get(keyPanHH0210FirstCustomerNo);
                                            mqJedis.set(dseconds, keyPanHH0210FirstCustomerNo, customerNo);
                                            long k = mqJedis.incr(keyPanHH021TrCount);
                                            double d = mqJedis.incrByFloat(dseconds, keyPanHH021TrAmount, amount);
                                            if (k == 2 && d >= 8000) {

                                                if (_customerNo != null && !_customerNo.equals(customerNo)) {
                                                    mqJedis.hset(dseconds, date + "_" + _customerNo + "_order_his", "HH02", "Y");
                                                    mqJedis.hset(dseconds, date + "_" + customerNo + "_order_his", "HH02", "Y");
                                                    mqJedis.set(dseconds, keyPanHH02, "Y");
                                                    logger.info("HH02 " + _customerNo + " " + customerNo);

                                                }
                                            }
                                            String _f = mqJedis.get(keyPanHH02);
                                            if (_f != null && "Y".equals(_f)) {
                                                mqJedis.hset(dseconds, date + "_" + customerNo + "_order_his", "HH02", "Y");
                                                logger.info("HH02 " + customerNo);
                                            }
                                        }
                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // try {
                            //
                            // Map<String, String> mlau = mqJedis.hgetAll("LAUNDER_ALL_FLAG");
                            // if (_mf != null && "Y".equals(_mf)) logger.info("mlau : " + mlau);
                            // if (mlau != null) {
                            // String la_All = mlau.get("LAUNDER_ALL");
                            // String la_INSERT = mlau.get("LAUNDER_INSERT");
                            // String la_R1_RE = mlau.get("LAUNDER_R1_RE");
                            // String la_R1_FOR = mlau.get("LAUNDER_R1_FOR");
                            // String la_R2_RE = mlau.get("LAUNDER_R2_RE");
                            // String la_R2_FOR = mlau.get("LAUNDER_R2_FOR");
                            // if ("Y".equals(la_All)) {
                            // String trueCustomerFlag = checkTrueCustomerFlag(date, mqJedis, customerNo, overtime);
                            // if (_mf != null && "Y".equals(_mf)) {
                            // logger.info("trueCustomerFlag : " + trueCustomerFlag + " openDays : " + openDays);
                            // }
                            // if ("N".equals(trueCustomerFlag) && la_All != null && "Y".equals(la_All) && openDays <= 30 && bank != null
                            // && mqJedis.sismember("BANK_LAUNDER", bank) && "DEBIT_CARD".equals(cardType)) {
                            // try {
                            // if (amount >= 5000) {
                            // Map<String, String> preXQinfo = mqJedis.hgetAll(date + "_" + pan + "_PANXQ");
                            // if (_mf != null && "Y".equals(_mf)) logger.info("preXQinfo : " + preXQinfo);
                            // if (preXQinfo != null) {
                            // String _customerNo = preXQinfo.get("preCustomerNo");
                            // if (_customerNo != null && !_customerNo.equals(customerNo)) {
                            // Long _openTime = Long.valueOf(preXQinfo.get("preOpenTime"));
                            //
                            // Integer _areadays = Math.abs((int) (_openTime - Long.valueOf(cOpenTimes)) / (1000 * 60 * 60 * 24));
                            // if (_mf != null && "Y".equals(_mf)) {
                            // logger.info("_areadays : " + _areadays);
                            // }
                            // if (_areadays <= 7) {
                            // Set<String> set = new HashSet<String>();
                            // set.add(_customerNo);
                            // set.add(customerNo);
                            // handleLaunder(la_R1_RE, la_R1_FOR, la_INSERT, pan, set, "R1");
                            // logger.info(Constant.AresMsg + pan + "命中规则R1" + customerNo + " 卡号 : " + pan + " 当次交易金额:" + amount);
                            // }
                            // }
                            // }
                            // }
                            // if (amount >= 25000 && amount <= 50000) {
                            // Map<String, String> mPre = new HashMap<String, String>();
                            // mPre.put("preCustomerNo", customerNo);
                            // mPre.put("preOpenTime", cOpenTimes);
                            // mqJedis.hmset(120, date + "_" + pan + "_PANXQ", mPre);
                            // }
                            //
                            // } catch (Exception e) {
                            // e.printStackTrace();
                            // }
                            // /**
                            // * 30天以内,有过余额查询, 有过10块以下的交易,卡类型为借记卡，当天累计交 易超过两万,当天卡交易商户超过两个。
                            // */
                            // // TODO
                            // try {
                            // String day30Key = date + "_XQ_PAN";
                            // if (mqJedis.sismember(day30Key, pan)) {
                            // if (_mf != null && "Y".equals(_mf)) logger.info("day30Key : " + pan);
                            // Double curAmount = amount.doubleValue();
                            // String curPanAmountKey = date + "_" + pan + MQRedisConstans.PAN_AMOUNT_SUM_KEY;
                            // // 当前卡的当日交易金额记录
                            // String curDaySum = mqJedis.get(curPanAmountKey);
                            // Double daySumAmount = curDaySum == null ? amount : ((Double.valueOf(curDaySum)) + curAmount);
                            //
                            // // 当前卡的交易商户,记录进redis,方便计算当前卡在哪些商户有过交易。
                            // String curPanCountKey = date + "_" + pan + MQRedisConstans.PAN_CUSTOMER_KEY;
                            // mqJedis.sadd(dseconds, curPanCountKey, customerNo);
                            // // 当前卡交易的商户数量
                            // Set<String> cNos = mqJedis.smembers(curPanCountKey);
                            // // 当前卡当日累计易金额>20000 , 交易商户超2个
                            // if (daySumAmount > 20000 && cNos.size() > 2) {
                            // handleLaunder(la_R2_RE, la_R2_FOR, la_INSERT, pan, cNos, "R2");
                            // logger.info(Constant.AresMsg + pan + "命中规则R2 " + customerNo + " 交易商户数:" + cNos.size() + "当前卡当日累计交易金额:"
                            // + daySumAmount + "当次交易金额:" + amount);
                            // }
                            // mqJedis.set(dseconds, curPanAmountKey, String.format("%.2f", daySumAmount));
                            //
                            // if (_mf != null && "Y".equals(_mf)) {
                            // logger.info(Constant.AresMsg + pan + " customerNo " + customerNo + " 交易商户数:" + cNos.size() + "当前卡当日累计交易金额:"
                            // + daySumAmount + "当次交易金额:" + amount);
                            // }
                            // }
                            // } catch (Exception e) {
                            // logger.error(e.getMessage());
                            // e.printStackTrace();
                            // }
                            // }
                            //
                            // }
                            //
                            // }
                            // } catch (Exception e) {
                            // logger.error(Constant.AresErr + " launder :" + e.getMessage());
                            // e.printStackTrace();
                            // }

                            logger.info(Constant.AresMsg + ": order t0 use:" + (new Date().getTime() - t0StartTime.getTime()) + " ms");

                        } catch (Exception e) {
                            logger.error(Constant.AresErr + e.getMessage() + "externalId:" + key + " status:" + status + " pan:" + pan + " amount:" + amount
                                    + " customerNo:" + customerNo);
                            e.printStackTrace();
                        }

                    }

                }

                // 释放资源
                map.clear();
                map = null;

            }
        } catch (Exception e) {
            logger.error(Constant.AresErr + e.getMessage());
            e.printStackTrace();
        } finally {
            // 释放redis链接
            mqJedis.close();
        }
    }

    private void setBlackListCDL(String bCode, String type) {

        RedisUtil jedis = new RedisUtil(RedisConstants.REDIS_CDL);
        try {
            String bKey = null;
            if ("pan".equals(type)) {
                bKey = "CDL_BLACKPAN_SET";
            }
            if ("customer".equals(type)) {
                bKey = "CDL_BLACKCUSTOMER_SET";
            }
            jedis.sadd(bKey, bCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();

        }

    }

    private String checkTrueCustomerFlag(String date, RedisUtil mqJedis, String customerNo, Integer overtime) {
        try {
            String key = date + "_" + customerNo + "_CUSTOMERTRUEFLAG";
            String _value = mqJedis.get(key);
            if (_value != null) {
                return _value;
            } else {
                String _v = this.merchantVerificationFacede.getMerchantVerification(customerNo);
                mqJedis.set(overtime, key, _v);
                return _v;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N";
    }

    // 前45天没有成功交易
    private String get45Notsu(RedisUtil mqJedis, String t0hisKey) {
        String _45Flag = mqJedis.hget(t0hisKey, "45TRANSFLAG");
        if (_45Flag != null && "Y".equals(_45Flag)) {
            return "N";
        }
        return "Y";
    }

    // _30AVG
    private String getFront30Avg(RedisUtil mqJedis, String t0hisKey) {
        String _30AVG = mqJedis.hget(t0hisKey, "30AVG");
        if (_30AVG == null || "".equals(_30AVG)) return null;
        return _30AVG;
    }

    private String getFront10Avg(RedisUtil mqJedis, String t0hisKey) {
        String _c10 = mqJedis.hget(t0hisKey, "totalTimes_order_his10");
        String _s10 = mqJedis.hget(t0hisKey, "totalAmount_order_his10");
        if (_s10 == null || _c10 == null || "".equals(_s10) || "".equals(_c10)) return null;
        try {
            Double int1 = Double.valueOf(_s10);
            Double int2 = Double.valueOf(_c10);
            return String.valueOf(int1 * 1.0 / int2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setTodayHisMin8K(int dseconds, String customerNo, String key) {

        RedisUtil mqJedis = new RedisUtil(Constant.MQJedis);
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String date = sdf.format(new Date());
            String hisKey = date + "_" + customerNo + "_order_his";
            String m8Ksum = mqJedis.hget(hisKey, "m8Ksum");
            // 第一次近来 m8Ksum没有值
            if (m8Ksum == null || "".equals(m8Ksum)) {
                // 8k前有交易
                String str = mqJedis.get(key);
                if (str != null && !"".equals(str)) {
                    mqJedis.hset(dseconds, hisKey, "m8Ksum", str);
                } else {
                    mqJedis.hset(dseconds, hisKey, "m8Ksum", "0");
                }
            }

        } catch (Exception e) {
            logger.error(Constant.AresErr + e.getMessage());
            e.printStackTrace();
        } finally {
            // 释放redis链接
            mqJedis.close();
        }

    }

    /**
     * 手工计算金额(因多线程需要,加锁)
     * @Description 一句话描述方法用法
     * @param amount
     * @param key
     * @param type
     * @see 需要参考的类或方法
     */
    private synchronized void calculate(int dseconds, Double amount, String key, Integer type) {
        RedisUtil mqJedis = new RedisUtil(Constant.MQJedis);
        try {
            if (type == 1) {

                String str = mqJedis.get(key);
                if (str != null && !"".equals(str)) {
                    // logger.info(Constant.AresMsg + " calculate other" + key);
                    Double hisAmount = Double.valueOf(str);
                    // logger.info(Constant.AresMsg + " calculate before " + key + "_" + hisAmount);
                    hisAmount += amount;
                    // logger.info(Constant.AresMsg + " calculate after " + key + "_" + hisAmount);
                    mqJedis.set(dseconds, key, String.format("%.2f", hisAmount));
                } else {
                    // logger.info(Constant.AresMsg + " calculate first " + key);
                    mqJedis.set(dseconds, key, String.format("%.2f", amount));
                }
            } else {
                String str = mqJedis.get(key);
                if (str != null && !"".equals(str)) {
                    Double hisAmount = Double.valueOf(str);
                    hisAmount -= amount;
                    mqJedis.set(dseconds, key, String.format("%.2f", hisAmount));
                }
            }

        } catch (Exception e) {
            logger.error(Constant.AresErr + e.getMessage());
            e.printStackTrace();
        } finally {
            // 释放redis链接
            mqJedis.close();
        }
    }

    /**
     * 处理洗钱措施
     * @param set
     * @Description 一句话描述方法用法
     * @see 需要参考的类或方法
     */
    private void handleLaunder(String rejectType, String forzenType, String insertType, String pan, Set<String> set, String ruleType) {
        for (String customerNo : set) {
            if ("Y".equals(rejectType)) {
                setBlackListCDL(pan, "pan");
                setBlackListCDL(customerNo, "customer");
            }

            if ("Y".equals(forzenType)) {
                this.secondCashServiceFacade.freezeAccount(customerNo);
            }
            if ("Y".equals(insertType)) {
                Map<String, String> _md = new HashMap<String, String>();
                _md.put("PAN", pan);
                _md.put("CUSTOMER_NO", customerNo);
                _md.put("TYPE", ruleType);
                this.secondCashServiceFacade.addFreezedInfo(_md);
            }
        }

    }

}
