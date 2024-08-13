package com.joyfarm.farmstival.board.services;

import com.joyfarm.farmstival.board.controllers.RequestBoard;
import com.joyfarm.farmstival.board.entities.BoardData;
import com.joyfarm.farmstival.board.repositories.BoardDataRepository;
import com.joyfarm.farmstival.board.repositories.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BoardSaveService {

    private final BoardRepository boardRepository;
    private final BoardDataRepository boardDataRepository;

    public BoardData save(RequestBoard form) {

        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode.trim() : "write";

        Long seq = form.getSeq();
        if (seq != null && mode.equals("update")) { // 글 수정

        } else { // 글 작성

        }

        return null;
    }
}
