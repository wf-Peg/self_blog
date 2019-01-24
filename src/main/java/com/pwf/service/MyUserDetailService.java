package com.pwf.service;

import com.pwf.dao.UserRepository;
import com.pwf.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailService implements UserDetailsService {

    public static final String ROLE = "ROLE_";
    public static final String USER_NOT_FOUND_EXCEPTION = "找不到用户";
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(s)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_EXCEPTION));
        String authorityList="";
        String[] roles = user.getUserRole().split(",");
        for (String role:roles){
            String s1 = ROLE + role+",";
            authorityList+=s1;
        }
        //TODO passwordEncoder.encode是注册的时候使用，之后要直接改成数据库的值
        return new org.springframework.security.core.userdetails.User(s, passwordEncoder.encode(user.getPassWord()), AuthorityUtils
                .commaSeparatedStringToAuthorityList(authorityList));

    }
}
