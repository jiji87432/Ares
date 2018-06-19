package com.pay.risk.dubbo;

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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.pay.ares.remote.service.ONLineRiskServiceFacade;
import com.pay.risk.Constant;
import com.pay.risk.constans.ONLineUtil;
import com.pay.risk.constans.OnLinePayConstants;
import com.pay.risk.remote.bean.ScanCodeLimit;
import com.pay.risk.remote.service.SmLimitFacade;
import com.pay.risk.service.OlOrderService;
import com.pay.risk.service.OlPaymentService;
import com.pay.risk.service.OrderService;
import com.pay.risk.service.PaymentService;
import com.pay.risk.thread.OlOrderHisCacheThread;
import com.pay.risk.thread.OlPaymentHisCacheThread;
import com.pay.risk.thread.OlPaymentSaveThread;
import com.pay.risk.util.DateUtils;
import com.pay.risk.util.StringUtil;
import com.riskrule.bean.RuleObj;
import com.riskrule.runmvel.RunRule;
import com.riskutil.redis.RedisUtil;

public class ONLineRiskServiceFacadeImpl implements ONLineRiskServiceFacade {
	private static final Logger logger = Logger.getLogger(ONLineRiskServiceFacadeImpl.class);

	private static final String mCalcCodeM3 = "M00003_t0";

	private static final String mCalcCodeM4 = "M00004_t0";

	private String Y = Constant.Y;
	private String myCountKey = Constant.REDIS_TIME_LIMIT_COUNT_KEY;
	private String myRuleKey = Constant.REDIS_TIME_LIMIT_RULE_KEY;
	private String myCommonFlag = Constant.REDIS_TIME_LIMIT_EFFECTIVE_FLAG;

	private static String NORISKEXTORGCODE = "NORISKEXTORGCODE";

	@Resource
	private OrderService orderService;

	@Resource
	private PaymentService paymentService;

	@Resource
	private OlOrderService olOrderService;

	@Resource
	private OlPaymentService olPaymentService;

	@Resource
	private SmLimitFacade smLimitFacade;

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public OlOrderService getOlOrderService() {
		return olOrderService;
	}

	public void setOlOrderService(OlOrderService olOrderService) {
		this.olOrderService = olOrderService;
	}

	public OlPaymentService getOlPaymentService() {
		return olPaymentService;
	}

	public void setOlPaymentService(OlPaymentService olPaymentService) {
		this.olPaymentService = olPaymentService;
	}

	@Override
	public String getOrderResult(String s) {

		logger.info("order str : " + s);

		try {
			long x = new Date().getTime();
			long j = new Date().getTime();
			Map<String, String> mp = ONLineUtil.generateCalcInfo(mCalcCodeM3);

			RuleObj obj = this.orderService.parseStr2RuleObj(s);

			Map<String, Object> m1 = obj.getRuleDetail();

			// 将trans_type、rz_status以及当日累计金额放入到m1中
			JSONObject objs = JSONObject.fromObject(s);
			String transType = null;
			String rzStatus = null;
			Double amount = 0D;

			if (objs.get("trans_type") != null) {
				transType = String.valueOf(objs.get("trans_type"));
			}
			if (objs.get("rz_status") != null) {
				rzStatus = String.valueOf(objs.get("rz_status"));
			}
			if (m1.get("amount") != null) {
				amount = (Double) m1.get("amount");
			}
			// 当日累计金额的Key值
			String sumAmountKey = getSumAmountKey(m1);
			// 当日累计金额数目
			String sumAmount = null;
			RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
			try {
				try {
					String customerOrgNo = m1.get("customerOrgNo") == null ? null : String.valueOf(m1.get("customerOrgNo"));
					if (StringUtil.isStrNotEmpty(customerOrgNo) && aresJedis.sismember(NORISKEXTORGCODE, customerOrgNo)) {
						logger.info("this is extorg SM");
						JSONObject json = new JSONObject();
						json.put("result", "PASS");
						return json.toString();
					}
				} catch (Exception e) {
					logger.error("this is extorg SM has error");
				}

				String logFlag = aresJedis.get("log_flag");
				boolean bf = logFlag == null || "N".equals(logFlag) ? false : true;
				sumAmount = aresJedis.get(sumAmountKey);
				sumAmount = sumAmount == null ? "0D" : sumAmount;

				if (bf) logger.info(sumAmount);
				if (transType != null && rzStatus != null) {
					amount = amount + Double.parseDouble(sumAmount);
					m1.put("trans_type", transType);
					m1.put("rz_status", rzStatus);
					m1.put("sum_wx", amount);
					if (bf) logger.info(m1);
				}
				// TODO 向ruleObj的map中添加数据
				try {
					setGzjyxehaPrefix(m1, aresJedis);
				} catch (Exception e) {
					logger.error("setGzjyxehaPrefix 方法出现异常*_**_**_*:" + e);
				}

				// 特殊时间段限制交易
				getSumCommonCount(m1, aresJedis);

				// 交易地区限制的汇总
				try {
					orderService.setAreaCodeInfo(m1, aresJedis);
				} catch (Exception e) {
					m1.put(OnLinePayConstants.AREA_CODE_COUNT, "-1");
					logger.error("setAreaCodeInfo 方法出现异常，将area_code_count 设置为 -1 *_**_**_*:" + e);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				aresJedis.close();
			}

			// 得到订单id
			String orderId = (String) m1.get("order_id");

			// 防止重复提交,如果分析过该规则跳过,将上一次分析结果返回
			String preResult = ONLineUtil.getFieldValue(orderId, "al_result");
			if (preResult != null && !"".equals(preResult)) {
				logger.info("order ana Exists return value :" + preResult + " orderId " + orderId);
				return preResult;
			}

			this.orderService.setCalcInfo(obj);
			logger.info("order gene mode cost : " + (new Date().getTime() - j));
			j = new Date().getTime();
			// logger.info(" cale m1 " + m1);
			// logger.info(" cale obj " + obj.getRuleDetail());
			Map<String, String> map = (Map<String, String>) m1.get("redisInfo");
			// 开始计算
			RunRule.exeRuleEngine(mp, obj);
			logger.info("order calc cost : " + (new Date().getTime() - j));
			logger.info("result " + obj.getRules());
			JSONObject json = setRuleInfo(obj, map);

			// 计算结束
			String createTime = String.valueOf(m1.get("create_time"));

			m1.put("create_time", createTime);

			map.put("create_time", createTime);

			Map<String, Object> mn = new HashMap<String, Object>();
			Set<String> set = map.keySet();
			for (String ss : set) {
				String vv = map.get(ss);
				if (ss != null && "create_time".equals(ss)) {

					mn.put("order_createTime", ONLineUtil.returnDateFull(vv));

				} else if (ss != null && "amount".equals(ss)) {

					mn.put("amount", Double.parseDouble(vv));
				} else {
					mn.put(ss, vv);
				}
			}
			String alResult = json.toString();
			if (alResult != null) map.put("al_result", alResult);
			Long xx = new Date().getTime() - x;
			new OlOrderHisCacheThread(orderService, obj).start();;
			// this.orderService.setData2Cache(obj);
			mn.put("cost", xx.intValue());
			try {
				this.olOrderService.saveOlOrder(mn, obj.getRules(), orderId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			logger.info("order cost :" + xx);
			logger.info(json.toString());
			return json.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private JSONObject setRuleInfo(RuleObj obj, Map<String, String> map) {
		List<String> list = obj.getRules();
		JSONObject json = this.orderService.anaReslut(list, obj);

		if (json.get("ruleDetail") != null) {
			map.put("rule_detail", json.get("ruleDetail").toString());
		}

		map.put("rule_result", json.get("result").toString());

		return json;
	}

	@Override
	public String getPaymentResult(String s) {
		logger.info("payment str :" + s);
		long x = new Date().getTime();
		long j = new Date().getTime();
		Map<String, String> mp = ONLineUtil.generateCalcInfo(mCalcCodeM4);
		RuleObj obj = this.paymentService.parseStr2RuleObj(s);

		Map<String, Object> m1 = obj.getRuleDetail();

		String paymentId = (String) m1.get("payment_id");

		// 防止重复提交,如果分析过该规则跳过,将上一次分析结果返回

		String preResult = ONLineUtil.getFieldValue(paymentId, "al_result");
		if (!StringUtil.isEmptyWithTrim(preResult)) {
			logger.info("payment ana Exists return value :" + preResult + " paymentId : " + paymentId);
			return preResult;
		}

		String createTime = String.valueOf(m1.get("create_time"));

		m1.put("create_time", createTime);
		Map<String, String> map = (Map<String, String>) m1.get("redisInfo");
		map.put("create_time", createTime);

		this.paymentService.setCalcInfo(obj);
		logger.info("payment gene model cost " + (new Date().getTime() - j));
		j = new Date().getTime();
		RunRule.exeRuleEngine(mp, obj);
		logger.info("payment calc cost " + (new Date().getTime() - j));
		// logger.info(obj.getRuleDetail());

		JSONObject json = setRuleInfo(obj, map);
		Map<String, Object> mn = new HashMap<String, Object>();
		Set<String> set = map.keySet();
		for (String ss : set) {
			String vv = map.get(ss);
			if (ss != null && "create_time".equals(ss)) {

				mn.put("payment_createTime", ONLineUtil.returnDateFull(vv));

			} else if (ss != null && "amount".equals(ss)) {

				mn.put("amount", Double.parseDouble(vv));

			} else {
				mn.put(ss, vv);
			}
		}

		Long xx = new Date().getTime() - x;
		mn.put("cost", xx.intValue());
		String alResult = json.toString();
		if (alResult != null) map.put("al_result", alResult);

		new OlPaymentHisCacheThread(paymentService, obj).start();

		// this.paymentService.setData2Cache(obj);
		try {
			new OlPaymentSaveThread(olPaymentService, mn, obj.getRules(), paymentId).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// this.olPaymentService.saveOlPayment(mn, obj.getRules(), paymentId);

		logger.info("payment cost :　" + (new Date().getTime() - x));
		logger.info(json.toString());

		return json.toString();

	}

	@Override
	public String setOrderResult(String s) {
		try {
			this.orderService.saveOnResult(s);
			this.paymentService.saveOnResult(s);
			RuleObj obj = this.paymentService.parseStr2RuleObj(s);
			Map<String, Object> map = obj.getRuleDetail();

			String result = null;
			if (map.get("result") != null) {
				result = String.valueOf(map.get("result"));
			}
			if ("SUCCESS".equals(result)) {
				// 获取累计交易金额的Key值
				String sumAmount = getSumAmountKey(map);
				// 此次交易金额
				Double amount = 0D;
				if (map.get("amount") != null) {
					amount = (Double) map.get("amount");
				}

				RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
				try {
					String str = aresJedis.get(sumAmount);
					if (str != null) {
						Double sum = Double.parseDouble(str);
						sum = sum + amount;
						aresJedis.set(getSeconds(), sumAmount, sum.toString());
					} else {
						aresJedis.set(getSeconds(), sumAmount, amount.toString());
					}
					// TODO 进行dayamount的汇总
					try {
						setGzjyxehaDayAmountSuffix(map, aresJedis);
					} catch (Exception e) {
						logger.error("setGzjyxehaDayAmountSuffix 方法出现异常*_**_**_*:" + e);
					}
					// 特殊时间段交易笔数统计
					setCommonCountLimit(map, aresJedis);

				} catch (Exception e) {
					logger.error("出现异常:" + e);
				} finally {
					aresJedis.close();
				}
			}

			try {
				this.olOrderService.updateStatusByOrderId(map);
				this.olPaymentService.updateStatusByOrderAndPaymentId(map);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return "SUCESS";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "FAIL";
	}

	public String getSumAmountKey(Map<String, Object> map) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String dd = format.format(date);
		String customerNo = null;
		if (map.get("customer_no") != null) {
			customerNo = String.valueOf(map.get("customer_no"));
		}
		String sumAmountKey = dd + "_" + customerNo + "_" + "amount_wx";
		return sumAmountKey;
	}

	public int getSeconds() {
		Calendar curDate = Calendar.getInstance();
		Calendar tommorowDate = new GregorianCalendar(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate.get(Calendar.DATE) + 1, 0, 0, 0);
		return (int) (tommorowDate.getTimeInMillis() - curDate.getTimeInMillis()) / 1000;
	}

	/**
	 * ==交易成功后，进行当天amont的累加==
	 * (date)_(customer_no)_dayamount_gzjyxeha : {wx_dayamount_gzjyxeha,alipay_dayamount_gzjyxeha}
	 * @Description 一句话描述方法用法
	 * @param map
	 * @see 需要参考的类或方法
	 */
	public void setGzjyxehaDayAmountSuffix(Map<String, Object> map, RedisUtil aresJedis) {
		String logFlag = aresJedis.get("LOG_FLAG_GZJYXEHA");// 这是一个控制日志的标签
		String status = null;
		if (map.get("status") != null) {
			status = String.valueOf(map.get("status"));
		}
		if (Y.equals(logFlag)) logger.info("进入setGzjyxehaDayAmountSuffix方法。 status 是" + status);
		if ("SUCCESS".equals(status)) {

			String date = DateUtils.getTodayStr();
			/** 取出基础数据 */
			Double amount = 0D;
			if (map.get("amount") != null) {
				amount = (Double) map.get("amount");
			}
			String customerNo = null;
			if (map.get("customer_no") != null) {
				customerNo = String.valueOf(map.get("customer_no"));
			}
			String payType = null;
			if (map.get("pay_type") != null) {
				payType = String.valueOf(map.get("pay_type"));
			}
			if (Y.equals(logFlag)) logger.info("进入setGzjyxehaDayAmountSuffix方法。 获取的基础数据与date是" + date + "_" + amount + "_" + customerNo + "_" + payType);

			/** day_amount计算 并在redis中存放 */
			String dayAomuntgzjyxehaKey = date + "_" + customerNo + "_dayamount_gzjyxeha";
			Map<String, String> dayAomuntgzjyxehaMap = null;// 存放商户当天交易金额的map

			// RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
			try {
				dayAomuntgzjyxehaMap = aresJedis.hgetAll(dayAomuntgzjyxehaKey);

				if (Y.equals(logFlag)) logger.info("进入setGzjyxehaDayAmountSuffix方法。从redis中获取的dayAomuntgzjyxeha key 与map " + dayAomuntgzjyxehaKey
						+ dayAomuntgzjyxehaMap);
				if (dayAomuntgzjyxehaMap == null) {// 没有，说明是第一笔交易，new一个

					if (Y.equals(logFlag)) logger.info("进入setGzjyxehaDayAmountSuffix方法。从redis中获取的 map 是空的 new 一个");
					dayAomuntgzjyxehaMap = new HashMap<String, String>();
				}
				// 进行day_amount计算
				// if ("wx".equals(pay_type)) {

				String dayAmount = dayAomuntgzjyxehaMap.get(payType + "_dayamount_gzjyxeha");
				if (Y.equals(logFlag)) logger.info("进入setGzjyxehaDayAmountSuffix方法。paytypes是" + payType
						+ "，在dayAomuntgzjyxehaMap里key 与值是 pay_type+_dayamount_gzjyxeha ==" + dayAmount);
				if (!StringUtil.isEmptyWithTrim(dayAmount)) {// 获取到统计的dayamount,累加
					Double dayAmountDouble = Double.valueOf(dayAmount) + amount;
					dayAmount = String.valueOf(dayAmountDouble);
					if (Y.equals(logFlag)) logger.info("进入setGzjyxehaDayAmountSuffix方法。当笔交易amount是" + amount + "=累加后得到的为" + dayAmount);

				} else {// 没获取到，说明是第一次计算
					dayAmount = String.valueOf(amount);
					if (Y.equals(logFlag)) logger.info("进入setGzjyxehaDayAmountSuffix方法。之前没有，将amount" + amount + "==作为dayamount" + dayAmount);
				}
				dayAomuntgzjyxehaMap.put(payType + "_dayamount_gzjyxeha", dayAmount);

				/*
				 * if ("alipay".equals(pay_type)) {
				 * String alipayDayAmount = dayAomuntgzjyxehaMap.get("alipay_dayamount_gzjyxeha");
				 * logger.info("进入setGzjyxehaDayAmountSuffix方法。paytypes是支付宝，在dayAomuntgzjyxehaMap里key 与值是 alipay_dayamount_gzjyxeha ==" + alipayDayAmount);
				 * if (alipayDayAmount != null && !"".equals(alipayDayAmount)) {// 获取到统计的dayamount,累加
				 * Double alipayDayAmountDouble = Double.valueOf(alipayDayAmount) + amount;
				 * alipayDayAmount = String.valueOf(alipayDayAmountDouble);
				 * logger.info("进入setGzjyxehaDayAmountSuffix方法。当笔交易amount是" + amount + "=累加后得到的为" + alipayDayAmount);
				 * } else {// 没获取到，说明是第一次计算
				 * alipayDayAmount = String.valueOf(amount);
				 * logger.info("进入setGzjyxehaDayAmountSuffix方法。之前没有，将amount" + amount + "==作为dayamount" + alipayDayAmount);
				 * }
				 * dayAomuntgzjyxehaMap.put("alipay_dayamount_gzjyxeha", alipayDayAmount);
				 * }
				 */

				if (Y.equals(logFlag)) logger.info("进入setGzjyxehaDayAmountSuffix方法。最后将dayaomunt存库key 与 map 是" + dayAomuntgzjyxehaKey + "==" + dayAomuntgzjyxehaMap);
				aresJedis.hmset(getSeconds(), dayAomuntgzjyxehaKey, dayAomuntgzjyxehaMap);// 存库

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * ==交易前，进行Map添加==
	 * (date)_(customer_no)_gzjyxeha :{专属4种限额}
	 * c_type + "_" + limit_type + "_" + pay_type + "_day_SM_COMMON_LIMIT":{通用4种限额}
	 * 需要向ruleDetail中：根据类型放入通用与专属限额，和统计的dayamount
	 * @Description 一句话描述方法用法
	 * @param map
	 * @see 需要参考的类或方法
	 */
	public void setGzjyxehaPrefix(Map<String, Object> map, RedisUtil aresJedis) {
		String logFlag = aresJedis.get("LOG_FLAG_GZJYXEHA");// 这是一个控制日志的标签
		String Y = Constant.Y;
		if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法");
		String date = DateUtils.getTodayStr();

		/** 取出基础数据 */
		Double amount = 0D;
		if (map.get("amount") != null) {
			amount = (Double) map.get("amount");
		}
		String customerNo = null;
		if (map.get("customer_no") != null) {
			customerNo = String.valueOf(map.get("customer_no"));
		}
		String payType = null;// 支付类型：tradeType
		if (map.get("pay_type") != null) {
			payType = String.valueOf(map.get("pay_type"));
		}
		String customerType = null;// 商户类型：customerType
		if (map.get("c_type") != null) {
			customerType = String.valueOf(map.get("c_type"));
		}
		String limitLevel = null;// 限制级别：limitLevel,通用扫码专属
		if (map.get("limit_type") != null) {
			limitLevel = String.valueOf(map.get("limit_type"));
		}
		if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。获取的基础数据与date是" + date + "_" + amount + "_" + customerNo + "_" + payType + "_" + customerType + "_"
				+ limitLevel);
		/** 从redis中获取数据。 再根据交易类型，向ruleObj的detail的Map中添加各种数据 */
		// RedisUtil aresJedis = new RedisUtil(Constant.AresJedis);
		try {

			/** day_amount */
			Map<String, String> dayAomuntgzjyxehaMap = aresJedis.hgetAll(date + "_" + customerNo + "_dayamount_gzjyxeha");// 统计的dayamount，当天已交易的总金额
			if (dayAomuntgzjyxehaMap != null && dayAomuntgzjyxehaMap.size() != 0) {

				if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。dayamount的key是" + date + "_" + customerNo + "_dayamount_gzjyxeha==map是"
						+ dayAomuntgzjyxehaMap);
				// 当天已交易的总金额
				String dayAmount = dayAomuntgzjyxehaMap.get(payType + "_dayamount_gzjyxeha");
				if (dayAmount != null && !"".equals(dayAmount)) {// 获取到统计的dayamount,累加
					if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。dayamount是" + dayAmount);
					map.put("day_amount", dayAmount);// 放入map中用于规则判断
				} else {// 没获取到，为0
					if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。dayamount为空。" + dayAmount);
					map.put("day_amount", "0");
				}
			} else {// 没获取到，说明是第一次计算,为0
				if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。_dayamount_gzjyxeha==map为空。");
				map.put("day_amount", "0");
			}

			/** common _day_amount 与 common _single_amount */
			// 通用当日限额，通用单笔限额
			String commonCustomerKey = date + Constant.COMMON_KEY;
			Map<String, String> commonCustomerMap = aresJedis.hgetAll(commonCustomerKey);
			if (commonCustomerMap == null || commonCustomerMap.size() == 0) {
				List<ScanCodeLimit> commonScanCodeData = smLimitFacade.getCommonScanCodeData();
				commonCustomerMap = getCommonMapResult(commonScanCodeData);
				aresJedis.hmset(getSeconds(), commonCustomerKey, commonCustomerMap);
			}
			String commondayAmount = commonCustomerMap.get(customerType + Constant.SYMBOL + limitLevel + Constant.SYMBOL + payType + Constant.LIMIT_DAY
					+ Constant.COMMON_LIMIT);
			String commonsingleAmount = commonCustomerMap.get(customerType + Constant.SYMBOL + limitLevel + Constant.SYMBOL + payType + Constant.LIMIT_SINGLE
					+ Constant.COMMON_LIMIT);
			if (!StringUtil.isEmptyWithTrim(commondayAmount)) {
				map.put("common_day_amount", commondayAmount);
			} else {// 不存在，说明出现问题，nodata
				if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。commondayAmount是空,不该这样，出现问题，ruleObj detail里放 nodata");
				map.put("common_day_amount", Constant.NODATA);
			}
			if (!StringUtil.isEmptyWithTrim(commonsingleAmount)) {
				map.put("common_single_amount", commonsingleAmount);
			} else {// 不存在，说明出现问题，nodata
				if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。commonsingleAmount是空，不该这样，出现问题，ruleObj detail里放 nodata");
				map.put("common_single_amount", Constant.NODATA);
			}

			/** spe_single_amount 与 spe_day_amount */
			// 特殊商户在redis中的key
			String gzjyxehaKey = date + "_" + customerNo + "_gzjyxeha";
			// 获取特殊商户限额的详细信息
			Map<String, String> gzjyxehaMap = new HashMap<String, String>();
			// 特殊扫码商户是否需要从数据库中获取最新的标记
			boolean freshFlag = false;
			// tagKey是一个set集合的key，用于标记商户在redis中的状态是否为最新
			String tagKey = date + Constant.TAG_KEY;
			Boolean myTagResult = aresJedis.sismember(tagKey, customerNo);
			if (!myTagResult) {
				freshFlag = true;
			} else {
				gzjyxehaMap = aresJedis.hgetAll(gzjyxehaKey);// 特殊商户限额map
				if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。特殊商户限额，key 与map是：" + gzjyxehaKey + "---" + gzjyxehaMap);
				if (gzjyxehaMap != null && gzjyxehaMap.size() > 2) {
					for (String val : gzjyxehaMap.values()) {
						if (Constant.NODATA.equals(val)) {
							freshFlag = true;
							break;
						}
					}
				} else {
					freshFlag = true;
				}
			}

			if (freshFlag) {
				List<ScanCodeLimit> myList = smLimitFacade.getSmCustomerLimitDataByCustomerNo(customerNo);
				gzjyxehaMap = getMapResult(myList, logFlag);
				if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。向redis里存入的的特殊商户限额 kye 与 map是" + gzjyxehaKey + "==" + gzjyxehaMap);
				// 更新redis中的数据
				aresJedis.hmset(getSeconds(), gzjyxehaKey, gzjyxehaMap);// 存库
				aresJedis.sadd(getSeconds(), tagKey, customerNo);// 更新
			}

			if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。特殊商户得到的限额map是" + gzjyxehaMap);
			// payType下的限制值
			String specialDayAmount = gzjyxehaMap.get(payType + Constant.LIMIT_DAY);
			String specialSingleAmount = gzjyxehaMap.get(payType + Constant.LIMIT_SINGLE);
			if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。根据交易类型" + payType + Constant.LIMIT_DAY + payType + Constant.LIMIT_SINGLE + "，得到的特殊商户相应限额"
					+ specialDayAmount + "==" + specialSingleAmount);

			if (StringUtil.isEmptyWithTrim(specialDayAmount)) {// 接口中获取不到对应的限额
				specialDayAmount = Constant.NODATA;
			}
			if (StringUtil.isEmptyWithTrim(specialSingleAmount)) {// 接口中获取不到对应的限额
				specialSingleAmount = Constant.NODATA;
			}
			// 特殊商户扫码限制金额
			map.put("spe_day_amount", specialDayAmount);
			map.put("spe_single_amount", specialSingleAmount);

			if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。通过汇总，放入detail里的数据为：day_amount" + map.get("day_amount") + "==common_day_amount =="
					+ map.get("common_day_amount") + "==common_single_amount==" + map.get("common_single_amount") + "==spe_single_amount=="
					+ map.get("spe_single_amount") + "==spe_day_amount==" + map.get("spe_day_amount"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Map<String, String> getMapResult(List<ScanCodeLimit> list, String logFlag) {
		Map<String, String> map = new HashMap<String, String>();
		if (Y.equals(logFlag)) logger.info("进入setGzjyxehaPrefix方法。特殊商户限额没查到统一补nodata ");
		// 旧数据
		map.put(Constant.WECHAT + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.WECHAT + Constant.LIMIT_SINGLE, Constant.NODATA);
		map.put(Constant.ALIPAY + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.ALIPAY + Constant.LIMIT_SINGLE, Constant.NODATA);
		map.put(Constant.UNIONPAY + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.UNIONPAY + Constant.LIMIT_SINGLE, Constant.NODATA);

		// 新数据
		map.put(Constant.WECHAT + Constant.SWEEP_ZS + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.WECHAT + Constant.SWEEP_BS + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.WECHAT + Constant.SWEEP_H5 + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.WECHAT + Constant.SWEEP_ZS + Constant.LIMIT_SINGLE, Constant.NODATA);
		map.put(Constant.WECHAT + Constant.SWEEP_BS + Constant.LIMIT_SINGLE, Constant.NODATA);
		map.put(Constant.WECHAT + Constant.SWEEP_H5 + Constant.LIMIT_SINGLE, Constant.NODATA);
		map.put(Constant.ALIPAY + Constant.SWEEP_ZS + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.ALIPAY + Constant.SWEEP_BS + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.ALIPAY + Constant.SWEEP_H5 + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.ALIPAY + Constant.SWEEP_ZS + Constant.LIMIT_SINGLE, Constant.NODATA);
		map.put(Constant.ALIPAY + Constant.SWEEP_BS + Constant.LIMIT_SINGLE, Constant.NODATA);
		map.put(Constant.ALIPAY + Constant.SWEEP_H5 + Constant.LIMIT_SINGLE, Constant.NODATA);
		map.put(Constant.UNIONPAY + Constant.SWEEP_ZS + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.UNIONPAY + Constant.SWEEP_BS + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.UNIONPAY + Constant.SWEEP_H5 + Constant.LIMIT_DAY, Constant.NODATA);
		map.put(Constant.UNIONPAY + Constant.SWEEP_ZS + Constant.LIMIT_SINGLE, Constant.NODATA);
		map.put(Constant.UNIONPAY + Constant.SWEEP_BS + Constant.LIMIT_SINGLE, Constant.NODATA);
		map.put(Constant.UNIONPAY + Constant.SWEEP_H5 + Constant.LIMIT_SINGLE, Constant.NODATA);
		for (ScanCodeLimit scanCodeLimit : list) {
			String tradeType = scanCodeLimit.getTradeType();
			String limitType = scanCodeLimit.getLimitType();
			String limitValue = scanCodeLimit.getLimitValue();
			if (!StringUtil.isEmptyWithTrim(tradeType) && !StringUtil.isEmptyWithTrim(limitType) && !StringUtil.isEmptyWithTrim(limitValue)) {
				map.put(tradeType + "_" + limitType, limitValue);
			}
		}
		return map;
	}

	public Map<String, String> getCommonMapResult(List<ScanCodeLimit> list) {
		Map<String, String> map = new HashMap<String, String>();
		logger.info("通用扫码商户数据更新！");
		for (ScanCodeLimit scanCodeLimit : list) {
			String customerType = scanCodeLimit.getCustomerType();
			String limitLevel = scanCodeLimit.getLimitLevel();
			String tradeType = scanCodeLimit.getTradeType();
			String limitType = scanCodeLimit.getLimitType();
			String limitValue = scanCodeLimit.getLimitValue();
			logger.info("customer_type:" + customerType + ",limit_level:" + limitLevel + ",trade_type:" + tradeType + ",limit_type:" + limitType + ",limit_value:"
					+ limitValue);
			if (!StringUtil.isEmptyWithTrim(customerType) && !StringUtil.isEmptyWithTrim(limitLevel) && !StringUtil.isEmptyWithTrim(tradeType)
					&& !StringUtil.isEmptyWithTrim(limitType) && !StringUtil.isEmptyWithTrim(limitValue)) {
				String key = customerType + Constant.SYMBOL + limitLevel + Constant.SYMBOL + tradeType + Constant.SYMBOL + limitType + Constant.COMMON_LIMIT;
				map.put(key, limitValue);
			}
		}
		logger.info("map:" + map);
		return map;
	}

	public void setCommonCountLimit(Map<String, Object> map, RedisUtil aresJedis) {
		try {
			String myStatus = aresJedis.hget(myCountKey, myCommonFlag);
			logger.info("订单完成时查看标志是否已经入特殊时间段：" + myStatus);
			// 表示已存在，进入特定时间的限制规则
			if (Y.equals(myStatus)) {
				// 获取商户编号
				String customerNo = String.valueOf(map.get(Constant.CUSTOMER_NO));
				String myFieldCount = customerNo + Constant.SYMBOL + StringUtils.substringBefore(String.valueOf(map.get(Constant.PAY_TYPE)), Constant.SYMBOL);
				// 存在阀值才累加
				String myFieldLimit = StringUtils.substringBefore(String.valueOf(map.get(Constant.PAY_TYPE)), Constant.SYMBOL) + Constant.COUNT;
				String limitValue = aresJedis.hget(myCountKey, myFieldLimit);
				logger.info("myCountKey:" + myCountKey + ",myFieldLimit:" + myFieldLimit + ",limitValue:" + limitValue);
				if (!StringUtil.isEmptyWithTrim(limitValue)) {
					// 累计笔数
					String countValue = aresJedis.hget(myCountKey, myFieldCount);
					logger.info("myCountKey:" + myCountKey + ",myFieldCount:" + myFieldCount + ",countValue:" + countValue);
					if (!StringUtil.isEmptyWithTrim(countValue)) {
						countValue = Integer.parseInt(countValue) + 1 + "";
					} else {
						countValue = "1";
					}
					// 统计成功交易笔数
					aresJedis.hset(myCountKey, myFieldCount, countValue);
				}
			}
		} catch (Exception e) {
			logger.error("特殊时间段交易汇总出现异常：" + e);
		}
	}

	public void getSumCommonCount(Map<String, Object> map, RedisUtil aresJedis) {
		try {
			map.put(Constant.COMMON_TIME_LIMIT_STATUS, Constant.N);
			// 默认初始值
			String limitValue = "1";
			String countValue = "0";
			boolean myContine = false;
			// 获取商户编号
			String customerNo = String.valueOf(map.get(Constant.CUSTOMER_NO));
			// 获取标志状态
			String myStatus = aresJedis.hget(myCountKey, myCommonFlag);
			logger.info("订单生成时查看标志是否已经入特殊时间段：" + myStatus);
			// 表示不存在，未创建
			if (!Y.equals(myStatus)) {
				// 获取设置参数,是一个 hashMap的结构
				Map<String, String> myMap = aresJedis.hgetAll(myRuleKey);
				logger.info("myMap:" + myMap);
				if (myMap != null && myMap.size() > 0) {
					logger.info("myMap.size():" + myMap.size());
					int startHour = Integer.parseInt(myMap.get(Constant.START_HOUR));
					int timeLong = Integer.parseInt(myMap.get(Constant.TIME_LONG));
					int seconds = getEffectiveSeconds(startHour, timeLong);
					logger.info("距离特殊时间段结束还剩：" + seconds);
					// 表示是在特殊时间段
					if (seconds > 0) {
						// 初始化redis
						initializeCommonLimitRedis(seconds, myMap, aresJedis);
						// 获取当前交易的阀值key
						String myFieldLimit = StringUtils.substringBefore(String.valueOf(map.get(Constant.PAY_TYPE)), Constant.SYMBOL) + Constant.COUNT;
						// 获取当前交易的阀值
						limitValue = aresJedis.hget(myCountKey, myFieldLimit);
						logger.info("阀值参数 key:" + myCountKey + ",field:" + myFieldLimit + ",value:" + limitValue);
						if (!StringUtil.isEmptyWithTrim(limitValue)) {
							// 累计值key
							String myFieldCount = customerNo + Constant.SYMBOL
									+ StringUtils.substringBefore(String.valueOf(map.get(Constant.PAY_TYPE)), Constant.SYMBOL);
							// 实际累计值
							countValue = aresJedis.hget(myCountKey, myFieldCount);
							logger.info("累计参数 key:" + myCountKey + ",field:" + myFieldCount + ",value:" + countValue);
							// 需要比较
							myContine = true;
							// 如果没有，放入初始值：0
							if (StringUtil.isEmptyWithTrim(countValue)) {
								countValue = "0";
								Long hset = aresJedis.hset(seconds, myCountKey, myFieldCount, countValue);
								logger.info("redis.hset(+seconds+," + myCountKey + "," + myFieldCount + "," + countValue + ") result:" + hset);
							}
						}
					}
				}
			} else {
				// 已存在，直接获取
				String myFieldCount = customerNo + Constant.SYMBOL + StringUtils.substringBefore(String.valueOf(map.get(Constant.PAY_TYPE)), "_");
				// 累计笔数
				countValue = aresJedis.hget(myCountKey, myFieldCount);
				// 判断是否存在这种交易方式的记录
				if (!StringUtil.isEmptyWithTrim(countValue)) {
					String myFieldLimit = StringUtils.substringBefore(String.valueOf(map.get(Constant.PAY_TYPE)), Constant.SYMBOL) + Constant.COUNT;
					// 获取当前交易的阀值
					limitValue = aresJedis.hget(myCountKey, myFieldLimit);
					logger.info("阀值参数 key:" + myCountKey + ",field:" + myFieldLimit + ",value:" + limitValue);
					logger.info("累计参数 key:" + myCountKey + ",field:" + myFieldCount + ",value:" + countValue);
					// 说明存在这个交易方式的限制，需要比较
					myContine = true;
				}
			}
			if (myContine) {
				// 触犯规则标识
				if (Integer.parseInt(limitValue) <= Integer.parseInt(countValue)) {
					map.put(Constant.COMMON_TIME_LIMIT_STATUS, Y);
					logger.info("当前交易触发特定时间段限制交易的规则！");
				}
			}
		} catch (Exception e) {
			logger.error("特殊时间段限制出现异常：" + e);
		}
	}

	/* 计算剩余的有效时间（s） */
	private int getEffectiveSeconds(int startHour, int timeLong) {
		Date nowTime = new Date();
		// 当前秒数
		long nowSeconds = nowTime.getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowTime);
		Calendar startDate = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), startHour, 0, 0);
		Date startTime = startDate.getTime();
		// 起始秒数
		long startSeconds = startTime.getTime();
		startDate.add(Calendar.HOUR_OF_DAY, timeLong);
		Date endTime = startDate.getTime();
		// 截止秒数
		long endSeconds = endTime.getTime();
		if (nowSeconds >= startSeconds && nowSeconds < endSeconds) {
			return (int) (endSeconds - nowSeconds) / 1000;
		}
		return 0;
	}

	private void initializeCommonLimitRedis(int seconds, Map<String, String> myMap, RedisUtil aresJedis) {
		for (String key : myMap.keySet()) {
			if (!StringUtil.isEmptyWithTrim(key) && !Constant.START_HOUR.equals(key) && !Constant.TIME_LONG.equals(key)) {
				String myValue = myMap.get(key);
				// value是一个json的结构
				JSONObject json = JSONObject.fromObject(myValue);
				String limitType = json.getString(Constant.LIMIT_TYPE);
				String limitMode = json.getString(Constant.LIMIT_MODE);
				String limitNum = json.getString(Constant.LIMIT_NUM);
				String myField = limitType + Constant.SYMBOL + limitMode;// WX_COUNT
				// 将限制类型全部放进去
				Long hset = aresJedis.hset(seconds, myCountKey, myField, limitNum);
				logger.info("redis.hset(" + seconds + "," + myCountKey + "," + myField + "," + limitNum + ") result:" + hset);
			}
		}
		Long hset = aresJedis.hset(myCountKey, myCommonFlag, Y);// 标志进入特殊时间段
		logger.info("redis.hset(" + seconds + "," + myCountKey + "," + myCommonFlag + "," + Y + ") result:" + hset);
	}
}
