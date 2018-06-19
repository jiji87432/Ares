/**
 *
 */
package com.pay.risk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pay.risk.dao.OlrModelDao;
import com.pay.risk.entity.OlrModel;
import com.pay.risk.service.OlrModelService;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: OlrModelServiceImpl 此处填写需要参考的类
 * @version 2015年8月13日 下午1:33:47
 * @author zikun.liu
 */
@Service("olrModelService")
public class OlrModelServiceImpl implements OlrModelService {

	@Autowired
	private OlrModelDao olrModelDao;

	@Override
	public OlrModel getModelByCode(String code) {
		return olrModelDao.getModelByCode(code);
	}

}
