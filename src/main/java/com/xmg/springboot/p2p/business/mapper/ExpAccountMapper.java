package com.xmg.springboot.p2p.business.mapper;

import com.xmg.springboot.p2p.business.domain.ExpAccount;
import org.apache.ibatis.annotations.Param;

public interface ExpAccountMapper {

	int insert(ExpAccount record);

	ExpAccount selectByPrimaryKey(@Param("id") Long id);

	int updateByPrimaryKey(ExpAccount record);
}