package com.xmg.springboot.p2p.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmg.springboot.p2p.business.domain.ExpAccount;
import com.xmg.springboot.p2p.business.domain.ExpAccountFlow;
import com.xmg.springboot.p2p.business.domain.ExpAccountGrantRecord;
import com.xmg.springboot.p2p.business.mapper.ExpAccountFlowMapper;
import com.xmg.springboot.p2p.business.mapper.ExpAccountGrantRecordMapper;
import com.xmg.springboot.p2p.business.mapper.ExpAccountMapper;
import com.xmg.springboot.p2p.business.service.IExpAccountService;

/**
 * 体验金账户服务
 * @author 1
 *
 */
@Service
@Transactional
public class ExpAccountServiceImpl implements IExpAccountService {
	@Autowired
	private ExpAccountMapper expAccountMapper;
	@Autowired
	private ExpAccountGrantRecordMapper expAccountGrantRecordMapper;
	@Autowired
	private ExpAccountFlowMapper expAccountFlowMapper;

	@Override
	public void update(ExpAccount account) {
		int count = expAccountMapper.updateByPrimaryKey(account);
		if (count <= 0) {
			throw new RuntimeException("乐观锁失败,体验金账户:" + account.getId());
		}
	}

	@Override
	public void save(ExpAccount expAccount) {
		expAccountMapper.insert(expAccount);
	}

	@Override
	public void grantExpMoney(ExpAccount expAccount, LastTime lastTime,
			BigDecimal expmoney, int expmoneyType) {
		//创建一个发放回收记录对象
		ExpAccountGrantRecord record = new ExpAccountGrantRecord();
		record.setAmount(expmoney);
		record.setGrantDate(new Date());
		record.setGrantType(expmoneyType);
		record.setNote("注册发放体验金");
		record.setGrantUserId(expAccount.getId());
		record.setReturnDate(lastTime.getReturnTime(new Date()));
		record.setState(ExpAccountGrantRecord.STATE_NORMAL);
		expAccountGrantRecordMapper.insert(record);
		//修改体验金账户;
		expAccount.setUsableAmount(expAccount.getUsableAmount().add(
				record.getAmount()));
		this.update(expAccount);
		//增加一条体验金流水
		ExpAccountFlow flow = new ExpAccountFlow();
		flow.setActionTime(new Date());
		flow.setActionType(expmoneyType);
		flow.setAmount(expmoney);
		flow.setExpAccountId(expAccount.getId());
		flow.setFreezedAmount(expAccount.getFreezedAmount());
		flow.setUsableAmount(expAccount.getUsableAmount());
		flow.setNote("注册发放体验金");
		expAccountFlowMapper.insert(flow);
	}

	@Override
	public ExpAccount get(Long id) {
		return expAccountMapper.selectByPrimaryKey(id);
	}
}
