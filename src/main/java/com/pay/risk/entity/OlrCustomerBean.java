/**
 *
 */
package com.pay.risk.entity;

import java.util.List;
import java.util.Map;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrCustomerBean 此处填写需要参考的类
 * @version 2015年8月12日 下午4:44:12
 * @author zikun.liu
 */
public class OlrCustomerBean extends AutoIDEntity {

	/** 商户编号*/
	private String customerNo;

	/** 订单号*/
	private String orderId;

	/** 交易金额*/
	private Double amount;

	/** 业务类型*/
	private String busType;

	/** 商户限额*/
	private Map<String,String> limitAmount;

	/** 历史交易明细*/
	private Map<String,String> historyAmount;

	/** 商户是否在黑名单中*/
	private String isBlack;

	/** 商户基本信息*/
	private Map<String,String> basicInformation;

	/** 规则命中状况*/
	private List<String> ruleRecord;


	/**
	 * @return the ruleRecord
	 */
	public List<String> getRuleRecord() {
		return ruleRecord;
	}

	/**
	 * @param ruleRecord the ruleRecord to set
	 */
	public void setRuleRecord(List<String> ruleRecord) {
		this.ruleRecord = ruleRecord;
	}

	/**
	 * @return the customerNo
	 */
	public String getCustomerNo() {
		return customerNo;
	}

	/**
	 * @param customerNo the customerNo to set
	 */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the busType
	 */
	public String getBusType() {
		return busType;
	}

	/**
	 * @param busType the busType to set
	 */
	public void setBusType(String busType) {
		this.busType = busType;
	}

	/**
	 * @return the limitAmount
	 */
	public Map<String, String> getLimitAmount() {
		return limitAmount;
	}

	/**
	 * @param limitAmount the limitAmount to set
	 */
	public void setLimitAmount(Map<String, String> limitAmount) {
		this.limitAmount = limitAmount;
	}

	/**
	 * @return the historyAmount
	 */
	public Map<String, String> getHistoryAmount() {
		return historyAmount;
	}

	/**
	 * @param historyAmount the historyAmount to set
	 */
	public void setHistoryAmount(Map<String, String> historyAmount) {
		this.historyAmount = historyAmount;
	}

	/**
	 * @return the isBlack
	 */
	public String getIsBlack() {
		return isBlack;
	}

	/**
	 * @param isBlack the isBlack to set
	 */
	public void setIsBlack(String isBlack) {
		this.isBlack = isBlack;
	}

	/**
	 * @return the basicInformation
	 */
	public Map<String, String> getBasicInformation() {
		return basicInformation;
	}

	/**
	 * @param basicInformation the basicInformation to set
	 */
	public void setBasicInformation(Map<String, String> basicInformation) {
		this.basicInformation = basicInformation;
	}

}
