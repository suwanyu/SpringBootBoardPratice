package com.web.config;

import com.web.oauth.ClientResources;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

// 각 소셜 미디어 리소스 정보를 빈으로 등록하기
@Configuration
@EnableWebSecurity // 웹에서 시큐리티 기능을사용하겠다는 어노테이션.
                // 스프링 부트에서는  @EnableWebSecurity를 사용하면 자동 설정 적용됨
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //WebSecurityConfigurerAdapter : 요청,권한,기타 설정에 대해서 최적화한 설정.
    // configure(HttpSecurity) 메서드를 오버라이드하여 원하는 형식의 시큐리티 설정

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //3.
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
                .authorizeRequests()
                    .antMatchers("/","/login/**","/css/**","/images/**","/js/**",
                            "/console/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .headers().frameOptions().disable()
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(
                            "/login"
                    ))
                .and()
                    .formLogin()
                    .successForwardUrl("/board/list")
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                .and()
                    .addFilterBefore(filter, CsrfFilter.class)
                    .csrf().disable();

    }



    @Bean
    @ConfigurationProperties("google")
    // 만약 @ConfigurationProperties이없다면 일일이 ㄱ프로퍼티값을 불러와야함.
    public ClientResources google() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("kakao")
    public ClientResources kakao(){
        return new ClientResources();
    }


}
