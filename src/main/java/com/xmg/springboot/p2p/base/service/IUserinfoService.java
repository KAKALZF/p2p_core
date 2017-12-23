package com.xmg.springboot.p2p.base.service;

import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.domain.Userinfo;
import com.xmg.springboot.p2p.base.vo.VerifyCodeVO;

/**
 * 用户基本信息服务
 * @author 1
 *
 */
public interface IUserinfoService {
	/**
	 * 所有修改userinfo的操作必须调用此方法,要控制乐观锁
	 * @param userinfo
	 */
	void update(Userinfo userinfo);

	/**
	 * 初始化用户信息
	 */
	void init(Logininfo logininfo);

	Userinfo get(Long id);

	/**
	 * 执行绑定手机
	 */
	boolean bindPhone(VerifyCodeVO vo, String phoneNumber, String code,
			Long userId);

	/**
	 * 执行绑定邮箱
	 */
	void bindEmail(String uuid);

	/**
	 * 更新个人基本信息
	 * @param userinfo
	 */
	void updateBasicInfo(Userinfo userinfo);

}
