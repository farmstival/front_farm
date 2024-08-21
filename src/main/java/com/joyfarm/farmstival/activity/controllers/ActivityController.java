package com.joyfarm.farmstival.activity.controllers;

import com.joyfarm.farmstival.activity.entities.Activity;
import com.joyfarm.farmstival.activity.services.ActivityInfoService;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.rests.JSONData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityInfoService infoService;


    /**
     * 목록 조회
     * @param search
     * @return
     */
    @GetMapping("/list")
    public JSONData list(@ModelAttribute ActivitySearch search) {
        ListData<Activity> data = infoService.getList(search);

        return new JSONData(data);
    }

    /**
     * 상세 조회
     * @param seq
     * @return
     */
    @GetMapping("/info/{seq}")
    public JSONData info(@PathVariable("seq") Long seq) {

        Activity data = infoService.get(seq);

        return new JSONData(data);
    }
}