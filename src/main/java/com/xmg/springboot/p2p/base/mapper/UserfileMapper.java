package com.xmg.springboot.p2p.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xmg.springboot.p2p.base.Query.UserfileQueryObject;
import com.xmg.springboot.p2p.base.domain.Userfile;

public interface UserfileMapper {

	int insert(Userfile record);

	Userfile selectByPrimaryKey(Long id);

	int updateByPrimaryKey(Userfile record);

	int queryForCount(UserfileQueryObject qo);

	List<Userfile> query(UserfileQueryObject qo);

	List<Userfile> listSelectTypeUserFiles(@Param("id") Long id,
			@Param("selected") boolean selected);
}