package com.xmg.springboot.p2p.base.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.springboot.p2p.base.Query.RealAuthQueryObject;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.domain.RealAuth;
import com.xmg.springboot.p2p.base.domain.Userinfo;
import com.xmg.springboot.p2p.base.mapper.RealAuthMapper;
import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.base.service.IRealAuthService;
import com.xmg.springboot.p2p.base.service.IUserinfoService;
import com.xmg.springboot.p2p.util.BitStatesUtils;

@Service
public class RealAuthServiceImpl implements IRealAuthService {
	@Autowired
	private RealAuthMapper realAuthMapper;
	@Autowired
	private IUserinfoService userinfoService;

	@Override
	public RealAuth get(Long realAuthId) {
		return realAuthMapper.selectByPrimaryKey(realAuthId);
	}

	@Override
	public void apply(RealAuth ra, Logininfo li) {
		//判断有没实名认证
		Userinfo current = userinfoService.get(li.getId());
		if (!current.getHasRealAuth() && current.getRealAuthId() == null) {
			//创建一个新的对象,拷贝相关参数
			RealAuth r = new RealAuth();
			r.setAddress(ra.getAddress());
			r.setApplier(li);
			r.setApplyTime(new Date());
			r.setBornDate(ra.getBornDate());
			r.setIdNumber(ra.getIdNumber());
			r.setImage1(ra.getImage1());
			r.setImage2(ra.getImage2());
			r.setRealName(ra.getRealName());
			r.setSex(ra.getSex());
			r.setState(RealAuth.STATE_NORMAL);
			//保存
			realAuthMapper.insert(r);
			//把id设给realAuthId
			current.setRealAuthId(r.getId());
			userinfoService.update(current);
		}
	}

	@Override
	public PageResult query(RealAuthQueryObject qo) {
		int count = realAuthMapper.queryForCount(qo);
		if (count > 0) {
			List<RealAuth> list = realAuthMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(),
					qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	/**
	 *实名认证审核
	 */
	@Override
	public void audit(Long id, int state, String remark, Logininfo auditor) {
		//判断是否有审核对象
		RealAuth realAuth = realAuthMapper.selectByPrimaryKey(id);
		if (realAuth != null && realAuth.getState() == realAuth.STATE_NORMAL) {
			//审核对象是否处于等待审核状态
			Userinfo userinfo = userinfoService.get(realAuth.getApplier()
					.getId());
			//申请人尚未进行实名认证
			if (!userinfo.getHasRealAuth()) {
				realAuth.setAuditor(auditor);
				realAuth.setAuditTime(new Date());
				realAuth.setRemark(remark);
				realAuth.setState(state);
				if (state == realAuth.STATE_PASS) {
					userinfo.addState(BitStatesUtils.OP_REAL_AUTH);
					userinfo.setRealName(realAuth.getRealName());
					userinfo.setIdNumber(realAuth.getIdNumber());
				} else {
					userinfo.setRealAuthId(null);
				}
				userinfoService.update(userinfo);
				realAuthMapper.updateByPrimaryKey(realAuth);
			}

		}

		//设置审核的公共属性
		//如果审核通过
		//1.修改申请人的状态码
		//2.设置realName和numberId
		//如果不通过
		//userinfo的realAuthId设为null
	}
}
