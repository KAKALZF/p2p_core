package com.xmg.springboot.p2p.business.service;

import java.math.BigDecimal;

import com.xmg.springboot.p2p.base.domain.Account;
import com.xmg.springboot.p2p.business.domain.Bid;
import com.xmg.springboot.p2p.business.domain.BidRequest;
import com.xmg.springboot.p2p.business.domain.PaymentSchedule;
import com.xmg.springboot.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.springboot.p2p.business.domain.RechargeOffline;

/**
 * 账户流水服务
 * @author 1
 *
 */
public interface IAccountFlowService {
	/**
	 * 充值流水账
	 * @param account
	 * @param ro
	 */
	void createRechargeFlow(Account account, RechargeOffline ro);

	/**
	 * 投资流水账
	 * @param account
	 * @param bid
	 */
	void createBidFlow(Account account, Bid bid);

	/**
	 * 投资失败流水账
	 * @param account
	 * @param bid
	 */
	void createBidFailedFlow(Account account, Bid bid);

	/**
	 * 借款成功流水
	 * @param account
	 * @param br
	 */
	void createSuccessBorrowFlow(Account account, BidRequest br);

	/**
	 * 借款手续费流水
	 * @param account
	 * @param br
	 * @param manageFee
	 */
	void createBorrowManagerFeeFlow(Account account, BidRequest br,
			BigDecimal manageFee);

	/**
	 * 投资成功流水
	 * @param bidAccount
	 * @param bid
	 */
	void createBidSuccessFlow(Account bidAccount, Bid bid);

	/**
	 * 还款流水
	 * @param borrowAccount
	 * @param ps
	 */
	void createReturnMoneyFlow(Account borrowAccount, PaymentSchedule ps);

	void createReceiveMoneyFlow(Account bidAccount, PaymentScheduleDetail psd);
	/**
	 * 回款收取利息管理费
	 * @param bidAccount
	 * @param psd
	 * @param chargeFee
	 */
	void createReceiveMoneyFeeFlow(Account bidAccount,
			PaymentScheduleDetail psd, BigDecimal chargeFee);

}
