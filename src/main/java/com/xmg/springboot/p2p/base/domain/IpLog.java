package com.xmg.springboot.p2p.base.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录日志
 * @author 1
 *
 */
@Setter
@Getter
public class IpLog {
	public static final int STATE_SUCCESS = 0;
	public static final int STATE_FAILED = 1;
	private Long id;
	private String ip;
	private Date loginTime;
	private int state;
	private String username;
	private int userType;

	public String getStateDisplay() {
		return state == STATE_SUCCESS ? "登陆成功" : "登录失败";
	}

	public String getUserTypeDisplay() {
		return userType == Logininfo.TYPE_MANAGER ? "后台管理员" : "前台用户";

	}
}
