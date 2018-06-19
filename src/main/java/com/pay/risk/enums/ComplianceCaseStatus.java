package com.pay.risk.enums;

/**
 * 合规案例状态
 * @author rui.wang
 */
public enum ComplianceCaseStatus {
	INIT,//待处理
	AUDITING,//处理中
	ALREADY,//已结案
	CLOSE,//已关闭
	PROCESSCUTOUT, //流程终止
	NOPROCESS //已结案，无需发起流程
}
