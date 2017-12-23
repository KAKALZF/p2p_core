package com.xmg.springboot.p2p.base.service;

import com.xmg.springboot.p2p.base.domain.Logininfo;

/**
 * 登录对象的相关服务
 * @author 1
 *
 */
public interface ILogininfoService {
	/**
	 * 用户注册
	 */
	void register(String username, String password);

	/**
	 * 检查用户名是否存在
	 * true:存在
	 * false:不存在
	 * @param username
	 * @return
	 */
	boolean checkUsernameExist(String username);

	Logininfo login(String username, String password, String ip, int usrType);
}
