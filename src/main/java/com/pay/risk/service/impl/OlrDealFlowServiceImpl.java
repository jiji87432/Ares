/**
 *
 */
package com.pay.risk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.risk.dao.OlrDealFlowDao;
import com.pay.risk.entity.OlrDealFlow;
import com.pay.risk.service.OlrDealFlowService;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrDealFlow 此处填写需要参考的类
 * @version 2015年8月13日 下午1:33:47
 * @author ximin.yi
 */
@Service
public class OlrDealFlowServiceImpl implements OlrDealFlowService {

	@Autowired
	private OlrDealFlowDao olrDealFlowDao;


	@Override
	public void saveOlrDealFlow(OlrDealFlow olrDealFlow) {
		olrDealFlowDao.saveOlrDealFlow(olrDealFlow);

	}

	@Override
	public void updateOlrDealFlow(OlrDealFlow olrDealFlow) {
		olrDealFlowDao.updateOlrDealFlow(olrDealFlow);

	}

	@Override
	public OlrDealFlow getOlrDealFlowById(Long id) {

		return olrDealFlowDao.getOlrDealFlowById(id);
	}

	@Override
	public void deleteOlrDealFlowById(Long id) {
		olrDealFlowDao.deleteOlrDealFlowById(id);

	}

}
