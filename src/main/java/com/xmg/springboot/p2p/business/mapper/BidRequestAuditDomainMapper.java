package com.xmg.springboot.p2p.business.mapper;

import java.util.List;

import com.xmg.springboot.p2p.business.domain.BidRequestAuditDomain;

public interface BidRequestAuditDomainMapper {

    int insert(BidRequestAuditDomain record);

    BidRequestAuditDomain selectByPrimaryKey(Long id);

	List<BidRequestAuditDomain> listAuditsByBidRequestId(Long id);

}