package com.pay.risk.context;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import com.pay.risk.ares.interfaces.BaseRuleCalcService;

@Configuration
@ComponentScan(basePackages = "com.pay.risk.ares", excludeFilters = { @Filter(Controller.class), @Filter(Configuration.class) })
public class AllRulesConfig {
	/*
	 * @Resource
	 * private BaseRuleCalcService paiLoginRuleService;
	 * @Resource
	 * private BaseRuleCalcService paiSendCustomerRuleService;
	 */
	@Resource(name = "commissionDayQualiService")
	private BaseRuleCalcService commissionDayQualiServiceImpl;
	@Resource(name = "commissionDaySettleService")
	private BaseRuleCalcService CommissionDaySettleServiceImpl;
	@Resource
	private BaseRuleCalcService secondCashRuleService;
	@Resource
	private BaseRuleCalcService cdRuleService;
	@Resource
	private BaseRuleCalcService newSecondCashRuleService;
	@Resource
	private BaseRuleCalcService unionpayService;
	@Resource(name = "transactAfterCaseService")
	private BaseRuleCalcService TransactAfterCaseServiceImpl;
	@Resource(name = "sweepCodeService")
	private BaseRuleCalcService sweepCodeService;

	@Bean
	public Map<String, BaseRuleCalcService> allRuleService() {

		Map<String, BaseRuleCalcService> map = new HashMap<String, BaseRuleCalcService>();
		// map.put("M00005", paiLoginRuleService);
		// map.put("M00006", paiSendCustomerRuleService);
		map.put("M00005", commissionDayQualiServiceImpl);
		map.put("M00006", CommissionDaySettleServiceImpl);
		map.put("M00007", secondCashRuleService);
		map.put("M00008", cdRuleService);
		map.put("M00010", newSecondCashRuleService);
		map.put("M00011", unionpayService);
		map.put("M00012", TransactAfterCaseServiceImpl);
		map.put("M00013", sweepCodeService);
		return map;
	}
}
