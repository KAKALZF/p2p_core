package com.xmg.springboot.p2p.base.service;

/**
 * 邮箱验证服务
 * @author 1
 *
 */
public interface IEmailVerifyService {
	public void sendVerifyEmail(String email, Long userId);
}
