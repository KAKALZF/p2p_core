package com.xmg.springboot.p2p.base.service;

import com.xmg.springboot.p2p.base.Query.IpLogQueryObject;
import com.xmg.springboot.p2p.base.Query.QueryObject;
import com.xmg.springboot.p2p.base.page.PageResult;

/**
 * 登录日志服务
 * @author 1
 *
 */
public interface IIpLogService {
	public PageResult query(IpLogQueryObject qo);
}
