package com.xmg.springboot.p2p.business.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import lombok.Getter;

import com.xmg.springboot.p2p.business.domain.ExpAccount;

/**
 * 体验金账户服务
 * @author 1
 *
 */
public interface IExpAccountService {
	public void update(ExpAccount account);

	public void save(ExpAccount expAccount);

	/**
	 * 有效期
	 */
	@Getter
	class LastTime {
		private int amount;
		private LastTimeUnit unit;

		public LastTime(int amount, LastTimeUnit unit) {
			super();
			this.amount = amount;
			this.unit = unit;
		}

		public Date getReturnTime(Date date) {
			switch (this.unit) {
			case DAY:
				return DateUtils.addDays(date, amount);
			case MONTH:
				return DateUtils.addMonths(date, amount);
			case YEAR:
				return DateUtils.addYears(date, amount);
			default:
				return date;
			}
		}
	}

	/**
	 * 持续时间单位
	 */
	enum LastTimeUnit {
		DAY, MONTH, YEAR
	}

	public void grantExpMoney(ExpAccount expAccount, LastTime lastTime,
			BigDecimal registerGrantExpmoney, int expmoneyTypeRegister);

	public ExpAccount get(Long id);

}
