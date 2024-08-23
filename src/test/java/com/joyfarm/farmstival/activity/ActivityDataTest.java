package com.joyfarm.farmstival.activity;

import com.joyfarm.farmstival.activity.entities.Activity;
import com.joyfarm.farmstival.activity.entities.Reservation;
import com.joyfarm.farmstival.activity.services.ActivityInfoService;
import com.joyfarm.farmstival.activity.services.ReservationInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ActivityDataTest {

    @Autowired
    private ActivityInfoService infoService;
    private ReservationInfoService reservationInfoService;

    @Test
    @DisplayName("체험활동 데이터 seq로 상세 조회 테스트")
    void test1() {
        Long seq = 384L;
        Activity item = infoService.get(seq);
        System.out.println(item);
    }

}
