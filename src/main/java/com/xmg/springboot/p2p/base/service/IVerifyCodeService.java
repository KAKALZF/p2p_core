package com.xmg.springboot.p2p.base.service;

import com.xmg.springboot.p2p.base.vo.VerifyCodeVO;

/**
 * 验证马服务
 * @author 1
 *
 */
public interface IVerifyCodeService {
	public VerifyCodeVO sendVerifCode(String phoneNumber, VerifyCodeVO vo);
}
