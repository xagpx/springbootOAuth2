package com.example.demo.granter;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.demo.service.MobileUserDetailsService;


/**
 * 手机验证码登陆provider
 */
public class MobileAuthenticationProvider extends DaoAuthenticationProvider {

    public MobileAuthenticationProvider(MobileUserDetailsService mobileUserDetailsService) {
        super.setUserDetailsService(mobileUserDetailsService);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}