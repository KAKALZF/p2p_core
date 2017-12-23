package com.xmg.springboot.p2p.base.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据字典目录
 * @author 1
 *
 */
@Setter
@Getter
public class SystemDictionary {
	private Long id;
	private String sn;
	private String title;

	public String getJsonString() {
		Map<String, Object> json = new HashMap();
		json.put("id", id);
		json.put("sn", sn);
		json.put("title", title);
		return JSONObject.toJSONString(json);
	}
}
