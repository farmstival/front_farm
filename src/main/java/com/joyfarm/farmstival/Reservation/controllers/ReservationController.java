package com.joyfarm.farmstival.Reservation.controllers;

import com.joyfarm.farmstival.Reservation.entities.Reservation;
import com.joyfarm.farmstival.Reservation.services.ReservationInfoService;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.exceptions.ExceptionProcessor;
import com.joyfarm.farmstival.global.rests.JSONData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController implements ExceptionProcessor {

    private final ReservationInfoService infoService;

    /**
     * 목록 조회
     * @param search
     * @return
     */
    @GetMapping("/list")
    public JSONData list(@ModelAttribute ReservationSerach search) {
        ListData<Reservation> data = infoService.getList(search);

        return new JSONData(data);
    }

    /**
     * 상세 조회
     * @param seq
     * @return
     */
    @GetMapping("/info/{seq}")
    public JSONData info(@RequestParam("seq") Long seq) {

        Reservation data = infoService.get(seq);

        return new JSONData(data);
    }
}
