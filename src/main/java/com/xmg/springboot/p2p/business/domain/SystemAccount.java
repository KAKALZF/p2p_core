package com.xmg.springboot.p2p.business.domain;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import com.xmg.springboot.p2p.util.Consts;

/**
 * 系统账户
 * @author 1
 *
 */
@Setter
@Getter
public class SystemAccount {
	private Long id;
	private int version;
	private BigDecimal usableAmount = Consts.ZERO;
	private BigDecimal freezedAmount = Consts.ZERO;
	//充值-提现=所有账户(包括系统账户)可用余额+冻结金额(存量)
}
