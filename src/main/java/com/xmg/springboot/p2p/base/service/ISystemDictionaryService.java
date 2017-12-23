package com.xmg.springboot.p2p.base.service;

import java.util.List;

import com.xmg.springboot.p2p.base.Query.SystemDictionaryQueryObject;
import com.xmg.springboot.p2p.base.domain.SystemDictionary;
import com.xmg.springboot.p2p.base.domain.SystemDictionaryItem;
import com.xmg.springboot.p2p.base.page.PageResult;

/**
 * 数据字典服务
 * @author 1
 *
 */
public interface ISystemDictionaryService {

	public List<SystemDictionary> query(SystemDictionaryQueryObject qo);

	public List<SystemDictionary> selectAll();

	/**
	 * 数据字典明细分页查询
	 * @param qo
	 * @return
	 */
	public PageResult queryDictionayItem(SystemDictionaryQueryObject qo);

	/**
	 * 数据字典分页查询
	 * @param qo
	 * @return
	 */
	public PageResult queryDictionay(SystemDictionaryQueryObject qo);

	/**
	 * 新增或更新字典
	 */
	public void saveOrUpdate(SystemDictionary dic);

	/**
	 * 新增或更新字典明细
	 */
	public void saveOrUpdateUItem(SystemDictionaryItem item);

	/**
	 * 通过字典编码查询字典明细
	 * @param string
	 */
	public List<SystemDictionaryItem> loadItemsBySn(String string);
}
