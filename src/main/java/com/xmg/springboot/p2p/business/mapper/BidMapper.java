package com.xmg.springboot.p2p.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmg.springboot.p2p.business.domain.Bid;

public interface BidMapper {

	int insert(Bid record);

	Bid selectByPrimaryKey(Long id);

	/**
	 *批量处理借款对象的状态
	 * @param bidRequestId
	 * @param state
	 */
	void batchUpdateBidStates(@Param("bidRequestId") Long bidRequestId,
			@Param("state") int state);

	/**
	 * 通过借款对象的id获取投资对象
	 * @param id
	 * @return
	 */
	List<Bid> getBidsByBidRequestId(Long id);

}