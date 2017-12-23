package com.xmg.springboot.p2p.base.domain;

import java.math.BigDecimal;

import com.xmg.springboot.p2p.util.Consts;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户账户信息对象
 * @author 1
 *
 */
@Setter
@Getter
public class Account {
	private Long id;
	private int version;
	private String tradePassword;
	//可用金额
	private BigDecimal usableAmount = Consts.ZERO;
	//冻结金额
	private BigDecimal freezedAmount = Consts.ZERO;
	//待收利息
	private BigDecimal unReceiveInterest = Consts.ZERO;
	//待收本金
	private BigDecimal unReceivePrincipal = Consts.ZERO;
	//待还金额
	private BigDecimal unReturnAmount = Consts.ZERO;
	//剩余授信额度
	private BigDecimal remainBorrowLimit = Consts.DEFAULT_BORROW_LIMIT;
	//授信额度
	private BigDecimal borrowLimitAmount = Consts.DEFAULT_BORROW_LIMIT;

	/**
	 * 获取账户总金额
	 */
	public BigDecimal getTotalAmount() {
		return usableAmount.add(freezedAmount).add(unReceiveInterest)
				.add(unReceivePrincipal);
	}
}
