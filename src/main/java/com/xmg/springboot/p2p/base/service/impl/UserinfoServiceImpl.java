package com.xmg.springboot.p2p.base.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.domain.MailVerify;
import com.xmg.springboot.p2p.base.domain.Userinfo;
import com.xmg.springboot.p2p.base.mapper.MailVerifyMapper;
import com.xmg.springboot.p2p.base.mapper.UserinfoMapper;
import com.xmg.springboot.p2p.base.service.IUserinfoService;
import com.xmg.springboot.p2p.base.vo.VerifyCodeVO;
import com.xmg.springboot.p2p.util.BitStatesUtils;
import com.xmg.springboot.p2p.util.Consts;
import com.xmg.springboot.p2p.util.DateUtil;

@Service
@Transactional
public class UserinfoServiceImpl implements IUserinfoService {
	@Autowired
	private UserinfoMapper userinfoMapper;
	@Autowired
	private MailVerifyMapper mailVerifyMapper;

	@Override
	public void update(Userinfo userinfo) {
		int count = userinfoMapper.updateByPrimaryKey(userinfo);
		if (count <= 0) {
			throw new RuntimeException("乐观锁失败,Userinfo:" + userinfo.getId());
		}

	}

	@Override
	public void init(Logininfo logininfo) {
		Userinfo userinfo = new Userinfo();
		userinfo.setId(logininfo.getId());
		userinfoMapper.insert(userinfo);
	}

	@Override
	public Userinfo get(Long id) {
		return userinfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public boolean bindPhone(VerifyCodeVO vo, String phoneNumber, String code,
			Long userId) {
		Userinfo userinfo = this.get(userId);
		if (vo != null
				&& vo.getCode().equalsIgnoreCase(code)
				&& vo.getPhoneNumber().equals(phoneNumber)
				&& DateUtil.getSecondBetween(new Date(), vo.getSendDate()) < Consts.VERIFY_CODE_VALID_MIN * 60
				&& !userinfo.getIsBindPhone()) {
			//验证成功,执行绑定
			//1给用户添加绑定手机状态
			userinfo.addState(BitStatesUtils.OP_BIND_PHONE);
			//2设置phoneNumber
			userinfo.setPhoneNumber(phoneNumber);
			this.update(userinfo);
			return true;
		}

		return false;
	}

	@Override
	public void bindEmail(String uuid) {
		//根据uuid得到mailverify
		MailVerify mVerify = mailVerifyMapper.selectByUUID(uuid);
		//验证
		if (mVerify != null) {
			//邮箱验证对象不为空
			Userinfo userinfo = userinfoMapper.selectByPrimaryKey(mVerify
					.getUserId());
			//判断用户是否已绑定邮箱,判断条件:用户是否绑定邮箱,谅解是否在有效期内
			if (!userinfo.getIsBindEmail()
					&& DateUtil.getSecondBetween(mVerify.getSendTime(),
							new Date()) < Consts.VERIFY_EMAIL_VALID_DAY * 24 * 3600) {
				//通过
				//添加绑定邮箱状态码;
				userinfo.addState(BitStatesUtils.OP_BIND_EMAIL);
				//给用户设置email
				userinfo.setEmail(mVerify.getEmail());
				this.userinfoMapper.updateByPrimaryKey(userinfo);
				return;
			}
		}
		throw new RuntimeException("邮箱验证失败");

	}

	@Override
	public void updateBasicInfo(Userinfo userinfo) {
		Userinfo ui = userinfoMapper.selectByPrimaryKey(userinfo.getId());
		ui.setEducationBackground(userinfo.getEducationBackground());
		ui.setHouseCondition(userinfo.getHouseCondition());
		ui.setIncomeGrade(userinfo.getIncomeGrade());
		ui.setKidCount(userinfo.getKidCount());
		ui.setMarriage(userinfo.getMarriage());
		//添加填写资料的状态码
		if (!ui.getHasBasicInfo()) {
			ui.addState(BitStatesUtils.OP_BASIC_INFO);
		}
		update(ui);
	}
}
