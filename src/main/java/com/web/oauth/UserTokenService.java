package com.web.oauth;

import com.web.domain.enums.SocialType;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;
//UserInfoTokenServices는 스프링 시큐리티 OAuth2에서 제공하는 클래스.
//User 정보를 얻어오기 위해 소셜 서버와 통신하는 역할 수행, URI와 clientId 정보가 필요함.
//소셜 미디어 정보를 SocialType을 기준으로 관리할 것이기 때문에 UserInfoTokenServices 생성자에서
// super()를 사용하여 각각의 소셜 미디어 정보를 주입할 수 있도록 함.
public class UserTokenService extends UserInfoTokenServices {
    public UserTokenService(ClientResources resources, SocialType socialType){
        //UserInfoTokenServices 생성자에서 super()를 사용하여
        //각각의 소셜 미디어 정보를 주입할 수 있도록 함.
        super(resources.getResource().getUserInfoUri(), resources.getClient().getClientId());
        setAuthoritiesExtractor(new OAuth2AuthoritiesExtractor(socialType));
    }

    // AuthoritiesExtractor 인터페이스를 구현한 내부 클래스스
   public static class OAuth2AuthoritiesExtractor implements AuthoritiesExtractor {
        private String socialType;

        public OAuth2AuthoritiesExtractor(SocialType socialType){
            // 권한 생성 방식을 ROLE_ 으로 하기 위해 SocialType의 getRoleType()메소드 사용
            this.socialType= socialType.getRoleType();
        }

        public List<GrantedAuthority> extractAuthorities(Map<String, Object> map){
            return AuthorityUtils.createAuthorityList(this.socialType);
        }

    }

}
