package com.xmg.springboot.p2p.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.springboot.p2p.base.domain.Account;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.domain.VedioAuth;
import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.base.service.IAccountService;
import com.xmg.springboot.p2p.base.service.IUserinfoService;
import com.xmg.springboot.p2p.business.domain.RechargeOffline;
import com.xmg.springboot.p2p.business.mapper.RechargeOfflineMapper;
import com.xmg.springboot.p2p.business.query.RechargeOfflineQueryObject;
import com.xmg.springboot.p2p.business.service.IAccountFlowService;
import com.xmg.springboot.p2p.business.service.IRechargeOfflineService;

@Service
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {
	@Autowired
	private RechargeOfflineMapper rechargeMapper;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IAccountFlowService accountFlowService;

	@Override
	public List<RechargeOffline> listAll() {
		return rechargeMapper.selectAll();
	}

	@Override
	public void apply(RechargeOffline re, Logininfo current) {
		//创建一个线下充值对象并设置属性
		RechargeOffline r = new RechargeOffline();
		r.setAmount(re.getAmount());
		r.setApplier(current);
		r.setApplyTime(new Date());
		r.setBankInfo(re.getBankInfo());
		r.setNote(re.getNote());
		r.setState(RechargeOffline.STATE_NORMAL);
		r.setTradeCode(re.getTradeCode());
		r.setTradeTime(re.getTradeTime());
		rechargeMapper.insert(r);
	}

	@Override
	public PageResult query(RechargeOfflineQueryObject qo) {
		int count = rechargeMapper.queryForCount(qo);
		if (count > 0) {
			List<RechargeOffline> list = rechargeMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(),
					qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	@Override
	public void audit(Long id, int state, String remark, Logininfo current) {
		//验证,线下充值单处于待审核状态
		RechargeOffline ro = rechargeMapper.selectByPrimaryKey(id);
		if (ro != null && ro.getState() == RechargeOffline.STATE_NORMAL) {
			//设置审核的相关信息
			ro.setAuditor(current);
			ro.setAuditTime(new Date());
			ro.setRemark(remark);
			ro.setState(state);
			if (state == RechargeOffline.STATE_PASS) {
				//如果审核通过;
				//1.增加可用余额
				Account account = accountService.get(ro.getApplier().getId());
				account.setUsableAmount(account.getUsableAmount().add(
						ro.getAmount()));
				//2.添加一条充值流水
				accountFlowService.createRechargeFlow(account, ro);
				accountService.update(account);
			}
			rechargeMapper.updateByPrimaryKey(ro);
		}
	}

}
