package com.xmg.springboot.p2p.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.springboot.p2p.base.domain.Account;
import com.xmg.springboot.p2p.business.domain.AccountFlow;
import com.xmg.springboot.p2p.business.domain.Bid;
import com.xmg.springboot.p2p.business.domain.BidRequest;
import com.xmg.springboot.p2p.business.domain.PaymentSchedule;
import com.xmg.springboot.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.springboot.p2p.business.domain.RechargeOffline;
import com.xmg.springboot.p2p.business.mapper.AccountFlowMapper;
import com.xmg.springboot.p2p.business.service.IAccountFlowService;
import com.xmg.springboot.p2p.util.Consts;

@Service
public class AccountFlowServiceImpl implements IAccountFlowService {
	@Autowired
	private AccountFlowMapper accountFlowMapper;

	/**
	 * 基本的审核对象
	 */
	public AccountFlow createBaseFlow(Account account) {
		AccountFlow flow = new AccountFlow();
		flow.setActionTime(new Date());
		flow.setAccountId(account.getId());
		flow.setUsableAmount(account.getUsableAmount());
		flow.setFreezedAmount(account.getFreezedAmount());
		return flow;
	};

	/**
	 * 线下充值流水账服务
	 */
	@Override
	public void createRechargeFlow(Account account, RechargeOffline ro) {
		AccountFlow flow = new AccountFlow();
		flow.setActionTime(new Date());
		flow.setAccountId(account.getId());
		flow.setActionType(Consts.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE);
		flow.setAmount(ro.getAmount());
		flow.setUsableAmount(account.getUsableAmount());
		flow.setFreezedAmount(account.getFreezedAmount());
		flow.setNote("线下充值成功,充值流水号:" + ro.getTradeCode() + ",充值金额为"
				+ ro.getAmount() + "元");
		accountFlowMapper.insert(flow);
	}

	/**
	 * 投资流水账
	 */
	@Override
	public void createBidFlow(Account account, Bid bid) {
		AccountFlow flow = new AccountFlow();
		flow.setActionTime(new Date());
		flow.setAccountId(account.getId());
		flow.setActionType(Consts.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL);
		flow.setAmount(bid.getAvailableAmount());
		flow.setUsableAmount(account.getUsableAmount());
		flow.setFreezedAmount(account.getFreezedAmount());
		flow.setNote("投资,借款为:" + bid.getBidRequestTitle() + ",投资金额为"
				+ bid.getAvailableAmount() + "元");
		accountFlowMapper.insert(flow);
	}

	/**
	 * 投资失败流水账
	 */
	@Override
	public void createBidFailedFlow(Account account, Bid bid) {
		AccountFlow flow = createBaseFlow(account);
		flow.setActionType(Consts.ACCOUNT_ACTIONTYPE_BID_UNFREEZED);
		flow.setAmount(bid.getAvailableAmount());
		flow.setNote("投资失败,借款为:" + bid.getBidRequestTitle() + ",取消投标冻结金额"
				+ bid.getAvailableAmount() + "元");
		accountFlowMapper.insert(flow);
	}

	/**
	 * 借款成功流水账
	 */
	@Override
	public void createSuccessBorrowFlow(Account account, BidRequest br) {
		AccountFlow flow = createBaseFlow(account);
		flow.setActionType(Consts.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL);
		flow.setAmount(br.getBidRequestAmount());
		flow.setNote("借款:" + br.getTitle() + ",成功借到" + br.getBidRequestAmount()
				+ "元");
		accountFlowMapper.insert(flow);

	}

	@Override
	public void createBorrowManagerFeeFlow(Account account, BidRequest br,
			BigDecimal manageFee) {
		AccountFlow flow = createBaseFlow(account);
		flow.setActionType(Consts.ACCOUNT_ACTIONTYPE_CHARGE);
		flow.setAmount(manageFee);
		flow.setNote("借款:" + br.getTitle() + ",借款成功,支付借款手续费" + manageFee + "元");
		accountFlowMapper.insert(flow);
	}

	@Override
	public void createBidSuccessFlow(Account bidAccount, Bid bid) {
		AccountFlow flow = createBaseFlow(bidAccount);
		flow.setActionType(Consts.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL);
		flow.setAmount(bid.getAvailableAmount());
		flow.setNote("投资" + bid.getBidRequestTitle() + ",成功投资"
				+ bid.getAvailableAmount() + "元");
		accountFlowMapper.insert(flow);
	}

	@Override
	public void createReturnMoneyFlow(Account borrowAccount, PaymentSchedule ps) {
		AccountFlow flow = createBaseFlow(borrowAccount);
		flow.setActionType(Consts.ACCOUNT_ACTIONTYPE_RETURN_MONEY);
		flow.setAmount(ps.getTotalAmount());
		flow.setNote("借款" + ps.getBidRequestTitle() + ",第" + ps.getMonthIndex()
				+ "期还款,还款金额:" + ps.getTotalAmount() + "元");
		accountFlowMapper.insert(flow);
	}

	@Override
	public void createReceiveMoneyFlow(Account bidAccount,
			PaymentScheduleDetail psd) {
		AccountFlow flow = createBaseFlow(bidAccount);
		flow.setActionType(Consts.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY);
		flow.setAmount(psd.getTotalAmount());
		flow.setNote("借款" + psd.getBidRequestedTitle() + ",第"
				+ psd.getMonthIndex() + "期还款,收款金额:" + psd.getTotalAmount()
				+ "元");
		accountFlowMapper.insert(flow);

	}

	@Override
	public void createReceiveMoneyFeeFlow(Account bidAccount,
			PaymentScheduleDetail psd, BigDecimal chargeFee) {
		AccountFlow flow = createBaseFlow(bidAccount);
		flow.setActionType(Consts.ACCOUNT_ACTIONTYPE_INTEREST_SHARE);
		flow.setAmount(chargeFee);
		flow.setNote("借款" + psd.getBidRequestedTitle() + ",第"
				+ psd.getMonthIndex() + "期还款,支付信息管理费:" + chargeFee + "元");
		accountFlowMapper.insert(flow);

	}
}
