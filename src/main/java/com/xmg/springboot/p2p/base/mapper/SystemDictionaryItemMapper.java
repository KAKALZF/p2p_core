package com.xmg.springboot.p2p.base.mapper;

import java.util.List;

import com.xmg.springboot.p2p.base.Query.SystemDictionaryQueryObject;
import com.xmg.springboot.p2p.base.domain.SystemDictionaryItem;

public interface SystemDictionaryItemMapper {

	int insert(SystemDictionaryItem record);

	SystemDictionaryItem selectByPrimaryKey(Long id);

	int updateByPrimaryKey(SystemDictionaryItem record);

	int queryForCount(SystemDictionaryQueryObject qo);

	List<SystemDictionaryItem> query(SystemDictionaryQueryObject qo);

	List<SystemDictionaryItem> loadItemsBySn(String sn);
}