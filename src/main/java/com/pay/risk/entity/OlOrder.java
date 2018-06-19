/**
 *
 */
package com.pay.risk.entity;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlOrder 此处填写需要参考的类
 * @version 2015年8月24日 下午2:45:18
 * @author zikun.liu
 */
public class OlOrder {

	/** 主键id*/
	private Long id;

	/** 版本号*/
	private Integer optimistic;

	/** 商户编号*/
	private String customer_no;

	/** 订单编号*/
	private String order_id;

	/** 金额*/
	private Double amount;

	/** 交易类型*/
	private String bus_type;

	/** 命中规则*/
	private String rule_detail;

	/** 规则结果*/
	private String rule_result;

	/** 创建时间*/
	private String create_time;

	/** 订单状态*/
	private String status;


	/** 完成时间*/
	private String complete_time;

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

	public String getComplete_time() {
		return complete_time;
	}

	public void setComplete_time(String complete_time) {
		this.complete_time = complete_time;
	}


}
