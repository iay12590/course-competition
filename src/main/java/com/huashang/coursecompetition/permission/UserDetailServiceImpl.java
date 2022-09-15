package com.huashang.coursecompetition.permission;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author linjianhua
 * @date 2022/9/15
 */
@Component
public class UserDetailServiceImpl implements UserDetailsService {
    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("lin".equals(username)) {
            return new UserEntity(username, new BCryptPasswordEncoder().encode("123456"), "USER");
        }
        return new UserEntity(username, new BCryptPasswordEncoder().encode("12345678"), "ADMIN");
    }
}
