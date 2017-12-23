package com.xmg.springboot.p2p.business.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmg.springboot.p2p.base.domain.Logininfo;
import com.xmg.springboot.p2p.util.Consts;

@Setter
@Getter
public class BidRequest {
	private Long id;
	private int version;
	private int returnType;
	private int bidRequestType;
	private int bidRequestState;
	private BigDecimal bidRequestAmount;
	private BigDecimal currentRate;
	private BigDecimal minBidAmount;
	private int monthes2Return;
	private int bidCount;
	private BigDecimal totalRewardAmount;
	private BigDecimal currentSum = Consts.ZERO;
	private String title;
	private String description;
	private String note;
	private Date disableDate;
	private int disableDays;
	private Logininfo createUser;
	private List<Bid> Bids = new ArrayList<>();
	private Date applyTime;
	private Date publishTime;

	/**
	 * 还需金额
	 * @return
	 */
	public BigDecimal getRemainAmount() {
		return bidRequestAmount.subtract(currentSum);
	}

	/**
	 * 投标进度
	 * @return
	 */
	public int getPersent() {
		return currentSum.divide(bidRequestAmount, 2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal(100)).intValue();
	}

	public String getJsonString() {
		Map<String, Object> json = new HashMap<>();
		json.put("id", id);
		json.put("title", title);
		json.put("username", createUser.getUsername());
		json.put("bidRequestAmount", bidRequestAmount);
		json.put("monthes2Return", monthes2Return);
		json.put("returnTypeDisplay", this.getReturnTypeDisplay());
		json.put("totalRewardAmount", totalRewardAmount);
		json.put("currentRate", currentRate);
		return JSONObject.toJSONString(json);

	}

	public String getReturnTypeDisplay() {
		return returnType == Consts.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL ? "等额本息"
				: "按月到期";
	}

	public String getBidRequestStateDisplay() {
		switch (this.bidRequestState) {
		case Consts.BIDREQUEST_STATE_PUBLISH_PENDING:
			return "待发布";
		case Consts.BIDREQUEST_STATE_BIDDING:
			return "招标中";
		case Consts.BIDREQUEST_STATE_UNDO:
			return "已撤销";
		case Consts.BIDREQUEST_STATE_BIDDING_OVERDUE:
			return "流标";
		case Consts.BIDREQUEST_STATE_APPROVE_PENDING_1:
			return "满标1审";
		case Consts.BIDREQUEST_STATE_APPROVE_PENDING_2:
			return "满标2审";
		case Consts.BIDREQUEST_STATE_REJECTED:
			return "满标审核被拒绝";
		case Consts.BIDREQUEST_STATE_PAYING_BACK:
			return "已还清";
		case Consts.BIDREQUEST_STATE_PAY_BACK_OVERDUE:
			return "逾期";
		case Consts.BIDREQUEST_STATE_PUBLISH_REFUSE:
			return "发标审核拒绝状态";
		default:
			return "";
		}
	}
}
