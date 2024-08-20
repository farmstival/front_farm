package com.joyfarm.farmstival.Reservation.controllers;

import lombok.Data;

@Data
public class ReservationSerach {
    private int page = 1;
    private int limit = 20;

    private String sopt; //검색 조건
    private String skey; //컴색 키워드

    private Double latitude;
    private Double longitude;
    private Integer radius = 1000;
}
