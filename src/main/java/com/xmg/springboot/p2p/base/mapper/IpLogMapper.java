package com.xmg.springboot.p2p.base.mapper;

import java.util.List;

import com.xmg.springboot.p2p.base.Query.IpLogQueryObject;
import com.xmg.springboot.p2p.base.domain.IpLog;

public interface IpLogMapper {

	int insert(IpLog record);

	int queryForCount(IpLogQueryObject qo);

	List<IpLog> query(IpLogQueryObject qo);
}