package com.joyfarm.farmstival.board.entities;

import com.joyfarm.farmstival.global.entities.BaseEntity;
import com.joyfarm.farmstival.member.entities.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = @Index(name="idx_board_data", columnList = "notice DESC, createdAt DESC"))
public class BoardData extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;

    @Column(length=65, nullable=false, updatable = false)
    private String gid; // 이미지나 파일을 찾기위한 용도, 처음에 글 추가할 때만 들어가야함

    @JoinColumn(name="bid")
    @Column(updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board; // 추가할때만 들어가고 수정할 땐 바뀌면 안됨.

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(updatable = false)
    private Member member; // 추가할때만 들어가고 수정할 땐 바뀌면 안됨.

    @Column(length = 65)
    private String guestPw; // 비회원 비밀번호(수정, 삭제)

    @Column(length=60)
    private String category; // 게시글 분류

    private boolean notice; // 공지글 여부

    @Column(length = 40, nullable = false)
    private String poster; // 작성자

    @Column(nullable = false)
    private String subject; // 제목

    @Lob
    @Column(nullable = false)
    private String content; // 내용

    private int viewCount; // 조회수
    private boolean editorView; // 에디터를 사용해서 글 작성했는지 여부

    @Column(length=20, updatable = false)
    private String ip; // IP 주소 , 수정시에 바뀌면 안된다.
    @Column(updatable = false)
    private String ua; // User-Agent

    private Long num1; // 정수 추가 필드1 - 필요한 이유는 상품 번호 같은게 있으면 다양하게 사용할 수 있나봄
    private Long num2; // 정수 추가 필드2
    private Long num3; // 정수 추가 필드3

    @Column(length=100)
    private String text1; // 한줄 텍스트 추가 필드1
    @Column(length=100)
    private String text2; // 한줄 텍스트 추가 필드2
    @Column(length=100)
    private String text3; // 한줄 텍스트 추가 필드3

    @Lob
    private String longText1; // 여러줄 텍스트 추가 필드1
    @Lob
    private String longText2; // 여러줄 텍스트 추가 필드2

}

