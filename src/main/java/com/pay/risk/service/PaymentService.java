package com.pay.risk.service;

import com.riskrule.bean.RuleObj;

public interface PaymentService {

	public RuleObj parseStr2RuleObj(String str);

	public void setCalcInfo(RuleObj rObj);

	public void setData2Cache(RuleObj rObj );

	public String saveCaleResult(String s);

	public String saveOnResult(String str);
}
