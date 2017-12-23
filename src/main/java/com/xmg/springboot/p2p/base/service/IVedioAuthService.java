package com.xmg.springboot.p2p.base.service;

import com.xmg.springboot.p2p.base.Query.VedioAuthQueryObject;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.page.PageResult;

public interface IVedioAuthService {

	PageResult query(VedioAuthQueryObject qo);

	void audit(String remark, int state, Long loginInfoValue, Logininfo current);
}
