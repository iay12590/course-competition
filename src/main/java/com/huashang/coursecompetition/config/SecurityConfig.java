package com.huashang.coursecompetition.config;

import com.huashang.coursecompetition.permission.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author linjianhua
 * @date 2022/9/15
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final UserDetailsService userDetailsService;

    public SecurityConfig(CustomAccessDeniedHandler customAccessDeniedHandler, UserDetailsService userDetailsService) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                // 不拦截登陆请求
                .antMatchers("/login")
                .permitAll()
                // user接口必须有USER角色
                .antMatchers("/user", "/user/**")
                .hasAuthority("USER")
                // 其他接口通过rbacPermission.hasPermission判定是否有权限
                .anyRequest()
                .access("@rbacPermission.hasPermission(request, authentication)")
                .and()
                // 表单登陆
                .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                // 登陆成功跳转页面
                .defaultSuccessUrl("/index.html")
                .and()
                // 异常处理
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .logout()
                // 登出
                .logoutUrl("/login?logout");
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
