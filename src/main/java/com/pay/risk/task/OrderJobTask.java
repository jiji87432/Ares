package com.pay.risk.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pay.risk.Constant;
import com.pay.risk.constans.ONLineUtil;
import com.pay.risk.service.OlOrderService;
import com.pay.risk.service.OlPaymentService;
import com.pay.risk.service.OrderService;
import com.pay.risk.service.PaymentService;
import com.pay.risk.thread.OlOrderHisCacheThread;
import com.pay.risk.thread.OlOrderSaveThread;
import com.riskrule.bean.RuleObj;
import com.riskutil.redis.RedisUtil;

@Component("orderJobTask")
public class OrderJobTask {

    private static final Logger logger = Logger.getLogger(OrderJobTask.class);

    @Resource
    private OrderService orderService;

    @Resource
    private PaymentService paymentService;

    @Resource
    private OlOrderService olOrderService;

    @Resource
    private OlPaymentService olPaymentService;

    public OlPaymentService getOlPaymentService() {
        return olPaymentService;
    }

    public void setOlPaymentService(OlPaymentService olPaymentService) {
        this.olPaymentService = olPaymentService;
    }

    public OlOrderService getOlOrderService() {
        return olOrderService;
    }

    public void setOlOrderService(OlOrderService olOrderService) {
        this.olOrderService = olOrderService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void exeution() throws IOException {

        double a = 0;
        int i = 0;

        // FileReader reader = new FileReader("D:\\Work\\haiyang.wang\\json1.txt");

        // BufferedReader br = new BufferedReader(new FileReader(OrderJobTask.class.getClassLoader().getResource("json1.txt")));
        // new BufferedReader(new InputStreamReader(DroolUtil.class.getClassLoader().getResourceAsStream(ruleFilePath)))

        // BufferedReader br = new BufferedReader(new File(OrderJobTask.class.getClassLoader().getResourceAsStream("")));

        BufferedReader br = new BufferedReader(new InputStreamReader(OrderJobTask.class.getClassLoader().getResourceAsStream("json1.txt")));
        String s = null;

        while ((s = br.readLine()) != null) {
            i++;
            long m = new Date().getTime();
            logger.info(s);

            RuleObj obj = this.orderService.parseStr2RuleObj(s);

            this.orderService.setCalcInfo(obj);

            // 开始计算

            List<String> list = obj.getRules();
            JSONObject json = this.orderService.anaReslut(list, obj);
            Map<String, Object> m1 = obj.getRuleDetail();
            Map<String, String> map = (Map<String, String>) m1.get("redisInfo");

            if (json.get("ruleDetail") != null) {
                map.put("rule_detail", json.get("ruleDetail").toString());
            }

            map.put("rule_result", json.get("result").toString());

            new OlOrderSaveThread(olOrderService, map).start();

            // this.orderService.setData2Cache(obj);

            new OlOrderHisCacheThread(orderService, obj).start();
            if (i % 500 == 0) logger.info(i);
            a += (new Date().getTime() - m);

            logger.info(new Date().getTime() - m);
        }

        br.close();

        logger.info(a / i);

    }

    public void exeutionPayment() throws IOException {

        double a = 0;
        int i = 0;

        BufferedReader br = new BufferedReader(new InputStreamReader(OrderJobTask.class.getClassLoader().getResourceAsStream("json2.txt")));
        String s = null;

        while ((s = br.readLine()) != null) {
            i++;
            long m = new Date().getTime();
            RuleObj obj = this.paymentService.parseStr2RuleObj(s);
            this.paymentService.setCalcInfo(obj);

            List<String> list = obj.getRules();
            JSONObject json = this.orderService.anaReslut(list, obj);
            Map<String, Object> m1 = obj.getRuleDetail();
            Map<String, String> map = (Map<String, String>) m1.get("redisInfo");

            if (json.get("ruleDetail") != null) {
                map.put("rule_result", json.get("ruleDetail").toString());
            }

            map.put("final_result", json.get("result").toString());
            // new OlPaymentHisCacheThread(paymentService, obj).start();
            this.paymentService.setData2Cache(obj);
            // new OlPaymentSaveThread(olPaymentService, map).start();;

            Map<String, Object> mk = new HashMap<String, Object>();
            Set<String> set = map.keySet();
            for (String ss : set) {
                String _v = map.get(ss);
                if (ss != null && "create_time".equals(ss)) {
                    logger.info(_v);
                    mk.put("payment_createtime", ONLineUtil.returnDateFull(_v));
                    logger.info(ONLineUtil.returnDateFull(_v));
                } else if (ss != null && "amount".equals(ss)) {

                    mk.put("amount", Double.parseDouble(_v));

                } else {
                    mk.put(ss, _v);
                }
            }

            this.olPaymentService.saveOlPayment(mk);

            if (i % 500 == 0) logger.info(i);
            a += (new Date().getTime() - m);
            logger.info(new Date().getTime() - m);
        }

        br.close();

        logger.info(a / i);

    }

    public static void main(String[] args) {

        RedisUtil jedis = new RedisUtil(Constant.AresJedis);
        try {
            logger.info(jedis.smembers("ON_PAYMENT_20150711")); // PREFIX_PAYEMNT_LIST
            logger.info(jedis.smembers("ON_PAYMENT_20150711").size());
            logger.info(jedis.smembers("8614208845_20150711_CUSTOME_RPAYMENT_RELATION"));
            logger.info(jedis.smembers("8614208845_20150711_CUSTOME_RPAYMENT_RELATION").size());
            logger.info(jedis.smembers("8612288806_20150711_CUSTOME_RPAYMENT_RELATION"));
            logger.info(jedis.smembers("8612288806_20150711_CUSTOME_RPAYMENT_RELATION").size());

            logger.info(jedis.smembers("8615255777_20150711_CUSTOME_RPAYMENT_RELATION"));
            logger.info(jedis.smembers("8615255777_20150711_CUSTOME_RPAYMENT_RELATION").size());

            logger.info(jedis.smembers("8615245738_20150711_CUSTOME_RPAYMENT_RELATION"));
            logger.info(jedis.smembers("8615245738_20150711_CUSTOME_RPAYMENT_RELATION").size());

            for (String s : jedis.smembers("ON_PAYMENT_20150711")) {
                logger.info(jedis.hgetAll(s));
            }

            logger.info("-------------");
            logger.info(jedis.smembers("TOSA20150711012653_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711012210_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711012184_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711012027_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711011991_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711011944_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711011820_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711008626_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711008608_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711008530_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001606_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001599_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001596_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001594_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001592_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001589_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001585_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001583_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001578_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001577_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001575_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001572_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001568_20150711_ORERDATE_RELATION"));
            logger.info(jedis.smembers("TOSA20150711001567_20150711_ORERDATE_RELATION"));

            logger.info("4367485107105886_20150711_CARDDATE_RELATION*****************");

            logger.info(jedis.smembers("4367485107105886_20150711_CARDDATE_RELATION"));
            logger.info(jedis.smembers("4367485106568852_20150711_CARDDATE_RELATION"));
            logger.info(jedis.smembers("4367485106554266_20150711_CARDDATE_RELATION"));
            logger.info(jedis.smembers("4367485107140735_20150711_CARDDATE_RELATION"));
            logger.info(jedis.smembers("4033920016511737_20150711_CARDDATE_RELATION"));
            logger.info(jedis.smembers("6282680002873169_20150711_CARDDATE_RELATION"));
            logger.info(jedis.smembers("4041170044807927_20150711_CARDDATE_RELATION"));
            logger.info(jedis.smembers("6226020121123106_20150711_CARDDATE_RELATION"));
            logger.info(jedis.smembers("5201080010537577_20150711_CARDDATE_RELATION"));
            logger.info(jedis.smembers("4367455164391687_20150711_CARDDATE_RELATION"));
            logger.info("_PHONEDATE_RELATIONCARDDATE_RELATION*****************");
            logger.info(jedis.smembers("13008910373_20150711_PHONEDATE_RELATION"));
            logger.info(jedis.smembers("13075737818_20150711_PHONEDATE_RELATION"));
            logger.info(jedis.smembers("18858619358_20150711_PHONEDATE_RELATION"));
            logger.info(jedis.smembers("15720980055_20150711_PHONEDATE_RELATION"));
            logger.info(jedis.smembers("13552930597_20150711_PHONEDATE_RELATION"));
            logger.info(jedis.smembers("13568287653_20150711_PHONEDATE_RELATION"));
            logger.info(jedis.smembers("13775352781_20150711_PHONEDATE_RELATION"));
            logger.info(jedis.smembers("13963581037_20150711_PHONEDATE_RELATION"));
            logger.info(jedis.smembers("13866115436_20150711_PHONEDATE_RELATION"));
            logger.info(jedis.smembers("15228130706_20150711_PHONEDATE_RELATION"));
            logger.info("IDCARDNODATE_RELATION");
            logger.info(jedis.smembers("420325198303133619_20150711_IDCARDNODATE_RELATION"));
            logger.info(jedis.smembers("330327197302060231_20150711_IDCARDNODATE_RELATION"));
            logger.info(jedis.smembers("332621196911277896_20150711_IDCARDNODATE_RELATION"));
            logger.info(jedis.smembers("360429197810051271_20150711_IDCARDNODATE_RELATION"));
            logger.info(jedis.smembers("410928197911106038_20150711_IDCARDNODATE_RELATION"));
            logger.info(jedis.smembers("513335198108240029_20150711_IDCARDNODATE_RELATION"));
            logger.info(jedis.smembers("321121198011112526_20150711_IDCARDNODATE_RELATION"));
            logger.info(jedis.smembers("372524196804040018_20150711_IDCARDNODATE_RELATION"));
            logger.info(jedis.smembers("340122197310132433_20150711_IDCARDNODATE_RELATION"));
            logger.info(jedis.smembers("511302198410134914_20150711_IDCARDNODATE_RELATION"));

            logger.info("_payment_his");
            logger.info(jedis.hgetAll("8614208845_PAYMENT_HIS"));
            logger.info(jedis.hgetAll("8612288806_PAYMENT_HIS"));
            logger.info(jedis.hgetAll("8615255777_PAYMENT_HIS"));
            logger.info(jedis.hgetAll("8615245738_PAYMENT_HIS"));

            logger.info("_cardtrans_his");

            logger.info(jedis.hgetAll("4367485107105886_CARDTRANS_HIS"));
            logger.info(jedis.hgetAll("4367485106568852_CARDTRANS_HIS"));
            logger.info(jedis.hgetAll("4367485106554266_CARDTRANS_HIS"));
            logger.info(jedis.hgetAll("4367485107140735_CARDTRANS_HIS"));
            logger.info(jedis.hgetAll("4033920016511737_CARDTRANS_HIS"));
            logger.info(jedis.hgetAll("6282680002873169_CARDTRANS_HIS"));
            logger.info(jedis.hgetAll("4041170044807927_CARDTRANS_HIS"));
            logger.info(jedis.hgetAll("6226020121123106_CARDTRANS_HIS"));
            logger.info(jedis.hgetAll("5201080010537577_CARDTRANS_HIS"));
            logger.info(jedis.hgetAll("4367455164391687_CARDTRANS_HIS"));

            logger.info("_Phonetrans_his");
            logger.info(jedis.hgetAll("13008910373_PHONETRANS_HIS"));
            logger.info(jedis.hgetAll("13075737818_PHONETRANS_HIS"));
            logger.info(jedis.hgetAll("18858619358_PHONETRANS_HIS"));
            logger.info(jedis.hgetAll("15720980055_PHONETRANS_HIS"));
            logger.info(jedis.hgetAll("13552930597_PHONETRANS_HIS"));
            logger.info(jedis.hgetAll("13568287653_PHONETRANS_HIS"));
            logger.info(jedis.hgetAll("13775352781_PHONETRANS_HIS"));
            logger.info(jedis.hgetAll("13963581037_PHONETRANS_HIS"));
            logger.info(jedis.hgetAll("13866115436_PHONETRANS_HIS"));
            logger.info(jedis.hgetAll("15228130706_PHONETRANS_HIS"));

            logger.info("idnos_his");

            logger.info(jedis.hgetAll("420325198303133619_IDCARDNOTRANS_HIS"));
            logger.info(jedis.hgetAll("330327197302060231_IDCARDNOTRANS_HIS"));
            logger.info(jedis.hgetAll("332621196911277896_IDCARDNOTRANS_HIS"));
            logger.info(jedis.hgetAll("360429197810051271_IDCARDNOTRANS_HIS"));
            logger.info(jedis.hgetAll("410928197911106038_IDCARDNOTRANS_HIS"));
            logger.info(jedis.hgetAll("513335198108240029_IDCARDNOTRANS_HIS"));
            logger.info(jedis.hgetAll("321121198011112526_IDCARDNOTRANS_HIS"));
            logger.info(jedis.hgetAll("372524196804040018_IDCARDNOTRANS_HIS"));
            logger.info(jedis.hgetAll("340122197310132433_IDCARDNOTRANS_HIS"));
            logger.info(jedis.hgetAll("511302198410134914_IDCARDNOTRANS_HIS"));

            /*
             * logger.info(jedis.smembers("ON_ORDER_20150711"));
             * logger.info(jedis.smembers("ON_ORDER_20150711").size());
             * Map<String , String> m2 = jedis.hgetAll("8615255777_ORDER_HIS");
             * logger.info(m2);
             * logger.info(jedis.smembers("8615255777_20150711_CUSTOMER_ORDERDATE"));
             * logger.info(jedis.smembers("8615255777_20150711_CUSTOMER_ORDERCOUNT"));
             */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }

    }
}
