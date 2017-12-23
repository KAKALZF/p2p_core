package com.xmg.springboot.p2p.business.domain;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

import com.alibaba.fastjson.JSON;
import com.xmg.springboot.p2p.base.domain.BaseAuditDomain;

/**
 * 线下充值
 * @author 1
 *
 */
@Setter
@Getter
public class RechargeOffline extends BaseAuditDomain {
	private Long id;
	private PlatformBankInfo bankInfo;
	private String tradeCode;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tradeTime;
	private BigDecimal amount;
	private String note;

	public String getJsonString() {
		Map<String, Object> json = new HashMap<>();
		json.put("id", id);
		json.put("username", applier.getUsername());
		json.put("tradeCode", tradeCode);
		json.put("amount", amount);
		json.put("tradeTime", DateFormat.getInstance().format(tradeTime));
		json.put("forkName", bankInfo.getForkName());
		return JSON.toJSONString(json);
	}

}
