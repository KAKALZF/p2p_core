package com.xmg.springboot.p2p;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//这个标签的具体作用是怎样的
@EnableAutoConfiguration
@ComponentScan
@MapperScan(basePackages = "com.xmg.springboot.p2p.*.mapper")
//开启事务管理
@EnableTransactionManagement
public class CoreAppConfig {

}
