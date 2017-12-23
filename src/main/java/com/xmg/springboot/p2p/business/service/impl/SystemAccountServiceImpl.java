package com.xmg.springboot.p2p.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmg.springboot.p2p.business.domain.BidRequest;
import com.xmg.springboot.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.springboot.p2p.business.domain.SystemAccount;
import com.xmg.springboot.p2p.business.domain.SystemAccountFlow;
import com.xmg.springboot.p2p.business.mapper.SystemAccountFlowMapper;
import com.xmg.springboot.p2p.business.mapper.SystemAccountMapper;
import com.xmg.springboot.p2p.business.service.ISystemAccountService;
import com.xmg.springboot.p2p.util.Consts;

@Service
@Transactional
public class SystemAccountServiceImpl implements ISystemAccountService {
	@Autowired
	private SystemAccountMapper systemAccountMapper;
	@Autowired
	private SystemAccountFlowMapper systemAccountFlowMapper;

	@Override
	public void update(SystemAccount account) {
		int count = systemAccountMapper.updateByPrimaryKey(account);
		if (count <= 0) {
			throw new RuntimeException("系统账户乐观锁失败");
		}
	}

	@Override
	public void chargeBorrowManageFee(BidRequest br, BigDecimal manageFee) {
		//获取当前用户
		SystemAccount current = systemAccountMapper.selectCurrent();
		//系统账户可用余额增加
		current.setUsableAmount(current.getUsableAmount().add(manageFee));
		//创建一个系统账户流水
		SystemAccountFlow flow = new SystemAccountFlow();
		//设置相关属性
		flow.setActionTime(new Date());
		flow.setActionType(Consts.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE);
		flow.setAmount(manageFee);
		flow.setFreezedAmount(current.getFreezedAmount());
		flow.setUsableAmount(current.getUsableAmount());
		flow.setNote("借款" + br.getTitle() + "成功,支付借款手续费:" + manageFee);
		systemAccountFlowMapper.insert(flow);
		this.update(current);
	}

	@Override
	public void chargeInterestManageFee(PaymentScheduleDetail psd,
			BigDecimal chargeFee) {
		//获取当前用户
		SystemAccount current = systemAccountMapper.selectCurrent();
		//系统账户可用余额增加
		current.setUsableAmount(current.getUsableAmount().add(chargeFee));
		//创建一个系统账户流水
		SystemAccountFlow flow = new SystemAccountFlow();
		//设置相关属性
		flow.setActionTime(new Date());
		flow.setActionType(Consts.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE);
		flow.setAmount(chargeFee);
		flow.setFreezedAmount(current.getFreezedAmount());
		flow.setUsableAmount(current.getUsableAmount());
		flow.setNote("借款" + psd.getBidRequestedTitle() + "换款成功,收取利息手续费:"
				+ chargeFee);
		systemAccountFlowMapper.insert(flow);
		this.update(current);
	}

}
