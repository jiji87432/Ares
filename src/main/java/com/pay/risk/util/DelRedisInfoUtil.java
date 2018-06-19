/**
 *
 */
package com.pay.risk.util;

import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;

import com.pay.risk.Constant;
import com.riskutil.redis.RedisUtil;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: DelRedisInfoUtil 此处填写需要参考的类
 * @version 2015年11月12日 下午3:07:07
 * @author xiaohui.wei
 */
public class DelRedisInfoUtil {
    private static final Logger logger = Logger.getLogger(DelRedisInfoUtil.class);

    /**
     * @Description 查找所有符合给定模式 pattern 的 key
     * @see 需要参考的类或方法
     *      delKeyInfo(String matchKey, int frontIndex) {
     */
    public static Set<String> findMatchKeys(RedisUtil jedis, String patternKey) {
        logger.info("Ares msg DelRedisInfoUtil findMatchKeys method start...");

        try {
            if (null != patternKey && !"".equals(patternKey)) {

                /* 匹配模式找到所有符合的key */
                Set<String> keys = jedis.keys(patternKey);

                try {
                    logger.info("Ares msg DelRedisInfoUtil findMatchKeys 匹配所有key的长度：" + keys.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return keys;
            }

        } catch (Exception e) {
            logger.info("Ares err DelRedisInfoUtil findMatchKeys method err msg" + e.getMessage());
            e.printStackTrace();
        }
        logger.info("Ares msg DelRedisInfoUtil findMatchKeys method end...");
        return null;
    }

    /**
     * @Description String类型的字符串分割成数组
     * @param key 字符串
     * @param separator 分隔符
     * @return
     * @see 需要参考的类或方法
     */
    public static String[] splitKey(String str, String separator) {
        if (null != str) {
            String[] strArr = str.split(separator);
            return strArr;
        }
        return null;
    }

    /**
     * @Description 删除所有匹配符合条件key的信息
     * @param patternKey
     * @param frontIndex
     * @see 需要参考的类或方法
     */
    public static void delKeyInfo(String patternKey, int frontIndex) {
        long x = new Date().getTime();
        logger.info(Constant.AresMsg + " DelRedisInfoUtil delKeyInfo method start...匹配的patternKey" + patternKey);
        RedisUtil jedis = new RedisUtil(Constant.MQJedis);
        try {
            /* 查找所有符合给定模式 pattern 的 key */
            Set<String> keys = findMatchKeys(jedis, patternKey);
            if (null != keys && keys.size() > 0) {
                int i = 0;
                for (String key : keys) {
                    String[] keyArr = splitKey(key, "_");
                    if (null != keyArr && keyArr.length > 0) {

                        /* 日期不是今天的要删除 */
                        // logger.info("Ares msg DelRedisInfoUtil delKeyInfo method key:" + key + " 是否要删除：" + DateUtils.compareNoToday(keyArr[frontIndex]));
                        boolean dateBool = DateUtils.compareNoToday(keyArr[frontIndex]);
                        if (dateBool) {
                            jedis.del(key);
                            i++;
                            if (i % 10000 == 0) {
                                logger.info("========当前删除rendis中的数据为======" + i + "条========");
                                logger.info(Constant.AresMsg + " DelRedisInfoUtil delKeyInfo method key:" + key + " 是否要删除：" + dateBool);
                            }
                        }
                    }
                }
                logger.info("=======总共删除rendis中的数据为======" + i + "条========");
            }
        } catch (Exception e) {
            logger.info(Constant.AresErr + " DelRedisInfoUtil findMatchKeys method err msg" + e.getMessage());
        } finally {
            jedis.close();
        }
        logger.info(Constant.AresMsg + " DelRedisInfoUtil delKeyInfo method end...匹配key" + patternKey + "用时：" + (new Date().getTime() - x) + "ms");
    }

}
