package com.fourpeople.runninghi.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourpeople.runninghi.config.auth.PrincipalDetails;
import com.fourpeople.runninghi.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter가 작동한다
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login으로 로그인 요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도");

        // username, password 받아서 로그인 시도
        // authenticationManager로 로그인 시도를 하면 PrincipalDetailService가 호출됨
        // PrincipalDetailService가 호출되면 loadByUsername 호출됨
        // PrincipalDetails를 세션에 담고 - 왜 담아? 세션에 담지 않으면 권한 관리가 안 됨
        // jwt 토큰을 만들어 응답해주면 됨
        try {
//            BufferedReader reader = request.getReader();
//            String input = null;
//            while ((input = reader.readLine()) != null) {
//                System.out.println("input = " + input);
//            }

            ObjectMapper objectMapper = new ObjectMapper();
            Users user = objectMapper.readValue(request.getInputStream(), Users.class);
            System.out.println("user = " + user);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailService의 loadUserByUsername() 함수 실행됨
            // 로그인한 정보가 담겨있다
            // authenticaion은 임시로 만들어진 상태
            //
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("principalDetails = " + principalDetails);
            // 리턴할 때 authenticaion이 세션에 저장된다
            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 앞 메소드 실행 후 실행
    // 인증이 정상적이었으면 실행되는 메소드
    // jwt 만들어서 요청한 사용자에게 주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("success authentication");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
//                .withSubject("cos token")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10) )
                .withClaim("id", principalDetails.getUsers().getId())
                .withClaim("username", principalDetails.getUsername())
                .sign(Algorithm.HMAC256("cos"));

        response.addHeader("Authorization", "Bearer " + jwtToken);

    }


}
