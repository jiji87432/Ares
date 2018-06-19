package com.pay.risk.dubbo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.pay.ares.remote.service.CommonRiskServiceFacade;
import com.pay.risk.Constant;
import com.pay.risk.ares.cdl.constant.RedisConstants;
import com.pay.risk.ares.cdl.util.CDUtil;
import com.pay.risk.ares.interfaces.BaseRuleCalcService;
import com.pay.risk.constans.DowngradeConstants;
import com.pay.risk.constans.ONLineUtil;
import com.pay.risk.remote.service.DowngradeDealListFacade;
import com.pay.risk.remote.service.RiskRuleCaseAresFacade;
import com.pay.risk.remote.service.SecondCashServiceFacade;
import com.pay.risk.remote.service.WhiteListModleFacade;
import com.pay.risk.util.DateUtils;
import com.riskrule.bean.RuleObj;
import com.riskrule.runmvel.RunRule;
import com.riskutil.redis.RedisUtil;

public class CommonRiskServiceFacadeImpl implements CommonRiskServiceFacade {
	private static final Logger logger = Logger.getLogger(CommonRiskServiceFacadeImpl.class);

	@Resource
	private Map<String, BaseRuleCalcService> allRuleService;

	@Resource
	private SecondCashServiceFacade secondCashServiceFacade;

	@Resource
	private RiskRuleCaseAresFacade riskRuleCaseAresFacade;

	@Resource
	private WhiteListModleFacade whiteListModleFacade;

	/** 降级交易接口 */
	@Resource
	private DowngradeDealListFacade downgradeDealListFacade;

	/** M0007特殊规则set集合 */
	private static final String M0007_SPECIAL_RULE_SET = "M0007_SPECIAL_RULE_SET";

	/** 保证一个商户只能触发一次规则 */
	private static final String CUSTOMER_M0007_RULE = "_M0007_RULE";

	/** 每条规则对应的降级金额 */
	private static final String RULE_DOWNGRADE_AMOUNT = "RULE_DOWNGRADE_AMOUNT";

	@Override
	public String getCommonResult(String str) {
		String mCode = null;
		RedisUtil jedis = null;
		try {
			jedis = new RedisUtil(RedisConstants.REDIS_Ares);
			logger.info(Constant.AresMsg + " on str : " + str);
			JSONObject json = JSONObject.fromObject(str);
			long x = new Date().getTime();
			mCode = json.getString("M_CODE");
			// 判断当前请求是不是秒到
			if (mCode != null && "M00007".equals(mCode)) {
				// 判断是否执行高可用方案
				if ("N".equals(Constant.secondCashFlag)) {
					logger.info(Constant.AresMsg + ":高可用执行");
					/*
					 * String custoemr_no = json.getString("customer_no");
					 * try {
					 * String result = secondCashServiceFacade.highAvailabilityByM00007(custoemr_no);
					 * return result;
					 * } catch (Exception e) {
					 * logger.error(Constant.AresErr + " highAvilablilty msg:" + e.getMessage());
					 * e.printStackTrace();
					 * JSONObject _o = new JSONObject();
					 * _o.put("result", "PASS");
					 * _o.put("error", "Y");
					 * return _o.toString();
					 * }
					 */
				}
				try {
					String newSecondCashFlagTrue = jedis.get("NEW_SECONDCASH_ON_OFF_TRUE");

					// 正式切换
					if ("Y".equals(newSecondCashFlagTrue)) {
						json.put("M_CODE", "M00010");
						mCode = json.getString("M_CODE");
						str = json.toString();
					}
				} catch (Exception e) {
					logger.error("NewSecondCashRuleService***** has error***** ");
					e.printStackTrace();
				}
			}

			if (mCode != null && "M00008".equals(mCode)) {
				// 切换
				try {
					String doSomeSecondCachRuleFlag = jedis.get("DOSOME_SECONDCASH_RULE_ON_OFF");

					if ("Y".equals(doSomeSecondCachRuleFlag)) {
						doSomeSecondCachRule(str);
					}
				} catch (Exception e) {
					logger.error("doSomeSecondCachRule***** has error***** ");
					e.printStackTrace();
				}
			}

			try {
				boolean tf = checkWhiteListModle(str, jedis);
				if (tf) {
					logger.info(Constant.AresMsg + " checkWhiteListModle method return true!");
					JSONObject obj = new JSONObject();
					obj.put("result", "PASS");
					return obj.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			BaseRuleCalcService brc = this.allRuleService.get(mCode);
			boolean flag = brc.isCalcFlag(str);
			if (!flag) {
				logger.info(Constant.AresMsg + " isCalFlag method return false , pass!");
				JSONObject obj = new JSONObject();
				obj.put("result", "PASS");
				return obj.toString();
				/*
				 * JSONArray arr = new JSONArray();
				 * JSONObject ob = new JSONObject();
				 * ob.put("CHECKERROR", "数据校验未通过，数据不全或已重复");
				 * arr.add(ob);
				 * obj.put("ruleDetail", arr);
				 * return obj.toString();
				 */
			}
			if (flag) {
				RuleObj obj = brc.parseStr2RuleObj(str);
				int size = 0;
				List<String> rules = null;
				brc.setCalcInfo(obj);
				Map<String, String> mp = ONLineUtil.generateCalcInfo(mCode + "_t0");
				RunRule.exeRuleEngine(mp, obj);
				try {
					logger.info(Constant.AresMsg + " obj2 " + obj.getRuleDetail());
					logger.info(Constant.AresMsg + ":" + obj.getRules());
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (obj == null || obj.getRules() == null) logger.info("null");

				String strResult = brc.anaReslut(obj);
				brc.setData2Cache(obj);
				if (obj.getRules() != null) {
					rules = obj.getRules();
					size = obj.getRules().size();
					logger.info(Constant.AresMsg + " common:" + size);
					for (String liststr : rules) {
						logger.info(Constant.AresMsg + " common:" + liststr);
					}
				} else {
					logger.info(Constant.AresMsg + " common: rules is null");
				}
				brc.handleResult(obj);
				long y = new Date().getTime() - x;
				logger.info(Constant.AresMsg + " mcode: " + mCode + " cost : " + y + "ms");
				try {
					if (rules != null && size > 0) {

						Set<String> set = new HashSet<String>();
						for (String rule : rules) {

							set.add(rule);
						}

						if ("Y".equals(jedis.get("RULEHANDLEFILTER_CDL_0420"))) {

							JSONObject jsonResult = JSONObject.fromObject(strResult);
							logger.info("set.size:" + set.size() + "JSONRESULT:" + jsonResult);

							if ("FILTER".equals(jsonResult.get("result")) && jsonResult != null) {

								jsonResult.put("result", "PASS");
								jsonResult.remove("ruleDetail");

								// 从新赋值,这样做目的是防止timeOut之后。返回信息不对的问题
								strResult = jsonResult.toString();

								// 保存风险规则的记录
								saveSiskRuleRecord(str, set, "FILTER", null);

							} else {

								// 保存风险规则的记录
								saveSiskRuleRecord(str, set, "REJECT", null);
							}
						} else {

							logger.info("set.size:" + set.size());
							String result = riskRuleCaseAresFacade.riskRuleCaseRecord(str, set, "REJECT", null);
							logger.info("riskRuleCaseAresFacade.riskRuleCaseRecord result:" + result);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return strResult;
			}

		} catch (Exception e) {
			logger.error(Constant.AresErr + " CommonRiskServiceFacadeImpl msg:" + e.getMessage());
			e.printStackTrace();
			JSONObject _o = new JSONObject();
			_o.put("result", "PASS");
			_o.put("error", "Y");
			try {
				if ("M00007".equals(mCode)) {
					logger.info("15201400976" + "业务号 " + mCode + "运行出错 : " + e.getMessage());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			return _o.toString();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		return null;
	}

	/**
	 * @Description 保存风险规则记录
	 * @param str
	 * @param set
	 * @param string
	 * @param object
	 * @see 需要参考的类或方法
	 */
	private void saveSiskRuleRecord(String str, Set<String> set, String type, Object object) {
		String result = riskRuleCaseAresFacade.riskRuleCaseRecord(str, set, type, null);
		logger.info("riskRuleCaseAresFacade result》》:" + result);
	}

	/**
	 * @Description 检查交易状态
	 * @param json
	 * @param jedis
	 * @return
	 * @see 需要参考的类或方法
	 */
	private boolean checkTransTypeIsVoid(JSONObject json, RedisUtil jedis) {
		try {
			if ("Y".equals(jedis.get("CDL_CHECK_TRANSTYPE_FLAG_20170509"))) {
				if ("M00008".equals(json.opt("M_CODE")) && json.has("trans_type")) {
					return "PURCHASE_VOID".equals(json.getString("trans_type"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @Description 检查是否在模型白名单里面
	 * @param jsonStr
	 * @return boolean
	 * @see 需要参考的类或方法
	 */
	private boolean checkWhiteListModle(String str, RedisUtil jedis) {
		try {
			String flg = jedis.get("CDL_WH_20170405");
			if ("Y".equals(flg)) {
				return whiteListModleFacade.customerQueryWhiteListCheck(str);
			}
		} catch (Exception e) {
			logger.error("error 白名单验证接口失败 ！返回 false .... 默认不在白名单", e);
		}
		return false;
	}

	/**
	 * @Description 如果是cdl，调用秒到的规则，判断几个规则
	 * @param str
	 * @see 需要参考的类或方法
	 */
	public void doSomeSecondCachRule(String str) {
		RedisUtil jedis = null;
		try {
			long startTime = System.currentTimeMillis();
			// yyyymmdd
			String nowDate = CDUtil.getNowTime();
			// 将 cdl json 字段 转 秒到 字段
			JSONObject json = toM000010Json(str);

			String mCode = json.getString("M_CODE");
			String customerNo = json.getString("customer_no");

			str = json.toString();

			logger.info("doSomeSecondCachRule***** on str : " + str);

			jedis = new RedisUtil(RedisConstants.REDIS_Ares);
			BaseRuleCalcService brc = this.allRuleService.get(mCode);
			boolean flag = brc.isCalcFlag(str);
			if (flag) {

				RuleObj obj = brc.parseStr2RuleObj(str);
				brc.setCalcInfo(obj);

				Map<String, String> mp = ONLineUtil.generateCalcInfo(mCode + "_t0");
				RunRule.exeRuleEngine(mp, obj);

				logger.info("doSomeSecondCachRule***** New SecondCash ruleObj Result is " + obj.getRuleDetail() + "-" + obj.getRules());

				List<String> rules = obj.getRules();

				// 过滤规则 这些规则一天只命中一次，
				rules = ruleFiltration(jedis, rules, customerNo, nowDate);

				// 过滤后这些规则还有，就调用接口
				if (null != rules & rules.size() > 0) {
					logger.info("doSomeSecondCachRule***** strResult is : " + rules + " do 降级  ");
					downgrade(jedis, str, customerNo, rules);
				} else {
					logger.info("doSomeSecondCachRule***** strResult is 空 : " + rules);
				}

				logger.info("doSomeSecondCachRule***** Calculate Using Time is " + (System.currentTimeMillis() - startTime));

			}
		} catch (Exception e) {
			logger.error("doSomeSecondCachRule***** has error***** " + e);
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
	}

	/**
	 * @Description 将 M00008 的字段转称M00010 的
	 * @param json
	 * @return
	 * @see 需要参考的类或方法
	 */
	private JSONObject toM000010Json(String str) {
		JSONObject json = JSONObject.fromObject(str);

		String openTimeStr = "";

		if (!json.has("open_time")) {
			openTimeStr = String.valueOf(new Date().getTime());
			json.put("open_time", openTimeStr);
		} else {
			openTimeStr = json.getString("open_time");
		}

		String createTimeStr = json.getString("create_time");
		String bizType = json.getString("biz_type");

		Date openTime = new Date(Long.parseLong(openTimeStr));
		Date createTime = new Date(Long.parseLong(createTimeStr));

		json.put("M_CODE", "M00010");
		json.put("open_time", DateUtils.get24DateTime(openTime));
		json.put("create_time", DateUtils.get24DateTime(createTime));
		json.put("bizType", bizType);

		return json;
	}

	/**
	 * 新秒到 试跑
	 * @Description 一句话描述方法用法
	 * @param str
	 * @see 需要参考的类或方法
	 */
	public void getCommonResultPrepare(String str) {
		long startTime = System.currentTimeMillis();

		JSONObject json = JSONObject.fromObject(str);
		json.put("M_CODE", "M00010");
		String mCode = json.getString("M_CODE");
		str = json.toString();

		logger.info("NewSecondCashRuleService***** on str : " + str);
		try {
			BaseRuleCalcService brc = this.allRuleService.get(mCode);
			boolean flag = brc.isCalcFlag(str);
			if (flag) {

				RuleObj obj = brc.parseStr2RuleObj(str);
				brc.setCalcInfo(obj);

				Map<String, String> mp = ONLineUtil.generateCalcInfo(mCode + "_t0");
				RunRule.exeRuleEngine(mp, obj);

				logger.info("NewSecondCashRuleService***** New SecondCash ruleObj Result is " + obj.getRuleDetail() + "-" + obj.getRules());

				logger.info("NewSecondCashRuleService***** Calculate Using Time is " + (System.currentTimeMillis() - startTime));
				String strResult = brc.anaReslut(obj);
				brc.setData2Cache(obj);
				int size = 0;
				List<String> rules = null;
				if (obj.getRules() != null) {
					rules = obj.getRules();
					size = obj.getRules().size();
					logger.info(Constant.AresMsg + " NewSecondCashRuleService***** common:" + size);
					for (String liststr : rules) {
						logger.info(Constant.AresMsg + " NewSecondCashRuleService***** common:" + liststr);
					}
				} else {
					logger.info(Constant.AresMsg + " NewSecondCashRuleService***** common: rules is null");
				}
				brc.handleResult(obj);
				try {
					if (rules != null && size > 0) {

						Set<String> set = new HashSet<String>();
						for (String rule : rules) {
							set.add(rule);
						}
						logger.info("NewSecondCashRuleService***** 入库操作  set.size:" + set.size());
						String result = riskRuleCaseAresFacade.riskRuleCaseRecord(str, set, "REJECT", null);
						logger.info("NewSecondCashRuleService***** riskRuleCaseAresFacade.riskRuleCaseRecord result:" + result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				logger.info("NewSecondCashRuleService***** strResult is : " + strResult);
			}
		} catch (Exception e) {
			logger.error("NewSecondCashRuleService***** has error***** ");
			e.printStackTrace();
		}
	}

	private void downgrade(RedisUtil jedis, String str, String customerNo, List<String> rules) {
		String messageCode = customerNo;
		String content = "TMP_SECOND_CASH";
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		Map<String, String> ruleDownGradeAmount = jedis.hgetAll(RULE_DOWNGRADE_AMOUNT);

		// 遍历 命中规则的 list
		for (String rule : rules) {

			// 取出规则对应的降级金额
			String amount = ruleDownGradeAmount.get(rule);

			JSONObject js = new JSONObject();
			js.put(DowngradeConstants.TYPE, DowngradeConstants.CUS);// 商户
			js.put(DowngradeConstants.TYPE_NUMBER, customerNo);// 编号
			js.put(DowngradeConstants.LIMIT_TYPE, DowngradeConstants.DBJE);// 限制单笔
			js.put(DowngradeConstants.LIMIT_NUM, amount);// 限制金额
			js.put(DowngradeConstants.MESSAGE_SOURCE, DowngradeConstants.SOURCE_NUM);// 降级信息来源
			js.put(DowngradeConstants.MESSAGE_CODE, rule);// 信息编码
			js.put(DowngradeConstants.USABILITY_STATUS, DowngradeConstants.Y);// 表示是新增降级
			js.put(DowngradeConstants.DOWNGRADE_DAY, DowngradeConstants.DOWNGRADE_DEFULT_NUM);// 降级天数
			js.put(DowngradeConstants.OPERATOR, DowngradeConstants.SYSTEM);// 操作人
			js.put(DowngradeConstants.CONTENT, content);// 备注说明
			array.add(js);
		}

		json.put(DowngradeConstants.JSON_ARRAY, array);

		// 不要影响下面
		try {
			String result = downgradeDealListFacade.secondCashUpdateDowngradeDealList(json.toString());
			logger.info(" downgrade result : " + result);
		} catch (Exception e) {
			logger.error("downgradeDealListFacade error>>:", e);
		}

		saveSiskRuleRecord(str, new HashSet<String>(rules), "DOWN", null);
	}

	/**
	 * @param jedis
	 * @Description 过滤规则 这些规则一天只命中一次
	 * @param rules
	 * @param customerNo
	 * @param nowDate
	 * @return
	 * @see 需要参考的类或方法
	 */
	private List<String> ruleFiltration(RedisUtil jedis, List<String> rules, String customerNo, String nowDate) {
		List<String> listRule = new ArrayList<String>();

		boolean flagLog = false;
		if ("Y".equals(jedis.get("M0007_SPECIAL_RULE_LOG_FLAG"))) {
			flagLog = true;
		}

		if (flagLog) logger.info("ruleFiltration rules>>:" + rules);

		// 保证商户对规则只触发一次。
		String customerSetKey = nowDate + "_" + customerNo + CUSTOMER_M0007_RULE;

		if (rules != null && rules.size() > 0) {

			Integer dseconds = getDseconds(26);

			// Redis 集合特殊规则set
			Set<String> smembers = jedis.smembers(M0007_SPECIAL_RULE_SET);

			if (flagLog) logger.info("ruleFiltration smembers>>:" + smembers + ",KEY=" + M0007_SPECIAL_RULE_SET//
					+ ",customerVsRulse=" + customerSetKey);

			// 循环触发所有的规则,看是否有特殊规则
			for (int i = 0; i < rules.size(); i++) {
				String rule = rules.get(i);

				// 保证一个商户只触发一次
				if (!jedis.sismember(customerSetKey, rule)) {

					if (flagLog) logger.info("ruleFiltration sismember>>:true");

					// 是否是特殊拦截key
					if (smembers.contains(rule)) {

						// 添加到list 中。返回
						listRule.add(rule);

						// 保证一个商户只触发一次
						jedis.sadd(dseconds, customerSetKey, rule);

						if (flagLog) logger.info("ruleFiltration listRule>>:" + listRule);
					}
				}
			}
		}

		return listRule;
	}

	/**
	 * @Description 一句话描述方法用法
	 * @param i
	 * @return
	 * @see 需要参考的类或方法
	 */
	private Integer getDseconds(int num) {
		SimpleDateFormat sb = new SimpleDateFormat("HH");
		return (num - (Integer.valueOf(sb.format(new Date())))) * 3600;
	}

}
