package com.xmg.springboot.p2p.business.domain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.ibatis.type.Alias;

import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.util.Consts;

/**
 * 针对一个还款计划,投资人的回款明细
 * 
 * @author Administrator
 * 
 */
@Getter
@Setter
@Alias("PaymentScheduleDetail")
public class PaymentScheduleDetail {
	private Long id;
	// 该投标人总共投标金额,便于还款/垫付查询
	private BigDecimal bidAmount;
	private Long bidId; // 对应的投标IDs

	// 本期还款总金额(=本金+利息)
	private BigDecimal totalAmount = Consts.ZERO;
	// 本期应还款本金
	private BigDecimal principal = Consts.ZERO;
	// 本期应还款利息
	private BigDecimal interest = Consts.ZERO;
	private int monthIndex; // 第几期（即第几个月）
	private Date deadline; // 本期还款截止时间
	private Long bidRequestId; // 所属哪个借款
	private Date payDate; // 实际付款日期
	private int returnType; // 还款方式
	private Long paymentScheduleId; // 所属还款计划
	private Logininfo fromLogininfo; // 还款人(即发标人)
	private Long toLogininfoId; // 收款人(即投标人)
	private String bidRequestedTitle;//借款请求标题
}
