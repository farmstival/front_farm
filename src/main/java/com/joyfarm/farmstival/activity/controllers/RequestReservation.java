package com.joyfarm.farmstival.activity.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RequestReservation { //커맨드 객체
    
    @NotNull
    private Long activitySeq; //액티비티 등록 번호
    
    @NotBlank
    private String name; //예약자명

    @NotBlank
    private String email; //예약자 이메일

    @NotBlank
    private String mobile; //예약자 전화번혼

    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rDate; //예약일
    
    private String ampm; //오전, 오후
    
    private int persons = 1; //예약 인원수
}
