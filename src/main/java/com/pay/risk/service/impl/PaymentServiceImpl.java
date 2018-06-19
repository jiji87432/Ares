package com.pay.risk.service.impl;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.common.util.PropertyUtil;
import com.pay.risk.Constant;
import com.pay.risk.constans.ONLineUtil;
import com.pay.risk.constans.OnLinePayConstants;
import com.pay.risk.service.OlPaymentService;
import com.pay.risk.service.OrderService;
import com.pay.risk.service.PaymentService;
import com.pay.risk.thread.OlPaymentHisCacheThread;
import com.pay.risk.thread.OlPaymentSaveThread;
import com.pay.risk.util.BaseHttpComponent;
import com.pay.risk.util.DateUtils;
import com.riskrule.bean.RuleObj;
import com.riskrule.runmvel.RunRule;
import com.riskutil.redis.RedisUtil;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    private static final String mCalcCode = "M00002_t0";
    @Resource
    private OlPaymentService olPaymentService;

    @Resource
    private OrderService orderService;

    public OlPaymentService getOlPaymentService() {
        return olPaymentService;
    }

    public void setOlPaymentService(OlPaymentService olPaymentService) {
        this.olPaymentService = olPaymentService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public RuleObj parseStr2RuleObj(String str) {
        return ONLineUtil.transStr2Obj(str);
    }

    @Override
    public void setCalcInfo(RuleObj rObj) {
        RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
        try {
            int _s = DateUtils.getExpireSeconds(36);
            long k = new Date().getTime();
            Map<String, Object> map = rObj.getRuleDetail();
            // 获得IP信息
            if (ONLineUtil.isStrObj(map.get("trans_ip"))) {
                // 上线或上测试后修改
                String tIp = ONLineUtil.transStrObj(map.get("trans_ip"));
                PropertyUtil propertyUtil = PropertyUtil.getInstance("rule");
                String path = propertyUtil.getProperty("ip_find_area");

                String url = path + tIp;
                logger.info("ip url " + url);

                String response = null;
                try {
                    URI uri = new URI(url);
                    response = BaseHttpComponent.executeGet(uri);
                    logger.info("ip respone  " + response);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                if (response != null && !"".equals(response)) {
                    try {
                        JSONObject obj = JSONObject.fromObject(response);
                        if (obj.get("province") != null && obj.get("city") != null) {
                            String provinceCode = ONLineUtil.transStrObj(obj.get("province"));
                            String cityCode = ONLineUtil.transStrObj(obj.get("city"));
                            if (provinceCode != null && !"".equals(provinceCode) && cityCode != null && !"".equals(cityCode)) {
                                map.put("province", provinceCode);
                                map.put("city", cityCode);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            boolean checkFlag = checkData(map);
            if (!checkFlag) return;

            String dateFormat = ONLineUtil.transDateStr(String.valueOf(map.get("create_time")));

            String customerNo = ONLineUtil.transStrObj(map.get("customer_no"));

            String dateFormatFull = ONLineUtil.transDateStrFull(String.valueOf(map.get("create_time")));

            String dateFormatMonth = ONLineUtil.transDateStrMonth(String.valueOf(map.get("create_time")));

            Map<String, String> commonCustomerLimit = aresJedis.hgetAll(OnLinePayConstants.SUFFIX_COMMON_CUSTOMER_LIMITBYTYPE);

            Map<String, String> customerTransInfoDate = ONLineUtil.getTransInfo(dateFormat, customerNo, OnLinePayConstants.SUFFIX_CUSTOMERBYBTYPEDATE, aresJedis);

            Map<String, String> customerTransInfoMonth = ONLineUtil.getTransInfo(dateFormatMonth, customerNo, OnLinePayConstants.SUFFIX_CUSTOMERBYBTYPEMONTH,
                    aresJedis);

            String amount = String.valueOf(map.get("amount"));

            // 商户
            if (ONLineUtil.isStrObj(map.get("customer_no"))) {

                // 历史
                ONLineUtil.getPaymetHisData(aresJedis, map, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS, "customer_order_his");
                //
                // 商户卡类型交易历史
                ONLineUtil.getPaymetHisData(aresJedis, map, customerNo, OnLinePayConstants.SUFFIX_CUSTOMERCARDTYPE_HIS, "customer_cardtype_his");

                ONLineUtil.getPaymetHisData(aresJedis, map, customerNo, OnLinePayConstants.SUFFIX_PAYMENT_HIS, "customer_trade_his");

                // 业务限额
                try {
                    if (ONLineUtil.isStrObj(map.get("pay_type"))) {
                        String payType = ONLineUtil.transStrObj(map.get("pay_type"));
                        Map<String, String> mapCustomerLimit = aresJedis.hgetAll(customerNo + OnLinePayConstants.SUFFIX_CUSTOMER_LIMITBYTYPE);

                        Map<String, String> c = ONLineUtil.getLimitMapInfo(payType, amount, mapCustomerLimit, commonCustomerLimit, customerTransInfoDate,
                                customerTransInfoMonth);
                        logger.info("商户业务限额明细信息  " + customerNo + "  " + map.get("pay_type") + c);
                        map.put("customer_limit_bytype", c);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            // 卡
            if (ONLineUtil.isStrObj(map.get("user_pan"))) {
                String pan = ONLineUtil.transStrObj(map.get("user_pan"));
                // 卡号黑名单验证
                ONLineUtil.setBlackList(aresJedis, OnLinePayConstants.PAN_BLACKLIST, pan, "black_list_pan", map);

                // 卡号当日交易流水列表
                // ONLineUtil.getPaymetDate(pan ,aresJedis, dateFormat , map ,OnLinePayConstants.SUFFIX_CARDDATE_RELATION , "pan_payment_list_date");
                // logger.info("D");
                // logger.info(new Date().getTime() -k);

                // 卡号历史流水列表
                ONLineUtil.getPaymetHisData(aresJedis, map, pan, OnLinePayConstants.SUFFIX_CARDTRANS_HIS, "card_trade_his");

                // 限额
                Map<String, String> cLimit = new HashMap<String, String>();
                Map<String, String> cL1 = aresJedis.hgetAll("card_" + pan + "_single_limit");
                if (cL1 != null) {
                    String status = cL1.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL1.get("type_value") != null) cLimit.put("single", cL1.get("single"));
                    }
                }

                Map<String, String> cL2 = aresJedis.hgetAll("card_" + pan + "_single_card_day_limit");
                if (cL2 != null) {
                    String status = cL2.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL2.get("type_value") != null) cLimit.put("single_card_day", cL2.get("single_card_day"));
                    }
                }

                Map<String, String> cL3 = aresJedis.hgetAll("card_" + pan + "_single_card_month_limit");
                if (cL3 != null) {
                    String status = cL3.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL3.get("type_value") != null) cLimit.put("single_card_month", cL3.get("single_card_month"));
                    }
                }
                Map<String, String> cL4 = aresJedis.hgetAll("card_" + pan + "_card_day_frequency_limit");
                if (cL4 != null) {
                    String status = cL4.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL4.get("type_value") != null) cLimit.put("card_day_frequency", cL4.get("card_day_frequency"));
                    }
                }
                Map<String, String> cL5 = aresJedis.hgetAll("card_" + pan + "_card_month_frequency_limit");
                if (cL5 != null) {
                    String status = cL5.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL5.get("type_value") != null) cLimit.put("card_month_frequency", cL5.get("card_month_frequency"));
                    }
                }

                map.put("pan_limit", cLimit);

                // 银行卡业务限额明细信息
                try {
                    if (ONLineUtil.isStrObj(map.get("pay_type"))) {
                        String payType = ONLineUtil.transStrObj(map.get("pay_type"));
                        Map<String, String> mapPanLimit = aresJedis.hgetAll(pan + OnLinePayConstants.SUFFIX_PAN_LIMITBYTYPE);
                        Map<String, String> commonPanLimit = aresJedis.hgetAll(OnLinePayConstants.SUFFIX_COMMON_PAN_LIMITBYTYPE);
                        Map<String, String> panTransInfoDate = ONLineUtil.getTransInfo(dateFormat, pan, OnLinePayConstants.SUFFIX_PANBYBTYPEDATE, aresJedis);
                        Map<String, String> panTransInfoMonth = ONLineUtil.getTransInfo(dateFormatMonth, pan, OnLinePayConstants.SUFFIX_PANBYBTYPEMONTH, aresJedis);
                        Map<String, String> c = ONLineUtil.getLimitMapInfo(payType, amount, mapPanLimit, commonPanLimit, panTransInfoDate, panTransInfoMonth);
                        logger.info("银行卡业务限额明细信息  " + pan + "  " + map.get("pay_type") + c);
                        map.put("pan_limit_bytype", c);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (ONLineUtil.isStrObj(map.get("pay_type")) && ONLineUtil.isStrObj(map.get("user_pan"))) {
                String payType = ONLineUtil.transStrObj(map.get("pay_type"));
                String pan = ONLineUtil.transStrObj(map.get("user_pan"));
                Map<String, String> mapCPLimit = aresJedis.hgetAll(customerNo + "_" + pan + OnLinePayConstants.SUFFIX_CP_LIMITBYTYPE);
                Map<String, String> commonCPLimit = aresJedis.hgetAll(OnLinePayConstants.SUFFIX_COMMON_CP_LIMITBYTYPE);

                Map<String, String> cpTransInfoDate = ONLineUtil.getTransInfoCP(dateFormat, customerNo, pan, OnLinePayConstants.SUFFIX_CPBYTYPEDATE, aresJedis);
                Map<String, String> cpTransInfoMonth = ONLineUtil.getTransInfoCP(dateFormatMonth, customerNo, pan, OnLinePayConstants.SUFFIX_CPBYTYPEMONTH,
                        aresJedis);
                Map<String, String> c = ONLineUtil.getLimitMapInfo(payType, amount, mapCPLimit, commonCPLimit, cpTransInfoDate, cpTransInfoMonth);

                logger.info("银行卡业务限额明细信息  " + customerNo + " " + pan + " " + map.get("pay_type") + c);
                map.put("cp_limit_bytype", c);
            }

            // IP
            if (ONLineUtil.isStrObj(map.get("trans_ip"))) {
                String transIp = ONLineUtil.transStrObj(map.get("trans_ip"));

                ONLineUtil.getPaymetHisData(aresJedis, map, transIp, OnLinePayConstants.SUFFIX_IPTRANS_HIS, "ip_trade_his");

                Map<String, String> cLimit = new HashMap<String, String>();
                Map<String, String> cL1 = aresJedis.hgetAll("ip_" + transIp + "_single_limit");
                if (cL1 != null) {
                    String status = cL1.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL1.get("type_value") != null) cLimit.put("single", cL1.get("single"));
                    }
                }

                Map<String, String> cL2 = aresJedis.hgetAll("ip_" + transIp + "_single_ip_day_limit");
                if (cL2 != null) {
                    String status = cL2.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL2.get("type_value") != null) cLimit.put("single_ip_day", cL2.get("single_ip_day"));
                    }
                }

                Map<String, String> cL3 = aresJedis.hgetAll("ip_" + transIp + "_single_ip_month_limit");
                if (cL3 != null) {
                    String status = cL3.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL3.get("type_value") != null) cLimit.put("single_ip_month", cL3.get("single_ip_month"));
                    }
                }
                Map<String, String> cL4 = aresJedis.hgetAll("ip_" + transIp + "_ip_day_frequency_limit");
                if (cL4 != null) {
                    String status = cL4.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL4.get("type_value") != null) cLimit.put("ip_day_frequency", cL4.get("ip_day_frequency"));
                    }
                }
                Map<String, String> cL5 = aresJedis.hgetAll("ip_" + transIp + "_ip_month_frequency_limit");
                if (cL5 != null) {
                    String status = cL5.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL5.get("type_value") != null) cLimit.put("ip_month_frequency", cL5.get("ip_month_frequency"));
                    }
                }

                map.put("ip_limit", cLimit);
            }

            // 身份证
            if (ONLineUtil.isStrObj(map.get("user_idno"))) {
                String user_idno = ONLineUtil.transStrObj(map.get("user_idno"));
                // 身份证黑名单验证
                ONLineUtil.setBlackList(aresJedis, OnLinePayConstants.IDCARDNO_BLACKLIST, user_idno, "black_list_idno", map);

                // 身份证当日交易流水列表
                // ONLineUtil.getPaymetDate(user_idno ,aresJedis, dateFormat , map ,OnLinePayConstants.SUFFIX_IDCARDNODATE_RELATION , "idno_payment_list_date");

                // 身份证历史流水列表
                ONLineUtil.getPaymetHisData(aresJedis, map, user_idno, OnLinePayConstants.SUFFIX_IDCARDNOTRANS_HIS, "idno_trade_his");
            }

            // 电话
            if (ONLineUtil.isStrObj(map.get("user_phone"))) {
                String user_phone = ONLineUtil.transStrObj(map.get("user_phone"));
                // 身份证黑名单验证
                ONLineUtil.setBlackList(aresJedis, OnLinePayConstants.PHONENO_BLACKLIST, user_phone, "black_list_phone", map);

                // 身份证当日交易流水列表
                // ONLineUtil.getPaymetDate(user_phone ,aresJedis, dateFormat , map ,OnLinePayConstants.SUFFIX_PHONEDATE_RELATION , "phone_payment_list_date");

                // 身份证历史流水列表
                ONLineUtil.getPaymetHisData(aresJedis, map, user_phone, OnLinePayConstants.SUFFIX_PHONETRANS_HIS, "phone_trade_his");
            }

            // 卡 地点 交易关系维护

            if (map.get("user_pan") != null && map.get("province") != null && map.get("city") != null && map.get("amount") != null && dateFormatFull != null) {
                List<String> list = aresJedis.lrange(String.valueOf(map.get("user_pan")) + OnLinePayConstants.SUFFIX_DES_RELATION, (long) 0, (long) -1);
                map.put("card_relation", list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            aresJedis.close();
        }

    }

    private boolean checkData(Map<String, Object> map) {

        if (map.get("create_time") == null) {
            logger.info("关键信息缺失  create_time");
            return false;
        }

        if (map.get("order_id") == null) {
            logger.info("关键信息缺失  order_id");
            return false;
        }

        if (map.get("payment_id") == null) {
            logger.info("关键信息缺失  payment_id");
            return false;
        }

        if (map.get("customer_no") == null) {
            logger.info("关键信息缺失  customer_no");
            return false;
        }

        if (map.get("bus_type") == null) {
            logger.info("关键信息缺失  bus_type");
            return false;
        }

        if (map.get("pay_type") == null) {
            logger.info("关键信息缺失  pay_type");
            return false;
        }

        if (map.get("amount") == null) {
            logger.info("关键信息缺失  amount");
            return false;
        }
        return true;
    }

    @Override
    public void setData2Cache(RuleObj rObj) {
        logger.info("payment set cache begin");
        long j = new Date().getTime();
        RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
        try {
            int _s = DateUtils.getExpireSeconds(36);
            Map<String, Object> m1 = rObj.getRuleDetail();
            Map<String, String> map = (Map<String, String>) m1.get("redisInfo");
            String customerNo = ONLineUtil.transStrObj(map.get("customer_no"));

            String paymentId = ONLineUtil.transStrObj(map.get("payment_id"));

            logger.info("customer_no " + customerNo + " paymentId " + paymentId);

            String orderId = ONLineUtil.transStrObj(map.get("order_id"));

            String dateFormat = ONLineUtil.transDateStr(String.valueOf(map.get("create_time")));

            String dateFormatFull = ONLineUtil.transDateStrFull(String.valueOf(map.get("create_time")));

            String payType = ONLineUtil.transStrObj(String.valueOf(map.get("pay_type")));

            String busType = ONLineUtil.transStrObj(String.valueOf(map.get("bus_type")));

            String dateFormatMonth = ONLineUtil.transDateStrMonth(String.valueOf(map.get("create_time")));

            String pan = null;

            String amount = ONLineUtil.transStrObj(map.get("amount"));
            // 交易维护
            aresJedis.hmset(_s, paymentId, map);

            aresJedis.sadd(_s, OnLinePayConstants.PREFIX_PAYEMNT_LIST + dateFormat, paymentId);

            // 商户
            if (ONLineUtil.isStrObj(map.get("customer_no"))) {
                aresJedis.sadd(_s, ONLineUtil.geneKeyByData(customerNo, dateFormat, OnLinePayConstants.SUFFIX_CUSTOME_RPAYMENT_RELATION), paymentId);

                ONLineUtil.setHisIndoBy4Patams(_s, customerNo, OnLinePayConstants.SUFFIX_PAYMENT_HIS, dateFormat, payType, busType,
                        OnLinePayConstants._PAYMENTCOUNT, aresJedis, "INT", amount);

                ONLineUtil.setHisIndoBy4Patams(_s, customerNo, OnLinePayConstants.SUFFIX_PAYMENT_HIS, dateFormat, payType, busType,
                        OnLinePayConstants._PAYMENTAMOUNT, aresJedis, "DOUBLE", amount);

            }

            // 订单
            if (ONLineUtil.isStrObj(map.get("order_id"))) {

                aresJedis.sadd(_s, ONLineUtil.geneKeyByData(orderId, dateFormat, OnLinePayConstants.SUFFIX_ORERDATE_RELATION), paymentId);

            }

            // 卡
            if (ONLineUtil.isStrObj(map.get("user_pan"))) {
                pan = ONLineUtil.transStrObj(map.get("user_pan"));
                aresJedis.sadd(_s, ONLineUtil.geneKeyByData(pan, dateFormat, OnLinePayConstants.SUFFIX_CARDDATE_RELATION), paymentId);

                // 卡订单关系维护，如果当天该支付方式和业务类型下有该笔订单责不再维护其关系

                setHisData(_s, aresJedis, customerNo, orderId, dateFormat, payType, busType, pan, amount, OnLinePayConstants.SUFFIX_CARDORDER_RELATAION,
                        OnLinePayConstants.SUFFIX_CARDTRANS_HIS);

                // 商户卡种按日交易维护，如果当天该如果当天该支付方式和业务类型下有该笔订单责不再维护其关系

                String cardType = ONLineUtil.transStrObj(String.valueOf(map.get("card_type")));

                setHisData(_s, aresJedis, cardType, orderId, dateFormat, payType, busType, customerNo, amount,
                        OnLinePayConstants.SUFFIX_CARDTYPEORDERDATE_RELATAION, OnLinePayConstants.SUFFIX_CUSTOMERCARDTYPE_HIS);

            }

            // IP
            if (ONLineUtil.isStrObj(map.get("trans_ip"))) {
                String transIp = ONLineUtil.transStrObj(map.get("trans_ip"));
                aresJedis.sadd(_s, ONLineUtil.geneKeyByData(transIp, dateFormat, OnLinePayConstants.SUFFIX_IPDATE_RELATION), paymentId);

                setHisData(_s, aresJedis, customerNo, orderId, dateFormat, payType, busType, transIp, amount, OnLinePayConstants.SUFFIX_IPORDER_RELATAION,
                        OnLinePayConstants.SUFFIX_IPTRANS_HIS);

            }

            // 身份证
            if (ONLineUtil.isStrObj(map.get("user_idno"))) {
                String user_idno = ONLineUtil.transStrObj(map.get("user_idno"));
                aresJedis.sadd(_s, ONLineUtil.geneKeyByData(user_idno, dateFormat, OnLinePayConstants.SUFFIX_IDCARDNODATE_RELATION), paymentId);

                setHisData(_s, aresJedis, customerNo, orderId, dateFormat, payType, busType, user_idno, amount, OnLinePayConstants.SUFFIX_IDCARDNOORDER_RELATAION,
                        OnLinePayConstants.SUFFIX_IDCARDNOTRANS_HIS);
            }

            // 电话
            if (ONLineUtil.isStrObj(map.get("user_phone"))) {
                String user_phone = ONLineUtil.transStrObj(map.get("user_phone"));
                aresJedis.sadd(_s, ONLineUtil.geneKeyByData(user_phone, dateFormat, OnLinePayConstants.SUFFIX_PHONEDATE_RELATION), paymentId);

                setHisData(_s, aresJedis, customerNo, orderId, dateFormat, payType, busType, user_phone, amount, OnLinePayConstants.SUFFIX_PNONEORDER_RELATAION,
                        OnLinePayConstants.SUFFIX_PHONETRANS_HIS);
            }

            // 卡 地点 交易关系维护
            if (m1.get("user_pan") != null && m1.get("province") != null && m1.get("city") != null && m1.get("amount") != null && dateFormatFull != null) {
                String province = ONLineUtil.transStrObj(m1.get("province"));
                String city = ONLineUtil.transStrObj(m1.get("city"));
                aresJedis.lpush(_s, pan + OnLinePayConstants.SUFFIX_DES_RELATION, dateFormatFull + "_" + province + "_" + city + "_" + amount);
                aresJedis.ltrim(pan + OnLinePayConstants.SUFFIX_DES_RELATION, (long) 0, (long) 20);

            }

            // 卡按交易类型类型统计
            if (ONLineUtil.isStrObj(map.get("user_pan")) && ONLineUtil.isStrObj(map.get("pay_type")) && ONLineUtil.isStrObj(map.get("amount"))) {
                ONLineUtil.geneInfoBypayType(_s, aresJedis, dateFormat, payType, pan, amount, OnLinePayConstants.SUFFIX_PANBYBTYPEDATE,
                        OnLinePayConstants.SUFFIX_COUNT, OnLinePayConstants.SUFFIX_AMOUNT);

                ONLineUtil.geneInfoBypayType(_s, aresJedis, dateFormatMonth, payType, pan, amount, OnLinePayConstants.SUFFIX_PANBYBTYPEMONTH,
                        OnLinePayConstants.SUFFIX_COUNT, OnLinePayConstants.SUFFIX_AMOUNT);
            }

            // 商户按交易类型类型统计
            if (ONLineUtil.isStrObj(map.get("customer_no")) && ONLineUtil.isStrObj(map.get("pay_type")) && ONLineUtil.isStrObj(map.get("amount"))) {
                ONLineUtil.geneInfoBypayType(_s, aresJedis, dateFormat, payType, customerNo, amount, OnLinePayConstants.SUFFIX_CUSTOMERBYBTYPEDATE,
                        OnLinePayConstants.SUFFIX_COUNT, OnLinePayConstants.SUFFIX_AMOUNT);

                ONLineUtil.geneInfoBypayType(_s, aresJedis, dateFormatMonth, payType, customerNo, amount, OnLinePayConstants.SUFFIX_CUSTOMERBYBTYPEMONTH,
                        OnLinePayConstants.SUFFIX_COUNT, OnLinePayConstants.SUFFIX_AMOUNT);
            }

            if (ONLineUtil.isStrObj(map.get("customer_no")) && ONLineUtil.isStrObj(map.get("user_pan")) && ONLineUtil.isStrObj(map.get("pay_type"))
                    && ONLineUtil.isStrObj(map.get("amount"))) {
                ONLineUtil.geneInfoBypayTypeCP(_s, aresJedis, dateFormat, payType, customerNo, pan, amount, OnLinePayConstants.SUFFIX_CPBYTYPEDATE,
                        OnLinePayConstants.SUFFIX_COUNT, OnLinePayConstants.SUFFIX_AMOUNT);

                ONLineUtil.geneInfoBypayTypeCP(_s, aresJedis, dateFormatMonth, payType, customerNo, pan, amount, OnLinePayConstants.SUFFIX_CPBYTYPEMONTH,
                        OnLinePayConstants.SUFFIX_COUNT, OnLinePayConstants.SUFFIX_AMOUNT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            aresJedis.close();
        }
        logger.info("payment cache set cost " + (new Date().getTime() - j));

    }

    private void setHisData(int e, RedisUtil aresJedis, String customerNo, String orderId, String dateFormat, String payType, String busType, String busData,
            String amount, String constansRelation, String constansHis) {

        String key = ONLineUtil.reutnHisKey5param(busData, payType, busType, dateFormat, constansRelation);
        if (aresJedis.sismember(key, orderId)) {
            // 如果存在 ，什么都不做
        } else {
            aresJedis.sadd(e, key, orderId);

            ONLineUtil.setHisIndoBy5Patams(e, busData, customerNo, constansHis, dateFormat, payType, busType, OnLinePayConstants._ORDERCOUNTBYTYPE, aresJedis,
                    "INT", amount);

            ONLineUtil.setHisIndoBy5Patams(e, busData, customerNo, constansHis, dateFormat, payType, busType, OnLinePayConstants._ORDERAMOUNTBYTYPE, aresJedis,
                    "DOUBLE", amount);

        }

        ONLineUtil
                .setHisIndoBy5Patams(e, busData, customerNo, constansHis, dateFormat, payType, busType, OnLinePayConstants._PAYMENTCOUNT, aresJedis, "INT", amount);

        ONLineUtil.setHisIndoBy5Patams(e, busData, customerNo, constansHis, dateFormat, payType, busType, OnLinePayConstants._PAYMENTAMOUNT, aresJedis, "DOUBLE",
                amount);
    }

    @Override
    public String saveCaleResult(String s) {
        RuleObj obj = this.parseStr2RuleObj(s);
        this.setCalcInfo(obj);
        // 开始计算
        Map<String, String> md = ONLineUtil.generateCalcInfo(mCalcCode);
        RunRule.exeRuleEngine(md, obj);
        List<String> list = obj.getRules();
        JSONObject json = this.orderService.anaReslut(list, obj);
        Map<String, Object> m1 = obj.getRuleDetail();
        Map<String, String> map = (Map<String, String>) m1.get("redisInfo");

        ONLineUtil.transResultMap(map, json);
        new OlPaymentHisCacheThread(this, obj).start();
        // this.setData2Cache(obj);

        new OlPaymentSaveThread(olPaymentService, map).start();;
        // this.olPaymentService.saveOlPayment(mk);

        return ONLineUtil.generateResult("PAYMENT", map.get("payment_id"), json);

    }

    @Override
    public String saveOnResult(String str) {

        RedisUtil jedis = new RedisUtil(Constant.AresJedis);
        try {
            logger.info(str);
            RuleObj obj = this.parseStr2RuleObj(str);
            int _s = DateUtils.getExpireSeconds(36);
            Map<String, Object> m1 = obj.getRuleDetail();
            Map<String, String> map = (Map<String, String>) m1.get("redisInfo");
            String ordId = map.get("order_id");
            String paymentId = map.get("payment_id");

            String preResult = ONLineUtil.getFieldValue(paymentId, "result");
            if (preResult != null && "SUCCESS".equals(preResult)) {
                logger.info("save on result chongfu  paymentId id : " + paymentId);
                return null;
            }

            String result = map.get("result");

            Map<String, String> mapOrder = jedis.hgetAll(ordId);

            Map<String, String> mapPayment = jedis.hgetAll(paymentId);

            String complete_time = map.get("complete_time");

            String create_time = mapPayment.get("create_time");

            String customerNo = mapOrder.get("customer_no");

            String dateFormat = ONLineUtil.transDateStr(create_time);

            String dateFormatMonth = ONLineUtil.transDateStrMonth(String.valueOf(map.get("create_time")));

            String busType = mapOrder.get("bus_type");

            String amount = mapOrder.get("amount");

            String payType = mapPayment.get("pay_type");

            String pan = mapPayment.get("user_pan");
            String phone = mapPayment.get("user_phone");
            String ip = mapPayment.get("trans_ip");
            String idno = mapPayment.get("user_idno");
            String cardType = mapPayment.get("card_type");

            if (result != null) {
                jedis.hset(_s, paymentId, "result", result);
            }

            if (complete_time != null) {
                jedis.hset(_s, paymentId, "complete_time", complete_time);
            }

            if (result != null && "SUCCESS".equals(result)) {
                ONLineUtil.setHisIndoBy4Patams(_s, customerNo, OnLinePayConstants.SUFFIX_PAYMENT_HIS, dateFormat, payType, busType,
                        OnLinePayConstants._PAYMENTCOUNTSUCCESS, jedis, "INT", amount);
                ONLineUtil.setHisIndoBy4Patams(_s, customerNo, OnLinePayConstants.SUFFIX_PAYMENT_HIS, dateFormat, payType, busType,
                        OnLinePayConstants._PAYMENTAMOUNTSUCCESS, jedis, "DOUBLE", amount);
                // pan

                if (pan != null && !"".equals(pan)) {
                    ONLineUtil.setHisIndoBy5Patams(_s, pan, customerNo, OnLinePayConstants.SUFFIX_CARDTRANS_HIS, dateFormat, payType, busType,
                            OnLinePayConstants._PAYMENTCOUNTSUCCESS, jedis, "INT", amount);
                    ONLineUtil.setHisIndoBy5Patams(_s, pan, customerNo, OnLinePayConstants.SUFFIX_CARDTRANS_HIS, dateFormat, payType, busType,
                            OnLinePayConstants._PAYMENTAMOUNTSUCCESS, jedis, "DOUBLE", amount);

                }

                // cardTYPE
                if (cardType != null && !"".equals(cardType)) {
                    ONLineUtil.setHisIndoBy5Patams(_s, customerNo, cardType, OnLinePayConstants.SUFFIX_CUSTOMERCARDTYPE_HIS, dateFormat, payType, busType,
                            OnLinePayConstants._PAYMENTCOUNTSUCCESS, jedis, "INT", amount);
                    ONLineUtil.setHisIndoBy5Patams(_s, customerNo, cardType, OnLinePayConstants.SUFFIX_CUSTOMERCARDTYPE_HIS, dateFormat, payType, busType,
                            OnLinePayConstants._PAYMENTAMOUNTSUCCESS, jedis, "DOUBLE", amount);
                }

                // ip
                if (ip != null && !"".equals(ip)) {
                    ONLineUtil.setHisIndoBy5Patams(_s, ip, customerNo, OnLinePayConstants.SUFFIX_IPTRANS_HIS, dateFormat, payType, busType,
                            OnLinePayConstants._PAYMENTCOUNTSUCCESS, jedis, "INT", amount);
                    ONLineUtil.setHisIndoBy5Patams(_s, ip, customerNo, OnLinePayConstants.SUFFIX_IPTRANS_HIS, dateFormat, payType, busType,
                            OnLinePayConstants._PAYMENTAMOUNTSUCCESS, jedis, "DOUBLE", amount);

                }

                // phone
                if (phone != null && !"".equals(phone)) {
                    ONLineUtil.setHisIndoBy5Patams(_s, phone, customerNo, OnLinePayConstants.SUFFIX_PHONETRANS_HIS, dateFormat, payType, busType,
                            OnLinePayConstants._PAYMENTCOUNTSUCCESS, jedis, "INT", amount);
                    ONLineUtil.setHisIndoBy5Patams(_s, phone, customerNo, OnLinePayConstants.SUFFIX_PHONETRANS_HIS, dateFormat, payType, busType,
                            OnLinePayConstants._PAYMENTAMOUNTSUCCESS, jedis, "DOUBLE", amount);

                }

                // idno
                if (idno != null && !"".equals(idno)) {
                    ONLineUtil.setHisIndoBy5Patams(_s, idno, customerNo, OnLinePayConstants.SUFFIX_IDCARDNOTRANS_HIS, dateFormat, payType, busType,
                            OnLinePayConstants._PAYMENTCOUNTSUCCESS, jedis, "INT", amount);
                    ONLineUtil.setHisIndoBy5Patams(_s, idno, customerNo, OnLinePayConstants.SUFFIX_IDCARDNOTRANS_HIS, dateFormat, payType, busType,
                            OnLinePayConstants._PAYMENTAMOUNTSUCCESS, jedis, "DOUBLE", amount);

                }

                // 卡按交易类型类型统计
                if (ONLineUtil.isStrObj(pan) && ONLineUtil.isStrObj(map.get("pay_type")) && ONLineUtil.isStrObj(map.get("amount"))) {
                    ONLineUtil.geneInfoBypayType(_s, jedis, dateFormat, payType, pan, amount, OnLinePayConstants.SUFFIX_PANBYBTYPEDATE,
                            OnLinePayConstants.SUFFIX_SUCCESSCOUNT, OnLinePayConstants.SUFFIX_SUCCESAMOUNT);

                    ONLineUtil.geneInfoBypayType(_s, jedis, dateFormatMonth, payType, pan, amount, OnLinePayConstants.SUFFIX_PANBYBTYPEMONTH,
                            OnLinePayConstants.SUFFIX_SUCCESSCOUNT, OnLinePayConstants.SUFFIX_SUCCESAMOUNT);
                }
                // 商户按交易类型类型统计
                if (ONLineUtil.isStrObj(map.get("customer_no")) && ONLineUtil.isStrObj(map.get("pay_type")) && ONLineUtil.isStrObj(map.get("amount"))) {
                    ONLineUtil.geneInfoBypayType(_s, jedis, dateFormat, payType, customerNo, amount, OnLinePayConstants.SUFFIX_CUSTOMERBYBTYPEDATE,
                            OnLinePayConstants.SUFFIX_SUCCESSCOUNT, OnLinePayConstants.SUFFIX_SUCCESAMOUNT);

                    ONLineUtil.geneInfoBypayType(_s, jedis, dateFormatMonth, payType, customerNo, amount, OnLinePayConstants.SUFFIX_CUSTOMERBYBTYPEMONTH,
                            OnLinePayConstants.SUFFIX_SUCCESSCOUNT, OnLinePayConstants.SUFFIX_SUCCESAMOUNT);
                }

                if (ONLineUtil.isStrObj(map.get("customer_no")) && ONLineUtil.isStrObj(pan) && ONLineUtil.isStrObj(map.get("pay_type"))
                        && ONLineUtil.isStrObj(map.get("amount"))) {
                    ONLineUtil.geneInfoBypayTypeCP(_s, jedis, dateFormat, payType, customerNo, pan, amount, OnLinePayConstants.SUFFIX_CPBYTYPEDATE,
                            OnLinePayConstants.SUFFIX_SUCCESSCOUNT, OnLinePayConstants.SUFFIX_SUCCESAMOUNT);

                    ONLineUtil.geneInfoBypayTypeCP(_s, jedis, dateFormatMonth, payType, customerNo, pan, amount, OnLinePayConstants.SUFFIX_CPBYTYPEMONTH,
                            OnLinePayConstants.SUFFIX_SUCCESSCOUNT, OnLinePayConstants.SUFFIX_SUCCESAMOUNT);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

}
