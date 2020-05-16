package com.web.controller;

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

    @GetMapping(value ="{/google|kakao}/complete")
    public String lloginCompolete(HttpSession session){
        OAuth2Authentication authentication =
                (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> map = (HashMap<String, String>)
                authentication.getUserAuthentication().getDetails();
        session.setAttribute("user", User.builder().name(map.get("name"))
        .email(map.get("email"))
        .principal(map.get("id"))
        .socialType(SocialType.GOOGLE)
        .createdDate(LocalDateTime.now()));
        return null;
    };


}
