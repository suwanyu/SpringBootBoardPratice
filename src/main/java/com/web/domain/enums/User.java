package com.web.domain.enums;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table
public class User implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx; // 인덱스

    @Column
    private String name; // 회원 이름

    @Column
    private String password; // 회원 패스워드

    @Column
    private String email; // 회원 이메일

    @Column
    private String principal;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;


    @Column
    private LocalDateTime createdDate; // 생성 날짜

    @Column
    private LocalDateTime updatedDate; // 수정 날짜

    @Builder
    public User(String name, String password, String email, String principal, SocialType socialType, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.principal=principal;
        this.socialType=socialType;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
