package com.xmg.springboot.p2p.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AjaxResult {
	private boolean success = true;
	private String msg;
	private Object data;

	public void setMsg(String msg) {
		this.success = false;
		this.msg = msg;
	}
}
