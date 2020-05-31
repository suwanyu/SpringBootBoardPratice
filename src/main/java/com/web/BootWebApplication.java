package com.web;

import com.web.annotation.SocialUser;
import com.web.domain.enums.Board;
import com.web.domain.enums.BoardType;
import com.web.domain.enums.User;
import com.web.repository.BoardRepository;
import com.web.repository.UserRepository;
import com.web.resolver.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication

public class BootWebApplication extends WebMvcConfigurerAdapter{
    public static void main(String[] args) {
        SpringApplication.run(BootWebApplication.class,args);
    }

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver>
                                     argumentResolvers){
    argumentResolvers.add(userArgumentResolver);
    }
    @Bean
    public CommandLineRunner runner(UserRepository userRepository,
                                    BoardRepository boardRepository) throws Exception {
        return (args) ->{
            // 스프링은 빈으로 생성된 메서드에 파라미터로 DI시키는 매커니즘이 존재
            // 생성자를 통해 의존성을 주입시키는 방법과 유사함
            // 이를 이용하여 CommandLineRunner를 빈으로 등록한 후
            // UserRepository와 BoardRepository를 주입받음
            User user = userRepository.save(User.builder()
            .name("havi")
            .password("test")
            .email("havi@gmail.com")
            .createdDate(LocalDateTime.now())
            .build());
            // 메서드 내부에 실행이 필요한 코드 작성. User 객체를 빌더 패턴을 사용해여 생성한 후
            // 주입받은 UserRepository를 사용하여 User 객체 저장
            IntStream.rangeClosed(1,200).forEach(index->
                    boardRepository.save(Board.builder()
                    .title("게시글"+index)
                    .subTitle("순서"+index)
                    .content("콘텐츠")
                    .boardType(BoardType.free)
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .user(user).build())
            );
        };
    }
}
