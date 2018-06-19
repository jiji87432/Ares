package com.pay.risk.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.pay.common.util.PropertyUtil;
import com.pay.risk.Constant;
import com.pay.risk.entity.OlrModel;
import com.pay.risk.service.OlrModelService;
import com.riskrule.runmvel.RunRule;
import com.riskutil.redis.RedisUtil;

@Controller
public class InitRuleBaseAction extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(InitRuleBaseAction.class);

	public InitRuleBaseAction() {
		super();
	}

	/**
	 * 启动初始化规则引擎
	 * newPath flag_key = new_rule_path_0316
	 */
	public void init() throws ServletException {
		ServletContext sc = getServletContext();
		WebApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		// 注入service
		OlrModelService olrModelService = (OlrModelService) ac.getBean("olrModelService");

		RedisUtil aressJedis = new RedisUtil(Constant.AresJedis);

		try {
			PropertyUtil propertyUtil = PropertyUtil.getInstance("rule");
			String path = propertyUtil.getProperty("path");
			String files = propertyUtil.getProperty("files");
			String newPath = propertyUtil.getProperty("rule_path_new");
			// 判断文件是否存在
			if (new File(path).exists()) {
				String[] splits = files.split(";");
				for (int i = 0; i < splits.length; i++) {

					try {
						OlrModel oleModel = olrModelService.getModelByCode(files.split(";")[i]);
						if (oleModel != null) {
							JSONObject json = JSONObject.fromObject(oleModel.getVersion());
							for (Object key : json.keySet()) {
								// M0001 t1 14
								aressJedis.hset(oleModel.getCode(), String.valueOf(key), String.valueOf(json.get(key)));
							}
							Integer version = (Integer) json.get("t0"); // 4
							String ruleName = splits[i] + "_t0"; // M00001_t0
							String rulePath = null;
							// TODO
							if ("Y".equals(aressJedis.get("new_rule_path_0316"))) {
								rulePath = newPath + splits[i] + "t0_" + version + ".drl";
							} else {
								rulePath = path + splits[i] + "t0_" + version + ".drl"; // H:/rule/M00001t0_4.drl
							}
							logger.info("init rule path >>> " + rulePath);
							List<String> list = new ArrayList<String>();
							list.add(rulePath);
							RunRule.initRuleEngine(ruleName, version, list);
						}

					} catch (Exception e) {
						logger.error(Constant.AresErr + " 启动初始化规则引擎" + e.getMessage());
						e.printStackTrace();
					}

				}
				logger.info(Constant.AresMsg + " 规则引擎初始化完成！");
			} else {
				logger.info(Constant.AresMsg + " 未找到规则文件");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Constant.AresErr + " initAction contextInitialized msg:" + e.getMessage());
		} finally {
			aressJedis.close();
		}
	}
}