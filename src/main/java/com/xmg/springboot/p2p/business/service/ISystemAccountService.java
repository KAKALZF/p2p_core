package com.xmg.springboot.p2p.business.service;

import java.math.BigDecimal;

import com.xmg.springboot.p2p.business.domain.BidRequest;
import com.xmg.springboot.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.springboot.p2p.business.domain.SystemAccount;

/**
 * 系统账户服务
 * @author 1
 *
 */
public interface ISystemAccountService {
	void update(SystemAccount account);
	/**
	 * 收取借款管理费并生成流水
	 * @param br
	 * @param manageFee
	 */
	void chargeBorrowManageFee(BidRequest br, BigDecimal manageFee);
	/**
	 * 收取利息管理费
	 */
	void chargeInterestManageFee(PaymentScheduleDetail psd, BigDecimal chargeFee);
}
