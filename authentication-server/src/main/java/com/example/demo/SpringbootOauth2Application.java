package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.demo.annotation.EnableAuthJWTTokenStore;
import com.example.demo.annotation.EnableDBClientDetailsService;
import com.example.demo.annotation.EnableDBTokenStore;
@EnableAuthJWTTokenStore    // 使用 JWT 存储令牌
@EnableDBTokenStore
@EnableDBClientDetailsService //从 JDBC 加载客户端详情,需配置在启动类上，若在子类上会出现顺序问题，导致 Bean 创建失败
@SpringBootApplication
public class SpringbootOauth2Application {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootOauth2Application.class, args);
	}
	
	/**
     * 解决前后端分离跨域问题
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
