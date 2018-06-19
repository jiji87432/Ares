/**
 *
 */
package com.pay.risk.dubbo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.pay.ares.remote.service.PaiDataServiceFacade;
import com.pay.risk.Constant;
import com.pay.risk.ares.interfaces.BaseRuleCalcService;
import com.pay.risk.ares.interfaces.constants.LoginConstants;
import com.pay.risk.ares.interfaces.util.StringUtil;
import com.riskutil.redis.RedisUtil;


public class PaiDataServiceFacadeImpl implements PaiDataServiceFacade {
	private static final Logger logger = Logger.getLogger(PaiDataServiceFacadeImpl.class);

	@Resource
	private Map<String, BaseRuleCalcService> allRuleService;

	/**
	 * 参数type 只能是固定的参数值 device_no login_no
	 * str 为类型type对应的值
	 */
	@Override
	public String getLoginInfoByOid(String str, String type) {
		if (str != null) {
			logger.info("PaiDataServiceFacadeImpl getLoginInfoByOid method start...str：" + str + " type：" + type);
			RedisUtil aresJedis = new RedisUtil(Constant.PaiJedis);
			try {

				if (str != null && !"".equals(str) && type != null && !"".equals(type)) {

					// 从redis中获得登陆手机号所有相关的信息或则设备相关的信息
					Map<String, String> map = null;
					JSONObject jObject = new JSONObject();
					// 登陆手机号
					if ("login_no".equals(type)) {
						map = aresJedis.hgetAll(str + LoginConstants.SUFFIX_LOGIN_NO);
						if (map != null && map.size() > 0) {
							String device_no = map.get("device_no");
							// 登录手机号
							jObject.put("login_no", str);
							// 设备编号
							jObject.put("device_no", device_no);
						}

					}
					// 设备号
					else if ("device_no".equals(type)) {
						map = aresJedis.hgetAll(str + LoginConstants.SUFFIX_DEVICE_NO);
						if (map != null && map.size() > 0) {
							String login_no = map.get("login_no");
							// 登录手机号
							jObject.put("login_no", login_no);
							// 设备编号
							jObject.put("device_no", str);
						}
					}
					logger.info("返回的结果：" + jObject.toString());
					return jObject.toString();
				}

			} catch (Exception e) {
				logger.error("PaiDataServiceFacadeImpl getLoginInfoByOid method msg:" + e.getMessage());
			} finally {
				aresJedis.close();
			}
			logger.info("PaiDataServiceFacadeImpl getLoginInfoByOid method end...");
		}
		return null;
	}

	/**
	 * 参数type 只能是固定的参数值 device_no login_no
	 * str 为json字符串
	 */
	@Override
	public String updateLoginInfoByOid(String str, String type) {
		// 更新成功返回結果
		JSONObject jsonObject2 = new JSONObject();
		if (str != null) {
			logger.info("PaiDataServiceFacadeImpl updateLoginInfoByOid method start...str：" + str + " type：" + type);
			RedisUtil aresJedis = new RedisUtil(Constant.PaiJedis);
			try {
				if (str != null && !"".equals(str) && type != null && !"".equals(type)) {
					// 解析str解析成json
					JSONObject jObject = JSONObject.fromObject(str);
					// 登录手机号
					String login_no = StringUtil.parseObjectToString(jObject.get("login_no"));
					// 设备编号
					String device_no = StringUtil.parseObjectToString(jObject.get("device_no"));
					String key = "";
					// 登录手机号
					if ("login_no".equals(type)) {
						key = login_no + LoginConstants.SUFFIX_LOGIN_NO;
					}
					// 设备编号
					else if ("device_no".equals(type)) {
						key = device_no + LoginConstants.SUFFIX_DEVICE_NO;
					}

					Map<String, String> map = aresJedis.hgetAll(key);
					// 如果redis中存在该信息
					if (map != null && map.size() > 0) {
						// 删除该条信息
						aresJedis.del(key);

						// 向redis中添加数据
						// 定义添加数据的map
						Map<String, String> mapLogin = new HashMap<String, String>();
						// 如果类型是手机号，保存设备编号
						if ("login_no".equals(type)) {
							mapLogin.put("device_no", device_no);
						}
						// 如果类型是设备编号，保存手机号
						else if ("device_no".equals(type)) {
							mapLogin.put("login_no", login_no);
						}
						aresJedis.hmset(key, mapLogin);
						jsonObject2.put("result", "OK");
					}
					// 如果redis中不存在该条信息
					else {
						jsonObject2.put("result", "FAULT-该条信息不存在,无法更新");
					}

					logger.info(key + "==更新后的结果===" + aresJedis.hgetAll(key));

				}

			} catch (Exception e) {
				logger.error("PaiDataServiceFacadeImpl updateLoginInfoByOid method msg:" + e.getMessage());
				jsonObject2.put("result", "FAULT-系统报错");
			} finally {
				aresJedis.close();
			}
		}
		logger.info("PaiDataServiceFacadeImpl updateLoginInfoByOid method end...");
		logger.info("程序处理的结果：" + jsonObject2.toString());
		return jsonObject2.toString();
	}

	/**
	 * 参数type 只能是固定的参数值 device_no login_no
	 * str 为手机号/设备号
	 */
	@Override
	public String removcLoginInfoByOid(String str, String type) {
		logger.info("PaiDataServiceFacadeImpl removcLoginInfoByOid method start...str" + str);
		// 删除成功返回結果
		JSONObject jsonObject2 = new JSONObject();
		RedisUtil aresJedis = new RedisUtil(Constant.PaiJedis);
		try {
			if (str != null && !"".equals(str) && type != null && !"".equals(type)) {

				String key = "";
				if ("login_no".equals(type)) {
					key = str + LoginConstants.SUFFIX_LOGIN_NO;
				} else if ("device_no".equals(type)) {
					key = str + LoginConstants.SUFFIX_DEVICE_NO;
				}

				Map<String, String> map = aresJedis.hgetAll(key);
				// 如果redis中存在该信息 删除该条信息
				if (map != null && map.size() > 0) {
					if ("login_no".equals(type)) {
						Map<String, String> m1 = aresJedis.hgetAll(key);
						Set<String> set = m1.keySet();
						for (String s : set) {
							String value = m1.get(s);
							aresJedis.del(value);
						}
					}

					aresJedis.del(key);
					jsonObject2.put("result", "OK");
				} else {
					jsonObject2.put("result", "FAULT-该条信息不存在,无法删除");
				}

				logger.info(key + "数据删除后的结果" + aresJedis.hgetAll(key));

			}

		} catch (Exception e) {
			logger.error("PaiDataServiceFacadeImpl removcLoginInfoByOid method msg:" + e.getMessage());
			jsonObject2.put("result", "FAULT-系统报错");
		} finally {
			aresJedis.close();
		}
		logger.info("PaiDataServiceFacadeImpl removcLoginInfoByOid method end...");

		logger.info("程序处理的结果：" + jsonObject2.toString());
		// 返回删除成功后的结果
		return jsonObject2.toString();
	}

}
