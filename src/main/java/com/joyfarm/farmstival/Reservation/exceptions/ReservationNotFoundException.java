package com.joyfarm.farmstival.Reservation.exceptions;

import com.joyfarm.farmstival.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class ReservationNotFoundException extends CommonException {

    public ReservationNotFoundException() {
        super("NotFound.activity", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
