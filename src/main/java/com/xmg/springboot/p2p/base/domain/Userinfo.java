package com.xmg.springboot.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import com.xmg.springboot.p2p.util.BitStatesUtils;

/**
 * 用户基本信息
 * @author 1
 *
 */
@Setter
@Getter
public class Userinfo {
	private Long id;
	private int version;
	//状态位
	private Long bitState = 0L;
	private String realName;
	private String idNumber;
	private String phoneNumber;
	private String email;
	private SystemDictionaryItem incomeGrade;
	private SystemDictionaryItem marriage;
	private SystemDictionaryItem kidCount;
	private SystemDictionaryItem educationBackground;
	private SystemDictionaryItem houseCondition;
	private int score;//该用户累积的风控材料分数
	private Long realAuthId;//实名认证对象的id

	/**
	 * 判断用户是否绑定手机
	 * @return
	 */
	public Boolean getIsBindPhone() {
		return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_BIND_PHONE);

	}

	/**
	 * 判断用户是否绑定邮箱
	 * @return
	 */
	public Boolean getIsBindEmail() {
		return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_BIND_EMAIL);
	}

	/**
	 * 判断用户是否填写基本资料
	 * @return
	 */
	public Boolean getHasBasicInfo() {
		return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_BASIC_INFO);

	}

	/**
	 * 判断用户是否视频认证
	 * @return
	 */
	public Boolean getHasVedioAuth() {
		return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_VEDIO_AUTH);

	}

	/**
	 * 判断用户是否实名认证
	 * @return
	 */
	public Boolean getHasRealAuth() {
		return BitStatesUtils.hasState(bitState, BitStatesUtils.OP_REAL_AUTH);

	}

	/**
	 * 判断用户是否有一个借款在审核流程当中
	 * @return
	 */
	public Boolean getHasBidIRequestInProcess() {
		return BitStatesUtils.hasState(bitState,
				BitStatesUtils.HAS_BIDREQUEST_IN_PROCESS);

	}

	/**
	 * 给用户添加状态码
	 * 
	 */
	public void addState(Long state) {
		bitState = BitStatesUtils.addState(this.bitState, state);
	}

	/**
	 * 给用户移除状态码
	 * 
	 */
	public void removeState(Long state) {
		bitState = BitStatesUtils.removeState(this.bitState, state);
	}
}
