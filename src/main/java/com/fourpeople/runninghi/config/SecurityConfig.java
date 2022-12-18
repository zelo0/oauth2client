package com.fourpeople.runninghi.config;

import com.fourpeople.runninghi.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 내가 등록하는 필터가 스프링 필터체인에 등록된다
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // csrf 토큰 안 쓰기 위해 사용
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin().loginPage("/loginForm")
//                .usernameParameter("username2")
                .loginProcessingUrl("/login") // 이 주소로 오면 스프링 시큐리티가 낚아채서 로그인 해줌
                .defaultSuccessUrl("/") // 다른 경로로 가려고 했으면 로그인 후 그 경로로 보내줌
                .and()
                .oauth2Login().loginPage("/loginForm") // 1. 코드 받기(인증됨) 2. 액세스 토큰 받기(권한) 3. 사용자 정보 받기 4. 정보로 회원가입 자동 진행
                // 구글 로그인 완료되면 코드 받고나서 액세스 토큰 + 프로필 정보 받아서 가진 상태. 후처리가 필요
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

    }
}
