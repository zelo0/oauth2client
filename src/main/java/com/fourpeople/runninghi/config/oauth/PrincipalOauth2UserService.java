package com.fourpeople.runninghi.config.oauth;

import com.fourpeople.runninghi.config.auth.PrincipalDetails;
import com.fourpeople.runninghi.config.oauth.provider.FacebookUserInfo;
import com.fourpeople.runninghi.config.oauth.provider.GoogleUserInfo;
import com.fourpeople.runninghi.config.oauth.provider.NaverUserInfo;
import com.fourpeople.runninghi.config.oauth.provider.OAuth2UserInfo;
import com.fourpeople.runninghi.model.Users;
import com.fourpeople.runninghi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대해 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
        System.out.println("getAccessToken: " + userRequest.getAccessToken());
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 리턴(OAuth2Client 라이브러리가 처리) -> AccessToken 요청
        // 여기까지 userRequest 정보
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes: " + oAuth2User.getAttributes());
        // userRequest 정보로 loadUser 함수 호출하여 구글로부터 회원 프로필 받기

        OAuth2UserInfo oAuth2UserInfo = null;
        switch (userRequest.getClientRegistration().getRegistrationId()) {
            case "google" -> {
                System.out.println("구글 로그인 요청");
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            }
            case "facebook" -> {
                System.out.println("페이스북 로그인 요청");
                oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
            }
            case "naver" -> {
                System.out.println("네이버 로그인 요청");
                oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
            }
            default -> System.out.println("우리는 구글, 페이스북만 지원해요");
        }

        // 회원가입 진행
        String provider = oAuth2UserInfo.getProvider(); // google
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String role = "ROLE_USER";

        Users usersEntity = userRepository.findByUsername(username);
        if (usersEntity == null) {
            usersEntity = Users.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(usersEntity);
        }

        return new PrincipalDetails(usersEntity, oAuth2User.getAttributes());
    }
}
