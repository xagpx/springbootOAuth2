package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.annotation.EnableAuthJWTTokenStore;
@EnableAuthJWTTokenStore    // 使用 JWT 存储令牌
//@EnableDBClientDetailsService //从 JDBC 加载客户端详情,需配置在启动类上，若在子类上会出现顺序问题，导致 Bean 创建失败
@SpringBootApplication
public class SpringbootOauth2Application {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootOauth2Application.class, args);
	}

}
