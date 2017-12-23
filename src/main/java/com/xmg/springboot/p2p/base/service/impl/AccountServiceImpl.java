package com.xmg.springboot.p2p.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmg.springboot.p2p.base.domain.Account;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.mapper.AccountMapper;
import com.xmg.springboot.p2p.base.service.IAccountService;

@Service
@Transactional
public class AccountServiceImpl implements IAccountService {
	@Autowired
	private AccountMapper accountMapper;

	@Override
	public void update(Account account) {
		// TODO Auto-generated method stub
		int count = accountMapper.updateByPrimaryKey(account);
		if (count <= 0) {
			throw new RuntimeException("乐观锁失败,AccountId:" + account.getId());
		}
	}

	@Override
	public void init(Logininfo li) {
		Account account = new Account();
		account.setId(li.getId());
		accountMapper.insert(account);
	}

	@Override
	@Transactional(readOnly = true)
	public Account get(Long id) {
		return accountMapper.selectByPrimaryKey(id);
	}
}
