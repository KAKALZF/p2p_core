package com.xmg.springboot.p2p.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.business.domain.PlatformBankInfo;
import com.xmg.springboot.p2p.business.mapper.PlatformBankInfoMapper;
import com.xmg.springboot.p2p.business.query.PlatformBankInfoQueryObject;
import com.xmg.springboot.p2p.business.service.IPlatformBankInfoService;

@Service
public class PlatformBankInfoServiceImpl implements IPlatformBankInfoService {
	@Autowired
	private PlatformBankInfoMapper bankInfoMapper;

	@Override
	public PageResult query(PlatformBankInfoQueryObject qo) {
		int count = bankInfoMapper.queryForCount(qo);
		if (count > 0) {
			List<PlatformBankInfo> list = bankInfoMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(),
					qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());

	}

	@Override
	public void saveOrUpdate(PlatformBankInfo bankInfo) {
		if (bankInfo.getId() == null) {
			//新增
			bankInfoMapper.insert(bankInfo);
		} else {
			//编辑
			bankInfoMapper.updateByPrimaryKey(bankInfo);
		}

	}

	@Override
	public List<PlatformBankInfo> listAll() {
		return bankInfoMapper.selectAll();
	}
}
