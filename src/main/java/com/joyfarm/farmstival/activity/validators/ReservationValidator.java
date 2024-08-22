package com.joyfarm.farmstival.activity.validators;

import com.joyfarm.farmstival.activity.controllers.RequestReservation;
import com.joyfarm.farmstival.activity.entities.Activity;
import com.joyfarm.farmstival.activity.entities.Reservation;
import com.joyfarm.farmstival.activity.services.ActivityInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReservationValidator implements Validator {

    private final ActivityInfoService infoService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Reservation.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestReservation form = (RequestReservation) target;

        Long seq = form.getActivitySeq();
        Activity item = infoService.get(seq);
        Map<LocalDate, boolean[]> available = item.getAvailableDates();
        LocalDate rDate = form.getRDate();
        String amPm = form.getAmpm();

        String errorCode = "NotAvailable.activity";
        if (!available.containsKey(rDate)) { // 예약 불가능 일정
            errors.rejectValue("rDate", errorCode);
        } else { // 예약 가능 일정, 오전 오후 체크 - 선택한 날짜가 당일인 경우
            int hours = LocalTime.now().getHour();
            boolean[] time = available.get(rDate);
            if (rDate.equals(LocalDate.now()) && hours > 8 && ((amPm.equals("AM") && !time[0] || (amPm.equals("PM") && !time[1])))) { // 당일 예약
                errors.rejectValue("ampm", errorCode);

            }
        }
    }
}
