package com.xmg.springboot.p2p.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.springboot.p2p.base.Query.IpLogQueryObject;
import com.xmg.springboot.p2p.base.domain.IpLog;
import com.xmg.springboot.p2p.base.mapper.IpLogMapper;
import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.base.service.IIpLogService;

@Service
public class IpLogServiceImpl implements IIpLogService {
	@Autowired
	private IpLogMapper ipLogMapper;

	@Override
	public PageResult query(IpLogQueryObject qo) {
		int count = ipLogMapper.queryForCount(qo);
		if (count > 0) {
			List<IpLog> list = ipLogMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(),
					qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

}
