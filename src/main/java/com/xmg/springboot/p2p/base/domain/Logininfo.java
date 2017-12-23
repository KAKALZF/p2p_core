package com.xmg.springboot.p2p.base.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 代表系统中的可登陆对象
 * @author 1
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Logininfo {
	public static final int STATE_NORMAL = 0;//正常状态
	public static final int STATE_LOCK = 1;//锁定状态
	public static final int TYPE_USER = 0;//普通用户
	public static final int TYPE_MANAGER = 1;//后台管理员

	private Long id;
	private String username;
	private String password;
	private int state;
	private int userType;
}
