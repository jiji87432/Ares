/**
 *
 */
package com.pay.risk.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pay.risk.dao.OlrOrderDimDao;
import com.pay.risk.entity.OlrOrderDim;
import com.pay.risk.service.OlrOrderDimService;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrOrderDimServiceImpl 此处填写需要参考的类
 * @version 2015年8月13日 上午11:49:36
 * @author ximin.yi
 */
@Service("olrOrderDimService")
public class OlrOrderDimServiceImpl implements OlrOrderDimService {
	@Resource
	private OlrOrderDimDao olrOrderDimDao;


	@Override
	public void saveOlrOrderDim(OlrOrderDim olrOrderDim) {
		olrOrderDimDao.saveOlrOrderDim(olrOrderDim);

	}


	@Override
	public void updateOlrOrderDim(OlrOrderDim olrOrderDim) {
		olrOrderDimDao.updateOlrOrderDim(olrOrderDim);

	}


	@Override
	public OlrOrderDim getOlrOrderDimById(Long id) {

		return olrOrderDimDao.getOlrOrderDimById(id);
	}


	@Override
	public void deleteOlrOrderDimById(Long id) {
		olrOrderDimDao.deleteOlrOrderDimById(id);

	}

}
