package com.xmg.springboot.p2p.base.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmg.springboot.p2p.base.domain.IpLog;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.mapper.IpLogMapper;
import com.xmg.springboot.p2p.base.mapper.LogininfoMapper;
import com.xmg.springboot.p2p.base.service.IAccountService;
import com.xmg.springboot.p2p.base.service.ILogininfoService;
import com.xmg.springboot.p2p.base.service.IUserinfoService;
import com.xmg.springboot.p2p.business.domain.ExpAccount;
import com.xmg.springboot.p2p.business.service.IExpAccountService;
import com.xmg.springboot.p2p.util.Consts;
import com.xmg.springboot.p2p.util.MD5;

@Service
@Transactional
public class LogininfoServiceImpl implements ILogininfoService {
	@Autowired
	private LogininfoMapper logininfoMapper;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IUserinfoService userinfoService;
	@Autowired
	private IpLogMapper ipLogMapper;
	@Autowired
	private IExpAccountService expAccountService;

	/**
	 * 用户注册
	 * @throws Exception 
	 */
	@Override
	public void register(String username, String password) {
		//判断用户是否存在
		int count = logininfoMapper.countByUsername(username);
		//如果不存在
		if (count <= 0) {
			Logininfo li = new Logininfo();
			li.setUsername(username);
			li.setPassword(MD5.encode(password + "[" + username + "]"));
			li.setState(li.STATE_NORMAL);
			logininfoMapper.insert(li);
			//初始化account和userinfo
			accountService.init(li);
			userinfoService.init(li);
			//创建体验金账户
			ExpAccount expAccount = new ExpAccount();
			expAccount.setId(li.getId());
			expAccountService.save(expAccount);
			//发放体验金
			this.expAccountService.grantExpMoney(expAccount,
					new IExpAccountService.LastTime(1,
							IExpAccountService.LastTimeUnit.MONTH),
					Consts.REGISTER_GRANT_EXPMONEY,
					Consts.EXPMONEY_TYPE_REGISTER);

		} else {
			//如果存在则抛出异常

			throw new RuntimeException("用户名已存在");
		}
	}

	@Override
	public boolean checkUsernameExist(String username) {
		// TODO Auto-generated method stub
		return logininfoMapper.countByUsername(username) > 0;
	}

	@Override
	public Logininfo login(String username, String password, String ip,
			int userType) {
		Logininfo current = logininfoMapper.login(username,
				MD5.encode(password + "[" + username + "]"));
		//创建一个日志
		IpLog ipLog = new IpLog();
		ipLog.setIp(ip);
		ipLog.setLoginTime(new Date());
		ipLog.setUsername(username);
		ipLog.setUserType(userType);
		if (current == null) {
			ipLog.setState(ipLog.STATE_FAILED);
		} else {
			ipLog.setState(ipLog.STATE_SUCCESS);
		}
		ipLogMapper.insert(ipLog);
		return current;
	}
}
