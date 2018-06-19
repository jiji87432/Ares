package com.pay.risk.constans;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.pay.common.util.PropertyUtil;
import com.pay.risk.Constant;
import com.riskrule.bean.RuleObj;
import com.riskutil.redis.RedisUtil;

public class ONLineUtil {

    // private static final Logger logger = Logger.getLogger(ONLineUtil.class);
    private static final Logger logger = Logger.getLogger(ONLineUtil.class);
    private static final String nodata = "nodata";

    public static Date returnDateFull(String strFull) {
        try {
            SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return sdfFull.parse(strFull);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String returnDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public String returnDateStrFull(Date date) {
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdfFull.format(date);
    }

    public static String returnDateFullStr(Date date) {
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdfFull.format(date);
    }

    public static RuleObj transStr2Obj(String str) {
        RuleObj o = new RuleObj();
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, String> m2Redis = new HashMap<String, String>();

        JSONObject obj = JSONObject.fromObject(str);

        if (obj.get("order_id") != null) {
            String _s = String.valueOf(obj.get("order_id"));
            if (_s != null && !"".equals(_s)) {
                map.put("order_id", _s);
                m2Redis.put("order_id", _s);
            }

        }

        if (obj.get("payment_id") != null) {
            String _s = String.valueOf(obj.get("payment_id"));
            if (_s != null && !"".equals(_s)) {
                map.put("payment_id", _s);
                m2Redis.put("payment_id", _s);
            }

        }

        if (obj.get("customer_no") != null) {
            String _s = String.valueOf(obj.get("customer_no"));
            if (_s != null && !"".equals(_s)) {
                map.put("customer_no", _s);
                m2Redis.put("customer_no", _s);
            }

        }

        if (obj.get("amount") != null) {
            String _s = String.valueOf(obj.get("amount"));
            if (_s != null && !"".equals(_s)) {
                map.put("amount", Double.valueOf(_s));
                m2Redis.put("amount", _s);
            }

        }

        if (obj.get("bus_type") != null) {
            String _s = String.valueOf(obj.get("bus_type"));
            if (_s != null && !"".equals(_s)) {
                map.put("bus_type", _s);
                m2Redis.put("bus_type", _s);
            }

        }

        if (obj.get("user_idno") != null) {
            String _s = String.valueOf(obj.get("user_idno"));
            if (_s != null && !"".equals(_s)) {
                map.put("user_idno", _s);
                m2Redis.put("user_idno", _s);
            }

        }

        if (obj.get("user_pan") != null) {
            String _s = String.valueOf(obj.get("user_pan"));
            if (_s != null && !"".equals(_s)) {
                map.put("user_pan", _s);
                m2Redis.put("user_pan", _s);
            }

        }

        if (obj.get("user_name") != null) {
            String _s = String.valueOf(obj.get("user_name"));
            if (_s != null && !"".equals(_s)) {
                map.put("user_name", _s);
                m2Redis.put("user_name", _s);
            }

        }

        if (obj.get("user_phone") != null) {
            String _s = String.valueOf(obj.get("user_phone"));
            if (_s != null && !"".equals(_s)) {
                map.put("user_phone", _s);
                m2Redis.put("user_phone", _s);
            }

        }

        if (obj.get("card_type") != null) {
            String _s = String.valueOf(obj.get("card_type"));
            if (_s != null && !"".equals(_s)) {
                map.put("card_type", _s);
                m2Redis.put("card_type", _s);
            }

        }

        if (obj.get("issuer") != null) {
            String _s = String.valueOf(obj.get("issuer"));
            if (_s != null && !"".equals(_s)) {
                map.put("issuer", _s);
                m2Redis.put("issuer", _s);
            }

        }

        if (obj.get("pay_type") != null) {
            String _s = String.valueOf(obj.get("pay_type"));
            if (_s != null && !"".equals(_s)) {
                map.put("pay_type", _s);
                m2Redis.put("pay_type", _s);
            }

        }

        if (obj.get("trans_ip") != null) {
            String _s = String.valueOf(obj.get("trans_ip"));
            if (_s != null && !"".equals(_s)) {
                map.put("trans_ip", _s);
                m2Redis.put("trans_ip", _s);
            }

        }

        if (obj.get("mac") != null) {
            String _s = String.valueOf(obj.get("mac"));
            if (_s != null && !"".equals(_s)) {
                map.put("mac", _s);
                m2Redis.put("mac", _s);
            }
        }

        if (obj.get("pay_carrier") != null) {
            String _s = String.valueOf(obj.get("pay_carrier"));
            if (_s != null && !"".equals(_s)) {
                map.put("pay_carrier", _s);
                m2Redis.put("pay_carrier", _s);
            }
        }

        if (obj.get("IMEI_NO") != null) {
            String _s = String.valueOf(obj.get("IMEI_NO"));
            if (_s != null && !"".equals(_s)) {
                map.put("IMEI_NO", _s);
                m2Redis.put("IMEI_NO", _s);
            }

        }

        if (obj.get("create_time") != null) {
            String _s = String.valueOf(obj.get("create_time"));
            if (_s != null && !"".equals(_s)) {
                map.put("create_time", _s);
                m2Redis.put("create_time", _s);
            }

        }

        if (obj.get("IMSI_NO") != null) {
            String _s = String.valueOf(obj.get("IMSI_NO"));
            if (_s != null && !"".equals(_s)) {
                map.put("IMSI_NO", _s);
                m2Redis.put("IMSI_NO", _s);
            }

        }
        if (obj.get("CPU_NO") != null) {
            String _s = String.valueOf(obj.get("CPU_NO"));
            if (_s != null && !"".equals(_s)) {
                map.put("CPU_NO", _s);
                m2Redis.put("CPU_NO", _s);
            }
        }

        if (obj.get("result") != null) {
            String _s = String.valueOf(obj.get("result"));
            if (_s != null && !"".equals(_s)) {
                map.put("result", _s);
                m2Redis.put("result", _s);
            }
        }

        if (obj.get("complete_time") != null) {
            String _s = String.valueOf(obj.get("complete_time"));
            if (_s != null && !"".equals(_s)) {
                map.put("complete_time", _s);
                m2Redis.put("complete_time", _s);
            }
        }
        /**
         * 商户类型 限额级别 支付类型 金额 状态
         * C_TYPE LIMIT_TYPE PAY_TYPE AMOUNT STATUS
         * c_type limit_type pay_type(*) amount(*) status
         */
        if (obj.get("c_type") != null) {
            String _s = String.valueOf(obj.get("c_type"));
            if (_s != null && !"".equals(_s)) {
                map.put("c_type", _s);
                m2Redis.put("c_type", _s);
            }
        }
        if (obj.get("limit_type") != null) {
            String _s = String.valueOf(obj.get("limit_type"));
            if (_s != null && !"".equals(_s)) {
                map.put("limit_type", _s);
                m2Redis.put("limit_type", _s);
            }
        }

        if (obj.get("status") != null) {
            String _s = String.valueOf(obj.get("status"));
            if (_s != null && !"".equals(_s)) {
                map.put("status", _s);
                m2Redis.put("status", _s);
            }
        }
        map.put("redisInfo", m2Redis);
        o.setRuleDetail(map);
        return o;
    }

    public static boolean isStrObj(Object obj) {
        if (obj == null) return false;
        try {
            String s = String.valueOf(obj);
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    public static String transStrObj(Object obj) {
        return String.valueOf(obj);

    }

    public static void getPaymetDate(String str, RedisUtil jedis, String dateFormat, Map<String, Object> map, String suffix, String property) {
        String key = geneKeyByData(str, dateFormat, suffix);
        List<Map<String, String>> list = getPaymentList(jedis, key);
        map.put(property, list);

    }

    public static String geneKeyByData(String str, String dateFormat, String suffix) {
        return str + "_" + dateFormat + suffix;
    }

    private static List<Map<String, String>> getPaymentList(RedisUtil jedis, String key) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Set<String> set = jedis.smembers(key);
        for (String s : set) {
            Map<String, String> map = jedis.hgetAll(s);
            list.add(map);
        }
        return list;
    }

    public static String transDateStr(String strDate) {
        try {
            SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdfFull.parse(strDate);

            String dateSet = transDateStrReset();
            String dateStr = sdf.format(date);

            if (!dateStr.equals(dateSet)) {
                logger.info("error Date : " + dateStr);
                dateStr = dateSet;
                logger.info("on cache set map value : " + strDate);
                logger.info("dateFormat reset value = " + strDate + " new value : " + dateSet);
            }
            return dateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String transDateStrReset() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String transDateStrFull(String strDate) {
        try {
            SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sdfFull.parse(strDate);
            return sdfFull.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getCustomerPaymetDate(String customerNo, RedisUtil jedis, String dateFormat, Map<String, Object> map, String suffix) {

    }

    public static void setBlackList(RedisUtil jedis, String constans, String data, String property, Map<String, Object> map) {
        String _s = jedis.hget(constans, data);
        if (_s != null && !"".equals(_s)) {
            map.put(property, _s);
        }

    }

    public static void getPaymetHisData(RedisUtil aresJedis, Map<String, Object> map, String data, String constans, String property) {
        String key = generateKeyBy2Pars(data, constans);
        logger.info("getPaymetHisData " + key);
        Map<String, String> m2 = aresJedis.hgetAll(key);
        logger.info("getPaymetHisData property" + property);
        // logger.info("getPaymetHisData m2" + m2);
        map.put(property, m2);
    }

    public static String generateKeyBy2Pars(String data, String constans) {
        return data + constans;
    }

    public static String reutnHisKey4param(String dateStr, String paytype, String busType, String suffix) {
        return dateStr + "_" + paytype + "_" + busType + suffix;
    }

    public static String reutnHisKey5param(String dateStr, String customerNO, String paytype, String busType, String suffix) {
        return dateStr + "_" + customerNO + "_" + paytype + "_" + busType + suffix;
    }

    public static void setHisIndoBy4Patams(int e, String busData, String sufHis, String dateFormat, String payType, String busType, String sufMap, RedisUtil jedis,
            String calcType, String amount) {
        String key = busData + sufHis;
        String mKey = ONLineUtil.reutnHisKey4param(dateFormat, payType, busType, sufMap);
        String value = jedis.hget(key, mKey);
        valueReset(e, jedis, calcType, key, mKey, value, amount);
    }

    private static void valueReset(int e, RedisUtil jedis, String calcType, String key, String mKey, String value, String amount) {

        if ("INT".equals(calcType)) {
            intValueReset(e, jedis, key, mKey, value);
        }

        if ("DOUBLE".equals(calcType)) {
            doubleValueReset(e, jedis, key, mKey, value, amount);
        }
    }

    private static void doubleValueReset(int e, RedisUtil jedis, String key, String mKey, String value, String amount) {
        Double am = Double.parseDouble(amount);
        if (value != null) {
            am += Double.parseDouble(value);
        }

        jedis.hset(e, key, mKey, String.valueOf(am));
    }

    private static void intValueReset(int _s, RedisUtil jedis, String key, String mKey, String value) {
        int i = 0;
        if (value != null) i = Integer.parseInt(value) + 1;
        else i = 1;
        jedis.hset(_s, key, mKey, String.valueOf(i));
    }

    public static void setHisIndoBy5Patams(int s, String busData, String customerNo, String suffixHis, String dateFormat, String payType, String busType,
            String mapKeyType, RedisUtil aresJedis, String calcType, String amount) {
        String key = busData + suffixHis;
        String mKey = ONLineUtil.reutnHisKey5param(dateFormat, customerNo, payType, busType, mapKeyType);
        String value = aresJedis.hget(key, mKey);
        valueReset(s, aresJedis, calcType, key, mKey, value, amount);
    }

    public static void setHisIndoBy3Patams(int e, String busData, String sufHis, String dateFormat, String busType, String sufMap, RedisUtil jedis, String calcType,
            String amount) {

        String key = busData + sufHis;
        // logger.info("=== key" + key);

        String mKey = ONLineUtil.reutnHisKey2param(dateFormat, busType, sufMap);

        // logger.info("=== mKey" + mKey);
        String value = jedis.hget(key, mKey);

        // logger.info("=== value" + value);
        valueReset(e, jedis, calcType, key, mKey, value, amount);
    }

    private static String reutnHisKey2param(String dateFormat, String busType, String sufMap) {
        return dateFormat + "_" + busType + sufMap;
    }

    public static void transResultMap(Map<String, String> map, JSONObject json) {
        if (json.get("ruleDetail") != null) {
            map.put("rule_detail", json.get("ruleDetail").toString());
        }

        map.put("rule_result", json.get("result").toString());

    }

    public static String generateResult(String type, String value, JSONObject json) {
        JSONObject j1 = new JSONObject();
        j1.put("BUS_TYPE", type);
        j1.put("BUS_NO", value);
        if (json.get("ruleDetail") != null) j1.put("RULES", json.get("ruleDetail"));
        j1.put("FINAL_RESULT", json.get("result"));
        return j1.toString();
    }

    public static Map<String, String> generateCalcInfo(String mCodeStr) {
        RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
        try {
            String[] _s = mCodeStr.split("_");
            String mCode = _s[0];
            String type = _s[1];
            String version = aresJedis.hget(mCode, type);
            Map<String, String> map = new HashMap<String, String>();
            map.put("version", version);
            map.put("ruleName", mCodeStr);
            PropertyUtil propertyUtil = PropertyUtil.getInstance("rule");
            String path = propertyUtil.getProperty("path");
            String rulePath = path + mCode + type + "_" + version + ".drl"; // H:/rule/M00001t0_4.drl
            map.put("path", rulePath);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            aresJedis.close();
        }
        return null;
    }

    public static String transDateStrMonth(String strDate) {
        try {
            SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyyMM");
            Date date = sdfFull.parse(strDate);

            String dateSet = transDateStrMonthReset();
            String dateStr = sdfMonth.format(date);
            if (!dateStr.equals(dateSet)) {
                logger.info("error Date : " + dateStr);
                dateStr = dateSet;
                logger.info("on cache set map value : " + strDate);
                logger.info("dateFormat month reset value = " + strDate + " new value : " + dateSet);
            }
            return dateStr;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String transDateStrMonthReset() {
        try {
            SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyyMM");
            Date date = new Date();
            return sdfMonth.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFieldValue(String orderId, String field) {
        RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
        try {
            return aresJedis.hget(orderId, field);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            aresJedis.close();
        }
        return null;
    }

    public static String getTodayStrYMD() {
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String dateStr = sdfFull.format(date);
        return getDateStrYMD(dateStr);
    }

    public static String getDateStrYMD(String dateStr) {
        return dateStr.substring(0, 10).replaceAll("-", "");
    }

    public static String get2point(Double d) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(d);
    }

    public static String geneExtKey(String todayStrYMD, String customerNo) {
        String sKey = customerNo + "_" + todayStrYMD + OnLinePayConstants.CUSTOMER_ORDER_EXT;
        return sKey;
    }

    public static int geneIntValue(String c1) {
        if (c1 == null || "".equals(c1)) return 1;
        Integer i = Integer.valueOf(c1);
        return i + 1;
    }

    public static double geneDoubleValue(String a1, String amount) {
        if (a1 == null || "".equals(a1)) return Double.valueOf(amount);
        Double i = Double.valueOf(a1);
        Double i2 = Double.valueOf(amount);
        return i + i2;
    }

    public static void geneInfoBypayType(int e, RedisUtil aresJedis, String dateFormat, String payType, String pan, String amount, String rKeyConstants,
            String mKeyCountConstants, String mKeyAmountConstants) {
        String rKey = ONLineUtil.geneKeyByData(dateFormat, pan, rKeyConstants);
        saveLimitInfo(e, aresJedis, payType, amount, mKeyCountConstants, mKeyAmountConstants, rKey);
    }

    public static void saveLimitInfo(int e, RedisUtil aresJedis, String payType, String amount, String mKeyCountConstants, String mKeyAmountConstants, String rKey) {
        String mKeyCount = ONLineUtil.generateKeyBy2Pars(payType, mKeyCountConstants);
        String mKeyAmount = ONLineUtil.generateKeyBy2Pars(payType, mKeyAmountConstants);
        Map<String, String> m2 = aresJedis.hgetAll(rKey);
        String c1 = m2.get(mKeyCount);
        String a1 = m2.get(mKeyAmount);
        int _c = ONLineUtil.geneIntValue(c1);
        double _a = ONLineUtil.geneDoubleValue(a1, amount);
        m2.put(mKeyCount, String.valueOf(_c));
        m2.put(mKeyAmount, ONLineUtil.get2point(_a));
        aresJedis.hmset(e, rKey, m2);
    }

    public static void geneInfoBypayTypeCP(int e, RedisUtil aresJedis, String dateFormat, String payType, String customerNo, String pan, String amount,
            String suffixCpbytypedate, String suffixCount, String suffixAmount) {
        String rKey = geneKeyBy3Params(dateFormat, customerNo, pan, suffixCpbytypedate);

        saveLimitInfo(e, aresJedis, payType, amount, suffixCount, suffixAmount, rKey);
    }

    private static String geneKeyBy3Params(String dateFormat, String customerNo, String pan, String suffixCpbytypedate) {
        return dateFormat + "_" + customerNo + "_" + pan + suffixCpbytypedate;
    }

    public static String geneRedisMapKey(String p1, String p2, String p3) {
        return p1 + "_" + p2 + "_" + p3;
    }

    public static Map<String, String> getTransInfo(String dateFormat, String customerNo, String suffixCustomerbybtypedate, RedisUtil aresJedis) {
        String rKey = ONLineUtil.geneKeyByData(dateFormat, customerNo, suffixCustomerbybtypedate);
        return aresJedis.hgetAll(rKey);
    }

    // MOBILE_frequency_single_stroke
    //
    public static Map<String, String> getLimitMapInfo(String payType, String amount, Map<String, String> mapCustomerLimit, Map<String, String> commonCustomerLimit,
            Map<String, String> customerTransInfoDate, Map<String, String> customerTransInfoMonth) {
        // String cl1Key = geneRedisMapKey(payType ,OnLinePayConstants.FREQUENCY , OnLinePayConstants.SINGLE_STROKE );
        String cl2Key = geneRedisMapKey(payType, OnLinePayConstants.FREQUENCY, OnLinePayConstants.SINGLE_DAY);
        String cl3Key = geneRedisMapKey(payType, OnLinePayConstants.FREQUENCY, OnLinePayConstants.SINGLE_MONTH);
        String cl4Key = geneRedisMapKey(payType, OnLinePayConstants.AMOUNT, OnLinePayConstants.SINGLE_STROKE);
        String cl5Key = geneRedisMapKey(payType, OnLinePayConstants.AMOUNT, OnLinePayConstants.SINGLE_DAY);
        String cl6Key = geneRedisMapKey(payType, OnLinePayConstants.AMOUNT, OnLinePayConstants.SINGLE_MONTH);

        // String cl1LimitValue = getLimitValue(cl1Key,mapCustomerLimit , commonCustomerLimit);
        String cl2LimitValue = getLimitValue(cl2Key, mapCustomerLimit, commonCustomerLimit);
        String cl3LimitValue = getLimitValue(cl3Key, mapCustomerLimit, commonCustomerLimit);
        String cl4LimitValue = getLimitValue(cl4Key, mapCustomerLimit, commonCustomerLimit);
        String cl5LimitValue = getLimitValue(cl5Key, mapCustomerLimit, commonCustomerLimit);
        String cl6LimitValue = getLimitValue(cl6Key, mapCustomerLimit, commonCustomerLimit);

        String cl2CompareValue = getFREQUENCYValue(customerTransInfoDate, payType, OnLinePayConstants.SUFFIX_COUNT);
        String cl3CompareValue = getFREQUENCYValue(customerTransInfoMonth, payType, OnLinePayConstants.SUFFIX_COUNT);
        String cl4CompareValue = amount;
        String cl5CompareValue = getAmountValue(customerTransInfoDate, payType, OnLinePayConstants.SUFFIX_SUCCESAMOUNT, amount);
        String cl6CompareValue = getAmountValue(customerTransInfoMonth, payType, OnLinePayConstants.SUFFIX_SUCCESAMOUNT, amount);

        Map<String, String> mk = new HashMap<String, String>();
        mk.put("cl2LimitValue", cl2LimitValue);
        mk.put("cl3LimitValue", cl3LimitValue);
        mk.put("cl4LimitValue", cl4LimitValue);
        mk.put("cl5LimitValue", cl5LimitValue);
        mk.put("cl6LimitValue", cl6LimitValue);

        mk.put("cl2CompareValue", cl2CompareValue);
        mk.put("cl3CompareValue", cl3CompareValue);
        mk.put("cl4CompareValue", cl4CompareValue);
        mk.put("cl5CompareValue", cl5CompareValue);
        mk.put("cl6CompareValue", cl6CompareValue);

        return mk;
    }

    private static String getAmountValue(Map<String, String> customerTransInfoDate, String payType, String suffixCount, String amount) {
        String _v = customerTransInfoDate.get(payType + suffixCount);
        if (_v != null && !"".equals(_v)) {
            double d1 = Double.valueOf(_v);
            double d2 = Double.valueOf(amount);
            return get2point(d1 + d2);
        }

        return get2point(Double.valueOf(amount));
    }

    private static String getFREQUENCYValue(Map<String, String> customerTransInfoDate, String payType, String suffixCount) {
        String _v = customerTransInfoDate.get(payType + suffixCount);
        if (_v != null && !"".equals(_v)) {
            int i = Integer.parseInt(_v);
            return String.valueOf((i + i));
        }

        return "1";
    }

    private static String getLimitValue(String cl1Key, Map<String, String> mapCustomerLimit, Map<String, String> commonCustomerLimit) {

        if (mapCustomerLimit != null && mapCustomerLimit.size() > 0) {
            String _v = mapCustomerLimit.get(cl1Key);
            if (_v != null && !"".equals(_v)) {
                return _v;
            }
        }

        if (commonCustomerLimit != null && commonCustomerLimit.size() > 0) {
            String _v = commonCustomerLimit.get(cl1Key);
            if (_v != null && !"".equals(_v)) {
                return _v;
            }
        }
        return nodata;
    }

    public static Map<String, String> getTransInfoCP(String dateFormat, String customerNo, String pan, String suffixCpbytypedate, RedisUtil aresJedis) {
        String rKey = geneKeyBy3Params(dateFormat, customerNo, pan, suffixCpbytypedate);
        return aresJedis.hgetAll(rKey);

    }

}
