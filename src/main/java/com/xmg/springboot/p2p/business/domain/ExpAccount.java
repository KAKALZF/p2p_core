package com.xmg.springboot.p2p.business.domain;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

import com.xmg.springboot.p2p.util.Consts;

/**
 * 体验金账户
 * 
 * @author stef
 *
 */
@Setter
@Getter
public class ExpAccount {
	private Long id;
	private int version;
	private BigDecimal usableAmount = Consts.ZERO;// 体验金账户余额
	private BigDecimal freezedAmount = Consts.ZERO;// 体验金冻结金额
	private BigDecimal unReturnExpAmount = Consts.ZERO;// 临时垫收体验金
}
