/**
 *
 */
package com.pay.risk.entity;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrModel 此处填写需要参考的类
 * @version 2015年8月18日 上午10:18:37
 * @author zikun.liu
 */
public class OlrModel {

	private Long id;

	private Integer optimistic;

	private String code; // 模型编号

	private String name; // 模型名称

	private String prefix; // 前缀

	private String suffix; // 后缀

	private String obj_full; // 对象表达式全称

	private String obj_short; // 队形表达式简称

	private String version; // 缓存版本号

	private String create_time;

	private String status; // 状态


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
	 * @return the obj_full
	 */
	public String getObj_full() {
		return obj_full;
	}

	/**
	 * @param obj_full the obj_full to set
	 */
	public void setObj_full(String obj_full) {
		this.obj_full = obj_full;
	}

	/**
	 * @return the obj_short
	 */
	public String getObj_short() {
		return obj_short;
	}

	/**
	 * @param obj_short the obj_short to set
	 */
	public void setObj_short(String obj_short) {
		this.obj_short = obj_short;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
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

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
