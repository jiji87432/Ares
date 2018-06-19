/*
 * 文 件 名:  PosRequestDataServiceImpl.java
 * 版    权:  支付有限公司. Copyright 2011-2015,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  zikun.liu
 * 修改时间:  2015-11-10
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.pay.risk.mq.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.risk.Constant;
import com.pay.risk.ares.secnodcash.SecondCashConstant;
import com.pay.risk.mq.service.PosRequestDataService;
import com.pay.risk.util.DateUtils;
import com.pay.risk.util.PosRequestFilterUtil;
import com.pay.risk.util.StringUtil;
import com.riskutil.redis.RedisUtil;

/**
 * POS请求MQ数据报文解析、操作服务类
 * <p>
 * 针对POS请求MQ数据报文处理。
 * @author zikun.liu
 * @version [V1.0, 2015-11-10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service("posRequestDataService")
public class PosRequestDataServiceImpl implements PosRequestDataService {

    protected static final Logger logger = LogManager.getLogger(PosRequestDataServiceImpl.class);

    /** POS请求SERVICE层服务接口 */

    @Override
    public void handlePosRequestBatch(List<JSONObject> jsonList) {
        RedisUtil mqJedis = new RedisUtil(Constant.MQJedis);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String date = sdf.format(new Date());
            for (JSONObject json : jsonList) {
                // 判断json中是否有以下值
                String _mf = mqJedis.get("MQ_LOG_FLAG");
                if (_mf != null && "Y".equals(_mf)) logger.info("MQ REQUEST : " + json);
                if (json.get("transType") == null || json.get("customerNo") == null || json.get("createTime") == null || json.get("pan") == null
                        || json.get("amount") == null || json.get("responseCode") == null) {
                    // logger.info(Constant.AresMsg + " posRequest null transType:" + json.get("transType"));
                    // logger.info(Constant.AresMsg + " posRequest null customerNo:" + json.get("customerNo"));
                    // logger.info(Constant.AresMsg + " posRequest null createTime:" + json.get("createTime"));
                    // logger.info(Constant.AresMsg + " posRequest null pan:" + json.get("pan"));
                    // logger.info(Constant.AresMsg + " posRequest null amount:" + json.get("amount"));
                    // logger.info(Constant.AresMsg + " posRequest null responseCode:" + json.get("responseCode"));
                    // logger.info(Constant.AresMsg + " posRequest null exceptionCode:" + json.get("exceptionCode"));
                    continue;
                }

                if ("null".equals(String.valueOf(json.get("transType"))) || "null".equals(String.valueOf(json.get("customerNo")))
                        || "null".equals(String.valueOf(json.get("createTime"))) || "null".equals(String.valueOf(json.get("pan")))
                        || "null".equals(String.valueOf(json.get("amount"))) || "null".equals(String.valueOf(json.get("responseCode")))) {
                    // logger.info(Constant.AresMsg + " posRequest null transType:" + json.get("transType"));
                    // logger.info(Constant.AresMsg + " posRequest null customerNo:" + json.get("customerNo"));
                    // logger.info(Constant.AresMsg + " posRequest null createTime:" + json.get("createTime"));
                    // logger.info(Constant.AresMsg + " posRequest null pan:" + json.get("pan"));
                    // logger.info(Constant.AresMsg + " posRequest null amount:" + json.get("amount"));
                    // logger.info(Constant.AresMsg + " posRequest null responseCode:" + json.get("responseCode"));
                    // logger.info(Constant.AresMsg + " posRequest null exceptionCode:" + json.get("exceptionCode"));
                    continue;
                }

                if (PosRequestFilterUtil.posRequestDataFilter(json)) {
                    continue;
                }

                Calendar c1 = new GregorianCalendar();
                c1.set(Calendar.HOUR_OF_DAY, 0);
                c1.set(Calendar.MINUTE, 0);
                c1.set(Calendar.SECOND, 0);
                // 获得当日开始时间
                Long dayStartTime = c1.getTime().getTime();
                Long createTime = Long.valueOf(String.valueOf(json.get("createTime")));

                // 交易类型
                String transType = String.valueOf(json.get("transType"));
                String type = "PURCHASE;PURCHASE_VOID;BALANCE_INQUIRY;PURCHASE_REVERSAL;PURCHASE_VOID_REVERSAL;PURCHASE_REFUND;PRE_AUTH;PRE_AUTH_VOID;PRE_AUTH_REVERSAL;PRE_AUTH_VOID_REVERSAL;PRE_AUTH_COMP;PRE_AUTH_COMP_VOID;PRE_AUTH_COMP_REVERSAL;PRE_AUTH_COMP_VOID_REVERSAL;SPECIFY_QUANCUN;SPECIFY_QUANCUN_REVERSAL;NOT_SPECIFY_QUANCUN;NOT_SPECIFY_QUANCUN_REVERSAL;CASH_RECHARGE_QUNCUN;CASH_RECHARGE_QUNCUN_REVERSAL;OFFLINE_PURCHASE;SM_GET_TDCODE;SM_PURCHASE;";
                // 判断当前请求是否以PURCHASE开头或以VOID结尾
                if ((type.contains(transType)) && (createTime >= dayStartTime)) {

                    Date startTime = new Date();
                    Integer seconds = (48 - (Integer.valueOf(new SimpleDateFormat("HH").format(startTime)))) * 3600;
                    Integer dseconds = (26 - (Integer.valueOf(new SimpleDateFormat("HH").format(startTime)))) * 3600;
                    String key = new SimpleDateFormat("yyyyMMdd").format(startTime) + "_" + json.get("customerNo") + "_billcheck";
                    mqJedis.hset(seconds, key, "lastopertime", String.valueOf(createTime));
                    // 商户编号
                    String customerNo = String.valueOf(json.get("customerNo"));
                    // 卡号
                    String pan = String.valueOf(json.get("pan"));
                    // 交易金额
                    String amount = String.valueOf(json.get("amount"));
                    // 返回码
                    String responseCode = String.valueOf(json.get("responseCode"));
                    // 特殊风险返回码一
                    String hResponseCode = "04;07;34;35;36;37;41;43;42;53;56;67";
                    // 异常码
                    String exceptionCode = "exceptionCode";
                    // 历史信息综合KEY
                    String cHisKey = date + "_" + customerNo + SecondCashConstant.SUFFIX_ORDER_HIS;

                    String externalId = json.get("externalId") == null ? null : String.valueOf(json.get("externalId"));
                    // 先删除在该集合中的该商户,待后续计算用
                    mqJedis.srem(StringUtil.getT0Key(date, "CATEGORY003144", "SP1"), customerNo);

                    try {
                        if (json != null && json.get("mcc") != null) {
                            String mcc = String.valueOf(json.get("mcc"));
                            if ("2003".equals(mcc) || "2004".equals(mcc) || "2005".equals(mcc) || "2006".equals(mcc)) {
                                mqJedis.sadd(dseconds, date + "_zyt_customerset", customerNo);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (responseCode != null && !"".equals(responseCode)) {
                            Map<String, String> m2 = new HashMap<String, String>();
                            if ("62".equals(responseCode)) {
                                m2.put("R62P", "Y");
                            }
                            if (!"00".equals(responseCode)) {
                                // 异常返回码去重KEY数量
                                String rSCountKey = date + "_" + customerNo + "_RSCOUNT";
                                mqJedis.sadd(dseconds, rSCountKey, responseCode);
                                Long rSCount = mqJedis.scard(rSCountKey);
                                if (rSCount != null && rSCount > 0) {
                                    m2.put("RSCOUNT", String.valueOf(rSCount));
                                }
                            }
                            if (m2.size() > 0) mqJedis.hmset(dseconds, cHisKey, m2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!"null".equals(String.valueOf(json.get("exceptionCode")))) {
                        exceptionCode = String.valueOf(json.get("exceptionCode"));
                    }
                    // 特殊异常码
                    String hExceptionCode = "000040;000029;000034;000046;000009;000019;000113;000030;000053;000033;000054";
                    // 特殊风险返回码二
                    String tResponseCode = "04;13;14;33;34;36;37;41;43;51;53;54;55;56;57;61;62;63;65;67;75";
                    // 手动拼写唯一商户请求
                    String posrequest = customerNo + createTime;

                    // logger.info(Constant.AresMsg + " start:" + posrequest);
                    String e46Flag = "N";
                    try {
                        // 判断当前商户请求在当日请求集合中是否存在
                        if (!mqJedis.sismember(date + SecondCashConstant.posrequestList, posrequest)) {
                            // 添加进当日请求集合中
                            mqJedis.sadd(dseconds, date + SecondCashConstant.posrequestList, posrequest);
                            // 判断交易类型是否为PURCHASE
                            if ("PURCHASE".equals(transType)) {
                                // 该商户当日请求数量加一
                                mqJedis.incr(dseconds, date + "_" + customerNo + SecondCashConstant.purchaseCount);
                            }

                            // 判断返回码在特殊风险返回码一中是否存在
                            if (hResponseCode.contains(responseCode)) {
                                // 该商户当日特殊风险返回码数量加一
                                mqJedis.incr(dseconds, date + "_" + customerNo + SecondCashConstant.hlRcodeCount);
                                // 将卡号存入该商户当日特殊风险返回码银行卡集合中
                                mqJedis.sadd(dseconds, date + "_" + customerNo + SecondCashConstant.hlRcodeCardcount, pan);
                            }
                            // 判断返回码是否是96的同时异常码在特殊异常码中或返回码在特殊返回码二中
                            if (("96".equals(responseCode) && hExceptionCode.contains(exceptionCode)) || tResponseCode.contains(responseCode)) {
                                mqJedis.incr(dseconds, date + "_" + customerNo + SecondCashConstant.llRcodeCount);
                                // 当日返回码金额加本次金额
                                String llRcodeAmountStr = date + "_" + customerNo + SecondCashConstant.llRcodeAmount;
                                calculate(dseconds, Double.valueOf(amount), llRcodeAmountStr, 1);
                            }
                            // 判断返回码时00的同事交易类型是否以VOID结尾
                            if ("00".equals(responseCode) && transType.endsWith("VOID")) {
                                // 该商户当日成功交易笔数加一
                                mqJedis.incr(dseconds, date + "_" + customerNo + SecondCashConstant.voidCount);
                                // 该商户当日成功金额加本次金额
                                String voidAmountStr = date + "_" + customerNo + SecondCashConstant.voidAmount;
                                calculate(dseconds, Double.valueOf(amount), voidAmountStr, 1);
                            }

                            // 商户消费撤销冲正、预授权完成撤销冲正成功的交易
                            if ("00".equals(responseCode) && ("PURCHASE_VOID_REVERSAL".equals(transType) || "PRE_AUTH_COMP_VOID_REVERSAL".equals(transType))) {
                                // 该商户当日消费撤销冲正、预授权完成撤销冲正成功交易笔数加一
                                mqJedis.incr(dseconds, date + "_" + customerNo + Constant.reversalCount);
                                // 该商户当日消费撤销冲正、预授权完成撤销冲正成功金额加本次金额
                                String reversalAmountStr = date + "_" + customerNo + Constant.reversalAmount;
                                calculate(dseconds, Double.valueOf(amount), reversalAmountStr, 1);
                            }

                            // 与授权

                            // 商户查询余额
                            if ("BALANCE_INQUIRY".equals(transType)) {
                                // 商户查询余额次数加一
                                mqJedis.incr(dseconds, date + "_" + customerNo + Constant.balanceInquiryCount);
                                // 记录商户查询余额的卡片
                                if (pan != null && !"".equals(pan)) {
                                    mqJedis.sadd(dseconds, date + "_" + customerNo + Constant.balanceInquiryPan, pan);
                                }

                            }

                            // 商户消费退货
                            if ("PURCHASE_REFUND".equals(transType)) {
                                // 商户消费退货次数加一
                                mqJedis.incr(dseconds, date + "_" + customerNo + Constant.purchaseRefundCount);
                            }

                            if (externalId != null && "PRE_AUTH".equals(transType) && "00".equals(responseCode)) {
                                mqJedis.set(600, externalId + "_PRE_AUTH_RISKINFO", customerNo);
                            }

                            if ("96".equals(responseCode) && "000046".equals(exceptionCode)) {
                                // mqJedis.h(cHisKey, fields)(dseconds, date + "_" + customerNo+Constant.E000046,"Y");
                                mqJedis.hset(dseconds, cHisKey, Constant.E000046, "Y");
                                e46Flag = "Y";
                            }

                            /*
                             * 除00外的其他所有返回码,且 96反回码中只取exceptionCode = 000129 的数据 定义为异常交易（涉案交易<新>）
                             * 计算涉案交易的笔数和金额 放入MQ 缓存
                             */
                            try {
                                if ((!"00".equals(responseCode) && !"96".equals(responseCode)) || ("96".equals(responseCode) && "000129".equals(exceptionCode))) {
                                    // 涉案笔数 新
                                    String sabsx = date + "_" + customerNo + Constant.SABSX;
                                    // 涉案金额 新
                                    String sajex = date + "_" + customerNo + Constant.SAJEX;

                                    mqJedis.incr(dseconds, sabsx);
                                    mqJedis.incrByFloat(dseconds, sajex, amount == null ? 0.0 : Double.valueOf(amount));

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        logger.info(Constant.AresMsg + " posRequest success use " + (new Date().getTime() - startTime.getTime()) + " ms, customerNo:" + customerNo
                                + " amount:" + amount + " pan:" + pan + " responseCode:" + responseCode + " exceptionCode:" + exceptionCode + " transType:"
                                + transType);

                        /* t0数据汇总 */
                        Date t0StartTime = new Date();
                        // 超时时间
                        Integer overtime = 3600 * 23;
                        /*
                         * 乐付宝：同一商户发起的特殊风险应答码交易≥3笔或包括的卡≥2张
                         */
                        // 商户当前风险应答码数量
                        Double hriskcount = 0.0;
                        try {

                            String hrc = mqJedis.get(date + "_" + customerNo + SecondCashConstant.hlRcodeCount);
                            if (hrc != null && !"".equals(hrc)) {
                                hriskcount = Double.valueOf(hrc);
                            }
                            // 商户当前风险应答码包括的卡数
                            Long panCount = mqJedis.scard(date + "_" + customerNo + SecondCashConstant.hlRcodeCardcount);
                            if (hriskcount >= 3 || panCount >= 2) {
                                String t0key = StringUtil.getT0Key(date, "CATEGORY003164", "SP1");
                                mqJedis.sadd(overtime, t0key, customerNo);

                            }
                        } catch (Exception e) {
                            logger.info(Constant.AresErr + " CATEGORY003164 err " + e.getMessage());
                            e.printStackTrace();
                        }

                        try {
                            /*
                             * 日内同一商户的风险应答码消费交易或异常码占比≥50%
                             */
                            // 商户当前总请求量
                            Double sumRequest = 0.0;
                            String sr = mqJedis.get(date + "_" + customerNo + SecondCashConstant.purchaseCount);
                            if (sr != null && !"".equals(sr)) {
                                sumRequest = Double.valueOf(sr);
                            }

                            // 商户当前异常码数量
                            Double eriskcount = 0.0;
                            String ec = mqJedis.get(date + "_" + customerNo + SecondCashConstant.llRcodeCount);
                            if (ec != null && !"".equals(ec)) {
                                eriskcount = Double.valueOf(ec);
                            }
                            if (sumRequest > 0) {
                                if ((eriskcount / sumRequest >= 0.5)) {
                                    String t0key = StringUtil.getT0Key(date, "CATEGORY003144", "SP1");
                                    mqJedis.sadd(overtime, t0key, customerNo);

                                }
                            }
                            String llRcodeAmountStr = date + "_" + customerNo + SecondCashConstant.llRcodeAmount;
                            String llRcodeAmount = mqJedis.get(llRcodeAmountStr);
                            Double _dll = Double.parseDouble(llRcodeAmount == null ? "0.0" : llRcodeAmount);
                            if (_dll >= 60000.0) {
                                String t0key = StringUtil.getT0Key(date, "CATEGORY003002", "SP2");
                                mqJedis.sadd(overtime, t0key, customerNo);
                                if (eriskcount >= 3.0) {
                                    String _t0key = StringUtil.getT0Key(date, "CATEGORY003144", "SP3");
                                    mqJedis.sadd(overtime, _t0key, customerNo);
                                }
                            }
                        } catch (Exception e) {
                            logger.info(Constant.AresErr + " CATEGORY003144 err " + e.getMessage());
                            e.printStackTrace();
                        }

                        /*
                         * 同一商户消费撤销冲正、预授权完成撤销冲正成功的交易，总交易金额大于等于5000元
                         * if ("PURCHASE_VOID_REVERSAL".equals(transType) || "PRE_AUTH_COMP_VOID_REVERSAL".equals(transType)) {
                         * Double sumAmount = 0.0;
                         * String sa = mqJedis.get(date + "_" + customerNo + SecondCashConstant.voidAmount);
                         * if (sa != null && !"".equals(sa)) {
                         * sumAmount = Double.valueOf(sa);
                         * }
                         * if (sumAmount >= 5000) {
                         * String t0key = StringUtil.getT0Key(date, "CATEGORY003001", "SP1");
                         * mqJedis.sadd(overtime, t0key, customerNo);
                         * }
                         * }
                         */

                        /*
                         * 乐付宝：1日内商户或卡片10分钟内撤销操作≥2次 单笔金额大于等于1000
                         */
                        try {
                            if ("00".equals(responseCode) && transType.endsWith("VOID")) {

                                // 单笔金额大于等于1000
                                if (Double.valueOf(amount) >= 1000) {
                                    // 商户10分钟内撤销
                                    String cr = StringUtil.getT0Key(date, customerNo, "repal");
                                    String customer_repal = mqJedis.get(cr);
                                    if (customer_repal != null && !"".equals(customer_repal)) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY003070", "SP1");
                                        mqJedis.sadd(overtime, t0key, customerNo);
                                    }
                                    mqJedis.set(600, cr, "Y");

                                    // 卡片10分钟内撤销
                                    String pr = StringUtil.getT0Key(date, pan, "repal");
                                    String pan_repal = mqJedis.get(pr);
                                    if (pan_repal != null && !"".equals(pan_repal)) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY003070", "SP1");
                                        mqJedis.sadd(overtime, t0key, pan_repal, customerNo);
                                    }
                                    mqJedis.set(600, pr, customerNo);
                                }

                            }

                        } catch (Exception e) {
                            logger.info(Constant.AresErr + " CATEGORY003070 err " + e.getMessage());
                            e.printStackTrace();
                        }

                        /*
                         * 乐付宝：同一商户消费撤销冲正、预授权完成撤销冲正成功的交易，单笔大于等于1000，笔数大于等于2
                         * 乐付宝：同一商户消费撤销冲正、预授权完成撤销冲正成功的交易，总交易金额大于等于5000元
                         */

                        try {
                            if ("00".equals(responseCode) && ("PURCHASE_VOID_REVERSAL".equals(transType) || "PRE_AUTH_COMP_VOID_REVERSAL".equals(transType))) {
                                Long count = (long) 0;

                                if (Double.valueOf(amount) >= 1000) {
                                    String overkey = StringUtil.getT0Key(date, customerNo, "OVER1000");
                                    // 单笔大于等于1000的笔数
                                    count = mqJedis.incr(overtime, overkey);
                                }
                                // 总交易金额
                                Double reversalAmount = 0.0;
                                String ra = mqJedis.get(date + "_" + customerNo + Constant.reversalAmount);
                                if (ra != null && !"".equals(ra)) {
                                    reversalAmount = Double.valueOf(ra);
                                }
                                if (count >= 2 || reversalAmount >= 5000) {
                                    String t0key = StringUtil.getT0Key(date, "CATEGORY003001", "SP1");
                                    mqJedis.sadd(overtime, t0key, customerNo);
                                }
                            }
                        } catch (Exception e) {
                            logger.info(Constant.AresErr + " CATEGORY003001 err " + e.getMessage());
                            e.printStackTrace();
                        }

                        /*
                         * 乐付宝：1日内做过10次以上余额查询操作或余额查询卡片涉及≥5张
                         */
                        try {
                            Integer balanceInquiryCount = 0;
                            String bc = mqJedis.get(date + "_" + customerNo + Constant.balanceInquiryCount);
                            if (bc != null && !"".equals(bc)) {
                                balanceInquiryCount = Integer.valueOf(bc);
                            }
                            Long balanceInquiryPan = mqJedis.scard(date + "_" + customerNo + Constant.balanceInquiryPan);
                            if (balanceInquiryCount >= 10 || balanceInquiryPan >= 5) {
                                String t0key = StringUtil.getT0Key(date, "CATEGORY003073", "SP1");
                                mqJedis.sadd(overtime, t0key, customerNo);
                            }
                        } catch (Exception e) {
                            logger.info(Constant.AresErr + " CATEGORY003073 err " + e.getMessage());
                            e.printStackTrace();
                        }

                        /*
                         * 乐付宝：1日内商户交易在POS请求查询中消费退货次数≥ 3次
                         */

                        try {
                            Integer purchaseRefundCount = 0;
                            String prc = mqJedis.get(date + "_" + customerNo + Constant.purchaseRefundCount);
                            if (prc != null && !"".equals(prc)) {
                                purchaseRefundCount = Integer.valueOf(prc);
                            }
                            if (purchaseRefundCount >= 3) {
                                String t0key = StringUtil.getT0Key(date, "CATEGORY003071", "SP1");
                                mqJedis.sadd(overtime, t0key, customerNo);
                            }
                        } catch (Exception e) {
                            logger.info(Constant.AresErr + " CATEGORY003071 err " + e.getMessage());
                            e.printStackTrace();
                        }
                        /*
                         * 乐付宝：1日内特殊卡bin含消费撤销的交易商户
                         */
                        try {
                            if ("PURCHASE_VOID".equals(transType) && "96".equals(responseCode)) {
                                if (pan != null && !"".equals(pan) && (pan.startsWith("622335") || pan.startsWith("623001"))) {
                                    String t0key = StringUtil.getT0Key(date, "CATEGORY003074", "SP1");
                                    mqJedis.sadd(overtime, t0key, customerNo);
                                }
                            }
                        } catch (Exception e) {
                            logger.info(Constant.AresErr + " CATEGORY003074 err " + e.getMessage());
                            e.printStackTrace();
                        }

                        try {
                            if (externalId != null && "PRE_AUTH_COMP".equals(transType) && "00".equals(responseCode)) {
                                String cNo = mqJedis.get(externalId + "_PRE_AUTH_RISKINFO");
                                // 10分钟内与授权完成 KEY增长1 >=2则记录
                                if (cNo != null && !"".equals(cNo)) {
                                    mqJedis.incr(dseconds, date + "_" + customerNo + "_PRERISKCOUNT");
                                    String _PRERISKCOUNT = mqJedis.get(date + "_" + customerNo + "_PRERISKCOUNT");
                                    if (_PRERISKCOUNT != null && Integer.parseInt(_PRERISKCOUNT) >= 2) {
                                        String t0key = StringUtil.getT0Key(date, "CATEGORY003072", "SP1");
                                        mqJedis.sadd(overtime, t0key, customerNo);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error(Constant.AresErr + " CATEGORY003072:" + e.getMessage());
                            e.printStackTrace();
                        }

                        /**
                         * 商户开通30天以内且交易30日内（含当天）成功交易小于等于30笔的商户，
                         * 某一卡片在该商户成功交易大于等于3笔且当日返回码有51的失败交易，
                         * 该卡第一笔成功交易金额大于等于10000，该笔交易为该卡在该商户最后一笔成功交易金额的4.5倍以上。
                         * 定义 后缀 ：key 20161124_kpycjy 卡片异常交易 （用于判断单卡单商户 当天有过51返回码）
                         */

                        try {
                            String _ycjyKey = "_20161124_kpycjy";
                            String _pcmk = date + "_map_" + customerNo + "_" + pan + _ycjyKey; // 汇总的map
                            String _csk = date + "_set_" + customerNo + _ycjyKey; // customerNo key (set)
                            String _51code = "51code"; // 判断51返回码的key
                            String _famount = "famount"; // 第一笔成功交易的key
                            String _count = "count"; // 成功交易笔数的key
                            String _firstT = "firstT"; // 是不是第一笔成功交易的key

                            if (responseCode != null && transType != null && "PURCHASE".equals(transType) && "00".equals(responseCode)) {

                                Double _amount = Double.valueOf(amount); // 将string amount 转成 double _amount

                                Map<String, String> _pcmap = mqJedis.hgetAll(_pcmk); // 获取map

                                if (_mf != null && "Y".equals(_mf)) logger.info(_ycjyKey + " map: " + _pcmap);

                                if (_pcmap == null || _pcmap.isEmpty() || _pcmap.get(_famount) == null || "".equals(_pcmap.get(_famount).trim())
                                        || "null".equals(_pcmap.get(_famount).trim())) {
                                    // 该笔交易为该卡该商户的第一笔成功交易。
                                    if (_mf != null && "Y".equals(_mf)) logger.info(_ycjyKey + " 第一笔成功的交易: " + amount + "_" + pan + "_" + customerNo);

                                    if (_amount >= 10000
                                            && (_pcmap == null || _pcmap.get(_firstT) == null || "".equals(_pcmap.get(_firstT).trim())
                                                    || _pcmap.get(_famount) == null || "null".equals(_pcmap.get(_famount).trim()))) { // 判断成功的第一笔大于一万。

                                        if (_mf != null && "Y".equals(_mf)) logger.info(_ycjyKey + " 第一笔成功的交易并且该笔交易大于等于10000: " + amount + "_" + pan + "_"
                                                + customerNo);

                                        _pcmap = new HashMap<String, String>();
                                        _pcmap.put(_famount, amount); // 第一笔成功的交易并且该笔交易大于等于10000放入map
                                        _pcmap.put(_count, "1");

                                    } else {
                                        _pcmap.put(_firstT, "N"); // 第一笔成功的交易但是该笔交易小于10000

                                        if (_mf != null && "Y".equals(_mf)) logger.info(_ycjyKey + " 第一笔成功的交易但是该笔交易小于10000: " + amount + "_" + pan + "_"
                                                + customerNo);
                                    }

                                    mqJedis.hmset(dseconds, _pcmk, _pcmap);
                                } else { // 已经记录过了第一笔大于10000的交易
                                    Integer count = Integer.valueOf(_pcmap.get(_count)) + 1; // 交易次数+1

                                    if (_mf != null && "Y".equals(_mf)) logger.info(_ycjyKey + " 已经有过第一笔大于10000的成功交易: " + amount + "_" + pan + "_" + customerNo
                                            + " count:" + count + " 51code:" + _pcmap.get(_51code));

                                    if (count >= 3) {
                                        Double famount = Double.valueOf(_pcmap.get(_famount));
                                        if ((famount / _amount) >= 4.5 && _pcmap.get(_51code) != null && "Y".equals(_pcmap.get(_51code))) { // 第一笔交易是当次交易的4.5倍以上，并且该卡该商户有过51返回码
                                            mqJedis.sadd(dseconds, _csk, pan);
                                        } else {// 不满足条件 customerNo key 中 移除该卡 pan
                                            mqJedis.srem(_csk, pan);
                                        }
                                    }
                                    mqJedis.hset(dseconds, _pcmk, _count, count.toString());
                                }
                            } else if (responseCode != null && !"".equals(responseCode.trim()) && "PURCHASE".equals(transType) && responseCode.equals("51")) {
                                // 有51 返回码
                                mqJedis.hset(dseconds, _pcmk, _51code, "Y"); // 将该卡放入对应的map<_51code,pan> 中记录该商户在该卡有过51返回码

                                if (_mf != null && "Y".equals(_mf)) logger.info(_ycjyKey + " : " + amount + "_" + pan + "_" + customerNo + " responseCode:"
                                        + responseCode);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            if (("000160".equals(exceptionCode) || "62".equals(responseCode)) && Double.valueOf(amount) >= 2000) {
                                String keyss = DateUtils.getTodayStr() + "_YSQZTH_CUSTOMER_NO";
                                logger.info("exceptionCode_YSQZTH_CUSTOMER_NO:" + exceptionCode + ",amount:" + amount + ",key:" + key + ",customerNo:" + customerNo);
                                mqJedis.sadd(dseconds, keyss, customerNo);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        /**
                         * 当日卡BIN为622280、622168的卡片进行3笔900~1000元交易后，
                         * 禁止该卡片2日内再次在支付商户进行交易
                         * （由于HCE相关调查文件反馈时间普遍较长，所以禁止交易时间延长至2日）
                         */
                        RedisUtil cdlJedis = null;
                        try {
                            if ("Y".equals(mqJedis.get("20161212_hce_rule_mq"))) {
                                String hce = "_hce_cheat_mq"; // 定义key
                                if ("PURCHASE".equals(transType) && "00".equals(responseCode)) {
                                    Double _amount = Double.valueOf(amount);
                                    if ((pan.startsWith("622280") || pan.startsWith("622168")) && _amount >= 900 && _amount < 1000) {
                                        if (_mf != null && "Y".equals(_mf)) logger.info(hce + " : " + pan + " 该卡计数一次 amount: " + amount);
                                        String _k = date + "_" + pan + hce;
                                        Long incr = mqJedis.incr(dseconds, _k);// 计数器计数
                                        if (incr >= 3) {// 满足3笔 拦截交易
                                            String _cdlkey = pan + "_hce_cheat_cdl";
                                            cdlJedis = new RedisUtil(Constant.CDLJedis);
                                            cdlJedis.set(48 * 60 * 60, _cdlkey, "Y");
                                            if (_mf != null && "Y".equals(_mf)) logger.info(_cdlkey + " : " + pan + " 命中 hce规则");
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            /* 释放连接 */
                            if (cdlJedis != null) {
                                cdlJedis.close();
                            }
                        }
                        /* */
                        try {
                            if ("Y".equals(e46Flag)) {
                                String t0key = StringUtil.getT0Key(date, "CATEGORY006413", "SP1");
                                mqJedis.sadd(overtime, t0key, customerNo);
                            }
                        } catch (Exception e) {
                            logger.error(Constant.AresErr + " CATEGORY003072:" + e.getMessage());
                            e.printStackTrace();
                        }

                        logger.info(Constant.AresMsg + ": posRequest t0 use:" + (new Date().getTime() - t0StartTime.getTime()) + " ms");

                    } catch (Exception e) {
                        logger.error(Constant.AresErr + " msg:" + e.getMessage() + "customerNo:" + customerNo + " amount:" + amount + " pan:" + pan
                                + " responseCode:" + responseCode + " exceptionCode:" + exceptionCode + " transType:" + transType);
                        e.printStackTrace();
                    }

                }

            }
        } catch (Exception e) {
            logger.error(Constant.AresErr + " msg:" + e.getMessage());
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
    private synchronized void calculate(Integer seconds, Double amount, String key, Integer type) {
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
                    mqJedis.set(seconds, key, String.format("%.2f", hisAmount));

                } else {
                    // logger.info(Constant.AresMsg + " calculate first " + key);
                    mqJedis.set(seconds, key, String.format("%.2f", amount));

                }
            } else {
                String str = mqJedis.get(key);
                if (str != null && !"".equals(str)) {
                    Double hisAmount = Double.valueOf(str);
                    hisAmount -= amount;
                    mqJedis.set(seconds, key, String.format("%.2f", hisAmount));
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

}
