package com.xmg.springboot.p2p.base.Query;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import com.xmg.springboot.p2p.util.DateUtil;

@Setter
@Getter
public class UserfileQueryObject extends QueryObject {
	//开始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date beginDate;
	//结束日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date endDate;
	//状态
	public int state = -1;
	//用户id
	public Long userId;
	

	public Date getEndDate() {

		return endDate == null ? null : DateUtil.getEndDate(endDate);

	}


}
