package com.example.demo.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class CustomTokenEnhancer implements TokenEnhancer {
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, 
     OAuth2Authentication authentication) {
    	if(accessToken instanceof DefaultOAuth2AccessToken){
            DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
            token.setValue(KeyValue_lenght(128,false));
            /*OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if(refreshToken instanceof DefaultOAuth2RefreshToken){
                token.setRefreshToken(new DefaultOAuth2RefreshToken(KeyValue_lenght(128,false)));
            }*/
            
            DefaultExpiringOAuth2RefreshToken refreshToken = (DefaultExpiringOAuth2RefreshToken)token.getRefreshToken();
            //OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if(refreshToken instanceof DefaultExpiringOAuth2RefreshToken){
                token.setRefreshToken(new DefaultExpiringOAuth2RefreshToken(KeyValue_lenght(128,false), refreshToken.getExpiration()));
            }
    	
    	//token 添加内容
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("userInfo", authentication.getPrincipal());
        additionalInfo.put("organization", authentication.getName());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return token;
    	}
        return accessToken;
    }

	/**
	 * @Description 生成自定义token
	 * @Date 2019/7/9 19:50
	 * @Version 1.0
	 */
	private String KeyValue_lenght(int lenght, boolean... ma) {
		Random random = new Random();
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < lenght; ++i) {
            //产生0-61的数字
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return ma.length != 0 ? ma[0] ? sb.toString().toUpperCase() : sb.toString().toLowerCase() : sb.toString();
    }
}