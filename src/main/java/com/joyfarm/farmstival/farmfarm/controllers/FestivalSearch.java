package com.joyfarm.farmstival.farmfarm.controllers;

import lombok.Data;

@Data
public class FestivalSearch {
    private int page = 1; // 목록 조회시 페이징도 같이 함
    private int limit = 20; // 페이지 당 20개씩

    private String sopt; // 검색 조건
    private String skey; // 검색 키워드
}