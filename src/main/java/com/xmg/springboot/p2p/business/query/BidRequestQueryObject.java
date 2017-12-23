package com.xmg.springboot.p2p.business.query;

import lombok.Getter;
import lombok.Setter;

import com.xmg.springboot.p2p.base.Query.QueryObject;

@Setter
@Getter
public class BidRequestQueryObject extends QueryObject {
	private int bidRequestState = -1;

	private int[] bidRequestStates;
	private String orderBy;
	private String orderType;

}
