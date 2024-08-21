package com.joyfarm.farmstival.activity.services;

import com.joyfarm.farmstival.activity.constants.AM_PM;
import com.joyfarm.farmstival.activity.constants.Status;
import com.joyfarm.farmstival.activity.controllers.RequestReservation;
import com.joyfarm.farmstival.activity.entities.Activity;
import com.joyfarm.farmstival.activity.entities.Reservation;
import com.joyfarm.farmstival.activity.exceptions.ActivityNotFoundException;
import com.joyfarm.farmstival.activity.repositories.ActivityRepository;
import com.joyfarm.farmstival.activity.repositories.ReservationRepository;
import com.joyfarm.farmstival.member.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ReservationApplyService {
    private final ReservationRepository reservationRepository;
    private final ActivityRepository activityRepository;
    private final ReservationStatuService statuService;
    private final MemberUtil memberUtil;

    public Reservation apply(RequestReservation form) {
        Long activitySeq = form.getActivitySeq();
        Activity activity = activityRepository.findById(activitySeq).orElseThrow(ActivityNotFoundException::new);

        String mobile = form.getMobile();
        if(StringUtils.hasText(mobile)){
            mobile = mobile.replaceAll("\\D", "");
        }
        Reservation reservation = Reservation.builder()
                .activity(activity)
                .email(form.getEmail())
                .name(form.getName())
                .mobile(mobile)
                .member(memberUtil.getMember())
                .activityName(activity.getActivityName())
                .townName(activity.getTownName())
                .doroAddress(activity.getDoroAddress())
                .ownerName(activity.getOwnerName())
                .ownerTel(activity.getOwnerTel())
                .rDate(form.getRDate())
                .ampm(AM_PM.valueOf(form.getAmpm()))
                .persons(Math.max(form.getPersons(),1))
                .build();

        reservationRepository.saveAndFlush(reservation);

        // 예약 접수 상태로 변경
        statuService.change(reservation.getSeq(), Status.APPLY);

        return null;
    }
}
