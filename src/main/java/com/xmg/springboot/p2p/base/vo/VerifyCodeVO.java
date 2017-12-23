package com.xmg.springboot.p2p.base.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCodeVO {
	private Date sendDate;
	private String code;
	private String phoneNumber;

}
