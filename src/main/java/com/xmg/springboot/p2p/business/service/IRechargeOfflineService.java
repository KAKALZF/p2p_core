package com.xmg.springboot.p2p.business.service;

import java.util.List;

import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.business.domain.RechargeOffline;
import com.xmg.springboot.p2p.business.query.RechargeOfflineQueryObject;

/**
 * 线下充值服务器
 * @author 1
 *
 */
public interface IRechargeOfflineService {
	List<RechargeOffline> listAll();

	void apply(RechargeOffline re, Logininfo current);

	PageResult query(RechargeOfflineQueryObject qo);

	/**
	 * 线下充值审核
	 * @param id
	 * @param state
	 * @param remark
	 * @param current
	 */
	void audit(Long id, int state, String remark, Logininfo current);
}
