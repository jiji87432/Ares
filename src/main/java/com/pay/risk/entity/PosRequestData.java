/*
 * 文 件 名:  PosRequestData.java
 * 版    权:  支付有限公司. Copyright 2011-2015,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  tao.zhang
 * 修改时间:  2015-2-12
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.pay.risk.entity;

import java.util.Date;

/**
 * POS交易请求MQ同步数据报文实体类
 * <p>
 * @author tao.zhang
 * @version [V1.0, 2015-2-12]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PosRequestData extends AutoIDEntity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1935971425748739712L;
    private String transType; // 交易类型
    private String pan; // 卡号
    private Double amount; // 交易金额
    private String shopNo; // 网点编号
    private String posCati; // 终端号
    private String posBatch; // POS批次号
    private String posRequestId; // POS流水号
    private String operator; // 操作员
    private Date createTime; // 创建时间
    private Date completeTime; // 完成时间
    private String externalId; // 系统跟踪号/参考号
    private String currencyType; // 货币类型
    private String responseCode; // 返回码
    private String exceptionCode; // 异常码
    private String requestIp; // 请求IP
    private String callPhoneNo; // 来电号码
    private String baseStationInfo; // 基站信息
    private String simCard; // sim 卡信息
    private String remark; // 备注，暂时保存主机信息
    private String customerNo; // 商户号
    private String cardTrackInfo; // 卡的磁道信息及操作记录
    private String locationInfo; // 交易位置信息

    // 原交易信息类
    private String sourceBatchNo; // 原交易批次号
    private String sourcePosRequestId; // 原交易流水号
    private String sourceAuthorizationCode; // 原授权号
    private String sourceTransDate; // 原交易日期MMDD

    /**
     * 获取 transType
     * @return 返回 transType
     */
    public String getTransType() {
        return transType;
    }

    /**
     * 设置 transType
     * @param 对transType进行赋值
     */
    public void setTransType(String transType) {
        this.transType = transType;
    }

    /**
     * 获取 pan
     * @return 返回 pan
     */
    public String getPan() {
        return pan;
    }

    /**
     * 设置 pan
     * @param 对pan进行赋值
     */
    public void setPan(String pan) {
        this.pan = pan;
    }

    /**
     * 获取 amount
     * @return 返回 amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * 设置 amount
     * @param 对amount进行赋值
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * 获取 shopNo
     * @return 返回 shopNo
     */
    public String getShopNo() {
        return shopNo;
    }

    /**
     * 设置 shopNo
     * @param 对shopNo进行赋值
     */
    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    /**
     * 获取 posCati
     * @return 返回 posCati
     */
    public String getPosCati() {
        return posCati;
    }

    /**
     * 设置 posCati
     * @param 对posCati进行赋值
     */
    public void setPosCati(String posCati) {
        this.posCati = posCati;
    }

    /**
     * 获取 posBatch
     * @return 返回 posBatch
     */
    public String getPosBatch() {
        return posBatch;
    }

    /**
     * 设置 posBatch
     * @param 对posBatch进行赋值
     */
    public void setPosBatch(String posBatch) {
        this.posBatch = posBatch;
    }

    /**
     * 获取 posRequestId
     * @return 返回 posRequestId
     */
    public String getPosRequestId() {
        return posRequestId;
    }

    /**
     * 设置 posRequestId
     * @param 对posRequestId进行赋值
     */
    public void setPosRequestId(String posRequestId) {
        this.posRequestId = posRequestId;
    }

    /**
     * 获取 operator
     * @return 返回 operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * 设置 operator
     * @param 对operator进行赋值
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 获取 createTime
     * @return 返回 createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 createTime
     * @param 对createTime进行赋值
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 completeTime
     * @return 返回 completeTime
     */
    public Date getCompleteTime() {
        return completeTime;
    }

    /**
     * 设置 completeTime
     * @param 对completeTime进行赋值
     */
    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    /**
     * 获取 externalId
     * @return 返回 externalId
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * 设置 externalId
     * @param 对externalId进行赋值
     */
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    /**
     * 获取 currencyType
     * @return 返回 currencyType
     */
    public String getCurrencyType() {
        return currencyType;
    }

    /**
     * 设置 currencyType
     * @param 对currencyType进行赋值
     */
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    /**
     * 获取 responseCode
     * @return 返回 responseCode
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * 设置 responseCode
     * @param 对responseCode进行赋值
     */
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * 获取 exceptionCode
     * @return 返回 exceptionCode
     */
    public String getExceptionCode() {
        return exceptionCode;
    }

    /**
     * 设置 exceptionCode
     * @param 对exceptionCode进行赋值
     */
    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    /**
     * 获取 requestIp
     * @return 返回 requestIp
     */
    public String getRequestIp() {
        return requestIp;
    }

    /**
     * 设置 requestIp
     * @param 对requestIp进行赋值
     */
    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    /**
     * 获取 callPhoneNo
     * @return 返回 callPhoneNo
     */
    public String getCallPhoneNo() {
        return callPhoneNo;
    }

    /**
     * 设置 callPhoneNo
     * @param 对callPhoneNo进行赋值
     */
    public void setCallPhoneNo(String callPhoneNo) {
        this.callPhoneNo = callPhoneNo;
    }

    /**
     * 获取 baseStationInfo
     * @return 返回 baseStationInfo
     */
    public String getBaseStationInfo() {
        return baseStationInfo;
    }

    /**
     * 设置 baseStationInfo
     * @param 对baseStationInfo进行赋值
     */
    public void setBaseStationInfo(String baseStationInfo) {
        this.baseStationInfo = baseStationInfo;
    }

    /**
     * 获取 simCard
     * @return 返回 simCard
     */
    public String getSimCard() {
        return simCard;
    }

    /**
     * 设置 simCard
     * @param 对simCard进行赋值
     */
    public void setSimCard(String simCard) {
        this.simCard = simCard;
    }

    /**
     * 获取 remark
     * @return 返回 remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 remark
     * @param 对remark进行赋值
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取 customerNo
     * @return 返回 customerNo
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置 customerNo
     * @param 对customerNo进行赋值
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取 cardTrackInfo
     * @return 返回 cardTrackInfo
     */
    public String getCardTrackInfo() {
        return cardTrackInfo;
    }

    /**
     * 设置 cardTrackInfo
     * @param 对cardTrackInfo进行赋值
     */
    public void setCardTrackInfo(String cardTrackInfo) {
        this.cardTrackInfo = cardTrackInfo;
    }

    /**
     * 获取 locationInfo
     * @return 返回 locationInfo
     */
    public String getLocationInfo() {
        return locationInfo;
    }

    /**
     * 设置 locationInfo
     * @param 对locationInfo进行赋值
     */
    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    /**
     * 获取 sourceBatchNo
     * @return 返回 sourceBatchNo
     */
    public String getSourceBatchNo() {
        return sourceBatchNo;
    }

    /**
     * 设置 sourceBatchNo
     * @param 对sourceBatchNo进行赋值
     */
    public void setSourceBatchNo(String sourceBatchNo) {
        this.sourceBatchNo = sourceBatchNo;
    }

    /**
     * 获取 sourcePosRequestId
     * @return 返回 sourcePosRequestId
     */
    public String getSourcePosRequestId() {
        return sourcePosRequestId;
    }

    /**
     * 设置 sourcePosRequestId
     * @param 对sourcePosRequestId进行赋值
     */
    public void setSourcePosRequestId(String sourcePosRequestId) {
        this.sourcePosRequestId = sourcePosRequestId;
    }

    /**
     * 获取 sourceAuthorizationCode
     * @return 返回 sourceAuthorizationCode
     */
    public String getSourceAuthorizationCode() {
        return sourceAuthorizationCode;
    }

    /**
     * 设置 sourceAuthorizationCode
     * @param 对sourceAuthorizationCode进行赋值
     */
    public void setSourceAuthorizationCode(String sourceAuthorizationCode) {
        this.sourceAuthorizationCode = sourceAuthorizationCode;
    }

    /**
     * 获取 sourceTransDate
     * @return 返回 sourceTransDate
     */
    public String getSourceTransDate() {
        return sourceTransDate;
    }

    /**
     * 设置 sourceTransDate
     * @param 对sourceTransDate进行赋值
     */
    public void setSourceTransDate(String sourceTransDate) {
        this.sourceTransDate = sourceTransDate;
    }

    /**
     * <重载方法一句话功能简述>
     * <功能详细描述>
     * @return
     */
    @Override
    public String toString() {
        return "PosRequestData [transType=" + transType + ", pan=" + pan + ", amount=" + amount + ", shopNo=" + shopNo + ", posCati=" + posCati + ", posBatch="
                + posBatch + ", posRequestId=" + posRequestId + ", operator=" + operator + ", createTime=" + createTime + ", completeTime=" + completeTime
                + ", externalId=" + externalId + ", currencyType=" + currencyType + ", responseCode=" + responseCode + ", exceptionCode=" + exceptionCode
                + ", requestIp=" + requestIp + ", callPhoneNo=" + callPhoneNo + ", baseStationInfo=" + baseStationInfo + ", simCard=" + simCard + ", remark="
                + remark + ", customerNo=" + customerNo + ", cardTrackInfo=" + cardTrackInfo + ", locationInfo=" + locationInfo + ", sourceBatchNo=" + sourceBatchNo
                + ", sourcePosRequestId=" + sourcePosRequestId + ", sourceAuthorizationCode=" + sourceAuthorizationCode + ", sourceTransDate=" + sourceTransDate
                + "]";
    }

}
