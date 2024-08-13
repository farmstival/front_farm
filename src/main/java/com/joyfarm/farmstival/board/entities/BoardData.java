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
@Table(indexes = @Index(name = "idx_board_data", columnList = "notice DESC, createdAt DESC"))
public class BoardData extends BaseEntity { //게시글 데이터
    @Id @GeneratedValue
    private Long seq; //게시글 자동 증감 번호

    @Column(length = 65, nullable = false, updatable = false)
    private String gid; //이미지와 파일은 여러개가 들어올 수 있기 때문에 그룹으로 묶어줘야함

    @JoinColumn(name = "bid") //게시판 별 게시글 구분 명
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    //게시판 아이디(bid) 관계매핑을 통해 관련시켜 두면 특정 게시판엔 특정 게시글이 올것임!, 게시글쪽이 many

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    //회원쪽 아이디(seq) 한명의 회원이 여러개의 게시글 작성 가능, 게시글쪽이 many

    @Column(length = 65)
    private String guestPw; //비회원 비밀번호(글 수정, 삭제)

    @Column(length = 60)
    private String category; //게시글 분류

    private boolean notice; //공지글 여부

    @Column(length = 40, nullable = false)
    private String poster; // 작성자
    @Column(nullable = false)
    private String subject; // 제목
    @Lob
    @Column(nullable = false)
    private String content; // 내용

    private int viewCount; //조회수
    private boolean editorView; //에디터 사용여부에 따라 다르게 출력

    @Column(length = 20, updatable = false)
    private String ip; //사용자 ip 주소
    @Column(updatable = false)
    private String ua; //사용자 User-Agent

    private Long num1; //정수 추가 필드1
    private Long num2; //정수 추가 필드2
    private Long num3; //정수 추가 필드3

    @Column(length = 100)
    private String text1; //한 줄 텍스트 추가 필드1
    @Column(length = 100)
    private String text2; //한 줄 텍스트 추가 필드2
    @Column(length = 100)
    private String text3; //한 줄 텍스트 추가 필드3

    @Lob
    private String longText1; //여러줄 텍스트 추가 필드1
    @Lob
    private String longText2; //여러줄 텍스트 추가 필드2
}
