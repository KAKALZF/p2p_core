package com.xmg.springboot.p2p.business.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.ibatis.type.Alias;

import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.util.Consts;

/**
 * 
 * 还款计划对象，记录借款每月的还款信息(该信息针对借款用户，paymentscheduledetail针对投资人)
 * 
 * @author stef
 */
@Getter
@Setter
@Alias("PaymentSchedule")
public class PaymentSchedule {
	private Long id;
	private Long bidRequestId; // 对应借款
	private String bidRequestTitle;//借款名称
	private Logininfo borrowUser; // 还款人
	private Date deadLine; // 本期还款截止期限
	private Date payDate;// 还款时间
	// 本期还款总金额，利息 +本金

	private BigDecimal totalAmount = Consts.ZERO;
	// 本期还款本金
	private BigDecimal principal = Consts.ZERO;
	// 本期还款总利息
	private BigDecimal interest = Consts.ZERO;
	private int monthIndex; // 第几期 (即第几个月)
	// 本期还款状态（默认正常待还）
	private int state = Consts.PAYMENT_STATE_NORMAL;
	private int bidRequestType; // 借款类型
	private int returnType; // 还款方式，等同借款(BidRequest)中的还款方式

	// 本期还款计划对应的还款计划明细
	private List<PaymentScheduleDetail> paymentScheduleDetails = new ArrayList<PaymentScheduleDetail>();

	public String getStateDisplay() {
		switch (state) {
		case Consts.PAYMENT_STATE_NORMAL:
			return "正常待还";
		case Consts.PAYMENT_STATE_DONE:
			return "已还";
		case Consts.PAYMENT_STATE_OVERDUE:
			return "逾期";
		default:
			return "未知";
		}
	}
}
