package com.xmg.springboot.p2p.business.service;

import java.util.List;

import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.business.domain.PlatformBankInfo;
import com.xmg.springboot.p2p.business.query.PlatformBankInfoQueryObject;

/**
 * 线下充值银行服务
 * @author 1
 *
 */
public interface IPlatformBankInfoService {
	PageResult query(PlatformBankInfoQueryObject qo);

	void saveOrUpdate(PlatformBankInfo bankInfo);

	List<PlatformBankInfo> listAll();
}
