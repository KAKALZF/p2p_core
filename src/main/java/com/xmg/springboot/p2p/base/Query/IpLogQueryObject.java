package com.xmg.springboot.p2p.base.Query;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import com.xmg.springboot.p2p.util.DateUtil;

@Getter
@Setter
public class IpLogQueryObject extends QueryObject {
	//开始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date beginDate;
	//结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date endDate;
	//状态
	public int state = -1;
	//用户名
	private String username;
	//用户类型 
	private int userType = -1;

	public Date getEndDate() {

		return endDate == null ? null : DateUtil.getEndDate(endDate);

	}

	public String getUserName() {
		return org.springframework.util.StringUtils.hasLength(username) ? username
				: null;

	}
}
