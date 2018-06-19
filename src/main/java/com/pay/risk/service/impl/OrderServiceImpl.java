package com.pay.risk.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.risk.Constant;
import com.pay.risk.constans.ONLineUtil;
import com.pay.risk.constans.OnLinePayConstants;
import com.pay.risk.remote.service.CustomerAreaLimitAresFacade;
import com.pay.risk.service.OlOrderService;
import com.pay.risk.service.OrderService;
import com.pay.risk.thread.OlOrderHisCacheThread;
import com.pay.risk.thread.OlOrderSaveThread;
import com.pay.risk.util.DateUtils;
import com.pay.risk.util.SmsSendUtil;
import com.riskrule.bean.RuleObj;
import com.riskrule.runmvel.RunRule;
import com.riskutil.redis.AddressPositionUtil;
import com.riskutil.redis.RedisUtil;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);

    private static final String mCalcCodeM3 = "M00003_t0";

    @Resource
    private OlOrderService olOrderService;
    @Resource
    private CustomerAreaLimitAresFacade customerAreaLimitAresFacade;

    public OlOrderService getOlOrderService() {
        return olOrderService;
    }

    public void setOlOrderService(OlOrderService olOrderService) {
        this.olOrderService = olOrderService;
    }

    @Override
    public RuleObj parseStr2RuleObj(String str) {
        return ONLineUtil.transStr2Obj(str);
    }

    @Override
    public void setCalcInfo(RuleObj rObj) {
        RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
        try {

            long k = new Date().getTime();
            Map<String, Object> map = rObj.getRuleDetail();

            String dateFormat = ONLineUtil.transDateStr(String.valueOf(map.get("create_time")));
            // 商户
            if (ONLineUtil.isStrObj(map.get("customer_no"))) {

                String customerNo = ONLineUtil.transStrObj(map.get("customer_no"));
                logger.info("customerNo " + customerNo);

                // 该商户当天创建流水列表
                k = new Date().getTime();

                // 该商户历史流水明细
                ONLineUtil.getPaymetHisData(aresJedis, map, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS, "customer_trade_his");

                k = new Date().getTime();

                // 限额 customer_86181_single_limit
                Map<String, String> cLimit = new HashMap<String, String>();
                Map<String, String> cL1 = aresJedis.hgetAll("customer_" + customerNo + "_single_limit");
                if (cL1 != null) {
                    String status = cL1.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL1.get("type_value") != null) cLimit.put("single", cL1.get("single"));
                    }
                }

                Map<String, String> cL2 = aresJedis.hgetAll("customer_" + customerNo + "_single_day_limit");
                if (cL2 != null) {
                    String status = cL2.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL2.get("type_value") != null) cLimit.put("single_day", cL2.get("single_day"));
                    }
                }

                Map<String, String> cL3 = aresJedis.hgetAll("customer_" + customerNo + "_single_month_limit");
                if (cL3 != null) {
                    String status = cL3.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL3.get("type_value") != null) cLimit.put("single_month", cL3.get("single_month"));
                    }
                }

                Map<String, String> cL4 = aresJedis.hgetAll("customer_" + customerNo + "_customer_day_frequency_limit");
                if (cL4 != null) {
                    String status = cL4.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL4.get("type_value") != null) cLimit.put("customer_day_frequency", cL4.get("customer_day_frequency"));
                    }
                }

                Map<String, String> cL5 = aresJedis.hgetAll("customer_" + customerNo + "_customer_month_frequency_limit");
                if (cL5 != null) {
                    String status = cL5.get("status");
                    if (status != null && "Y".equals(status)) {
                        if (cL5.get("type_value") != null) cLimit.put("customer_month_frequency", cL5.get("customer_month_frequency"));
                    }
                }

                map.put("customer_limit", cLimit);

                // 商户黑名单验证
                ONLineUtil.setBlackList(aresJedis, OnLinePayConstants.CUSTOMER_BLACKLIST, customerNo, "black_list_customer", map);

                k = new Date().getTime();

                // 当天
                k = new Date().getTime();
                // 历史(天)
                ONLineUtil.getPaymetHisData(aresJedis, map, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS, "customer_order_his");

                // 历史(月)
                ONLineUtil.getPaymetHisData(aresJedis, map, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS_MONTH, "customer_order_his_month");

                String sKey = ONLineUtil.geneExtKey(dateFormat, customerNo);
                Map<String, String> m1 = aresJedis.hgetAll(sKey);
                map.put("order_his_ext", m1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            aresJedis.close();
        }
    }

    @Override
    public void setData2Cache(RuleObj rObj) {
        logger.info("order setData2Cache begin");
        long j = new Date().getTime();
        RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
        try {
            int _s = DateUtils.getExpireSeconds(36);
            Map<String, Object> m1 = rObj.getRuleDetail();
            Map<String, String> map = (Map<String, String>) m1.get("redisInfo");

            String customerNo = ONLineUtil.transStrObj(map.get("customer_no"));

            String amount = ONLineUtil.transStrObj(map.get("amount"));

            String busType = ONLineUtil.transStrObj(String.valueOf(map.get("bus_type")));

            String orderId = ONLineUtil.transStrObj(map.get("order_id"));
            aresJedis.hmset(_s, orderId, map);

            logger.info("customer_no " + customerNo + " orderId " + orderId);

            String dateFormat = ONLineUtil.transDateStr(String.valueOf(map.get("create_time")));

            String dateFormatMonth = ONLineUtil.transDateStrMonth(String.valueOf(map.get("create_time")));

            aresJedis.sadd(_s, OnLinePayConstants.PREFIX_ORDER_LIST + dateFormat, orderId);

            aresJedis.sadd(_s, customerNo + "_" + dateFormat + OnLinePayConstants.SUFFIX_CUSTOMER_ORDERDATE, orderId);

            ONLineUtil.setHisIndoBy3Patams(_s, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS, dateFormat, busType, OnLinePayConstants._ORDERCOUNT, aresJedis,
                    "INT", amount);

            ONLineUtil.setHisIndoBy3Patams(_s, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS, dateFormat, busType, OnLinePayConstants._ORDERAMOUNT, aresJedis,
                    "DOUBLE", amount);

            ONLineUtil.setHisIndoBy3Patams(_s, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS_MONTH, dateFormatMonth, busType, OnLinePayConstants._ORDERCOUNT,
                    aresJedis, "INT", amount);

            ONLineUtil.setHisIndoBy3Patams(_s, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS_MONTH, dateFormatMonth, busType, OnLinePayConstants._ORDERAMOUNT,
                    aresJedis, "DOUBLE", amount);

            logger.info("order cache set cost " + (new Date().getTime() - j));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            aresJedis.close();
        }

    }

    @Override
    public JSONObject anaReslut(List<String> list, RuleObj rObj) {
        JSONObject json = new JSONObject();
        json.put("result", "PASS");
        if (list == null || list.size() == 0) {
            return json;
        }
        RedisUtil jedis = new RedisUtil(Constant.AresJedis);
        try {
            JSONArray arrRule = new JSONArray();
            if (list != null && list.size() > 0) {
                for (String ruleStr : list) {
                    JSONObject obj = new JSONObject();
                    String nameRule = jedis.hget(ruleStr + OnLinePayConstants.RULECODE, "name");

                    if (nameRule != null && !"".equals(nameRule)) {
                        obj.put(ruleStr, nameRule);
                        arrRule.add(obj);
                    }

                    Set<String> handls = jedis.smembers(ruleStr + OnLinePayConstants.RULEHANDLERELATION);
                    String sf = jedis.get("SMS_FLAG");

                    for (String handle : handls) {
                        try {
                            if (sf != null && "Y".equals(sf)) {

                                Set<String> handlsSms = jedis.smembers("SMS_HANDELS");
                                logger.info("SMS handle :" + handle);

                                logger.info("SMS handlsSms :" + handlsSms);

                                Map<String, Object> m1 = rObj.getRuleDetail();

                                String orderId = (String) m1.get("order_id");

                                if (orderId == null) orderId = (String) m1.get("payment_id");
                                logger.info("SMS orderId :" + orderId);
                                if (handlsSms.contains(handle)) {
                                    SmsSendUtil.send2("18310601526", "业务号 " + orderId + "交易受限");
                                    SmsSendUtil.send2("13693008035", "业务号 " + orderId + "交易受限");
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String status = jedis.hget(handle + OnLinePayConstants.HANDLECODE, "status");
                        if (status != null && "Y".equals(status)) {
                            json.put("result", "REJECT");
                        }
                    }
                }
                // 返回警告，先写死
                if (list != null && list.size() == 1) {
                    if (list.contains("R00032") || list.contains("R00033")) json.put("result", "WARN");
                }

                if (list != null && list.size() == 2) {
                    if (list.contains("R00032") && list.contains("R00033")) json.put("result", "WARN");
                }
                // TODO
                if (list != null && list.size() == 1) {
                    if (list.contains("R00086")) {
                        logger.info("只触发  R00086 规则 ，  报警=====");
                        json.put("result", "WARN");
                    }
                }
                json.put("ruleDetail", arrRule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }

        return json;
    }

    @Override
    public String saveCaleResult(String s) {
        RuleObj obj = this.parseStr2RuleObj(s);

        this.setCalcInfo(obj);

        // 开始计算
        Map<String, String> md = ONLineUtil.generateCalcInfo(mCalcCodeM3);
        RunRule.exeRuleEngine(md, obj);

        List<String> list = obj.getRules();
        JSONObject json = this.anaReslut(list, obj);
        Map<String, Object> m1 = obj.getRuleDetail();
        Map<String, String> map = (Map<String, String>) m1.get("redisInfo");

        ONLineUtil.transResultMap(map, json);

        new OlOrderSaveThread(olOrderService, map).start();

        new OlOrderHisCacheThread(this, obj).start();

        return ONLineUtil.generateResult("ORDER", map.get("order_id"), json);

    }

    @Override
    public String saveOnResult(String str) {
        RedisUtil jedis = new RedisUtil(Constant.AresJedis);
        try {
            logger.info("saveOnResult str" + str);
            int _s = DateUtils.getExpireSeconds(36);
            RuleObj obj = this.parseStr2RuleObj(str);
            Map<String, Object> m1 = obj.getRuleDetail();
            Map<String, String> map = (Map<String, String>) m1.get("redisInfo");
            String ordId = map.get("order_id");
            String preResult = ONLineUtil.getFieldValue(ordId, "result");
            if (preResult != null && "SUCCESS".equals(preResult)) {
                logger.info("save on result chongfu  order id : " + ordId);
                return null;
            }

            String result = map.get("result");
            String complete_time = map.get("complete_time");

            String create_time = jedis.hget(ordId, "create_time");

            String customerNo = jedis.hget(ordId, "customer_no");

            String dateFormat = ONLineUtil.transDateStr(create_time);

            String busType = jedis.hget(ordId, "bus_type");

            String amount = jedis.hget(ordId, "amount");

            if (result != null) {
                jedis.hset(_s, ordId, "result", result);
            }
            logger.info("=== " + result);
            if (complete_time != null) {
                jedis.hset(_s, ordId, "complete_time", complete_time);
            }

            if (result != null && "SUCCESS".equals(result)) {
                logger.info("=== on" + result);

                String dateFormatMonth = ONLineUtil.transDateStrMonth(create_time);
                logger.info("on result map value : " + map.get("create_time"));
                logger.info("on result dateFormat : " + dateFormat);
                logger.info("on result dateFormatMonth : " + dateFormatMonth);
                ONLineUtil.setHisIndoBy3Patams(_s, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS, dateFormat, busType, OnLinePayConstants._ORDERCOUNTSUCCESS,
                        jedis, "INT", amount);

                ONLineUtil.setHisIndoBy3Patams(_s, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS, dateFormat, busType, OnLinePayConstants._ORDERAMOUNTSUCCESS,
                        jedis, "DOUBLE", amount);

                ONLineUtil.setHisIndoBy3Patams(_s, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS_MONTH, dateFormatMonth, busType,
                        OnLinePayConstants._ORDERCOUNTSUCCESS, jedis, "INT", amount);

                ONLineUtil.setHisIndoBy3Patams(_s, customerNo, OnLinePayConstants.SUFFIX_ORDER_LISTHIS_MONTH, dateFormatMonth, busType,
                        OnLinePayConstants._ORDERAMOUNTSUCCESS, jedis, "DOUBLE", amount);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    @Override
    public void setAreaCodeInfo(Map<String, Object> map, RedisUtil aresJedis) {

        String logFlag = aresJedis.get("LOG_FLAG_SHJYDQXZS");// 这是一个控制日志的标签
        if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ： logFlag是" + logFlag);

        String date = DateUtils.getTodayStr();

        String customer_no = null;
        if (map.get("customer_no") != null) {
            customer_no = String.valueOf(map.get("customer_no"));
        }
        String trans_ip = null;
        if (map.get("trans_ip") != null) {
            trans_ip = String.valueOf(map.get("trans_ip"));
        }

        if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ： 获取基础数据是 customer_no  是" + customer_no + "==trans_ip " + trans_ip);

        String cityCode = getCityCode(trans_ip, logFlag);

        if (cityCode == null || cityCode.trim().equals("") || "null".equals(cityCode)) {
            // 根据ip没用获取到地区码，直接跳过限制
            map.put(OnLinePayConstants.AREA_CODE_COUNT, "-1");
            if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ： 调用 查询地区的接口 没有得到的cityCode，map中放-1" + OnLinePayConstants.AREA_CODE_COUNT);
            return;
        }

        // count 的 key
        String count_key = date + "_" + customer_no + OnLinePayConstants._TRANS_LIMIT_AREA_COUNT;

        if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ：查询 count  key：" + count_key);

        if (!aresJedis.exists(count_key)) {// 不存在key
            if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ：不存在count 调用第一个接口 key：" + count_key);

            boolean customerAreaLimitRefresh = customerAreaLimitAresFacade.customerAreaLimitRefresh(customer_no, cityCode);
            if (!customerAreaLimitRefresh) {// 调用接口( 有异常直接put -1)
                map.put(OnLinePayConstants.AREA_CODE_COUNT, "-1");
                if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ： 调用第一个接口，出现异常 -1" + OnLinePayConstants.AREA_CODE_COUNT);
                return;
            }
        }
        String countStr = aresJedis.get(count_key);
        if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ：最终得到的count" + countStr);

        if ("-1".equals(countStr) || "0".equals(countStr) || "".equals(countStr)) {
            map.put(OnLinePayConstants.AREA_CODE_COUNT, countStr);// 如果是-1或0 “”，直接跳过以后的，在规则文件中判断
            if (logFlag != null && "Y".equals(logFlag)) logger
                    .info("进入 setAreaCodeInfo 方法 : count是 -1 或 0 或 ‘’ ，将count放进map返回" + OnLinePayConstants.AREA_CODE_COUNT);
            return;
        }

        String areaCode_key = date + "_" + customer_no + OnLinePayConstants._TRANS_LIMIT_AREA_SET;// 地区码set 的 key
        Set<String> areaSet = aresJedis.smembers(areaCode_key);
        if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 :得到的set key是" + areaCode_key + "==set 是" + areaSet);

        Integer countNum = Integer.valueOf(countStr);
        if (areaSet != null && areaSet.size() != 0 && countNum > areaSet.size() && !areaSet.contains(cityCode)) {// set 不是null 不是 0 且 size小于count且areaSet没有cityCode
            if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 :调用第二个接口，此时 =set 是" + areaSet + "cityCode 是 " + cityCode + "=count是"
                    + countNum);
            boolean customerAreaLimitAdd = customerAreaLimitAresFacade.customerAreaLimitAdd(customer_no, cityCode);
            if (!customerAreaLimitAdd) {// 调用接口( 有异常直接put -1)
                map.put(OnLinePayConstants.AREA_CODE_COUNT, "-1");
                if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 :调用第二个接口，出现异常 -1" + OnLinePayConstants.AREA_CODE_COUNT);
                return;
            }
            areaSet = aresJedis.smembers(areaCode_key);
            if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ：调用第二个接口后，得到的set" + areaSet);

        }
        map.put(OnLinePayConstants.AREA_CODE, cityCode);
        map.put(OnLinePayConstants.AREA_CODE_COUNT, countStr);
        map.put(OnLinePayConstants.AREA_CODE_SET, areaSet);
        if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ：最终放入 map中的结果是area_code::" + cityCode + ":area_code_count:" + countStr
                + ":area_code_set:" + areaSet);
    }

    public String getCityCode(String trans_ip, String logFlag) {
        Map<String, String> addressParam = new HashMap<String, String>();
        AddressPositionUtil.analyIpParam(trans_ip, addressParam);// {ip:111}
        String resultJson = AddressPositionUtil.queryIpInfo(addressParam);// {result}

        // String resultJson = HttpClientUtils.send(Method.POST, "10.10.111.51:8082/basicip/info/ip/get", "ip=" + trans_ip, true, "UTF-8");
        if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ： 调用  查询地区的接口 得到的result：" + resultJson);

        if (resultJson == null || resultJson.trim().equals("")) {
            // 根据ip得到结果，直接跳过限制
            if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ： 调用  查询地区的接口 得到的result是空的");
            return null;
        }
        String cityCode = AddressPositionUtil.analysisCityCodefromJsonStr(resultJson, "cityCode");
        if (logFlag != null && "Y".equals(logFlag)) logger.info("进入 setAreaCodeInfo 方法 ： 得到的cityCode是：" + cityCode);
        return cityCode;
    }

}