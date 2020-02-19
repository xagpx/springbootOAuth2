package com.example.demo.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import java.security.KeyPair;

/**
 * @author: 林塬
 * @date: 2018/1/20
 * @description: 授权服务器 TokenStore 配置类，使用 JWT RSA 非对称加密
 */
public class AuthJWTTokenStore {

    @Bean("keyProp")
    public KeyProperties keyProperties(){
        return new KeyProperties();
    }

    @Resource(name = "keyProp")
    private KeyProperties keyProperties;

    /**
     * jwt 对称加密密钥
     */
    @Value("${security.oauth2.jwt.signingKey}")
    private String signingKey;
    
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //对称加密
        converter.setSigningKey(signingKey); 
        //非对称加密
//       KeyPair keyPair = new KeyStoreKeyFactory
//                (keyProperties.getKeyStore().getLocation(), keyProperties.getKeyStore().getSecret().toCharArray())
//                .getKeyPair(keyProperties.getKeyStore().getAlias());
//        converter.setKeyPair(keyPair); 
        
        return converter;
    }

}
