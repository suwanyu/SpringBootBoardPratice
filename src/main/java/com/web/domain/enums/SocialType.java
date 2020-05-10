package com.web.domain.enums;

// 소셜미디어의 정보를 나타냄
public enum SocialType {
    FACEBOOK("facebook"),
        GOOGLE("google"),
        KAKAO("kakao");

    private final String ROLE_PREFIX = "ROLE_";
    private String name;

    SocialType(String name) {
        this.name = name;
    }

    public String getRoleType(){
        // 'ROLE_*' 형식으로 소셜 미디어의 권한명을 생성함.
        // enum을 사용해 권한 생성 로직을 공통 코드로 처리하여 중복 코드를 줄일 수 있음
        return ROLE_PREFIX + name.toUpperCase() ;
    }

    public String getValue(){
        return name;
    }

    public boolean isEquals(String authority){
        return this.getRoleType().equals(authority);
    }
}
