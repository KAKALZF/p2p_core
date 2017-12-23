package com.xmg.springboot.p2p.business.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.xmg.springboot.p2p.business.domain.PaymentScheduleDetail;

public interface PaymentScheduleDetailMapper {

	int insert(PaymentScheduleDetail record);

	void batchUpdatePayDate(@Param("paymentScheduleId") Long paymentScheduleId,
			@Param("payDate") Date payDateF);

}