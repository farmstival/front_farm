package com.joyfarm.farmstival.board.exceptions;

import com.joyfarm.farmstival.board.entities.Board;
import com.joyfarm.farmstival.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends CommonException {
    public BoardNotFoundException () {
        super("NotFound.Board", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
