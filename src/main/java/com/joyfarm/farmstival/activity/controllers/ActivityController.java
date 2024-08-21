package com.joyfarm.farmstival.activity.controllers;

import com.joyfarm.farmstival.activity.entities.Activity;
import com.joyfarm.farmstival.activity.services.ActivityInfoService;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.rests.JSONData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityInfoService infoService;

    @GetMapping
    public JSONData getList(@ModelAttribute ActivitySearch search) {
        ListData<Activity> data = infoService.getList(search);

        return new JSONData(data);
    }

    @GetMapping("/info/{seq}")
    public JSONData get(Long seq) {
        Activity item = infoService.get(seq);

        return new JSONData(item);
    }
}
