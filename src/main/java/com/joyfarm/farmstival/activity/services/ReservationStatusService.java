package com.joyfarm.farmstival.activity.services;


import com.joyfarm.farmstival.activity.constants.Status;
import com.joyfarm.farmstival.activity.entities.Reservation;
import com.joyfarm.farmstival.activity.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationStatusService {

    private final ReservationInfoService infoService;
    private final ReservationRepository repository;

    public void change(Long seq, Status status) {
        Reservation reservation = infoService.get(seq);
        Status prevStatus = reservation.getStatus();

        if(prevStatus != status) { //기존 상태와 동일하면 처리 x
            return;
        }

        reservation.setStatus(status);
        repository.saveAndFlush(reservation);

        // 예약 접수, 예약 취소 메일 전송
        String email = reservation.getEmail();
    }
}