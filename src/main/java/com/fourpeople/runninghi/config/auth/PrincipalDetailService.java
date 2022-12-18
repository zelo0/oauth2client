package com.fourpeople.runninghi.config.auth;

import com.fourpeople.runninghi.model.Users;
import com.fourpeople.runninghi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 로그인 요청이 오면 자동으로 UserDetailsService 타입으로 ioc 되어 있는 클래스의 loadUserByUsername이 실행된다
// 함수 종료 시 @AuthenticationPrincipal 어노테이션이 생성된다
@Service
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // form에서 전달되는 게 username이어야 함. 바꾸고 싶으면 설정에서 바꾸기 가능
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users usersEntity = userRepository.findByUsername(username);
        if (usersEntity != null) {
            return new PrincipalDetails(usersEntity);
        }
        return null;
    }
}
