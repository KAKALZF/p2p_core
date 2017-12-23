package com.xmg.springboot.p2p.base.mapper;

import java.util.List;

import com.xmg.springboot.p2p.base.Query.RealAuthQueryObject;
import com.xmg.springboot.p2p.base.domain.RealAuth;

public interface RealAuthMapper {

	int insert(RealAuth record);

	RealAuth selectByPrimaryKey(Long id);

	int updateByPrimaryKey(RealAuth record);

	int queryForCount(RealAuthQueryObject qo);

	List<RealAuth> query(RealAuthQueryObject qo);
}