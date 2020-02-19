package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.example.demo.exception.CustomWebResponseExceptionTranslator;
import com.example.demo.granter.MobileTokenGranter;
import com.example.demo.store.CustomTokenEnhancer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

/**
 * @author: 林塬
 * @date: 2018/1/20
 * @description: OAuth2 授权服务器配置类
 */
@EnableAuthorizationServer
public abstract class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TokenStore tokenStore;
    
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired(required = false)
    private JdbcClientDetailsService jdbcClientDetailsService;

    @Autowired
    public ClientDetailsService clientDetailsService;
    
    @Qualifier("dataSource")
    @Autowired(required = false)
    DataSource dataSource;
    
    //令牌失效时间
    public int accessTokenValiditySeconds;

    //刷新令牌失效时间
    public int refreshTokenValiditySeconds;

    //是否可以重用刷新令牌
    public boolean isReuseRefreshToken;

    //是否支持刷新令牌
    public boolean isSupportRefreshToken;

   /**
    * 
    * @param accessTokenValiditySeconds //令牌失效时间
    * @param refreshTokenValiditySeconds  //刷新令牌失效时间
    * @param isReuseRefreshToken //是否可以重用刷新令牌
    * @param isSupportRefreshToken//是否支持刷新令牌 
    */
    public AuthServerConfig(int accessTokenValiditySeconds, int refreshTokenValiditySeconds, boolean isReuseRefreshToken, boolean isSupportRefreshToken) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.isReuseRefreshToken = isReuseRefreshToken;
        this.isSupportRefreshToken = isSupportRefreshToken;
    }
    /**
     * 配置授权服务器端点，如令牌存储，令牌自定义，用户批准和授权类型，不包括端点安全配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        Collection<TokenEnhancer> tokenEnhancers = applicationContext.getBeansOfType(TokenEnhancer.class).values();
//        TokenEnhancerChain tokenEnhancerChain=new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));
//        
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setReuseRefreshToken(isReuseRefreshToken);
//        defaultTokenServices.setSupportRefreshToken(isSupportRefreshToken);
//        defaultTokenServices.setTokenStore(tokenStore);
//        defaultTokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
//        defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
//        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
        
        //若通过 JDBC 存储令牌
//       if (Objects.nonNull(jdbcClientDetailsService)){
//             defaultTokenServices.setClientDetailsService(jdbcClientDetailsService);
//       } 
  	
     	endpoints
     	.tokenStore(tokenStore)
     	.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
        .tokenEnhancer(tokenEnhancer())
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService)
        //.tokenServices(defaultTokenServices)
        .reuseRefreshTokens(true)
        .exceptionTranslator(customExceptionTranslator())
        //update by joe_chen add  granter
        .tokenGranter(tokenGranter(endpoints));
     	
     	//若通过 JDBC 存储令牌
     	/* if (Objects.nonNull(jdbcClientDetailsService)){
     		endpoints.setClientDetailsService(jdbcClientDetailsService);
         } 
     	 */
     	if (Objects.nonNull(dataSource)){
     		endpoints.authorizationCodeServices(authorizationCodeServices());
     		endpoints.approvalStore(approvalStore());
     	} 
    }
    
    /**
     * 配置授权服务器端点的安全
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
        // 开启/oauth/token_key验证端口无权限访问
                 .tokenKeyAccess("permitAll()")
                // 开启/oauth/check_token验证端口认证权限访问
                 .checkTokenAccess("permitAll()")
                 .allowFormAuthenticationForClients();
    }
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
        //jwt token
    	/*TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(), jwtAccessTokenConverter));
        return tokenEnhancerChain;*/
    }
    /**
     * 配置自定义的granter,手机号验证码登陆
     *
     * @param endpoints
     * @return
     * @auth joe_chen
     */
    public TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
    	
    	List<TokenGranter> granters=new ArrayList<TokenGranter>();
    	granters.add(endpoints.getTokenGranter());
    	TokenGranter mobile=new MobileTokenGranter(
                authenticationManager,
                endpoints.getTokenServices(),
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());
    	granters.add(mobile);
        return new CompositeTokenGranter(granters);
    }
    /**
     * 自定义OAuth2异常处理
     *
     * @return CustomWebResponseExceptionTranslator
     */
    @Bean
    public WebResponseExceptionTranslator customExceptionTranslator() {
        return new CustomWebResponseExceptionTranslator();
    }
    
    /**
     * 授权信息持久化实现
     *
     * @return JdbcApprovalStore
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 授权码模式持久化授权码code
     *
     * @return JdbcAuthorizationCodeServices
     */
    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        // 授权码存储等处理方式类，使用jdbc，操作oauth_code表
        return new JdbcAuthorizationCodeServices(dataSource);
    }

}
