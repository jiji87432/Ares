/**
 *
 */
package com.pay.risk.bean;

import java.io.Serializable;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: PaymentDelBean 此处填写需要参考的类
 * @version 2015年11月4日 下午4:05:33
 * @author xiaohui.wei
 */
public class PaymentDelBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -6584690060326729845L;

	private String matchesStr; // 匹配的key值

	private int offset; // 指定字符的索引,从后往前数,从1开始数

	private int frontIndex; //指定字符的索引,从前往后数,从0开始数

	private int dateSize; // 天 -- 删除多少天之外的信息


	public String getMatchesStr() {
		return matchesStr;
	}

	public void setMatchesStr(String matchesStr) {
		this.matchesStr = matchesStr;
	}


	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getDateSize() {
		return dateSize;
	}

	public void setDateSize(int dateSize) {
		this.dateSize = dateSize;
	}



	public int getFrontIndex() {
		return frontIndex;
	}

	public void setFrontIndex(int frontIndex) {
		this.frontIndex = frontIndex;
	}

	/**
	 * @param matchesStr
	 * @param offset
	 * @param dateSize
	 */
	public PaymentDelBean(String matchesStr, int offset, int dateSize) {

		this.matchesStr = matchesStr;
		this.offset = offset;
		this.dateSize = dateSize;
	}


	public PaymentDelBean(String matchesStr, int frontIndex) {
		super();
		this.matchesStr = matchesStr;
		this.frontIndex = frontIndex;
	}





}
