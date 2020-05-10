package com.web.oauth;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

public class ClientResources {
    @NestedConfigurationProperty
    // 해당 필드가 단일값이 아닌 중복으로 바인딩된다고 표시하는 어노테이션.
    // 소셜미디어의 프로퍼티를 각각 바인딩하므로 @NestedConfigurationProperty 어노테이션 붙임
    private AuthorizationCodeResourceDetails client=
            // AuthorizationCodeResourceDetails 객체는
            // 'client'를 기준으로 하위의 키/값을매핑해주는 대상 객체
            new AuthorizationCodeResourceDetails();

    @NestedConfigurationProperty
    //ResourceServerProperties객체는 원래 OAuth2 리소스값을 매핑하는 데 사용하지만
    // 예제에서는 회원 정보를 얻는 userInfoUri 값을 받는 데 사용함
    private ResourceServerProperties resource = new ResourceServerProperties();

    public AuthorizationCodeResourceDetails getClient(){
        return client;
    }

    public ResourceServerProperties getResource(){
        return resource;
    }

}
