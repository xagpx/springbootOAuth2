package com.linyuan.resource1server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.linyuan.resource1server.config.EnableResJWTTokenStore;

@SpringBootApplication
@EnableResJWTTokenStore //OAuth2 使用 JWT 解析令牌
public class Resource1ServerApplication {

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(Resource1ServerApplication.class, args);
	}
}
