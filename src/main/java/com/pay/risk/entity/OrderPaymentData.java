/*
 * 文 件 名:  OrderPaymentData.java
 * 版    权:  支付有限公司. Copyright 2011-2014,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  tao.zhang
 * 修改时间:  2014-12-16
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.pay.risk.entity;

import java.util.Date;

/**
 * 订单交易MQ同步数据报文实体类
 * <p>
 * @author tao.zhang
 * @version [V1.0, 2014-12-16]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class OrderPaymentData extends AutoIDEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1054159800549991478L;
    /** order表交易金额 */
    private String amount;
    /** payment表交易金额 */
    private String payAmount;
    /** 订单授权码 */
    private String authorizationCode;
    /** 交易授权码 */
    private String payAuthorizationCode;
    /** 银行批次号 */
    private String bankBatch;
    /** 银行通道 */
    private String bankChannel;
    /** 银行通道费率 */
    private String bankChannelRate;
    /** bankCost */
    private String bankCost;
    /** 银行商户级别 */
    private String bankCustomerLevel;
    /** 银行商户号 */
    private String bankCustomerNo;
    /** 银行交易日期 */
    private String bankDate;
    /** 银行流水号(37域) */
    private String bankExternalId;
    /** 收单接口银行 */
    private String bankInterface;
    /** 银行接口 */
    private String payBankInterface;
    /** 银行终端号 */
    private String bankPosCati;
    /** 银行请求号(11域) */
    private String bankRequestId;
    /** 银行交易时间 */
    private String bankTime;
    /** 卡类型 */
    private String cardType;
    /** 卡标识号 */
    private String cardVerifyCode;
    /** 订单完成时间 */
    private Date completeTime;
    /** 交易完成时间 */
    private Date payCompleteTime;
    /** 订单创建时间 */
    private Date createTime;
    /** 交易创建时间 */
    private Date payCreateTime;
    /** 入账状态 */
    private String creditStatus;
    /** 入账时间 */
    private Date creditTime;
    /** 币种 */
    private String currencyType;
    /** 商户手续费 */
    private String customerFee;
    /** POS商户号 */
    private String customerNo;
    /** 商家订单号 */
    private String customerOrderCode;
    /** 商户费率 */
    private String customerRate;
    /** 订单描述信息 */
    private String description;
    /** 系统流水号,参考号 */
    private String externalId;
    /** 最后仪表交易ID */
    private String finalPaymentId;
    /** 交易系统ID */
    private String payId;
    /** 发卡银行 */
    private String issuer;
    /** 订单原版本号 */
    private String orderOptimistic;
    /** 交易原版本号 */
    private String payOptimistic;
    /** 交易系统订单ID */
    private String orderId;
    /** 卡号 */
    private String pan;
    /** 交易费用ID */
    private String paymentfeeId;
    /** POS批次号 */
    private String posBatch;
    /** 交易批次号 */
    private String payPosBatch;
    /** POS终端号 */
    private String posCati;
    /** POS流水号 */
    private String posRequestId;
    /** POS流水号 */
    private String payPosRequestId;
    /** 结算时间 */
    private Date settleTime;
    /** 网点编号 */
    private String shopId;
    /** 交易系统原payment */
    private String sourcePaymentId;
    /** 订单状态 */
    private String status;
    /** 交易状态 */
    private String payStatus;
    /** 交易类型 */
    private String transType;
    /** 交易类型 */
    private String payTransType;
    /** MQ消息推送后续采用tags方式 */
    private String tags;
    /** 订单最终收付费 */
    private String orderCustomerFee;
    /** 订单最终的银行成本 */
    private String orderBankCost;
    /** 订单最终的银联商户号 */
    private String orderBankCustomerNo;
    /** mcc */
    private String mcc;

    /**
     * 获取 amount
     * @return 返回 amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * 设置 amount
     * @param 对amount进行赋值
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * 获取 payAmount
     * @return 返回 payAmount
     */
    public String getPayAmount() {
        return payAmount;
    }

    /**
     * 设置 payAmount
     * @param 对payAmount进行赋值
     */
    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    /**
     * 获取 authorizationCode
     * @return 返回 authorizationCode
     */
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * 设置 authorizationCode
     * @param 对authorizationCode进行赋值
     */
    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    /**
     * 获取 payAuthorizationCode
     * @return 返回 payAuthorizationCode
     */
    public String getPayAuthorizationCode() {
        return payAuthorizationCode;
    }

    /**
     * 设置 payAuthorizationCode
     * @param 对payAuthorizationCode进行赋值
     */
    public void setPayAuthorizationCode(String payAuthorizationCode) {
        this.payAuthorizationCode = payAuthorizationCode;
    }

    /**
     * 获取 bankBatch
     * @return 返回 bankBatch
     */
    public String getBankBatch() {
        return bankBatch;
    }

    /**
     * 设置 bankBatch
     * @param 对bankBatch进行赋值
     */
    public void setBankBatch(String bankBatch) {
        this.bankBatch = bankBatch;
    }

    /**
     * 获取 bankChannel
     * @return 返回 bankChannel
     */
    public String getBankChannel() {
        return bankChannel;
    }

    /**
     * 设置 bankChannel
     * @param 对bankChannel进行赋值
     */
    public void setBankChannel(String bankChannel) {
        this.bankChannel = bankChannel;
    }

    /**
     * 获取 bankChannelRate
     * @return 返回 bankChannelRate
     */
    public String getBankChannelRate() {
        return bankChannelRate;
    }

    /**
     * 设置 bankChannelRate
     * @param 对bankChannelRate进行赋值
     */
    public void setBankChannelRate(String bankChannelRate) {
        this.bankChannelRate = bankChannelRate;
    }

    /**
     * 获取 bankCost
     * @return 返回 bankCost
     */
    public String getBankCost() {
        return bankCost;
    }

    /**
     * 设置 bankCost
     * @param 对bankCost进行赋值
     */
    public void setBankCost(String bankCost) {
        this.bankCost = bankCost;
    }

    /**
     * 获取 bankCustomerLevel
     * @return 返回 bankCustomerLevel
     */
    public String getBankCustomerLevel() {
        return bankCustomerLevel;
    }

    /**
     * 设置 bankCustomerLevel
     * @param 对bankCustomerLevel进行赋值
     */
    public void setBankCustomerLevel(String bankCustomerLevel) {
        this.bankCustomerLevel = bankCustomerLevel;
    }

    /**
     * 获取 bankCustomerNo
     * @return 返回 bankCustomerNo
     */
    public String getBankCustomerNo() {
        return bankCustomerNo;
    }

    /**
     * 设置 bankCustomerNo
     * @param 对bankCustomerNo进行赋值
     */
    public void setBankCustomerNo(String bankCustomerNo) {
        this.bankCustomerNo = bankCustomerNo;
    }

    /**
     * 获取 bankDate
     * @return 返回 bankDate
     */
    public String getBankDate() {
        return bankDate;
    }

    /**
     * 设置 bankDate
     * @param 对bankDate进行赋值
     */
    public void setBankDate(String bankDate) {
        this.bankDate = bankDate;
    }

    /**
     * 获取 bankExternalId
     * @return 返回 bankExternalId
     */
    public String getBankExternalId() {
        return bankExternalId;
    }

    /**
     * 设置 bankExternalId
     * @param 对bankExternalId进行赋值
     */
    public void setBankExternalId(String bankExternalId) {
        this.bankExternalId = bankExternalId;
    }

    /**
     * 获取 bankInterface
     * @return 返回 bankInterface
     */
    public String getBankInterface() {
        return bankInterface;
    }

    /**
     * 设置 bankInterface
     * @param 对bankInterface进行赋值
     */
    public void setBankInterface(String bankInterface) {
        this.bankInterface = bankInterface;
    }

    /**
     * 获取 payBankInterface
     * @return 返回 payBankInterface
     */
    public String getPayBankInterface() {
        return payBankInterface;
    }

    /**
     * 设置 payBankInterface
     * @param 对payBankInterface进行赋值
     */
    public void setPayBankInterface(String payBankInterface) {
        this.payBankInterface = payBankInterface;
    }

    /**
     * 获取 bankPosCati
     * @return 返回 bankPosCati
     */
    public String getBankPosCati() {
        return bankPosCati;
    }

    /**
     * 设置 bankPosCati
     * @param 对bankPosCati进行赋值
     */
    public void setBankPosCati(String bankPosCati) {
        this.bankPosCati = bankPosCati;
    }

    /**
     * 获取 bankRequestId
     * @return 返回 bankRequestId
     */
    public String getBankRequestId() {
        return bankRequestId;
    }

    /**
     * 设置 bankRequestId
     * @param 对bankRequestId进行赋值
     */
    public void setBankRequestId(String bankRequestId) {
        this.bankRequestId = bankRequestId;
    }

    /**
     * 获取 bankTime
     * @return 返回 bankTime
     */
    public String getBankTime() {
        return bankTime;
    }

    /**
     * 设置 bankTime
     * @param 对bankTime进行赋值
     */
    public void setBankTime(String bankTime) {
        this.bankTime = bankTime;
    }

    /**
     * 获取 cardType
     * @return 返回 cardType
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * 设置 cardType
     * @param 对cardType进行赋值
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * 获取 cardVerifyCode
     * @return 返回 cardVerifyCode
     */
    public String getCardVerifyCode() {
        return cardVerifyCode;
    }

    /**
     * 设置 cardVerifyCode
     * @param 对cardVerifyCode进行赋值
     */
    public void setCardVerifyCode(String cardVerifyCode) {
        this.cardVerifyCode = cardVerifyCode;
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
     * 获取 payCompleteTime
     * @return 返回 payCompleteTime
     */
    public Date getPayCompleteTime() {
        return payCompleteTime;
    }

    /**
     * 设置 payCompleteTime
     * @param 对payCompleteTime进行赋值
     */
    public void setPayCompleteTime(Date payCompleteTime) {
        this.payCompleteTime = payCompleteTime;
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
     * 获取 payCreateTime
     * @return 返回 payCreateTime
     */
    public Date getPayCreateTime() {
        return payCreateTime;
    }

    /**
     * 设置 payCreateTime
     * @param 对payCreateTime进行赋值
     */
    public void setPayCreateTime(Date payCreateTime) {
        this.payCreateTime = payCreateTime;
    }

    /**
     * 获取 creditStatus
     * @return 返回 creditStatus
     */
    public String getCreditStatus() {
        return creditStatus;
    }

    /**
     * 设置 creditStatus
     * @param 对creditStatus进行赋值
     */
    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    /**
     * 获取 creditTime
     * @return 返回 creditTime
     */
    public Date getCreditTime() {
        return creditTime;
    }

    /**
     * 设置 creditTime
     * @param 对creditTime进行赋值
     */
    public void setCreditTime(Date creditTime) {
        this.creditTime = creditTime;
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
     * 获取 customerFee
     * @return 返回 customerFee
     */
    public String getCustomerFee() {
        return customerFee;
    }

    /**
     * 设置 customerFee
     * @param 对customerFee进行赋值
     */
    public void setCustomerFee(String customerFee) {
        this.customerFee = customerFee;
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
     * 获取 customerOrderCode
     * @return 返回 customerOrderCode
     */
    public String getCustomerOrderCode() {
        return customerOrderCode;
    }

    /**
     * 设置 customerOrderCode
     * @param 对customerOrderCode进行赋值
     */
    public void setCustomerOrderCode(String customerOrderCode) {
        this.customerOrderCode = customerOrderCode;
    }

    /**
     * 获取 customerRate
     * @return 返回 customerRate
     */
    public String getCustomerRate() {
        return customerRate;
    }

    /**
     * 设置 customerRate
     * @param 对customerRate进行赋值
     */
    public void setCustomerRate(String customerRate) {
        this.customerRate = customerRate;
    }

    /**
     * 获取 description
     * @return 返回 description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置 description
     * @param 对description进行赋值
     */
    public void setDescription(String description) {
        this.description = description;
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
     * 获取 finalPaymentId
     * @return 返回 finalPaymentId
     */
    public String getFinalPaymentId() {
        return finalPaymentId;
    }

    /**
     * 设置 finalPaymentId
     * @param 对finalPaymentId进行赋值
     */
    public void setFinalPaymentId(String finalPaymentId) {
        this.finalPaymentId = finalPaymentId;
    }

    /**
     * 获取 payId
     * @return 返回 payId
     */
    public String getPayId() {
        return payId;
    }

    /**
     * 设置 payId
     * @param 对payId进行赋值
     */
    public void setPayId(String payId) {
        this.payId = payId;
    }

    /**
     * 获取 issuer
     * @return 返回 issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * 设置 issuer
     * @param 对issuer进行赋值
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
     * 获取 orderOptimistic
     * @return 返回 orderOptimistic
     */
    public String getOrderOptimistic() {
        return orderOptimistic;
    }

    /**
     * 设置 orderOptimistic
     * @param 对orderOptimistic进行赋值
     */
    public void setOrderOptimistic(String orderOptimistic) {
        this.orderOptimistic = orderOptimistic;
    }

    /**
     * 获取 payOptimistic
     * @return 返回 payOptimistic
     */
    public String getPayOptimistic() {
        return payOptimistic;
    }

    /**
     * 设置 payOptimistic
     * @param 对payOptimistic进行赋值
     */
    public void setPayOptimistic(String payOptimistic) {
        this.payOptimistic = payOptimistic;
    }

    /**
     * 获取 orderId
     * @return 返回 orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置 orderId
     * @param 对orderId进行赋值
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
     * 获取 paymentfeeId
     * @return 返回 paymentfeeId
     */
    public String getPaymentfeeId() {
        return paymentfeeId;
    }

    /**
     * 设置 paymentfeeId
     * @param 对paymentfeeId进行赋值
     */
    public void setPaymentfeeId(String paymentfeeId) {
        this.paymentfeeId = paymentfeeId;
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
     * 获取 payPosBatch
     * @return 返回 payPosBatch
     */
    public String getPayPosBatch() {
        return payPosBatch;
    }

    /**
     * 设置 payPosBatch
     * @param 对payPosBatch进行赋值
     */
    public void setPayPosBatch(String payPosBatch) {
        this.payPosBatch = payPosBatch;
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
     * 获取 payPosRequestId
     * @return 返回 payPosRequestId
     */
    public String getPayPosRequestId() {
        return payPosRequestId;
    }

    /**
     * 设置 payPosRequestId
     * @param 对payPosRequestId进行赋值
     */
    public void setPayPosRequestId(String payPosRequestId) {
        this.payPosRequestId = payPosRequestId;
    }

    /**
     * 获取 settleTime
     * @return 返回 settleTime
     */
    public Date getSettleTime() {
        return settleTime;
    }

    /**
     * 设置 settleTime
     * @param 对settleTime进行赋值
     */
    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }

    /**
     * 获取 shopId
     * @return 返回 shopId
     */
    public String getShopId() {
        return shopId;
    }

    /**
     * 设置 shopId
     * @param 对shopId进行赋值
     */
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    /**
     * 获取 sourcePaymentId
     * @return 返回 sourcePaymentId
     */
    public String getSourcePaymentId() {
        return sourcePaymentId;
    }

    /**
     * 设置 sourcePaymentId
     * @param 对sourcePaymentId进行赋值
     */
    public void setSourcePaymentId(String sourcePaymentId) {
        this.sourcePaymentId = sourcePaymentId;
    }

    /**
     * 获取 status
     * @return 返回 status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置 status
     * @param 对status进行赋值
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取 payStatus
     * @return 返回 payStatus
     */
    public String getPayStatus() {
        return payStatus;
    }

    /**
     * 设置 payStatus
     * @param 对payStatus进行赋值
     */
    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

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
     * 获取 payTransType
     * @return 返回 payTransType
     */
    public String getPayTransType() {
        return payTransType;
    }

    /**
     * 设置 payTransType
     * @param 对payTransType进行赋值
     */
    public void setPayTransType(String payTransType) {
        this.payTransType = payTransType;
    }

    /**
     * 获取 tags
     * @return 返回 tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置 tags
     * @param 对tags进行赋值
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * 获取 orderCustomerFee
     * @return 返回 orderCustomerFee
     */
    public String getOrderCustomerFee() {
        return orderCustomerFee;
    }

    /**
     * 设置 orderCustomerFee
     * @param 对orderCustomerFee进行赋值
     */
    public void setOrderCustomerFee(String orderCustomerFee) {
        this.orderCustomerFee = orderCustomerFee;
    }

    /**
     * 获取 orderBankCost
     * @return 返回 orderBankCost
     */
    public String getOrderBankCost() {
        return orderBankCost;
    }

    /**
     * 设置 orderBankCost
     * @param 对orderBankCost进行赋值
     */
    public void setOrderBankCost(String orderBankCost) {
        this.orderBankCost = orderBankCost;
    }

    /**
     * 获取 orderBankCustomerNo
     * @return 返回 orderBankCustomerNo
     */
    public String getOrderBankCustomerNo() {
        return orderBankCustomerNo;
    }

    /**
     * 设置 orderBankCustomerNo
     * @param 对orderBankCustomerNo进行赋值
     */
    public void setOrderBankCustomerNo(String orderBankCustomerNo) {
        this.orderBankCustomerNo = orderBankCustomerNo;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    /**
     * <重载方法一句话功能简述>
     * <功能详细描述>
     * @return
     */
    @Override
    public String toString() {
        return "OrderPaymentData [amount=" + amount + ", payAmount=" + payAmount + ", authorizationCode=" + authorizationCode + ", payAuthorizationCode="
                + payAuthorizationCode + ", bankBatch=" + bankBatch + ", bankChannel=" + bankChannel + ", bankChannelRate=" + bankChannelRate + ", bankCost="
                + bankCost + ", bankCustomerLevel=" + bankCustomerLevel + ", bankCustomerNo=" + bankCustomerNo + ", bankDate=" + bankDate + ", bankExternalId="
                + bankExternalId + ", bankInterface=" + bankInterface + ", payBankInterface=" + payBankInterface + ", bankPosCati=" + bankPosCati
                + ", bankRequestId=" + bankRequestId + ", bankTime=" + bankTime + ", cardType=" + cardType + ", cardVerifyCode=" + cardVerifyCode
                + ", completeTime=" + completeTime + ", payCompleteTime=" + payCompleteTime + ", createTime=" + createTime + ", payCreateTime=" + payCreateTime
                + ", creditStatus=" + creditStatus + ", creditTime=" + creditTime + ", currencyType=" + currencyType + ", customerFee=" + customerFee
                + ", customerNo=" + customerNo + ", customerOrderCode=" + customerOrderCode + ", customerRate=" + customerRate + ", description=" + description
                + ", externalId=" + externalId + ", finalPaymentId=" + finalPaymentId + ", payId=" + payId + ", issuer=" + issuer + ", orderOptimistic="
                + orderOptimistic + ", payOptimistic=" + payOptimistic + ", orderId=" + orderId + ", pan=" + pan + ", paymentfeeId=" + paymentfeeId + ", posBatch="
                + posBatch + ", payPosBatch=" + payPosBatch + ", posCati=" + posCati + ", posRequestId=" + posRequestId + ", payPosRequestId=" + payPosRequestId
                + ", settleTime=" + settleTime + ", shopId=" + shopId + ", sourcePaymentId=" + sourcePaymentId + ", status=" + status + ", payStatus=" + payStatus
                + ", transType=" + transType + ", payTransType=" + payTransType + ", tags=" + tags + ", orderCustomerFee=" + orderCustomerFee + ", orderBankCost="
                + orderBankCost + ", orderBankCustomerNo=" + orderBankCustomerNo + ", mcc=" + mcc + " ]";
    }

}
