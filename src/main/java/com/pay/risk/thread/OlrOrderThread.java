/**
 *
 */
package com.pay.risk.thread;

import com.pay.risk.entity.OlrOrderDim;
import com.pay.risk.service.OlrOrderDimService;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrOrderThread 此处填写需要参考的类
 * @version 2015年8月13日 上午11:49:36
 * @author zikun.liu
 */
public class OlrOrderThread implements Runnable {

	private OlrOrderDimService olrOrderDimService;

	private OlrOrderDim olrOrderDim;

	/**
	 * @return the olrOrderDimService
	 */
	public OlrOrderDimService getOlrOrderDimService() {
		return olrOrderDimService;
	}

	/**
	 * @param olrOrderDimService the olrOrderDimService to set
	 */
	public void setOlrOrderDimService(OlrOrderDimService olrOrderDimService) {
		this.olrOrderDimService = olrOrderDimService;
	}

	/**
	 * @return the olrOrderDim
	 */
	public OlrOrderDim getOlrOrderDim() {
		return olrOrderDim;
	}

	/**
	 * @param olrOrderDim the olrOrderDim to set
	 */
	public void setOlrOrderDim(OlrOrderDim olrOrderDim) {
		this.olrOrderDim = olrOrderDim;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		olrOrderDimService.saveOlrOrderDim(olrOrderDim);
	}

}
