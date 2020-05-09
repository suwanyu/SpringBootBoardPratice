package com.web.domain.enums;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter // Lombok 어노테이션. getXxx() 자동생성

@Entity // JPA 어노테이션. 테이블과 자바 클래스 매핑. 하나의 엔티티 타입을 생성한다= 하나의 클래스를 작성한다
//         엔티티라는 용어는 때로는 클래스를 의미하는 경우도 있고, 클래스에 의해 생성된 인스턴스를 의미하는 경우가 있습니다.
//         정확히 얘기 하자면, 엔티티 클래스 와 엔티티 인스턴스 혹은 엔티티 객체라는 표현이 정확합니다.
@Table // @Entity 클래스 관련 어노테이션. 클래스가 테이블이 되기 때문에 클래스의 선언부에 작성하여 테이블명을 어떻게 정할지 결정합니다.
//        기본적으로 @Table이 지정되지 않으면, 클래스 명으로 테이블이 생성됩니다.
@NoArgsConstructor //Lombok 어노테이션. 생성자 자동 생성 ; 매개변수(파라미터)가 없는 생성자를 생성
public class Board implements Serializable {

    @Id // @Entity 클래스 관련 어노테이션. 해당 칼럼이 식별키(PK, Primary key)라는 것을 의미합니다. 모든 엔티티에 반드시 @Id 지정해 주어야 합니다.
    // 주로 @GeneratedValue와 함께 식별키를 어떤 전략으로 생서하는지 명시합니다.
    @Column // @Entity 클래스 관련 어노테이션. 인스턴스 변수가 칼럼이 되기 때문에,
    // 컬럼명을 별도로 지정하거나 컬럼의 사이즈, 제약조건들을 추가하기 위해 사용됩니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본 키가 자동으로 할당되도록 설정하는 어노테이션. 기본 키 할당전략을 선택할 수 있는데,
    // 키 생성을 데이터베이스에 위임하는 IDENTITY 전략을 사용함.
    private Long idx; // 인덱스

    @Column
    private String title; // 제목

    @Column
    private String subTitle; // 부제목

    @Column
    private String content; // 내용

    @Column
    @Enumerated(EnumType.STRING) //Enum 타입 매핑용 어노테이션. @Enumerated 어노테이션을 이용해 자바 enum형과 데이터베이스 데이터 변환을 지원합니다.
    // 실제로 자바 enum형이지만 데이터베이스의 String형으로 변환하여 저장하겠다고 선언한 것.
    private BoardType boardType; // 게시판 타입

    @Column
    private LocalDateTime createdDate; // 생성 날짜

    @Column
    private LocalDateTime updatedDate; // 수정 날짜

    @OneToOne(fetch = FetchType.LAZY) //도메인 Board와 Board가 필드값으로 갖고 있는 User 도메인을 1:1 관계로 설정하는 어노테이션. 실제로 DB에 저장될 때는 User 객체가 저장되는 것이 아니라 User의 PK인 user_idx 값이 저장됨.
    // fetch는 eager와 lazy 두 종류가 있는데 전자는 처음 Board 도메인을 조회할 때 즉시 관련 User 객체를 함께조회한다는 뜻이고
    // 후자는 User 객체를 조회하는 시점이 아닌 객체가 실제로 사용될 때 조회한다는 뜻임.
    private User user; //  User의 PK인 user_idx(유저 인덱스) 값이 저장됨.

    @Builder // 객체생성할 때 쓰는 거라는데 잘 모르겠음..
    public Board(String title, String subTitle, String content, BoardType boardType, LocalDateTime createdDate, LocalDateTime updatedDate, User user) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.boardType = boardType;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.user = user;
    }
}
