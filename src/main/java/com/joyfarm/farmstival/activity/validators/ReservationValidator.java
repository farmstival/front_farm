package com.joyfarm.farmstival.activity.validators;

import com.joyfarm.farmstival.activity.constants.AM_PM;
import com.joyfarm.farmstival.activity.controllers.RequestReservation;
import com.joyfarm.farmstival.activity.entities.Activity;
import com.joyfarm.farmstival.activity.entities.Reservation;
import com.joyfarm.farmstival.activity.services.ActivityInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.List;
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
        Activity activity = infoService.get(seq); //체험활동 seq 번호

        LocalDate rDate = form.getRDate(); //예약일
        String ampm = form.getAmpm(); //오전, 오후 구분

        //예약일 검증 S
        List<LocalDate> availableDates = activity.getAvailableDates();
        if (availableDates == null || availableDates.isEmpty() || !availableDates.contains(rDate)) {
            errors.rejectValue("rDate", "NotAvailable.activity");
        } else { // 예약 가능일인 경우 예약 시간도 검증

            Map<String, List<AM_PM>> availableTimes = activity.getAvailableTimes();
            boolean possible = true;
            List<AM_PM> _availableTimes = null;

            
            //noneMatch : 하나도 일치하지 않을 때
            if (!possible || (possible && _availableTimes.stream().noneMatch(t -> t.equals(ampm)))) {
                errors.rejectValue("ampm", "NotAvailable.activity");
            }
        }
        //예약일 검증 E

    }
}
