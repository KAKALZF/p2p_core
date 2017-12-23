package com.xmg.springboot.p2p.base.domain;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据字典明细
 * @author 1
 *
 */
@Setter
@Getter
public class SystemDictionaryItem {
	private Long id;
	private Long parentId;
	private String title;
	private String sequence;

	public String getJsonString() {
		Map<String, Object> json = new HashMap();
		json.put("id", id);
		json.put("parentId", parentId);
		json.put("title", title);
		json.put("sequence", sequence);
		return JSONObject.toJSONString(json);
	}
}
