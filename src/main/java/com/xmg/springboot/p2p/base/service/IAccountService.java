package com.xmg.springboot.p2p.base.service;

import com.xmg.springboot.p2p.base.domain.Account;
import com.xmg.springboot.p2p.base.domain.Logininfo;

/**
 * 用户账户服务
 * @author 1
 *
 */
public interface IAccountService {
	/**
	 * 所有修改Account的操作必须调用此方法,要控制乐观锁
	 * @param userinfo
	 */
	void update(Account acocunt);

	/**
	 * 初始化账户
	 */
	void init(Logininfo li);

	Account get(Long id);
}
