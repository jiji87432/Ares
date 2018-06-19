/*
 * 文 件 名:  CategoryExeType.java
 * 版    权:  支付有限公司. Copyright 2011-2014,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  tao.zhang
 * 修改时间:  2014-8-29
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.pay.risk.enums;

/**
 * 风险类别执行方式
 * <p>
 * @author tao.zhang
 * @version [版本号, 2014-8-29]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public enum CategoryExecType {
/** 按天执行，起止时间为执行计划修改日凌晨零点至执行计划生效日凌晨零点 */
DAY,
/** 定时执行，包含T1, T2 两种具体方式。只作为标识，不作为具体值使用。 */
TIMER,
/** 定时执行方式一，依赖型定时的起始筛选时间为同一风险类别执行计划列表中距该条执行计划时间轴上最近的筛选截止时间！ */
T1,
/** 定时执行方式二，非依赖型定时的起始筛选时间为该风险类别执行计划列表生效日零时！ */
T2
}