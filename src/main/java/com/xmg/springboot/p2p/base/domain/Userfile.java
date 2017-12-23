package com.xmg.springboot.p2p.base.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * 风控材料
 * @author 1
 *
 */
@Setter
@Getter
public class Userfile extends BaseAuditDomain {

	private Long id;
	private String image;
	private int score;
	private SystemDictionaryItem fileType;

	public String getJsonString() {
		Map<String, Object> json = new HashMap<>();
		json.put("id", id);
		json.put("image", image);
		json.put("applier", applier.getUsername());
		json.put("fileType", fileType.getTitle());
		return JSONObject.toJSONString(json);
	}
}
