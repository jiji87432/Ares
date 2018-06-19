/**
 *
 */
package com.pay.risk.entity;


/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrOlderDim 此处填写需要参考的类
 * @version 2015年8月12日 下午4:44:12
 * @author ximin.yi
 */
public class OlrOrderDim extends AutoIDEntity {

	/** 商户编号*/
	private String customerNo;

	/** 订单号*/
	private String orderId;

	/** 交易金额*/
	private Double amount;

	/** 业务类型*/
	private String busType;

	/** 订单支付结果*/
	private String result;

	/** 原因*/
	private String responseCode;

	/** 完成时间*/
	private String completeTime;

	/** 规则结果*/
	private String ruleResult;

	/** 最终结果*/
	private String finalResult;

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
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}



	/**
	 * @return the completeTime
	 */
	public String getCompleteTime() {
		return completeTime;
	}

	/**
	 * @param completeTime the completeTime to set
	 */
	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}

	/**
	 * @return the ruleResult
	 */
	public String getRuleResult() {
		return ruleResult;
	}

	/**
	 * @param ruleResult the ruleResult to set
	 */
	public void setRuleResult(String ruleResult) {
		this.ruleResult = ruleResult;
	}

	/**
	 * @return the finalResult
	 */
	public String getFinalResult() {
		return finalResult;
	}

	/**
	 * @param finalResult the finalResult to set
	 */
	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}

}
