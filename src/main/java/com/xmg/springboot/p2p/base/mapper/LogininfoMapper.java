package com.xmg.springboot.p2p.base.mapper;

import org.apache.ibatis.annotations.Param;

import com.xmg.springboot.p2p.base.domain.Logininfo;

public interface LogininfoMapper {

	int insert(Logininfo record);

	Logininfo selectByPrimaryKey(Integer id);

	int updateByPrimaryKey(Logininfo record);

	int countByUsername(String username);

	Logininfo login(@Param("username") String username,
			@Param("password") String password);

}