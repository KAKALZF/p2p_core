package com.xmg.springboot.p2p.base.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 审核相关对象基类
 * @author 1
 *
 */
@Setter
@Getter
public abstract class BaseAuditDomain {

	public static final int STATE_NORMAL = 0;//待审核
	public static final int STATE_PASS = 1;//审核通过
	public static final int STATE_REJECT = 2;//审核拒绝

	protected int state;
	protected String remark;//审核备注
	protected Logininfo auditor;//审核人
	protected Logininfo applier;//申请人
	protected Date applyTime;//申请时间
	protected Date auditTime;//审核时间

	public String getStateDisplay() {

		switch (this.state) {
		case STATE_NORMAL:
			return "待审核";
		case STATE_PASS:
			return "审核通过";
		case STATE_REJECT:
			return "审核拒绝";
		default:
			return "";
		}

	}
}
