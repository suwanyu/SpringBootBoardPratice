package com.web.config;

import com.web.domain.enums.SocialType;
import com.web.oauth.ClientResources;


import com.web.oauth.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import static com.web.domain.enums.SocialType.KAKAO;
import static com.web.domain.enums.SocialType.GOOGLE;
// 각 소셜 미디어 리소스 정보를 빈으로 등록하기
@Configuration
@EnableWebSecurity // 웹에서 시큐리티 기능을사용하겠다는 어노테이션.
                // 스프링 부트에서는  @EnableWebSecurity를 사용하면 자동 설정 적용됨
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //WebSecurityConfigurerAdapter : 요청,권한,기타 설정에 대해서 최적화한 설정.
    // configure(HttpSecurity) 메서드를 오버라이드하여 원하는 형식의 시큐리티 설정

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //3.
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
                .authorizeRequests()// 인증 메커니즘을 요청한 HttpServletRequest 기반으로 설정
                    .antMatchers("/","/login/**","/css/**","/images/**","/js/**", // antMatchers() : 요청 을 리스트 형식으로 설정
                            "/console/**").permitAll() //permitAll() : 설정한 리퀘스트 패턴을 누구나 접근할 수 있도록 허용
                    .anyRequest().authenticated() //anyRequest() : 설정한 요청 이외의 리퀘스트 요청을 표현,
                                                    //authenticated() : 해당 요청은 인증된 사용자만 할 수 있다.
                .and()
                    .headers().frameOptions().disable()
                //headers() : 응답에 해당하는 header를 설정한다. 설정하지 않으면 디폴트값으로 설정
                // frameOptions().disable() : XFrameOptionsHeaderWriter의 최적화 설정을 허용하지 않음
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(
                            "/login"
                    ))
                //인증의 진입 지점. 인증되지 않은 사용자가 허용되지 않는 경로로 리퀘스트를 요청할 경우 '/login'으로 이동됨
                .and()
                    .formLogin()
                    .successForwardUrl("/board/list")
                // formLogin().successForwardUrl("/board/list") : 로그인에 성공하면 설정된 경로로 포워딩됨
                .and()
                    .logout()
                // logout() : 로그아웃에 대한 설정. 코드에서는 로그아웃이 수행될 URL(logoutUrl), 로그아웃이 성공했을 때 포워딩도리 URL(logoutSuccessUrl),
                // 로그아웃을 성공했을 때 삭제될 쿠키값(deleteCookies), 설정된 세션의 무효화(invalidateHttpSession)를 수행하게끔 설정되어 있음
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                .and()
                    .addFilterBefore(filter, CsrfFilter.class)
                // addFilterBefore(filter,beforeFilter): 첫 번째 인자보다 먼저 시작될 필터를 등록함.
                // addFilterBefore(filter, CsrFilter.class) : 문자 인코딩 필터(filter)보다 CsrFilter를 먼저 실행하도록 설정함.
                    .addFilterBefore(oauth2Filter(), BasicAuthenticationFilter.class)
                    .csrf().disable();

    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    private Filter oauth2Filter(){
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(oauth2Filter(google(),"/login/google",GOOGLE));
        filters.add(oauth2Filter(kakao(),"/login/kakao",KAKAO));
        filter.setFilters(filters);
        return filter;
    }

    private Filter oauth2Filter(ClientResources client, String path, SocialType socialType){
        // 1. 인증이 수행될 경로를 넣어 OAuth2 클라이언트용 인증 처리 필터 생성
        OAuth2ClientAuthenticationProcessingFilter filter=
                new OAuth2ClientAuthenticationProcessingFilter(path);
        // 2. 권한 서버와의 통신을 위해 OAuth2RestTemplate을 생성.
        // 이를 생성하기 위해선 client 프로퍼티 정보와 OAuth2ClientContext가 필요
        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(),
                oAuth2ClientContext);
        filter.setRestTemplate(template);
        // 3. User의 권한을 최적화해서 생성하고자 UserInfoTokenServices를 상속받은
        // UserTokenService를 생성함. OAuth2 AccessToken 검증을 위해 생성한
        // UserTokenService를 필터의 토큰 서비스로 등록함
        filter.setTokenServices(new UserTokenService(client, socialType));
        // 4. 인증이 성공적으로 이루어지면 피렅에 리다이렉트될 URL을 설정함.
        filter.setAuthenticationSuccessHandler((request, response, authentication) ->
                response.sendRedirect("/"+socialType.getValue()+"/complete"));
        // 5. 인증이 실패하면 필터에 리다이렉트될 URL을 설정
        filter.setAuthenticationFailureHandler((request,response,exception)->
                response.sendRedirect("/error"));
        return filter;
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
