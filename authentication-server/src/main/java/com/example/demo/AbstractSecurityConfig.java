package com.example.demo;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.constants.AuthoritiesEnum;
import com.example.demo.granter.MobileAuthenticationProvider;
import com.example.demo.service.MobileUserDetailsService;

import javax.annotation.PostConstruct;

/**
 * @author: 林塬
 * @date: 2018/1/20
 * @description: Web 权限配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public abstract class AbstractSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    @Qualifier("mobileUserDetailsService")
    private MobileUserDetailsService mobileUserDetailsService;
    
    @PostConstruct
    public void init() {
        try {
            authenticationManagerBuilder
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
            authenticationManagerBuilder.authenticationProvider(mobileAuthenticationProvider());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	  http
    	 .authorizeRequests()
    	 .antMatchers("/").permitAll()
    	/*  .antMatchers("/user/getEmployeesList")
          .hasAnyRole("ADMIN")*/
    	 .anyRequest()
         .authenticated().and().formLogin()
         .permitAll()
         .and()
         .logout()
         .permitAll();
     http.csrf().disable(); 
    }
    
    /**
     * 创建手机验证码登陆的AuthenticationProvider
     *
     * @return mobileAuthenticationProvider
     */
    @Bean
    public MobileAuthenticationProvider mobileAuthenticationProvider() {
        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider(this.mobileUserDetailsService);
        mobileAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return mobileAuthenticationProvider;
    }
}