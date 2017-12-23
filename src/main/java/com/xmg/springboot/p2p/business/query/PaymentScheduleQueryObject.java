package com.xmg.springboot.p2p.business.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.xmg.springboot.p2p.base.Query.QueryObject;
import com.xmg.springboot.p2p.util.DateUtil;

public class PaymentScheduleQueryObject extends QueryObject {
	//开始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date beginDate;
	//结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date endDate;
	//状态
	public int state = -1;

	public Date getEndDate() {
		return endDate == null ? null : DateUtil.getEndDate(endDate);
	}

}
