package com.xmg.springboot.p2p.base.mapper;

import com.xmg.springboot.p2p.base.domain.Account;

public interface AccountMapper {

	int insert(Account record);

	Account selectByPrimaryKey(Long id);

	int updateByPrimaryKey(Account record);


}