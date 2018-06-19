package com.pay.risk.constans;

public class OnLinePayConstants {

	/**
	 * 订单号列表(按日期)
	 */
	public static final String PREFIX_ORDER_LIST = "ON_ORDER_";

	/**
	 * 订单历史
	 */
	public static final String SUFFIX_ORDER_LISTHIS = "_ORDER_HIS";

	/**
	 * 订单历史(月)
	 */
	public static final String SUFFIX_ORDER_LISTHIS_MONTH = "_ORDER_HIS_MONTH";

	/**
	 * 卡类型交易历史
	 */
	public static final String SUFFIX_CARHTYPE_HIS = "_CARHTYPE_HIS";

	/**
	 * 商户日订单
	 */
	public static final String SUFFIX_CUSTOMER_ORDERDATE = "_CUSTOMER_ORDERDATE";

	/**
	 * 流水号列表(按日期)
	 */
	public static final String PREFIX_PAYEMNT_LIST = "ON_PAYMENT_"; // 流水号列表(按日期)
	/**
	 * 订单流水关系维护(按日期)
	 */
	public static final String SUFFIX_ORERDATE_RELATION = "_ORERDATE_RELATION"; //
	/**
	 * 银行卡交易流水关系维护（按日期）
	 */
	public static final String SUFFIX_CARDDATE_RELATION = "_CARDDATE_RELATION"; //
	/**
	 * 银行卡类型流水关系维护（按日期）
	 */
	public static final String SUFFIX_CARDTYPEORDERDATE_RELATAION = "_CARDTYPEORDERDATE_RELATAION"; //

	/**
	 * 商户银行卡类型交易汇总（按日期）
	 */
	public static final String SUFFIX_CUSTOMERCARDTYPE_HIS = "_CUSTOMERCARDTYPE_HIS"; //

	/**
	 * 商户卡交易类型
	 */
	public static final String SUFFIX_CARDTYPEORDER_RELATAION = "_CARDTYPEORDER_RELATAION"; //

	/**
	 * IP交易流水关系维护{按日期}
	 */
	public static final String SUFFIX_IPDATE_RELATION = "_IPDATE_RELATION"; //
	/**
	 * 商户订单流水关系（按日期）
	 * @param customerNO date
	 */
	public static final String SUFFIX_CUSTOME_RPAYMENT_RELATION = "_CUSTOME_RPAYMENT_RELATION"; //

	/**
	 * 电话订单流水关系（按日期）
	 */
	public static final String SUFFIX_PHONEDATE_RELATION = "_PHONEDATE_RELATION"; //

	/**
	 * 身份证订单流水关系（按日期）
	 */
	public static final String SUFFIX_IDCARDNODATE_RELATION = "_IDCARDNODATE_RELATION"; //

	/**
	 * 卡交易地点关系维护
	 */
	public static final String SUFFIX_DES_RELATION = "_DES_RELATION"; // 卡交易地点关系维护
	/**
	 * 商户历史交易信息维护
	 */
	public static final String SUFFIX_PAYMENT_HIS = "_PAYMENT_HIS"; //
	/**
	 * 卡交易历史维护
	 */
	public static final String SUFFIX_CARDTRANS_HIS = "_CARDTRANS_HIS"; //
	/**
	 * IP历史维护
	 */
	public static final String SUFFIX_IPTRANS_HIS = "_IPTRANS_HIS"; //

	/**
	 * 身份证交易历史维护
	 */
	public static final String SUFFIX_IDCARDNOTRANS_HIS = "_IDCARDNOTRANS_HIS"; //
	/**
	 * 电话历史交易维护
	 */
	public static final String SUFFIX_PHONETRANS_HIS = "_PHONETRANS_HIS"; //
	/**
	 * IP订单关系 SET
	 */
	public static final String SUFFIX_IPORDER_RELATAION = "_IPORDER_RELATAION"; //
	/**
	 * 卡订单关系 SET
	 */
	public static final String SUFFIX_CARDORDER_RELATAION = "_CARDORDER_RELATAION"; //

	/**
	 * 手机单关系 SET
	 */
	public static final String SUFFIX_PNONEORDER_RELATAION = "_PNONEORDER_RELATAION"; //
	/**
	 * 身份证单关系 SET
	 */
	public static final String SUFFIX_IDCARDNOORDER_RELATAION = "_IDCARDNOORDER_RELATAION"; //

	/**
	 * 商户黑名单
	 */
	public static final String CUSTOMER_BLACKLIST = "CUSTOMERNO_blacklist"; //
	/**
	 * 电话黑名单
	 */
	public static final String PHONENO_BLACKLIST = "PHONENUMBER_blacklist"; //
	/**
	 * 身份证黑名单
	 */
	public static final String IDCARDNO_BLACKLIST = "IDCARD_blacklist"; //
	/**
	 * 卡黑名单
	 */
	public static final String PAN_BLACKLIST = "PAN_blacklist"; //

	/**
	 * 交易数
	 */
	public static final String _PAYMENTCOUNT = "_PAYMENTCOUNT";

	/**
	 * 交易金额
	 */
	public static final String _PAYMENTAMOUNT = "_PAYMENTAMOUNT";

	/**
	 * 成功交易数
	 */
	public static final String _PAYMENTCOUNTSUCCESS = "_PAYMENTCOUNTSUCCESS";

	/**
	 * 成功交易金额
	 */
	public static final String _PAYMENTAMOUNTSUCCESS = "_PAYMENTAMOUNTSUCCESS";

	/**
	 * 订单笔数
	 */
	public static final String _ORDERCOUNTBYTYPE = "_ORDERCOUNTBYTYPE";

	/**
	 * 订单金额
	 */
	public static final String _ORDERAMOUNTBYTYPE = "_ORDERAMOUNTBYTYPE";

	/**
	 * 订单笔数
	 */
	public static final String _ORDERCOUNT = "_ORDERCOUNT";

	/**
	 * 订单金额
	 */
	public static final String _ORDERAMOUNT = "_ORDERAMOUNT";

	/**
	 * 成功订单笔数
	 */
	public static final String _ORDERCOUNTSUCCESS = "_ORDERCOUNTSUCCESS";

	/**
	 * 成功订单笔数
	 */
	public static final String _ORDERAMOUNTSUCCESS = "_ORDERAMOUNTSUCCESS";

	public static final String RULECODE = "_RULECODE";

	public static final String HANDLECODE = "_HANDLECODE";

	public static final String RULEHANDLERELATION = "_RH";

	public static final String CUSTOMER_ORDER_EXT = "_CUSTOMER_ORDERHIS_EXT";

	/**
	 * 银行卡日期交易类型统计
	 */
	public static final String SUFFIX_PANBYBTYPEDATE = "_PAN_DATE_BYBTYPE";

	/**
	 * 银行卡月交易类型统计
	 */
	public static final String SUFFIX_PANBYBTYPEMONTH = "_PAN_MONTH_BYBTYPE";
	/**
	 * 商户日期交易类型统计
	 */
	public static final String SUFFIX_CUSTOMERBYBTYPEDATE = "_CUSTOMER_DATE_BYBTYPE";
	/**
	 * 商户月交易类型统计
	 */
	public static final String SUFFIX_CUSTOMERBYBTYPEMONTH = "_CUSTOMER_MONTH_BYBTYPE";
	/**
	 * 商户、卡日期交易类型统计
	 */
	public static final String SUFFIX_CPBYTYPEDATE = "_CP_DATE_BYBTYPE";
	/**
	 * 商户、卡月交易类型统计
	 */
	public static final String SUFFIX_CPBYTYPEMONTH = "_CP_MONTH_BYBTYPE";

	public static final String SUFFIX_COUNT = "_COUNT";

	public static final String SUFFIX_SUCCESSCOUNT = "_SUCCESSCOUNT";

	public static final String SUFFIX_AMOUNT = "_AMOUNT";

	public static final String SUFFIX_SUCCESAMOUNT = "_SUCCESSAMOUNT";
	/***
	 * key 861814444_customer_limitbytype
	 * map key
	 * MOBILE_frequency_single_stroke
	 */
	public static final String SUFFIX_CUSTOMER_LIMITBYTYPE = "_customer_limitbytype";

	public static final String SUFFIX_PAN_LIMITBYTYPE = "_pan_limitbytype";

	public static final String SUFFIX_COMMON_CUSTOMER_LIMITBYTYPE = "common_customer_limitbytype";

	public static final String SUFFIX_COMMON_PAN_LIMITBYTYPE = "common_pan_limitbytype";

	public static final String SUFFIX_COMMON_CP_LIMITBYTYPE = "common_limitcpbytype";

	public static final String SUFFIX_CP_LIMITBYTYPE = "_limitcpbytype";

	public static final String FREQUENCY = "frequency";

	public static final String AMOUNT = "amount";

	public static final String SINGLE_STROKE = "single_stroke";

	public static final String SINGLE_DAY = "single_day";

	public static final String SINGLE_MONTH = "single_month";

	/** redis中存放地区码set的后缀 */
	public static final String _TRANS_LIMIT_AREA_SET = "_SHJYDQXZM";

	/** redis中存放地区码数量的后缀 */
	public static final String _TRANS_LIMIT_AREA_COUNT = "_SHJYDQXZS";

	/** rulObj 中map 地区码对应的key */
	public static final String AREA_CODE = "area_code";
	/** rulObj 中map 地区数量对应的key */
	public static final String AREA_CODE_COUNT = "area_code_count";
	/** rulObj 中map 地区set对应的key */
	public static final String AREA_CODE_SET = "area_code_set";
}
