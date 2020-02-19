package com.linyuan.resource2server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linyuan.resource2server.config.EnableRemoteTokenService;
import com.linyuan.resource2server.config.EnableResJWTTokenStore;
@SpringBootApplication
@EnableRemoteTokenService
//@EnableResJWTTokenStore //OAuth2 使用 JWT 解析令牌
public class Resource2ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Resource2ServerApplication.class, args);
	}
}
