package com.web.resolver;

import com.web.annotation.SocialUser;
import com.web.domain.enums.SocialType;
import com.web.domain.enums.User;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import static com.web.domain.enums.SocialType.GOOGLE;
import static com.web.domain.enums.SocialType.KAKAO;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private UserRepository userRepository;

    public UserArgumentResolver(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    @Override
    //파라미터에 @SocialUser 어노테이션이 있고 타입이 User인 파라미터만 true를 반환
    public boolean supportsParameter(MethodParameter parameter){
        return parameter.getParameterAnnotation(SocialUser.class) !=null &&
                parameter.getParameterType().equals(User.class);
    }





    @Override
    // 검증이 완료된 파라미터 정보를받음. 이미 검증ㅇ이 되어 세션에 해당 User 객체가 있으면
    // User 객체를 구성하는 로직을 수행하지 않도록 세션을 먼저 확인하는 코드
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        User user = (User) session.getAttribute("user");
        return getUser(user, session);
    }

    private User getUser(User user, HttpSession session) {
        if(user == null) {
            try {
                OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
                Map<String, String> map = (HashMap<String, String>) authentication.getUserAuthentication().getDetails();
                User convertUser = convertUser(String.valueOf(authentication.getAuthorities().toArray()[0]), map);
                user = userRepository.findByEmail(convertUser.getEmail());
                if (user == null) { user = userRepository.save(convertUser); }
                setRoleIfNotSame(user, authentication, map);
                session.setAttribute("user", user);
            } catch (ClassCastException e) {
                return user;
            }
        }
        return user;
    }

    // 사용자의 인증된 소셜 미디어 타입에 따라 빌더를 사용하여 User 객체를 만들어주는 가교 역할
    private User convertUser(String authority, Map<String, String> map) {

        if(GOOGLE.isEquals(authority)) return getModernUser(GOOGLE, map);
        else if(KAKAO.isEquals(authority)) return getKaKaoUser(map);
        return null;
    }

    // 공통되는 명명규칙을 가진 그룹을 User 객체로 매핑
    private User getModernUser(SocialType socialType, Map<String, String> map) {
        return User.builder()
                .name(map.get("name"))
                .email(map.get("email"))
                .socialType(socialType)
                .createdDate(LocalDateTime.now())
                .build();
    }

    private User getKaKaoUser(Map<String, String> map) {
        HashMap<String, String> propertyMap = (HashMap<String, String>)(Object) map.get("properties");
        return User.builder()
                .name(propertyMap.get("nickname"))
                .email(map.get("kaccount_email"))
                .socialType(KAKAO)
                .createdDate(LocalDateTime.now())
                .build();
    }

    // 인증된 authentication이 권한을 갖고 있는지 체크하는 용도로 쓰임
    // 만약 저장된 User 권한이 없으면 SecurityContextHoldedr를 사용하여
    // 해당 소셜 미디어 타입으로 권한 저장
    private void setRoleIfNotSame(User user, OAuth2Authentication authentication, Map<String, String> map) {
        if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority(user.getSocialType().getRoleType()))) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A", AuthorityUtils.createAuthorityList(user.getSocialType().getRoleType())));
        }
    }

}
