package com.xmg.springboot.p2p.base.mapper;

import java.util.List;

import com.xmg.springboot.p2p.base.Query.SystemDictionaryQueryObject;
import com.xmg.springboot.p2p.base.domain.SystemDictionary;

public interface SystemDictionaryMapper {

	int insert(SystemDictionary record);

	SystemDictionary selectByPrimaryKey(Long id);

	int updateByPrimaryKey(SystemDictionary record);

	List<SystemDictionary> query();

	List<SystemDictionary> selectAll();

	int queryForCount(SystemDictionaryQueryObject qo);

	List<SystemDictionary> query(SystemDictionaryQueryObject qo);
}