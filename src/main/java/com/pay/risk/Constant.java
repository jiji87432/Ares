package com.pay.risk;

public class Constant {
	// 规则内容的类型
	/** 规则内容为SQL */
	public static final String CONTENT_TYPE_SQL = "SQL";

	/** 规则内容为本类的方法 */
	public static final String CONTENT_TYPE_METHOD = "METHOD";

	/** 规则内容格式定义有误 */
	public static final String CONTENT_TYPE_ERROR = "ERROR";

	/** 白名单业务类型 - 商户 */
	public static final String WHITELIST_BUSINESSTYPE_CUSTOMER = "CUSTOMER";
	/** 白名单业务范围 - 风控 */
	public static final String WHITELIST_SCOPE_RISK = "RISK";
	/** Excel 文件后缀名 - xls */
	public static final String EXCEL_SUFFIX_XLS = ".xls";
	/** Excel 文件后缀名 - xlsx */
	public static final String EXCEL_SUFFIX_XLSX = ".xlsx";

	/** 后台操作处理结果标识 - 成功 */
	public static final String OPERATE_RESULT_SUCCESS = "SUCCESS";
	/** 后台操作处理结果标识 - 失败 */
	public static final String OPERATE_RESULT_ERROR = "ERROR";
	/** 通用错误明细信息变量名 */
	public static final String ERROR_INFO_DETAIL = "ERROR_INFO_DETAIL";

	/** 案前处理措施，调接口 */
	public static final String BEFORE_INTERFACE_HANDLE = "C008,R002,C032,CM001";

	/** 案前处理措施，本系统 */
	public static final String BEFORE_LOCAL_HANDLE = "WARN";

	/*
	 *
	 * */
	/** 数据库表字段名 */
	public static final String TBL_FIELDS_TITLE = "title";
	/** 数据库表字段展示名 */
	public static final String TBL_FIELDS_LABLE = "lable";

	/** Excel单元格为空（NULL） */
	public static final String EXCEL_CELL_VALUE_NULL = "EXCEL_CELL_VALUE_NULL";

	/** 黑名单 - 交易限制条件缓存KEY前缀 */
	public static final String PREFX_TRNSCTN_RSTRCS = "TRNSCTN_RSTRCS";

	/** 跨省交易标识 */
	public static final String CROSS_PROVINCE_TRANS_TAG = "KS";

	/** 持有者角色 - 商户 */
	public static final String OWNER_ROLE_CUSTOMER = "CUSTOMER";

	/** 商户基本信息查询步长 */
	public static final int QUERY_NUM = 10000;

	/** 商户基本信息查询添加标识 */
	public static final String QUERY_STR = "_LEVEL";

	/** 商户评级系统redis的index 本地测试5上线为0 */
	public static final Integer DB_INDEX = 0;

	/*
	 * MQ工具配置信息
	 */
	/** 消费者配置文件名称 */
	public static final String ASTROTRAIN_CONSUMER_CONFIG_KEY = "astrotrain-consumer";
	/** 订单交易同步topic标识名 */
	public static final String ASTROTRAIN_CONSUMER_REGIST_TOPIC_KEY = "consumer.regist.topic";
	/** MQ数据同步环境（正式生产还是生产测试testing） */
	public static final String ASTROTRAIN_CONSUMER_MQDATA_MODULE_FLAG_KEY = "consumer.mqdata.module";
	/** MQ数据同步环境（生产测试testing） */
	public static final String ASTROTRAIN_MQDATAMODULE_VALUE_TEST = "testing";

	/** 消费端数据文件存储路径 */
	public static final String STORE_DIRECTORY_KEY = "data.consumer.localdir";
	/** 消费端最大文件大小 */
	public static final String CONSUMER_MAXMESSAGE_KEY = "data.consumer.maxmessage";
	/** 消费端每次拉取文件大小 */
	public static final String CONSUMER_PULLSIZE_KEY = "data.consumer.pullsize";
	/***/
	public static final String CONSUMER_CORSIZE_KEY = "astrotrain.consumer.corSize";
	/** 频率 */
	public static final String CONSUMER_DEFAULTRATE_KEY = "astrotrain.consumer.defaultRate";

	/**
	 * 时间格式化格式
	 * <p>
	 * yyyy-MM-dd
	 */
	public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
	/**
	 * 时间格式化格式
	 * <p>
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

	/** MQ数据核对短信发送 **/
	public static final String MQDATA_ALARM_PHONENO = "13426375229";

	/** 线上订单同步短信警告 **/
	public static final String ONLINE_ORDER_PHONENO = "18301332850,13051060771";

	/** 系统logger开头 */
	public static final String AresMsg = "Ares msg";

	/** 系统logger error 开头 */
	public static final String AresErr = "Ares err";

	public static final String MQJedis = "redisMQ";

	public static final String ApolloJedis = "redisApollo";

	public static final String AresJedis = "redisAres";

	public static final String TiledJedis = "redisTiled";

	public static final String PaiJedis = "redisPai";

	public static final String CDLJedis = "redisCDL";

	/** 判断秒到风控执行模式标志位 */
	public static String secondCashFlag = "Y";

	public static final String reversalCount = "_reversal_count";

	public static final String reversalAmount = "_reversal_amount";

	public static final String balanceInquiryCount = "_balanceinquiry_count";

	public static final String balanceInquiryPan = "_balanceinquiry_pan";

	public static final String purchaseRefundCount = "_purchaserefund_count";

	public static final String initPan = "_initpan";

	public static final String E000046 = "E000046";

	/* 除00外的其他返回码 (96 返回码 只含000129) 涉案金额 */
	public static final String SAJEX = "_SAJEX_000129";

	/* 除00外的其他返回码 (96 返回码 只含000129) 涉案笔数 */
	public static final String SABSX = "_SABSX_000129";

	/** 扫码商户常用量 **/
	public static final String WECHAT = "WX";
	public static final String ALIPAY = "ALIPAY";
	public static final String UNIONPAY = "UNIONPAY";
	public static final String SWEEP_ZS = "_ZS";
	public static final String SWEEP_BS = "_BS";
	public static final String SWEEP_H5 = "_H5";
	/** 限制维度 **/
	public static final String LIMIT_DAY = "_DAY";
	public static final String LIMIT_SINGLE = "_SINGLE";
	/** 状态 **/
	public static final String Y = "Y";
	public static final String N = "N";
	/** 通用key后缀 **/
	public static final String COMMON_KEY = "_COMMON_SCAN_CODE_KEY";
	public static final String COMMON_LIMIT = "_SM_COMMON_LIMIT";
	public static final String TAG_KEY = "_SPECIAL_SCAN_CODE_CUSTOMER_TAG_LIST";
	/** 对应规则值 **/
	public static final String NODATA = "nodata";
	/** 下划线 **/
	public static final String SYMBOL = "_";

	/** 特定时间限制商户交易笔数常用量 **/
	/* redis中存放限制参数的key，hash结构 */
	public static final String REDIS_TIME_LIMIT_RULE_KEY = "COMMON_CUSTOMER_NO_LIMIT_RULE_TIME";
	/* redis中存放已生效的阀值跟累计值的key */
	public static final String REDIS_TIME_LIMIT_COUNT_KEY = "COMMON_CUSTOMER_NO_LIMIT_TRADE_TIMING";
	/* 是否生效的标识 */
	public static final String REDIS_TIME_LIMIT_EFFECTIVE_FLAG = "COMMON_LIMIT_STATUS";
	/* 商编 */
	public static final String CUSTOMER_NO = "customer_no";
	/* 支付类型 */
	public static final String PAY_TYPE = "pay_type";
	/* 相应规则判断的key */
	public static final String COMMON_TIME_LIMIT_STATUS = "COMMON_TIME_LIMIT_STATUS";
	/* 起始时间 */
	public static final String START_HOUR = "START_HOUR";
	/* 限制时间长度 */
	public static final String TIME_LONG = "TIME_LONG";
	/* 限制支付类型 */
	public static final String LIMIT_TYPE = "LIMIT_TYPE";
	/* 限制的维度，目前是累计，也就是：_count */
	public static final String LIMIT_MODE = "LIMIT_MODE";
	public static final String COUNT = "_COUNT";
	/* 限制值 */
	public static final String LIMIT_NUM = "LIMIT_NUM";

}
