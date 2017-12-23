package com.xmg.springboot.p2p.base.mapper;

import java.util.List;

import com.xmg.springboot.p2p.base.Query.VedioAuthQueryObject;
import com.xmg.springboot.p2p.base.domain.VedioAuth;

public interface VedioAuthMapper {

	int insert(VedioAuth record);

	VedioAuth selectByPrimaryKey(Long id);

	int queryForCount(VedioAuthQueryObject qo);

	List<VedioAuth> query(VedioAuthQueryObject qo);

}