package com.xmg.springboot.p2p.business.mapper;

import java.util.List;

import com.xmg.springboot.p2p.business.domain.PlatformBankInfo;
import com.xmg.springboot.p2p.business.query.PlatformBankInfoQueryObject;

public interface PlatformBankInfoMapper {

	int insert(PlatformBankInfo record);

	PlatformBankInfo selectByPrimaryKey(Long id);

	int updateByPrimaryKey(PlatformBankInfo record);

	int queryForCount(PlatformBankInfoQueryObject qo);

	List<PlatformBankInfo> query(PlatformBankInfoQueryObject qo);

	List<PlatformBankInfo> selectAll();
}