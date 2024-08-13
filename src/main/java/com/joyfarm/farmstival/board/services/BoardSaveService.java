package com.joyfarm.farmstival.board.services;

import com.joyfarm.farmstival.board.controllers.RequestBoard;
import com.joyfarm.farmstival.board.entities.Board;
import com.joyfarm.farmstival.board.entities.BoardData;
import com.joyfarm.farmstival.board.exceptions.BoardDataNotFoundException;
import com.joyfarm.farmstival.board.exceptions.BoardNotFoundException;
import com.joyfarm.farmstival.board.repositories.BoardDataRepository;
import com.joyfarm.farmstival.board.repositories.BoardRepository;
import com.joyfarm.farmstival.member.MemberUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/*게시글 저장*/
@Service
@RequiredArgsConstructor
@Transactional //엔티티의 영속성을 유지하기 위해
public class BoardSaveService {
    private final HttpServletRequest request;
    private final PasswordEncoder encoder;
    private final BoardRepository boardRepository;
    private final BoardDataRepository boardDataRepository;
    private final MemberUtil memberUtil;
    //파일 의존성 추가

    public BoardData save(RequestBoard form){//게시글 작성 이후 이동(게시글 목록, 게시글 보기)하려면 게시글 정보가 필요함

        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode.trim() : "write"; //mode가 null이거나 비어있을때 기본값은 쓰기 형태로

        String gid = form.getGid();

        BoardData data = null;
        Long seq  = form.getSeq();//글 수정시 seq번호로 조회

        if(seq != null && mode.equals("update")){ //글 수정
            data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new); //게시글이 없을때 에러 던짐
        }else { //글 작성
            String bid = form.getBid();
            //게시판 조회
            Board board = boardRepository.findById(bid).orElseThrow(BoardNotFoundException::new); //게시판이 없을때 에러 던짐

            data = BoardData.builder()
                    .gid(gid)
                    .board(board)
                    .member(memberUtil.getMember())
                    .ip(request.getRemoteAddr())
                    .ua(request.getHeader("User-Agent"))
                    .build();
        }

        /* 글 작성, 글 수정 공통 S*/
        //db에서 값 가져와서 설정
        data.setPoster(form.getPoster());
        data.setSubject(form.getSubject());
        data.setContent(form.getContent());
        data.setCategory(form.getCategory());
        data.setEditorView(data.getBoard().isUseEditor()); //에디터를 사용하면 에디터 띄우도록

        data.setNum1(form.getNum1());
        data.setNum2(form.getNum2());
        data.setNum3(form.getNum3());

        data.setText1(form.getText1());
        data.setText2(form.getText2());
        data.setText3(form.getText3());

        data.setLongText1(form.getLongText1());
        data.setLongText2(form.getLongText2());

        // 비회원 비밀번호 처리
        String guestPw = form.getGuestPw();
        if(StringUtils.hasText(guestPw)){
            data.setGuestPw(encoder.encode(guestPw));
        }

        // 관리자일때만 공지글 여부 수정 가능
        if(memberUtil.isAdmin()){
            data.setNotice(form.isNotice());
        }

        /* 글 작성, 글 수정 공통 E*/

        // 파일 업로드 완료 처리

        return data;
    }
}
