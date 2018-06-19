package com.pay.risk.dubbo;

import org.apache.log4j.Logger;

import com.pay.ares.remote.service.HighAvailabilityServiceFacade;
import com.pay.risk.Constant;

public class HighAvailabilityServiceFacadeImpl implements HighAvailabilityServiceFacade {
	private static final Logger logger = Logger.getLogger(HighAvailabilityServiceFacadeImpl.class);

	/*
	 * (non-Javadoc)
	 * @see com.pay.ares.remote.service.HighAvailabilityServiceFacade#changeStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public String changeStatus(String ip, String mCode, String str) {
		// TODO Auto-generated method stub
		logger.info(Constant.AresMsg + ":ip:" + ip + ",mCode:" + mCode + ",str:" + str);
		Constant.secondCashFlag = str;

		return str;
	}

}
