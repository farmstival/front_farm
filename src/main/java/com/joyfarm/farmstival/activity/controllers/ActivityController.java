package com.joyfarm.farmstival.activity.controllers;

import com.joyfarm.farmstival.activity.entities.Activity;
import com.joyfarm.farmstival.activity.entities.Reservation;
import com.joyfarm.farmstival.activity.services.ActivityInfoService;
import com.joyfarm.farmstival.activity.services.ReservationApplyService;
import com.joyfarm.farmstival.activity.validators.ReservationValidator;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.Utils;
import com.joyfarm.farmstival.global.exceptions.BadRequestException;
import com.joyfarm.farmstival.global.rests.JSONData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityInfoService infoService;
    private final ReservationValidator validator;
    private final ReservationApplyService applyService;
    private final Utils utils;

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

    /**
     * 예약 접수
     * @return
     */
    @PostMapping("/apply")
    public ResponseEntity<JSONData> apply(@RequestBody @Valid RequestReservation form, Errors errors) {
        validator.validate(form, errors); //예약 검증

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        Reservation reservation = applyService.apply(form);

        HttpStatus status = HttpStatus.CREATED;
        JSONData jsonData = new JSONData(reservation);
        jsonData.setStatus(status);

        return ResponseEntity.status(status).body(jsonData);
    }

}