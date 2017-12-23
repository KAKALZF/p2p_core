package com.xmg.springboot.p2p.business.domain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.xmg.springboot.p2p.base.domain.Logininfo;

@Setter
@Getter
public class Bid {
	private Long id;
	private BigDecimal actualRate;
	private BigDecimal availableAmount;
	private Long bidRequestId;
	private String bidRequestTitle;
	private Logininfo bidUser;
	private Date bidTime;
	private int bidRequestState;

}
