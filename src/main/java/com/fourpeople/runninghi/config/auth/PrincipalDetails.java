package com.fourpeople.runninghi.config.auth;

// 로그인이 완료되면 시큐리티 세션을 만들어준다 (Security contextHolder에)
// 시큐리티 세션에 들어가는 오브젝트는 Authentication 객체여야 한다
// Authentication 안에 들어가야 하는 users 정보는 UserDetails 타입 객체여야 한다
// UserDetails in Authentication in SecuritySession

import com.fourpeople.runninghi.model.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Users users;

    private Map<String, Object> attributes;

    // 일반 로그인 시
    public PrincipalDetails(Users users) {
        this.users = users;
    }

    // oauth 로그인 시
    public PrincipalDetails(Users users, Map<String, Object> attributes) {
        this.users = users;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // User의 권한들을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return users.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
