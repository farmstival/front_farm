package com.joyfarm.farmstival.activity.validators;

import com.joyfarm.farmstival.activity.entities.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ReservationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Reservation.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
