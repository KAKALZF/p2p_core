package com.xmg.springboot.p2p.base.service.impl;

import java.util.List;

import javax.sound.midi.SysexMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmg.springboot.p2p.base.Query.SystemDictionaryQueryObject;
import com.xmg.springboot.p2p.base.domain.IpLog;
import com.xmg.springboot.p2p.base.domain.SystemDictionary;
import com.xmg.springboot.p2p.base.domain.SystemDictionaryItem;
import com.xmg.springboot.p2p.base.mapper.SystemDictionaryItemMapper;
import com.xmg.springboot.p2p.base.mapper.SystemDictionaryMapper;
import com.xmg.springboot.p2p.base.page.PageResult;
import com.xmg.springboot.p2p.base.service.ISystemDictionaryService;

@Service
public class SystemDictionaryServiceImpl implements ISystemDictionaryService {
	@Autowired
	private SystemDictionaryMapper systemDictionaryMapper;
	@Autowired
	private SystemDictionaryItemMapper itemMapper;

	@Override
	public List<SystemDictionary> query(SystemDictionaryQueryObject qo) {

		return null;
	}

	@Override
	public List<SystemDictionary> selectAll() {

		return systemDictionaryMapper.selectAll();
	}

	/**
	 * 数据字典明细分页查询
	 */
	@Override
	public PageResult queryDictionayItem(SystemDictionaryQueryObject qo) {
		int count = itemMapper.queryForCount(qo);
		if (count > 0) {
			List<SystemDictionaryItem> list = itemMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(),
					qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	/**
	 * 数据字典分页查询
	 */
	@Override
	public PageResult queryDictionay(SystemDictionaryQueryObject qo) {
		int count = systemDictionaryMapper.queryForCount(qo);
		if (count > 0) {
			List<SystemDictionary> list = systemDictionaryMapper.query(qo);
			return new PageResult(list, count, qo.getCurrentPage(),
					qo.getPageSize());
		}
		return PageResult.empty(qo.getPageSize());
	}

	@Override
	public void saveOrUpdate(SystemDictionary dic) {
		if (dic.getId() == null) {
			systemDictionaryMapper.insert(dic);
		} else {
			systemDictionaryMapper.updateByPrimaryKey(dic);
		}
	}

	@Override
	public void saveOrUpdateUItem(SystemDictionaryItem item) {
		if (item.getId() == null) {
			itemMapper.insert(item);
		} else {
			itemMapper.updateByPrimaryKey(item);
		}
	}
	/**
	 * 通过字典编码查询字典明细
	 * @param string
	 */
	@Override
	public List<SystemDictionaryItem> loadItemsBySn(String sn) {
		return itemMapper.loadItemsBySn(sn);
	}

}
