package com.xmg.springboot.p2p.base.service;

import com.xmg.springboot.p2p.base.Query.RealAuthQueryObject;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.domain.RealAuth;
import com.xmg.springboot.p2p.base.domain.Userinfo;
import com.xmg.springboot.p2p.base.page.PageResult;

/**
 * 实名认证服务
 * @author 1
 *
 */
public interface IRealAuthService {

	RealAuth get(Long realAuthId);

	/**
	 * 实名认证申请
	 * @param model
	 * @return
	 */
	void apply(RealAuth ra, Logininfo li);

	/**
	 * 实名认证对象查询
	 */
	PageResult query(RealAuthQueryObject qo);

	/**
	 * 实名认证对象审核
	 * @param id
	 * @param state
	 * @param remark
	 */
	void audit(Long id, int state, String remark, Logininfo auditor);
}
