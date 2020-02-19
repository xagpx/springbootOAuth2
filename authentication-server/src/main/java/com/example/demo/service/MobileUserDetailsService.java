package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constants.AuthoritiesEnum;

/**
 * 手机验证码登陆, 用户相关获取
 */
@Slf4j
@Service("mobileUserDetailsService")
public class MobileUserDetailsService extends UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) {

        //User user = userService.getByUniqueId(uniqueId);
        //log.info("load user by mobile:{}", user.toString());

        // 如果为mobile模式，从短信服务中获取验证码（动态密码）
        //String credentials = smsCodeProvider.getSmsCode(uniqueId, "LOGIN");
    	String credentials ="123456";
    	 if (username.equals("linyuan")) {
             PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
             String password = passwordEncoder.encode("123456");
             UserDetails userDetails = new User("linyuan",
                     password,
                     AuthorityUtils.commaSeparatedStringToAuthorityList(AuthoritiesEnum.USER.getRole()));
             return userDetails;
         }
         return null;
    }
}
