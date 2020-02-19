package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import com.example.demo.AuthServerConfig;

import java.util.concurrent.TimeUnit;

/**
 * @author: 林塬
 * @date: 2018/1/10
 * @description: OAuth2 授权服务器配置
 */
@Configuration
public class AuthorizationServerConfig extends AuthServerConfig {
    /**
     * 调用父类构造函数，设置令牌失效日期等信息
     */
    public AuthorizationServerConfig() {
    	  /**
    	    * 
    	    * @param accessTokenValiditySeconds //令牌失效时间
    	    * @param refreshTokenValiditySeconds  //刷新令牌失效时间
    	    * @param isReuseRefreshToken //是否可以重用刷新令牌
    	    * @param isSupportRefreshToken//是否支持刷新令牌 
    	    */
         super((int)TimeUnit.DAYS.toSeconds(1), 0, false, false);
    }

    /**
     * 配置客户端详情
     * @param clients
     * @throws Exception
     */
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	// 配置客户端信息，从数据库中读取，对应oauth_client_details表
         //clients.jdbc(dataSource);
         clients.withClientDetails(clientDetailsService);
//    	super.configure(clients);
//        clients.inMemory()                          // 使用内存存储客户端信息
//        .withClient("resource1")       // client_id
//                .secret("secret")                   // client_secret
//                .authorizedGrantTypes("client_credentials", "password", "refresh_token", "authorization_code","mobile")
//                //.accessTokenValiditySeconds(60)               // Token 的有效期
//                //.refreshTokenValiditySeconds(3600)    //
//                .scopes("read")                    // 允许的授权范围
//                .autoApprove(true);                  //登录后绕过批准询问(/oauth/confirm_access)
    }
}
