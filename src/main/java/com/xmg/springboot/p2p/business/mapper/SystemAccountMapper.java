package com.xmg.springboot.p2p.business.mapper;

import com.xmg.springboot.p2p.business.domain.SystemAccount;

public interface SystemAccountMapper {

	SystemAccount selectCurrent();

	int updateByPrimaryKey(SystemAccount systemAccount);
}