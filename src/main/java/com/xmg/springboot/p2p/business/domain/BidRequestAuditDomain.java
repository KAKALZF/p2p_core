package com.xmg.springboot.p2p.business.domain;

import lombok.Getter;
import lombok.Setter;

import com.xmg.springboot.p2p.base.domain.BaseAuditDomain;

@Setter
@Getter
public class BidRequestAuditDomain extends BaseAuditDomain {
	public static final int PUBLISH_AUDIT = 0;//发表前审核
	public static final int FULL_AUDIT1 = 1;//满标一审
	public static final int FULL_AUDIT2 = 2;//满标二审
	private Long id;
	private Long bidRequestId; //借款审核对应的借款
	private int auditType; //借款审核类型

	public String getAuditTypeDisplay() {

		switch (auditType) {
		case PUBLISH_AUDIT:
			return "发表前审核";
		case FULL_AUDIT1:
			return "满标一审";
		case FULL_AUDIT2:
			return "满标二审";
		default:
			return "";

		}
		

	}
}
