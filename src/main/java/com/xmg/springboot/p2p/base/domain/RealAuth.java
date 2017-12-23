package com.xmg.springboot.p2p.base.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * 实名认证对象
 * @author 1
 *
 */
@Setter
@Getter
public class RealAuth extends BaseAuditDomain {
	public static final int MALE = 0;
	public static final int FEMALE = 1;


	private Long id;
	private String realName;
	private int sex;
	private String idNumber;//身份证
	private String bornDate;//出生日期
	private String address;//地址
	private String image1;//正面图片路径
	private String image2;//反面图片路径


	public String getJsonString() {
		Map<String, Object> json = new HashMap();
		json.put("id", id);
		json.put("username", applier.getUsername());
		json.put("realName", realName);
		json.put("sex", getSexDisplay());
		json.put("idNumber", idNumber);
		json.put("bornDate", bornDate);
		json.put("address", address);
		json.put("image1", image1);
		json.put("image2", image2);
		json.put("state", state);
		json.put("auditor", auditor);
		json.put("applier", applier);
		json.put("applyTime", applyTime);
		json.put("auditTime", auditTime);
		return JSONObject.toJSONString(json);
	}

	public String getSexDisplay() {
		return sex == MALE ? "男" : "女";

	}



}
