package com.xmg.springboot.p2p.base.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 邮件验证对象
 * @author 1
 *
 */
@Setter
@Getter
public class MailVerify {
	private Long id;
	private String email;
	private Long userId;
	private String uuid;
	private Date sendTime;
}
