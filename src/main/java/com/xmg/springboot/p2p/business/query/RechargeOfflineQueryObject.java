package com.xmg.springboot.p2p.business.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.xmg.springboot.p2p.base.Query.QueryObject;
import com.xmg.springboot.p2p.util.DateUtil;

@Getter
@Setter
@ToString
public class RechargeOfflineQueryObject extends QueryObject {
	//开始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date beginDate;
	//结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date endDate;
	//状态
	public int state = -1;
	//银行
	public int bankInfoId = -1;
	//银行
	public String tradeCode;

	public Date getEndDate() {
		return endDate == null ? null : DateUtil.getEndDate(endDate);
	}

	public String getTradeCode() {
		return StringUtils.hasLength(tradeCode) ? tradeCode : null;
	}
}
