package com.joyfarm.farmstival.board.controllers;

import com.joyfarm.farmstival.board.entities.BoardData;
import com.joyfarm.farmstival.board.services.BoardInfoService;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.constants.DeleteStatus;
import com.joyfarm.farmstival.global.rests.JSONData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/admin")
@RequiredArgsConstructor
public class BoardAdminController {

    private final BoardInfoService boardInfoService;

    @GetMapping("/search")
    public JSONData getList(BoardDataSearch search){
        ListData<BoardData> data = boardInfoService.getList(search, DeleteStatus.ALL);

        return new JSONData(data);
    }
    @PatchMapping
    public ResponseEntity<Void> listUpdate(@RequestBody RequestAdminList form){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info/{seq}")
    public JSONData getInfo(Long seq){
        BoardData item = boardInfoService.get(seq,DeleteStatus.ALL);

        return new JSONData(item);
    }
}
