package com.xmg.springboot.p2p.base.Query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QueryObject {
	public static final int PAGE_SIZE_UMLIMIT = -1;
	private int pageSize = 5;
	private int currentPage = 1;

	public int getBeginIndex() {
		return (currentPage - 1) * pageSize;
	}

	public void setPageSize(int pageSizeUmlimit) {
		// TODO Auto-generated method stub
		pageSize = pageSizeUmlimit;
	}
}
