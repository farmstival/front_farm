package com.joyfarm.farmstival.tour.controllers;

import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.rests.JSONData;
import com.joyfarm.farmstival.tour.entities.TourPlace;
import com.joyfarm.farmstival.tour.services.TourPlaceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourPlaceController {
    private final TourPlaceInfoService infoService;

    @GetMapping("/list")
    public JSONData list(@ModelAttribute TourPlaceSearch search) {
        ListData<TourPlace> data = infoService.getList(search);

        return new JSONData(data);
    }

    @GetMapping("/info/{seq}")
    public JSONData info(@PathVariable("seq") Long seq) {
        TourPlace item = infoService.get(seq);

        return new JSONData(item);
    }
}
