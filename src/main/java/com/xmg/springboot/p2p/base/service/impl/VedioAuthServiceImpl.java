package com.xmg.springboot.p2p.base.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.springboot.p2p.base.Query.VedioAuthQueryObject;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.domain.Userinfo;
import com.xmg.springboot.p2p.base.domain.VedioAuth;
import com.xmg.springboot.p2p.base.mapper.VedioAuthMapper;
import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.base.service.IUserinfoService;
import com.xmg.springboot.p2p.base.service.IVedioAuthService;
import com.xmg.springboot.p2p.util.BitStatesUtils;

@Service
public class VedioAuthServiceImpl implements IVedioAuthService {
	@Autowired
	private VedioAuthMapper vedioAuthMapper;
	@Autowired
	private IUserinfoService userinfoService;

	@Override
	public PageResult query(VedioAuthQueryObject qo) {
		int count = vedioAuthMapper.queryForCount(qo);
		if (count > 0) {
			List<VedioAuth> list = vedioAuthMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(),
					qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	@Override
	public void audit(String remark, int state, Long loginInfoValue,
			Logininfo current) {
		//该用户对象没有进行用户认证
		Userinfo applier = userinfoService.get(loginInfoValue);
		if (applier != null && !applier.getHasVedioAuth()) {
			//新建一个视频认真对象,设置相关属性
			VedioAuth va = new VedioAuth();
			Logininfo li = new Logininfo();
			li.setId(loginInfoValue);
			va.setApplier(li);
			va.setApplyTime(new Date());
			va.setAuditor(current);
			va.setAuditTime(new Date());
			va.setRemark(remark);
			va.setState(state);
			//如果通过,用户新增一个视频认证的状态码
			if (state == VedioAuth.STATE_PASS) {
				applier.addState(BitStatesUtils.OP_VEDIO_AUTH);
				userinfoService.update(applier);
			}
			vedioAuthMapper.insert(va);
		}

	}

}
