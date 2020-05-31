package com.web;

import com.web.resolver.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

public class BootWebApllication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(BootWebApplication.class, args);
    }

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    public void addArguumentResolvers(List<HandlerMethodArgumentResolver>
                                      argumentResolvers){
        argumentResolvers.add(userArgumentResolver);
    }

}
