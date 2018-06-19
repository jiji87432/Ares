package com.pay.risk.constans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ONPayment implements Serializable {



	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String  orderId	;//订单号
	private String  paymentId;//	流水号
	private String  customerNo	;//商户编号
	private Double  amount;//	交易金额
	private String  busType;//	业务类型
	private String  userIdno;//	身份证号
	private String  userPan;//	卡号
	private String  userName;//	姓名
	private String  userPhone;//	手机号
	private String  cardType;//	卡种
	private String  issuer;//	所属银行
	private String  payType;//	支付方式
	private String  transIp;//	IP
	private String  mac;//	MAC
	private String  payCarrier;//	支付载体
	private String  IMEINo;//	IMEI码
	private String  IMSINo;//	IMSI码
	private String  CPUNo;//	CPU码
	private String province; //省
	private String city; //城市

	private String status;//   交易结果
	private String anaResult;//   分析结果
	private String retResult;//   返回结果


	private List<Map<String,String>> cusPayDate; //商户日流水汇总

	private List<Map<String,String>> cardPayDate; //单卡日流水汇总

	private List<Map<String,String>> ipPayDate; //单卡日流水汇总

	private List<String> cardDes; //卡跨省交易

	private Map<String , String> cusPayHis; //商户交易历史
	private Map<String , String> cardPayHis; //卡交易历史
	private Map<String , String> ipPayHis;  //ip交易历史




	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAnaResult() {
		return anaResult;
	}
	public void setAnaResult(String anaResult) {
		this.anaResult = anaResult;
	}
	public String getRetResult() {
		return retResult;
	}
	public void setRetResult(String retResult) {
		this.retResult = retResult;
	}
	public List<Map<String, String>> getCusPayDate() {
		return cusPayDate;
	}
	public void setCusPayDate(List<Map<String, String>> cusPayDate) {
		this.cusPayDate = cusPayDate;
	}
	public List<Map<String, String>> getCardPayDate() {
		return cardPayDate;
	}
	public void setCardPayDate(List<Map<String, String>> cardPayDate) {
		this.cardPayDate = cardPayDate;
	}
	public List<Map<String, String>> getIpPayDate() {
		return ipPayDate;
	}
	public void setIpPayDate(List<Map<String, String>> ipPayDate) {
		this.ipPayDate = ipPayDate;
	}
	public List<String> getCardDes() {
		return cardDes;
	}
	public void setCardDes(List<String> cardDes) {
		this.cardDes = cardDes;
	}
	public Map<String, String> getCusPayHis() {
		return cusPayHis;
	}
	public void setCusPayHis(Map<String, String> cusPayHis) {
		this.cusPayHis = cusPayHis;
	}
	public Map<String, String> getCardPayHis() {
		return cardPayHis;
	}
	public void setCardPayHis(Map<String, String> cardPayHis) {
		this.cardPayHis = cardPayHis;
	}
	public Map<String, String> getIpPayHis() {
		return ipPayHis;
	}
	public void setIpPayHis(Map<String, String> ipPayHis) {
		this.ipPayHis = ipPayHis;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getUserIdno() {
		return userIdno;
	}
	public void setUserIdno(String userIdno) {
		this.userIdno = userIdno;
	}
	public String getUserPan() {
		return userPan;
	}
	public void setUserPan(String userPan) {
		this.userPan = userPan;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getTransIp() {
		return transIp;
	}
	public void setTransIp(String transIp) {
		this.transIp = transIp;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getPayCarrier() {
		return payCarrier;
	}
	public void setPayCarrier(String payCarrier) {
		this.payCarrier = payCarrier;
	}
	public String getIMEINo() {
		return IMEINo;
	}
	public void setIMEINo(String iMEINo) {
		IMEINo = iMEINo;
	}
	public String getIMSINo() {
		return IMSINo;
	}
	public void setIMSINo(String iMSINo) {
		IMSINo = iMSINo;
	}
	public String getCPUNo() {
		return CPUNo;
	}
	public void setCPUNo(String cPUNo) {
		CPUNo = cPUNo;
	}




}
