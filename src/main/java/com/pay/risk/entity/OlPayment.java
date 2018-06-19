/**
 *
 */
package com.pay.risk.entity;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlPayment 此处填写需要参考的类
 * @version 2015年8月24日 下午2:45:29
 * @author zikun.liu
 */
public class OlPayment {

	/** 主键id*/
	private Long id;

	/** 版本号*/
	private Integer optimistic;

	/** 订单id*/
	private String order_id;

	/** 流水ID*/
	private String payment_id;

	/** 商户编号*/
	private String customer_no;

	/** 金额*/
	private Double amount;

	/** 交易类型*/
	private String bus_type;

	/** 用户卡号*/
	private String user_pan;

	/** 用户身份证号*/
	private String user_idno;

	/** 用户姓名*/
	private String user_name;

	/** 用户手机*/
	private String user_phone;

	/** 卡类型*/
	private String card_type;

	/** 发卡行*/
	private String issuer;

	/** 支付类型*/
	private String pay_type;

	/** 支付ip*/
	private String trans_ip;

	/** mac码*/
	private String mac;

	/** 支付载体*/
	private String pay_carrier;

	/** IMEI码*/
	private String IMEI_NO;

	/** IMSI码*/
	private String IMSI_NO;

	/** CPU码*/
	private String CPU_NO;

	/** 命中规则*/
	private String rule_detail;

	/** 规则返回结果*/
	private String rule_result;

	/** 创建时间*/
	private String create_time;

	/** 交易状态*/
	private String status;


	/** 完成时间*/
	private String complete_time;



	public String getComplete_time() {
		return complete_time;
	}

	public void setComplete_time(String complete_time) {
		this.complete_time = complete_time;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the optimistic
	 */
	public Integer getOptimistic() {
		return optimistic;
	}

	/**
	 * @param optimistic the optimistic to set
	 */
	public void setOptimistic(Integer optimistic) {
		this.optimistic = optimistic;
	}

	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}

	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	/**
	 * @return the payment_id
	 */
	public String getPayment_id() {
		return payment_id;
	}

	/**
	 * @param payment_id the payment_id to set
	 */
	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}

	/**
	 * @return the customer_no
	 */
	public String getCustomer_no() {
		return customer_no;
	}

	/**
	 * @param customer_no the customer_no to set
	 */
	public void setCustomer_no(String customer_no) {
		this.customer_no = customer_no;
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
	 * @return the bus_type
	 */
	public String getBus_type() {
		return bus_type;
	}

	/**
	 * @param bus_type the bus_type to set
	 */
	public void setBus_type(String bus_type) {
		this.bus_type = bus_type;
	}

	/**
	 * @return the user_pan
	 */
	public String getUser_pan() {
		return user_pan;
	}

	/**
	 * @param user_pan the user_pan to set
	 */
	public void setUser_pan(String user_pan) {
		this.user_pan = user_pan;
	}

	/**
	 * @return the user_idno
	 */
	public String getUser_idno() {
		return user_idno;
	}

	/**
	 * @param user_idno the user_idno to set
	 */
	public void setUser_idno(String user_idno) {
		this.user_idno = user_idno;
	}

	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * @return the user_phone
	 */
	public String getUser_phone() {
		return user_phone;
	}

	/**
	 * @param user_phone the user_phone to set
	 */
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	/**
	 * @return the card_type
	 */
	public String getCard_type() {
		return card_type;
	}

	/**
	 * @param card_type the card_type to set
	 */
	public void setCard_type(String card_type) {
		this.card_type = card_type;
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
	 * @return the pay_type
	 */
	public String getPay_type() {
		return pay_type;
	}

	/**
	 * @param pay_type the pay_type to set
	 */
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	/**
	 * @return the trans_ip
	 */
	public String getTrans_ip() {
		return trans_ip;
	}

	/**
	 * @param trans_ip the trans_ip to set
	 */
	public void setTrans_ip(String trans_ip) {
		this.trans_ip = trans_ip;
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
	 * @return the pay_carrier
	 */
	public String getPay_carrier() {
		return pay_carrier;
	}

	/**
	 * @param pay_carrier the pay_carrier to set
	 */
	public void setPay_carrier(String pay_carrier) {
		this.pay_carrier = pay_carrier;
	}

	/**
	 * @return the iMEI_NO
	 */
	public String getIMEI_NO() {
		return IMEI_NO;
	}

	/**
	 * @param iMEI_NO the iMEI_NO to set
	 */
	public void setIMEI_NO(String iMEI_NO) {
		IMEI_NO = iMEI_NO;
	}

	/**
	 * @return the iMSI_NO
	 */
	public String getIMSI_NO() {
		return IMSI_NO;
	}

	/**
	 * @param iMSI_NO the iMSI_NO to set
	 */
	public void setIMSI_NO(String iMSI_NO) {
		IMSI_NO = iMSI_NO;
	}

	/**
	 * @return the cPU_NO
	 */
	public String getCPU_NO() {
		return CPU_NO;
	}

	/**
	 * @param cPU_NO the cPU_NO to set
	 */
	public void setCPU_NO(String cPU_NO) {
		CPU_NO = cPU_NO;
	}

	/**
	 * @return the rule_detail
	 */
	public String getRule_detail() {
		return rule_detail;
	}

	/**
	 * @param rule_detail the rule_detail to set
	 */
	public void setRule_detail(String rule_detail) {
		this.rule_detail = rule_detail;
	}

	/**
	 * @return the rule_result
	 */
	public String getRule_result() {
		return rule_result;
	}

	/**
	 * @param rule_result the rule_result to set
	 */
	public void setRule_result(String rule_result) {
		this.rule_result = rule_result;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
