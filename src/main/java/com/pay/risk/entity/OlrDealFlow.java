/**
 *
 */
package com.pay.risk.entity;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrDealFlow 此处填写需要参考的类
 * @version 2015年8月12日 下午4:52:16
 * @author ximin.yi
 */
public class OlrDealFlow extends AutoIDEntity {

	/** 订单号*/
	private String orderId;

	/** 流水号*/
	private String paymentId;

	/** 商户编号*/
	private String customerNo;

	/** 交易金额*/
	private Double amount;

	/** 业务类型*/
	private String busType;

	/** 身份证号*/
	private String userIdno;

	/** 卡号*/
	private String userPan;

	/** 姓名*/
	private String userName;

	/** 手机号*/
	private String userPhone;

	/** 卡种*/
	private String cardType;

	/** 所属银行*/
	private String issuer;

	/** 支付方式*/
	private String payType;

	/** IP*/
	private String transIp;

	/** MAC*/
	private String mac;

	/** 支付载体*/
	private String payCarrier;

	/** IMEI码*/
	private String IMEINO;

	/** IMSI码*/
	private String IMSINO;

	/** CPU码*/
	private String CPUNO;

	/** 规则结果*/
	private String ruleResult;

	/** 最终结果*/
	private String finalResult;

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
	 * @return the paymentId
	 */
	public String getPaymentId() {
		return paymentId;
	}

	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
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
	 * @return the userIdno
	 */
	public String getUserIdno() {
		return userIdno;
	}

	/**
	 * @param userIdno the userIdno to set
	 */
	public void setUserIdno(String userIdno) {
		this.userIdno = userIdno;
	}

	/**
	 * @return the userPan
	 */
	public String getUserPan() {
		return userPan;
	}

	/**
	 * @param userPan the userPan to set
	 */
	public void setUserPan(String userPan) {
		this.userPan = userPan;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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

	/**
	 * @return the userPhone
	 */
	public String getUserPhone() {
		return userPhone;
	}

	/**
	 * @param userPhone the userPhone to set
	 */
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * @param issuer the issuer to set
	 */
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	/**
	 * @return the payType
	 */
	public String getPayType() {
		return payType;
	}

	/**
	 * @param payType the payType to set
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}

	/**
	 * @return the transIp
	 */
	public String getTransIp() {
		return transIp;
	}

	/**
	 * @param transIp the transIp to set
	 */
	public void setTransIp(String transIp) {
		this.transIp = transIp;
	}

	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * @return the payCarrier
	 */
	public String getPayCarrier() {
		return payCarrier;
	}

	/**
	 * @param payCarrier the payCarrier to set
	 */
	public void setPayCarrier(String payCarrier) {
		this.payCarrier = payCarrier;
	}

	/**
	 * @return the iMEINO
	 */
	public String getIMEINO() {
		return IMEINO;
	}

	/**
	 * @param iMEINO the iMEINO to set
	 */
	public void setIMEINO(String iMEINO) {
		IMEINO = iMEINO;
	}

	/**
	 * @return the iMSINO
	 */
	public String getIMSINO() {
		return IMSINO;
	}

	/**
	 * @param iMSINO the iMSINO to set
	 */
	public void setIMSINO(String iMSINO) {
		IMSINO = iMSINO;
	}

	/**
	 * @return the cPUNO
	 */
	public String getCPUNO() {
		return CPUNO;
	}

	/**
	 * @param cPUNO the cPUNO to set
	 */
	public void setCPUNO(String cPUNO) {
		CPUNO = cPUNO;
	}

}
