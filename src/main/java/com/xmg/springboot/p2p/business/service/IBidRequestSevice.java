package com.xmg.springboot.p2p.business.service;

import java.math.BigDecimal;
import java.util.List;

import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.business.domain.BidRequest;
import com.xmg.springboot.p2p.business.domain.BidRequestAuditDomain;
import com.xmg.springboot.p2p.business.query.BidRequestQueryObject;
import com.xmg.springboot.p2p.business.query.PaymentScheduleQueryObject;

/**
 * 借款申请服务
 * @author 1
 *
 */
public interface IBidRequestSevice {

	void update(BidRequest bidRequest);

	/**
	 * 借款申请
	 * @param bidRequest
	 * @param logininfo 
	 */

	void apply(BidRequest bidRequest, Logininfo logininfo);

	/**
	 * 借款列表查询
	 */
	PageResult query(BidRequestQueryObject qo);

	/**
	 * 借款审核
	 * @param id
	 * @param state
	 * @param remark
	 * @param current
	 */
	void audit(Long id, int state, String remark, Logininfo current);

	/**
	 * 获取借款详情
	 * @param id
	 */
	BidRequest get(Long id);

	/**
	 * 根据借款对象获取审核对象
	 * @param id
	 * @return
	 */
	List<BidRequestAuditDomain> listAuditsByBidRequestId(Long id);

	List<BidRequest> queryForList(BidRequestQueryObject qo);

	/**
	 * 投标
	 * @param amount
	 * @param bidRequestId
	 * @param current
	 */
	void bid(BigDecimal amount, Long bidRequestId, Logininfo current);

	/**
	 * 满标一审
	 * @param id
	 * @param state
	 * @param remark
	 * @param current
	 */
	void fullAudit1(Long id, int state, String remark, Logininfo current);

	/**
	 * 满标二审
	 * @param id
	 * @param state
	 * @param remark
	 * @param current
	 */
	void fullAudit2(Long id, int state, String remark, Logininfo current);

	/**
	 * 还款列表
	 */
	PageResult queryPaymentSchedule(PaymentScheduleQueryObject qo);

	/**
	 * 还款
	 * @param id
	 * @param logininfo 
	 */
	void returnMoney(Long id, Logininfo logininfo);
}
