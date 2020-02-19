package com.linyuan.resource2server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author: 林塬
 * @date: 2018/1/22
 * @description: 通过访问远程授权服务器 check_token 端点验证令牌
 */
public class RemoteTokenService {
	@Autowired
	private DataSource dataSource;
	
    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Resource(name = "authServerProp")
    private AuthorizationServerProperties authorizationServerProperties;

    @Bean(name = "authServerProp")
    public AuthorizationServerProperties authorizationServerProperties(){
        return new AuthorizationServerProperties();
    }

    /*@Bean
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl(authorizationServerProperties.getCheckTokenAccess());
        if (StringUtils.isEmpty(oAuth2ClientProperties.getClientId())) {
            remoteTokenServices.setClientId(oAuth2ClientProperties.getClientId());
        }
        if (StringUtils.isEmpty(oAuth2ClientProperties.getClientSecret())){
            remoteTokenServices.setClientSecret(oAuth2ClientProperties.getClientSecret());
        }
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
        return remoteTokenServices;
    }*/

    @Bean
    public AccessTokenConverter accessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }

   @Bean
   public TokenStore tokenStore(){
       return new JdbcTokenStore(dataSource);
   }
   /**
    * @Description 令牌服务
    * @Date 2019/7/15 18:07
    * @Version  1.0
    */
   @Bean
   public DefaultTokenServices tokenServices(){
       DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
       defaultTokenServices.setTokenStore(tokenStore());
       return defaultTokenServices;
   }
}
