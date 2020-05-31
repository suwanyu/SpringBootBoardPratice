package com.web.controller;

import com.web.annotation.SocialUser;
import com.web.domain.enums.SocialType;
import com.web.domain.enums.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    // 1. 인증이 성공적으로 처리된 이후에 리다이렉트 되는경로.
    // 허용하는 요청의 URL 매핑을 /google/complete 으로 제한함.

    @GetMapping(value ="{/google|kakao}/complete")
    public String loginCompolete(@SocialUser User user){
    return "redirect:/baord/list";
    }




/*//*
/ 2. SecurityContextHolder에서 인증된 정보를 OAuth2Autentication 형태로 받아옴.
        // OAuth2Autentication은 기본적인 인증에 대한 정보뿐만 아니라
        // OAuth2 인증과 관련된 정보로도 함께 제공함.
        OAuth2Authentication authentication =
                (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

        // 3. 리소스 서버에서 받아온 개인정보를 getDetails()를 사용해 Map 타입으로 받을 수 있음
        Map<String, String> map = (HashMap<String, String>)
                authentication.getUserAuthentication().getDetails();
        session.setAttribute(
                // 4. 세션에 빌더를 사용하여 인증된   User 정보를 User 객체로 변환하여 저장
                "user", User.builder().name(map.get("name"))
        .email(map.get("email"))
        .principal(map.get("id"))
        .socialType(SocialType.GOOGLE)
        .createdDate(LocalDateTime.now()));
        return null;*//*

    };
*/


}
