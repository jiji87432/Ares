package com.pay.risk.dubbo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.pay.ares.remote.service.CommonResultServiceFacade;
import com.pay.risk.Constant;
import com.pay.risk.ares.interfaces.BaseRuleCalcService;
import com.pay.risk.constans.ONLineUtil;
import com.pay.risk.remote.service.RiskRuleCaseAresFacade;
import com.pay.risk.remote.service.WhiteListModleFacade;
import com.riskrule.bean.RuleObj;
import com.riskrule.runmvel.RunRule;

public class CommonSaveResultServiceFacadeImpl implements CommonResultServiceFacade {
	private static final Logger logger = Logger.getLogger(CommonSaveResultServiceFacadeImpl.class);

	@Resource
	private Map<String, BaseRuleCalcService> allRuleService;

	@Resource
	private RiskRuleCaseAresFacade riskRuleCaseAresFacade;

	@Resource
	private WhiteListModleFacade whiteListModleFacade;

	@Override
	public String setResult(String str) {
		String finalResult = "";
		try {
			long startTime = System.currentTimeMillis();

			/** 老 逻辑 。saveCalcResult */
			logger.info(Constant.AresMsg + " commonSaveResult on str：" + str);
			JSONObject json = JSONObject.fromObject(str);
			String mCode = json.getString("M_CODE");
			BaseRuleCalcService brc = null;
			brc = this.allRuleService.get(mCode);
			boolean flag = brc.isSaveFlag(str);
			if (flag) {
				JSONObject obj = new JSONObject();
				String result = brc.saveCalcResult(str);
				obj.put("result", result);
				logger.info("final return is  " + obj.toString());
				finalResult = obj.toString();
			} else {
				JSONObject obj = new JSONObject();
				obj.put("result", "PASS");
				finalResult = obj.toString();
			}
			// logger.info(Constant.AresMsg + " str is not complete : " + str);
			/** 至此 ，老逻辑 */

			JSONObject jsonObject = null;
			boolean isConversionm = false;
			String param = null;
			if (mCode != null && "M00008".equals(mCode)) {
				// 转换
				jsonObject = toM000012Json(str);
				param = jsonObject.toString();
				isConversionm = true;
				// logger.info(Constant.AresMsg + " commonSaveResult on str：" + str + "，conversionm str：" + param);
			}
			if (isConversionm) {
				// 过滤模型白名单
				boolean tf = checkWhiteListModle(param);
				if (tf) {
					logger.info(Constant.AresMsg + " checkWhiteListModle method return true!");
					return finalResult;
				}

				String conversionmCode = jsonObject.getString("M_CODE");
				brc = this.allRuleService.get(conversionmCode);
				// 数据校验
				// flag = brc.isCalcFlag(param); 使用 M0008的校验逻辑
				if (flag) {
					// 转换RuleObj
					RuleObj obj = brc.parseStr2RuleObj(param);
					// 组装map
					brc.setCalcInfo(obj);
					Map<String, String> mp = ONLineUtil.generateCalcInfo(conversionmCode + "_t0");
					// 执行规则
					RunRule.exeRuleEngine(mp, obj);
					logger.info(Constant.AresMsg + " commonSaveResult exeRuleEngine：" + obj.getRules());
					// 返回命中结果与处理措施
					String strResult = brc.anaReslut(obj);
					logger.info(Constant.AresMsg + " commonSaveResult anaReslut：" + strResult.toString());
					// 规则执行完后数据沉淀
					brc.setData2Cache(obj);
					// 执行结果
					brc.handleResult(obj);
					logger.info(Constant.AresMsg + "commonSaveResult Calculate Using Time is " + (System.currentTimeMillis() - startTime));

					List<String> rules = null;
					if (obj.getRules() != null) {
						rules = obj.getRules();
					} else {
						logger.info(Constant.AresMsg + " commonSaveResult: rules is null");
					}
					if (rules != null && rules.size() > 0) {
						Set<String> set = new HashSet<String>();
						for (String rule : rules) {
							set.add(rule);
						}
						JSONObject jsonResult = JSONObject.fromObject(strResult);
						if (jsonResult != null && "FILTER".equals(jsonResult.get("result"))) {
							jsonResult.put("result", "PASS");
							jsonResult.remove("ruleDetail");
							// 保存风险规则的记录
							saveSiskRuleRecord(param, set, "FILTER", null);
						} else {
							// 保存风险规则的记录
							saveSiskRuleRecord(param, set, "REJECT", null);
						}
						// logger.info(Constant.AresMsg + "commonSaveResult saveSiskRuleRecord " + param);
					}
				}
			}

		} catch (Exception e) {
			logger.error(Constant.AresErr + " commonSaveResult msg:" + e.getMessage());
			e.printStackTrace();
			JSONObject _o = new JSONObject();
			_o.put("result", "PASS");
			_o.put("error", "Y");
			logger.info("final return is  " + _o.toString());
			return _o.toString();
		}
		logger.info("final return is  " + finalResult);
		return finalResult;
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

	private JSONObject toM000012Json(String str) {
		JSONObject json = JSONObject.fromObject(str);

		json.put("M_CODE", "M00012");

		return json;
	}

	/**
	 * @Description 检查是否在模型白名单里面
	 * @param jsonStr
	 * @return boolean
	 * @see 需要参考的类或方法
	 */
	private boolean checkWhiteListModle(String str) {
		try {
			return whiteListModleFacade.customerQueryWhiteListCheck(str);
		} catch (Exception e) {
			logger.error("error 白名单验证接口失败 ！返回 false .... 默认不在白名单", e);
		}
		return false;
	}
}