package com.xmg.springboot.p2p.business.domain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 账户流水
 * @author 1
 *
 */
@Setter
@Getter
public class AccountFlow {
	private Long id;
	private Date actionTime;
	private BigDecimal amount;//产生金额
	private Long accountId;//关联的账户
	private int actionType;//流水类型
	private BigDecimal usableAmount;//操作完成之后的账户可用余额
	private BigDecimal freezedAmount;//操作完成之后的账户冻结金额
	private String note;//操作说明
}
