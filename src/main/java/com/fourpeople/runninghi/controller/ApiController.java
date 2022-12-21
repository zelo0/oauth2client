package com.fourpeople.runninghi.controller;

import com.fourpeople.runninghi.model.Users;
import com.fourpeople.runninghi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("token")
    public String token() {
        return "token";
    }

    @PostMapping("join")
    public String join(@RequestBody Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return "회원가입 성공";
    }

    @GetMapping("jwt/{accessToken}")
    public String jwt(@PathVariable("accessToken") String accessToken) {
        return "accessToken 주기 성공";
    }
}
