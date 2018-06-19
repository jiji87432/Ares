/*
 * 文 件 名:  CustomerStatus.java
 * 版    权:  支付有限公司. Copyright 2011-2014,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  tao.zhang
 * 修改时间:  2014-8-27
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.pay.risk.enums;

/**
 * 商户基本状态
 * <p>
 * @author tao.zhang
 * @version [V1.0, 2014-8-27]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public enum CustomerStatus {
/** 可用 */
TRUE,
/** 禁用 */
FALSE,
/** 待提交 */
INIT,
/** 待审核 */
WAIT_AUDIT,
/** 审核中 */
AUDITING,
/** 审核拒绝 */
AUDIT_REJECT,
/** 注销 */
DELETE,
/** 挂起 */
SUSPEND,
/** 待开通 */
WAIT_OPEN,

PRETRANS // 预交易
}
