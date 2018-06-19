/**
 *
 */
package com.pay.risk.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pay.risk.Constant;
import com.pay.risk.bean.PaymentDelBean;
import com.pay.risk.service.PaymentDelService;
import com.pay.risk.util.PaymentDelUtil;
import com.riskutil.redis.RedisUtil;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: PaymentDelServiceImpl 此处填写需要参考的类
 * @version 2015年11月4日 下午1:50:35
 * @author xiaohui.wei
 */
@Service("paymentDelService")
public class PaymentDelServiceImpl implements PaymentDelService {
    private static final Logger logger = Logger.getLogger(PaymentDelServiceImpl.class);

    /**
     * @Description 删除redis中关于交易的信息
     *              保存信息用set 其中set集合的value值是另一个set集合的key
     * @see 需要参考的类或方法
     */
    public void delPaymentSet() {
        Long x = new Date().getTime();
        logger.info("PaymentDelServiceImpl delPaymentSet method start...");
        RedisUtil jedis = new RedisUtil(Constant.AresJedis);
        try {
            String delDateKey = jedis.get("delDateKey");

            List<PaymentDelBean> lists = new ArrayList<PaymentDelBean>();
            if (delDateKey == null) {
                lists.add(new PaymentDelBean("ON_PAYMENT_*", 1, 17));
                lists.add(new PaymentDelBean("*_CUSTOME_RPAYMENT_RELATION", 4, 17));
                lists.add(new PaymentDelBean("*_ORERDATE_RELATION", 3, 17));
                lists.add(new PaymentDelBean("*_CARDDATE_RELATION", 3, 17));
                lists.add(new PaymentDelBean("*_CARDORDER_RELATAION", 3, 17));

                lists.add(new PaymentDelBean("*_CARDTYPEORDERDATE_RELATAION", 3, 17));
                lists.add(new PaymentDelBean("*_IPDATE_RELATION", 3, 17));
                lists.add(new PaymentDelBean("*_IPORDER_RELATAION", 3, 17));
                lists.add(new PaymentDelBean("*_IDCARDNODATE_RELATION", 3, 17));
                lists.add(new PaymentDelBean("*_IDCARDNOORDER_RELATAION", 3, 17));

                lists.add(new PaymentDelBean("*_PHONEDATE_RELATION", 3, 17));
                lists.add(new PaymentDelBean("*_PNONEORDER_RELATAION", 3, 17));
            } else {
                lists.add(new PaymentDelBean("ON_PAYMENT_*" + delDateKey + "*", 1, 17));
                lists.add(new PaymentDelBean("*" + delDateKey + "*_CUSTOME_RPAYMENT_RELATION", 4, 17));
                lists.add(new PaymentDelBean("*" + delDateKey + "*_ORERDATE_RELATION", 3, 17));
                lists.add(new PaymentDelBean("*" + delDateKey + "*_CARDDATE_RELATION", 3, 17));
                lists.add(new PaymentDelBean("*" + delDateKey + "*_CARDORDER_RELATAION", 3, 17));

                lists.add(new PaymentDelBean("*" + delDateKey + "*_CARDTYPEORDERDATE_RELATAION", 3, 17));
                lists.add(new PaymentDelBean("*" + delDateKey + "*_IPDATE_RELATION", 3, 17));
                lists.add(new PaymentDelBean("*" + delDateKey + "*_IPORDER_RELATAION", 3, 17));
                lists.add(new PaymentDelBean("*" + delDateKey + "*_IDCARDNODATE_RELATION", 3, 17));
                lists.add(new PaymentDelBean("*" + delDateKey + "*_IDCARDNOORDER_RELATAION", 3, 17));

                lists.add(new PaymentDelBean("*" + delDateKey + "*_PHONEDATE_RELATION", 3, 17));
                lists.add(new PaymentDelBean("*" + delDateKey + "*_PNONEORDER_RELATAION", 3, 17));
            }

            for (PaymentDelBean obj : lists) {
                PaymentDelUtil.delPaymentCommon(obj.getMatchesStr(), obj.getOffset(), obj.getDateSize());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        logger.info("PaymentDelServiceImpl delPaymentSet method end...use：" + (new Date().getTime() - x) + "ms");
    }

    /**
     * @Description 删除redis中关于交易的信息 保存信息用hash
     * @see 需要参考的类或方法
     */
    public void delPaymentHash() {
        Long x = new Date().getTime();
        logger.info("PaymentDelServiceImpl delPaymentHash method start...");
        RedisUtil jedis = new RedisUtil(Constant.AresJedis);
        try {

            List<PaymentDelBean> lists = new ArrayList<PaymentDelBean>();
            lists.add(new PaymentDelBean("*_PAYMENT_HIS", 0, 10));
            lists.add(new PaymentDelBean("*_CARDTRANS_HIS", 0, 10));
            lists.add(new PaymentDelBean("*_CUSTOMERCARDTYPE_HIS", 0, 10));
            lists.add(new PaymentDelBean("*_IPTRANS_HIS", 0, 10));
            lists.add(new PaymentDelBean("*_IDCARDNOTRANS_HIS", 0, 10));
            lists.add(new PaymentDelBean("*_PHONETRANS_HIS", 0, 10));

            for (PaymentDelBean obj : lists) {
                PaymentDelUtil.delPaymentCommon_hash(obj.getMatchesStr(), obj.getOffset(), obj.getDateSize());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }

        logger.info("PaymentDelServiceImpl delPaymentHash method end...use：" + (new Date().getTime() - x) + "ms");

    }

    /*
     * map结构 判断日期是redis中的key 日期精确到年月
     */
    @Override
    public void delRedisBeforeMonth_Hash() {
        Long x = new Date().getTime();
        logger.info("PaymentDelServiceImpl delRedisBeforeMonth_Hash method start...");
        try {
            List<PaymentDelBean> lists = new ArrayList<PaymentDelBean>();
            // 留2月 这个月 上个月
            lists.add(new PaymentDelBean("*_PAN_MONTH_BYBTYPE", 0, 2));
            lists.add(new PaymentDelBean("*_CUSTOMER_MONTH_BYBTYPE", 0, 2));
            lists.add(new PaymentDelBean("*_CP_MONTH_BYBTYPE", 0, 2));

            for (PaymentDelBean obj : lists) {
                PaymentDelUtil.delCommonMonth_hash(obj.getMatchesStr(), obj.getOffset(), obj.getDateSize());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("PaymentDelServiceImpl delRedisBeforeMonth_Hash method end...use：" + (new Date().getTime() - x) + "ms");

    }

    /**
     * map结构 判断日期是redis中的key 日期精确到年月日
     */
    @Override
    public void delRedisBeforeDay_Hash() {
        Long x = new Date().getTime();
        logger.info("PaymentDelServiceImpl delRedisBeforeDay_Hash method start...");
        try {
            List<PaymentDelBean> lists = new ArrayList<PaymentDelBean>();
            // 留三天 今天 昨天 后天
            lists.add(new PaymentDelBean("*_PAN_DATE_BYBTYPE", 0, 2));
            lists.add(new PaymentDelBean("*_CUSTOMER_DATE_BYBTYPE", 0, 2));
            lists.add(new PaymentDelBean("*_CP_DATE_BYBTYPE", 0, 2));
            for (PaymentDelBean obj : lists) {
                PaymentDelUtil.delRedisBeforeDay_Hash(obj.getMatchesStr(), obj.getOffset(), obj.getDateSize());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("PaymentDelServiceImpl delRedisBeforeDay_Hash method end...use：" + (new Date().getTime() - x) + "ms");

    }

}
