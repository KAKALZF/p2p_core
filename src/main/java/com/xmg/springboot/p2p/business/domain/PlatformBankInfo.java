package com.xmg.springboot.p2p.business.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import lombok.Getter;
import lombok.Setter;

/**
 * 平台对外公布的线下充值银行信息
 * @author 1
 *
 */
@Setter
@Getter
public class PlatformBankInfo {
	private Long id;
	private String bankName;
	private String accountName;
	private String accountNumber;
	private String forkName;

	public String getJsonString() {
		Map<String, Object> json = new HashMap<>();
		json.put("id", id);
		json.put("bankName", bankName);
		json.put("accountName", accountName);
		json.put("accountNumber", accountNumber);
		json.put("forkName", forkName);
		return JSON.toJSONString(json);
	}
}
