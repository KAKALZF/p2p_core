package com.xmg.springboot.p2p.base.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xmg.springboot.p2p.base.domain.MailVerify;
import com.xmg.springboot.p2p.base.domain.Userinfo;
import com.xmg.springboot.p2p.base.mapper.MailVerifyMapper;
import com.xmg.springboot.p2p.base.service.IEmailVerifyService;
import com.xmg.springboot.p2p.base.service.IUserinfoService;
import com.xmg.springboot.p2p.util.Consts;

@Service
public class EmailVerifyServiceImpl implements IEmailVerifyService {
	@Autowired
	private IUserinfoService userinfoService;
	@Autowired
	private MailVerifyMapper mailVerifyMapper;
	@Value("${app.hostName}")
	private String appHostName;

	@Override
	public void sendVerifyEmail(String email, Long userId) {
		//验证,用户没有绑定邮箱
		Userinfo userinfo = userinfoService.get(userId);
		if (!userinfo.getIsBindEmail()) {
			//发邮件
			//创建一个邮件验证对象
			MailVerify mailVerify = new MailVerify();
			mailVerify.setEmail(email);
			mailVerify.setSendTime(new Date());
			mailVerify.setUserId(userId);
			//构造一个uuid
			String uuid = UUID.randomUUID().toString().substring(0, 4);
			mailVerify.setUuid(uuid);
			mailVerifyMapper.insert(mailVerify);
			//发送邮件
			StringBuilder content = new StringBuilder(100);
			content.append("点击<a href='").append(appHostName)
					.append("bindEmail?uuid=").append(uuid)
					.append("'>这里</a>完成邮箱绑定.有效期为")
					.append(Consts.VERIFY_EMAIL_VALID_DAY).append("天");
			System.out.println("发送邮件:" + content);
		}

	}
}
