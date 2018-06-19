package com.pay.risk.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 线上交易MQ同步数据报文实体类
 * @author ximin.yi
 */

public class OnlineOrderData extends AutoIDEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1642785900993498398L;
	// 订单号
	private String orderCode;// 长度20
	// 业务类型
	private String businessType;// 长度20
	// 商户编号
	private String customerNo;
	/** 币种 */
	private String currency;
	/** 订单金额 */
	private BigDecimal amount;
	/** 手续费 */
	private BigDecimal customerFee;
	/** 通道成本 */
	private BigDecimal bankCost;
	/** 订单状态 */
	private String status;
	/** 支付方式 */
	private String payType;
	/** 成功支付时间 */
	private Date successPayTime;

	private int cycle;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getCustomerFee() {
		return customerFee;
	}

	public void setCustomerFee(BigDecimal customerFee) {
		this.customerFee = customerFee;
	}

	public BigDecimal getBankCost() {
		return bankCost;
	}

	public void setBankCost(BigDecimal bankCost) {
		this.bankCost = bankCost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Date getSuccessPayTime() {
		return successPayTime;
	}

	public void setSuccessPayTime(Date successPayTime) {
		this.successPayTime = successPayTime;
	}

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

}
