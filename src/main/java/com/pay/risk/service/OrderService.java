package com.pay.risk.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.riskrule.bean.RuleObj;
import com.riskutil.redis.RedisUtil;

public interface OrderService {

	public RuleObj parseStr2RuleObj(String str);

	public void setCalcInfo(RuleObj rObj);

	public void setData2Cache(RuleObj rObj);

	public JSONObject anaReslut(List<String> list, RuleObj rObj);

	public String saveCaleResult(String str);

	public String saveOnResult(String str);

	/**
	 * 交易地区码的限制规则
	 * 获取地区码（精确到市）及相关信息，存入RuleObj
	 * @Description 一句话描述方法用法
	 * @param map
	 * @param aresJedis
	 * @see 需要参考的类或方法
	 */
	public void setAreaCodeInfo(Map<String, Object> map, RedisUtil aresJedis);
}
