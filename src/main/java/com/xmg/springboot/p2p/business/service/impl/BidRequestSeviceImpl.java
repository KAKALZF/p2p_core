package com.xmg.springboot.p2p.business.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.tools.internal.jxc.apt.Const;
import com.xmg.springboot.p2p.base.domain.Account;
import com.xmg.springboot.p2p.base.domain.BaseAuditDomain;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.base.domain.Userinfo;
import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.base.service.IAccountService;
import com.xmg.springboot.p2p.base.service.IUserinfoService;
import com.xmg.springboot.p2p.business.domain.Bid;
import com.xmg.springboot.p2p.business.domain.BidRequest;
import com.xmg.springboot.p2p.business.domain.BidRequestAuditDomain;
import com.xmg.springboot.p2p.business.domain.PaymentSchedule;
import com.xmg.springboot.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.springboot.p2p.business.mapper.BidMapper;
import com.xmg.springboot.p2p.business.mapper.BidRequestAuditDomainMapper;
import com.xmg.springboot.p2p.business.mapper.BidRequestMapper;
import com.xmg.springboot.p2p.business.mapper.PaymentScheduleDetailMapper;
import com.xmg.springboot.p2p.business.mapper.PaymentScheduleMapper;
import com.xmg.springboot.p2p.business.query.BidRequestQueryObject;
import com.xmg.springboot.p2p.business.query.PaymentScheduleQueryObject;
import com.xmg.springboot.p2p.business.service.IAccountFlowService;
import com.xmg.springboot.p2p.business.service.IBidRequestSevice;
import com.xmg.springboot.p2p.business.service.ISystemAccountService;
import com.xmg.springboot.p2p.util.BitStatesUtils;
import com.xmg.springboot.p2p.util.CalculatetUtil;
import com.xmg.springboot.p2p.util.Consts;

@Service
@Transactional
public class BidRequestSeviceImpl implements IBidRequestSevice {
	@Autowired
	private BidRequestMapper bidRequestMapper;
	@Autowired
	private IUserinfoService userinfoService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private BidRequestAuditDomainMapper bidRequestAuditDomainMapper;
	@Autowired
	private BidMapper bidMapper;
	@Autowired
	private IAccountFlowService accountFlowService;
	@Autowired
	private ISystemAccountService systemAccountService;
	@Autowired
	private PaymentScheduleMapper paymentScheduleMapper;
	@Autowired
	private PaymentScheduleDetailMapper paymentScheduleDetailMapper;

	@Override
	public void update(BidRequest bidRequest) {
		int count = bidRequestMapper.updateByPrimaryKey(bidRequest);
		if (count <= 0) {
			throw new RuntimeException("乐观锁失败,Userinfo:" + bidRequest.getId());
		}
	}

	@Override
	public void apply(BidRequest bidRequest, Logininfo current) {
		//判断
		Userinfo applier = userinfoService.get(current.getId());
		Account account = accountService.get(current.getId());
		if (applier.getHasBasicInfo()
				&& applier.getHasRealAuth()
				&& applier.getHasVedioAuth()
				&& applier.getScore() >= Consts.BORROW_SCORE_LIMIT
				&& !applier.getHasBidIRequestInProcess()
				&& bidRequest.getBidRequestAmount().compareTo(
						Consts.SMALLEST_BIDREQUEST_AMOUNT) >= 0
				&& bidRequest.getBidRequestAmount().compareTo(
						account.getRemainBorrowLimit()) <= 0
				&& bidRequest.getCurrentRate().compareTo(
						Consts.SMALLEST_CURRENT_RATE) >= 0
				&& bidRequest.getCurrentRate().compareTo(
						Consts.MAX_CURRENT_RATE) <= 0
				&& bidRequest.getMinBidAmount().compareTo(
						Consts.SMALLEST_BID_AMOUNT) >= 0) {

			//4个基本判断;没有借款在审核流程当中
			//最小借款金额<=借款金额<=剩余信用额度
			//最小借款利息<=借款利息<=最大借款利息
			//系统最小投标金额<=最小投标金额
			//创建一个借款对象
			BidRequest br = new BidRequest();
			//设置属性
			br.setApplyTime(new Date());
			br.setBidRequestAmount(bidRequest.getBidRequestAmount());
			br.setBidRequestState(Consts.BIDREQUEST_STATE_PUBLISH_PENDING);
			br.setBidRequestType(Consts.BIDREQUEST_TYPE_NORMAL);
			br.setCreateUser(current);
			br.setCurrentRate(bidRequest.getCurrentRate());
			br.setDescription(bidRequest.getDescription());
			br.setDisableDays(bidRequest.getDisableDays());
			br.setMinBidAmount(bidRequest.getMinBidAmount());
			br.setMonthes2Return(bidRequest.getMonthes2Return());
			br.setReturnType(Consts.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL);
			br.setTitle(bidRequest.getTitle());
			br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(
					bidRequest.getReturnType(),
					bidRequest.getBidRequestAmount(),
					bidRequest.getCurrentRate(), bidRequest.getMonthes2Return()));
			bidRequestMapper.insert(br);
			//添加一个状态码
			applier.addState(BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
			userinfoService.update(applier);
		}

	}

	@Override
	public PageResult query(BidRequestQueryObject qo) {
		int count = bidRequestMapper.queryForCount(qo);
		if (count > 0) {
			List<BidRequest> list = bidRequestMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(),
					qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	@Override
	public void audit(Long id, int state, String remark, Logininfo current) {
		//判断,借款处于发表前审核状态
		BidRequest br = bidRequestMapper.selectByPrimaryKey(id);
		if (br != null
				&& br.getBidRequestState() == Consts.BIDREQUEST_STATE_PUBLISH_PENDING) {

			//1,创建一个借款审核对象
			BidRequestAuditDomain audit = new BidRequestAuditDomain();
			//2,设置审核对象的相关信息
			audit.setApplier(br.getCreateUser());
			audit.setApplyTime(br.getApplyTime());
			audit.setAuditor(current);
			audit.setAuditTime(new Date());
			audit.setAuditType(BidRequestAuditDomain.PUBLISH_AUDIT);
			audit.setBidRequestId(id);
			audit.setRemark(remark);
			audit.setState(state);
			bidRequestAuditDomainMapper.insert(audit);
			//3,如果审核通过;
			if (state == BaseAuditDomain.STATE_PASS) {

				//3.1修改借款状态为招标中;
				br.setBidRequestState(Consts.BIDREQUEST_STATE_BIDDING);
				//3.2设置发布时间;
				br.setPublishTime(new Date());
				//3.3计算招标到期时间;
				br.setDisableDate(DateUtils.addDays(br.getPublishTime(),
						br.getDisableDays()));
				//3.4设置note
				br.setNote(remark);
			} else {
				//4如果审核失败
				//4.1修改借款状态为发布审核失败
				br.setBidRequestState(Consts.BIDREQUEST_STATE_PUBLISH_REFUSE);
				//4.2去掉用户的状态码
				Userinfo applier = userinfoService.get(br.getCreateUser()
						.getId());
				applier.removeState(BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
				userinfoService.update(applier);
			}

			this.update(br);

		}
	}

	@Override
	public BidRequest get(Long id) {
		return bidRequestMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<BidRequestAuditDomain> listAuditsByBidRequestId(Long id) {
		return bidRequestAuditDomainMapper.listAuditsByBidRequestId(id);
	}

	@Override
	public List<BidRequest> queryForList(BidRequestQueryObject qo) {
		return bidRequestMapper.query(qo);
	}

	/**
	 * 投标
	 */
	@Override
	public void bid(BigDecimal amount, Long bidRequestId, Logininfo current) {
		//判断
		BidRequest br = bidRequestMapper.selectByPrimaryKey(bidRequestId);
		Account account = accountService.get(current.getId());
		//1,借款处于招标状态
		//2,投标金额>=最小投标金额
		//3,投标金额<=min(剩余金额,账户余额);
		//4,不是借款人;
		if (br != null
				&& br.getBidRequestState() == Consts.BIDREQUEST_STATE_BIDDING
				&& amount.compareTo(br.getMinBidAmount()) >= 0
				&& (amount.compareTo(account.getUsableAmount().min(
						br.getRemainAmount()))) <= 0
				&& account.getId() != br.getCreateUser().getState()) {
			//投标
			//1,创建一个投标对象,设置属性
			Bid bid = new Bid();
			bid.setActualRate(br.getCurrentRate());
			bid.setAvailableAmount(amount);
			bid.setBidRequestId(bidRequestId);
			bid.setBidRequestState(br.getBidRequestState());
			bid.setBidRequestTitle(br.getTitle());
			bid.setBidTime(new Date());
			bid.setBidUser(current);
			bidMapper.insert(bid);
			//2,投标次数,当前已投标金额增加;
			br.setBidCount(br.getBidCount() + 1);
			br.setCurrentSum(br.getCurrentSum().add(amount));
			//3,投标人可用余额减少,冻结金额增加;
			account.setUsableAmount(account.getUsableAmount().subtract(amount));
			account.setFreezedAmount(account.getFreezedAmount().add(amount));
			//更新账户
			accountService.update(account);
			//4.生成一条投标流水;
			accountFlowService.createBidFlow(account, bid);
			//投标之后
			//1,是否投满,如果投满,设置借款状态为满标一审;
			if (br.getRemainAmount().equals(Consts.ZERO)) {
				br.setBidRequestState(Consts.BIDREQUEST_STATE_APPROVE_PENDING_1);
				//2,修改投标对象的状态
				bidMapper.batchUpdateBidStates(bidRequestId,
						Consts.BIDREQUEST_STATE_APPROVE_PENDING_1);
			}
		}

		this.update(br);

	}

	/**
	 * 满标一审
	 */
	@Override
	public void fullAudit1(Long id, int state, String remark, Logininfo current) {
		//判断,是否处于一审状态
		BidRequest br = bidRequestMapper.selectByPrimaryKey(id);
		if (br != null
				&& br.getBidRequestState() == Consts.BIDREQUEST_STATE_APPROVE_PENDING_1) {
			//创建一个审核对象,设置相关属性;
			BidRequestAuditDomain ad = new BidRequestAuditDomain();
			ad.setApplier(br.getCreateUser());
			ad.setApplyTime(new Date());
			ad.setAuditor(current);
			ad.setAuditTime(new Date());
			ad.setAuditType(BidRequestAuditDomain.FULL_AUDIT1);
			ad.setBidRequestId(id);
			ad.setRemark(remark);
			ad.setState(state);
			bidRequestAuditDomainMapper.insert(ad);
			//如果审核通过;
			if (state == BidRequestAuditDomain.STATE_PASS) {
				//1,修改借款的状态为满标二审
				br.setBidRequestState(Consts.BIDREQUEST_STATE_APPROVE_PENDING_2);
				//2,修改投标的状态为满标二审
				bidMapper.batchUpdateBidStates(br.getId(),
						Consts.BIDREQUEST_STATE_APPROVE_PENDING_2);
			} else {
				cancleBid(br);
			}
			this.update(br);
		}
	}

	/**
	 * 取消投标
	 * @param br
	 */
	public void cancleBid(BidRequest br) {
		//如果审核拒绝;
		//1,修改借款的状态为满标审核拒绝;
		br.setBidRequestState(Consts.BIDREQUEST_STATE_REJECTED);
		//2,修改投标的状态为满标审核拒绝;
		bidMapper.batchUpdateBidStates(br.getId(),
				Consts.BIDREQUEST_STATE_REJECTED);
		//3,退钱
		//3.1,遍历投标对象;
		for (Bid bid : bidMapper.getBidsByBidRequestId(br.getId())) {
			//3.2,投标人可用余额增加,冻结金额减少
			Account account = accountService.get(bid.getBidUser().getId());
			account.setUsableAmount(account.getUsableAmount().add(
					bid.getAvailableAmount()));
			account.setFreezedAmount(account.getFreezedAmount().subtract(
					bid.getAvailableAmount()));
			accountService.update(account);
			//3.3生成一条投标失败流水;
			accountFlowService.createBidFailedFlow(account, bid);
		}
		//4,借款人移除状态码
		Userinfo userinfo = userinfoService.get(br.getCreateUser().getId());
		userinfo.removeState(BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
		userinfoService.update(userinfo);
	}

	@Override
	public void fullAudit2(Long id, int state, String remark, Logininfo current) {
		//判断,处于满标二审
		BidRequest br = bidRequestMapper.selectByPrimaryKey(id);
		if (br != null
				&& br.getBidRequestState() == Consts.BIDREQUEST_STATE_APPROVE_PENDING_2) {
			//创建一个审核对象,设置相关属性
			BidRequestAuditDomain ad = new BidRequestAuditDomain();
			ad.setApplier(br.getCreateUser());
			ad.setApplier(br.getCreateUser());
			ad.setApplyTime(new Date());
			ad.setAuditor(current);
			ad.setAuditTime(new Date());
			ad.setAuditType(BidRequestAuditDomain.FULL_AUDIT2);
			ad.setBidRequestId(id);
			ad.setRemark(remark);
			ad.setState(state);
			bidRequestAuditDomainMapper.insert(ad);
			//通过审核;
			if (state == BidRequestAuditDomain.STATE_PASS) {
				//1,针对借款对象
				//1.1,修改状态为还款中
				br.setBidRequestState(Consts.BIDREQUEST_STATE_PAYING_BACK);
				//1.2,修改投标对象为还款中
				bidMapper.batchUpdateBidStates(id,
						Consts.BIDREQUEST_STATE_PAYING_BACK);
				//2,针对借款人
				Userinfo userinfo = userinfoService.get(br.getCreateUser()
						.getId());
				//2.1,移除借款状态码
				userinfo.removeState(BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);
				//2.2,借款成功,可用余额增加;
				Account borrowAccount = accountService.get(br.getCreateUser()
						.getId());
				borrowAccount.setUsableAmount(borrowAccount.getUsableAmount()
						.add(br.getBidRequestAmount()));
				//2.3,生成借款成功流水;
				accountFlowService.createSuccessBorrowFlow(borrowAccount, br);
				//2.4,剩余信用额度减少;
				borrowAccount.setRemainBorrowLimit(borrowAccount
						.getRemainBorrowLimit().subtract(
								br.getBidRequestAmount()));
				//2.5,待还本息增加;
				borrowAccount.setUnReturnAmount(borrowAccount
						.getUnReturnAmount().add(br.getBidRequestAmount())
						.add(br.getTotalRewardAmount()));
				//2.6,支付借款手续费;
				BigDecimal manageFee = CalculatetUtil
						.calAccountManagementCharge(br.getBidRequestAmount());
				//2.7,生成借款手续费流水;
				accountFlowService.createBorrowManagerFeeFlow(borrowAccount,
						br, manageFee);
				//2.8,系统账户收取手续费并生成流水;
				systemAccountService.chargeBorrowManageFee(br, manageFee);
				accountService.update(borrowAccount);
				//3,针对投资人
				//3.1,遍历投标对象;
				Map<Long, Account> bidAccounts = new HashMap();
				for (Bid bid : br.getBids()) {
					Long bidAccountId = bid.getBidUser().getId();
					Account bidAccount = bidAccounts.get(bidAccountId);
					if (bidAccount == null) {
						//3.2,减少冻结金额,生成投标成功流水;
						bidAccount = accountService.get(bid.getBidUser()
								.getId());
						bidAccounts.put(bidAccountId, bidAccount);
					}
					bidAccount.setFreezedAmount(bidAccount.getFreezedAmount()
							.subtract(bid.getAvailableAmount()));
					accountFlowService.createBidSuccessFlow(bidAccount, bid);
					//3.3,代收本金和代收利息增加;
					bidAccount.setUnReceivePrincipal(bidAccount
							.getUnReceivePrincipal().add(
									bid.getAvailableAmount()));
					BigDecimal rate = bid.getAvailableAmount().divide(
							br.getBidRequestAmount(), Consts.CAL_SCALE,
							RoundingMode.HALF_UP);
					bidAccount
							.setUnReceiveInterest(bidAccount
									.getUnReceiveInterest()
									.add(rate.multiply(br
											.getTotalRewardAmount()))
									.setScale(Consts.STORE_SCALE,
											RoundingMode.HALF_UP));
				}
				for (Account bidAccount : bidAccounts.values()) {
					accountService.update(bidAccount);
				}
				//4,后续业务:生成还款对象和回款对象;
				createPaymentSchedule(br);
			} else {
				//审核失败;
				this.cancleBid(br);
				//和满标一审社和失败业务相同
			}
			this.update(br);

		}
	}

	/**
	 * 生成还款对象
	 */
	public void createPaymentSchedule(BidRequest br) {
		BigDecimal totalPrincipal = Consts.ZERO;
		BigDecimal totalInterest = Consts.ZERO;
		for (int i = 0; i < br.getMonthes2Return(); i++) {
			PaymentSchedule ps = new PaymentSchedule();
			ps.setBidRequestId(br.getId());
			ps.setBidRequestTitle(br.getTitle());
			ps.setBidRequestType(br.getBidRequestType());
			ps.setBorrowUser(br.getCreateUser());
			ps.setDeadLine(DateUtils.addMonths(br.getPublishTime(), i + 1));
			ps.setState(Consts.PAYMENT_STATE_NORMAL);
			ps.setReturnType(br.getReturnType());
			ps.setMonthIndex(i + 1);
			//计算
			ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(
					ps.getReturnType(), br.getBidRequestAmount(),
					br.getCurrentRate(), i + 1, br.getMonthes2Return()));
			ps.setInterest(CalculatetUtil.calMonthlyInterest(
					br.getReturnType(), br.getBidRequestAmount(),
					br.getCurrentRate(), i + 1, br.getMonthes2Return()));
			ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
			if (i < br.getMonthes2Return() - 1) {
				ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(
						ps.getReturnType(), br.getBidRequestAmount(),
						br.getCurrentRate(), i + 1, br.getMonthes2Return()));
				ps.setInterest(CalculatetUtil.calMonthlyInterest(
						br.getReturnType(), br.getBidRequestAmount(),
						br.getCurrentRate(), i + 1, br.getMonthes2Return()));
				ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
				totalPrincipal = totalPrincipal.add(ps.getPrincipal());
				totalInterest = totalInterest.add(ps.getInterest());
			} else {
				//最后一期减去前面的,用于填平数据
				ps.setInterest(br.getTotalRewardAmount()
						.subtract(totalInterest));
				ps.setPrincipal(br.getBidRequestAmount().subtract(
						totalPrincipal));
				ps.setTotalAmount(ps.getInterest().add(ps.getPrincipal()));
			}
			paymentScheduleMapper.insert(ps);
			//计算还款明细
			createPaymentScheduleDetail(ps, br);
		}

	}

	/**
	 * 计算还款明细
	 */
	public void createPaymentScheduleDetail(PaymentSchedule ps, BidRequest br) {
		BigDecimal totalPrincipal = Consts.ZERO;
		BigDecimal totalInterest = Consts.ZERO;
		for (int i = 0; i < br.getBidCount(); i++) {
			Bid bid = br.getBids().get(i);
			PaymentScheduleDetail psd = new PaymentScheduleDetail();
			psd.setBidAmount(bid.getAvailableAmount());
			psd.setBidId(bid.getId());
			psd.setBidRequestId(br.getId());
			psd.setBidRequestedTitle(br.getTitle());
			psd.setDeadline(ps.getDeadLine());
			psd.setFromLogininfo(br.getCreateUser());
			psd.setMonthIndex(ps.getMonthIndex());
			psd.setPaymentScheduleId(ps.getId());
			psd.setReturnType(br.getReturnType());
			psd.setToLogininfoId(bid.getBidUser().getId());

			if (i < br.getBidCount() - 1) {
				BigDecimal rate = bid.getAvailableAmount().divide(
						br.getBidRequestAmount(), Consts.CAL_SCALE,
						RoundingMode.HALF_UP);
				psd.setPrincipal(ps.getPrincipal().multiply(rate)
						.setScale(Consts.STORE_SCALE, RoundingMode.HALF_UP));
				psd.setInterest(ps.getInterest().multiply(rate)
						.setScale(Consts.STORE_SCALE, RoundingMode.HALF_UP));
				psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
				totalPrincipal = totalPrincipal.add(psd.getPrincipal());
				totalInterest = totalInterest.add(psd.getInterest());
			} else {
				psd.setInterest(ps.getInterest().subtract(totalInterest));
				psd.setPrincipal(ps.getPrincipal().subtract(totalPrincipal));
				psd.setTotalAmount(psd.getPrincipal().add(psd.getInterest()));
			}
			paymentScheduleDetailMapper.insert(psd);
		}
	}

	@Override
	public PageResult queryPaymentSchedule(PaymentScheduleQueryObject qo) {
		int count = paymentScheduleMapper.queryForCount(qo);
		if (count > 0) {
			List<PaymentSchedule> list = paymentScheduleMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(),
					qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	@Override
	public void returnMoney(Long id, Logininfo current) {
		//判断
		PaymentSchedule ps = paymentScheduleMapper.selectByPrimaryKey(id);
		Account borrowAccount = accountService.get(current.getId());
		if (ps != null && ps.getState() == Consts.PAYMENT_STATE_NORMAL//1,待还
				&& current.getId().equals(current.getId())//2,当前人是借款人
				&& borrowAccount.getUsableAmount().compareTo(//账户余额否足够
						ps.getTotalAmount()) >= 0) {
			//1,待还
			//2,当前人是借款人
			//3,账户余额是够足够;
			//执行还款
			//1,借款人
			//1.1,可用余额减少;生成还款流水;
			borrowAccount.setUsableAmount(borrowAccount.getUsableAmount()
					.subtract(ps.getTotalAmount()));
			accountFlowService.createReturnMoneyFlow(borrowAccount, ps);
			//1.2,修改还款对象的还款时间和状态;	
			ps.setState(Consts.PAYMENT_STATE_DONE);
			ps.setPayDate(new Date());
			paymentScheduleMapper.updateByPrimaryKey(ps);
			//1.3,减少待还总额;	
			borrowAccount.setUnReturnAmount(borrowAccount.getUnReturnAmount()
					.subtract(ps.getTotalAmount()));
			//1.4,增加剩余可用额度;		
			borrowAccount.setRemainBorrowLimit(borrowAccount
					.getRemainBorrowLimit().add(ps.getPrincipal()));
			accountService.update(borrowAccount);
			//2	,投资人;
			//2.1,遍历还款明细对象;
			Map<Long, Account> bidAccounts = new HashMap();
			//遍历还款对象的还款明细
			for (PaymentScheduleDetail psd : ps.getPaymentScheduleDetails()) {
				Account bidAccount = bidAccounts.get(psd.getToLogininfoId());
				if (bidAccount == null) {
					bidAccount = accountService.get(psd.getToLogininfoId());
					bidAccounts.put(bidAccount.getId(), bidAccount);
				}
				//2.2,可用余额增加,生成收款流水;
				bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(
						psd.getTotalAmount()));
				accountFlowService.createReceiveMoneyFlow(bidAccount, psd);
				//2.3,待收本金待收利息减少;
				bidAccount.setUnReceiveInterest(bidAccount
						.getUnReceiveInterest().subtract(psd.getInterest()));
				bidAccount.setUnReceivePrincipal(bidAccount
						.getUnReceivePrincipal().subtract(psd.getPrincipal()));
				//2.4,支付信息管理费,生成流水
				BigDecimal chargeFee = CalculatetUtil
						.calInterestManagerCharge(psd.getInterest());
				bidAccount.setUsableAmount(bidAccount.getUsableAmount()
						.subtract(chargeFee));
				accountFlowService.createReceiveMoneyFeeFlow(bidAccount, psd,
						chargeFee);
				//2.5,系统账户收取利息管理费,生成流水;
				systemAccountService.chargeInterestManageFee(psd, chargeFee);

			}
			//2.6,更新还款明细对象的还款时间;
			paymentScheduleDetailMapper.batchUpdatePayDate(id, new Date());
			for (Account bidAccount : bidAccounts.values()) {
				this.accountService.update(bidAccount);
			}
			//3,后续检测;
			List<PaymentSchedule> pss = paymentScheduleMapper
					.listByBidRequestId(ps.getBidRequestId());
			//3.1,查询所有的还款对象,如果状态都是已还,修改借款状态;
			boolean flag = false;
			for (PaymentSchedule temp : pss) {
				if (temp.getState() == Consts.PAYMENT_STATE_NORMAL
						|| temp.getState() == Consts.PAYMENT_STATE_OVERDUE) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				//3.2,修改投标对象的状态;
				BidRequest br = this.get(ps.getBidRequestId());
				br.setBidRequestState(Consts.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
				bidMapper.batchUpdateBidStates(br.getId(),
						Consts.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
				this.update(br);
			}

		}
	}
}