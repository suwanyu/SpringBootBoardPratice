package com.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//소셜 미디어에 인증된 user를 가져온다ㅏ는 사실을 더 명확하게 표한히기 위해 파라미터용 어노테이션 추가
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SocialUser {
}
