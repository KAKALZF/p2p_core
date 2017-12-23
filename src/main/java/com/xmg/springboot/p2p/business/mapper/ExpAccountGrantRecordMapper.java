package com.xmg.springboot.p2p.business.mapper;

import com.xmg.springboot.p2p.business.domain.ExpAccountGrantRecord;

public interface ExpAccountGrantRecordMapper {

	int insert(ExpAccountGrantRecord record);

	ExpAccountGrantRecord selectByPrimaryKey(Long id);

	int updateByPrimaryKey(ExpAccountGrantRecord record);
}