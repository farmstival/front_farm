package com.joyfarm.farmstival.farmfarm.controllers;

import com.joyfarm.farmstival.farmfarm.entities.TourPlace;
import com.joyfarm.farmstival.farmfarm.services.TourInfoService;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.rests.JSONData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {

    private final TourInfoService infoService;

    /**
     * 목록 조회
     * @param tourSearch
     * @return
     */

    @GetMapping("/list")
    public JSONData list(@ModelAttribute TourSearch tourSearch) {

        ListData<TourPlace> data = infoService.getList(tourSearch);

        return new JSONData(data);
    }

    /**
     * 상세 조회
     * @param seq
     * @return
     */
    
    @GetMapping("/info/{seq}")
    public JSONData info(@PathVariable("seq") Long seq) {
        TourPlace data = infoService.get(seq);
        return new JSONData(data);
    }
}
