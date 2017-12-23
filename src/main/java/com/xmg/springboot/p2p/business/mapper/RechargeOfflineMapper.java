package com.xmg.springboot.p2p.business.mapper;

import java.util.List;

import com.xmg.springboot.p2p.business.domain.RechargeOffline;
import com.xmg.springboot.p2p.business.query.RechargeOfflineQueryObject;

public interface RechargeOfflineMapper {

	int insert(RechargeOffline record);

	RechargeOffline selectByPrimaryKey(Long id);

	List<RechargeOffline> selectAll();

	int updateByPrimaryKey(RechargeOffline record);

	int queryForCount(RechargeOfflineQueryObject qo);

	List<RechargeOffline> query(RechargeOfflineQueryObject qo);
}