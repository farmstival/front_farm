package com.joyfarm.farmstival.tour.services;

import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.Pagination;
import com.joyfarm.farmstival.tour.controllers.TourPlaceSearch;
import com.joyfarm.farmstival.tour.entities.QTourPlace;
import com.joyfarm.farmstival.tour.entities.TourPlace;
import com.joyfarm.farmstival.tour.exceptions.TourPlaceNotFoundException;
import com.joyfarm.farmstival.tour.repositories.TourPlaceRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class TourPlaceInfoService {

    private final TourPlaceRepository repository;
    private final HttpServletRequest request;

    public ListData<TourPlace> getList(TourPlaceSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit;

        /* 검색 처리 S */
        QTourPlace tourPlace = QTourPlace.tourPlace;
        BooleanBuilder andBuilder = new BooleanBuilder();
        // AND 조건이 주축, 필요에 따라 OR 조건 추가

        String sopt = search.getSopt();
        String skey = search.getSkey();
        String sido = search.getSido();
        String sigungu = search.getSigungu();

        sopt = StringUtils.hasText(sopt) ? sopt : "ALL"; // 통합 검색이 기본

        // 키워드가 있을 때 조건별 검색
        if (StringUtils.hasText(skey) && StringUtils.hasText(skey.trim())) {
            /**
             * sopt
             * ALL - 통합 검색 - TITLE, TEL, ADDRESS, DESCRIPTION
             * TITLE, TEL, ADDRESS, DESCRIPTION
             */
            sopt = sopt.trim();
            skey = skey.trim();

            BooleanExpression condition = null;
            if (sopt.equals("ALL")){ // 통합 검색
                condition = tourPlace.title.concat(tourPlace.tel).concat(tourPlace.address).concat(tourPlace.description).contains(skey);

            } else if (sopt.equals("TITLE")) { // 여행지 명
                condition = tourPlace.title.contains(skey);

            } else if (sopt.equals("TEL")) { // 여행지 연락처
                skey = skey.replaceAll("\\D", ""); // TEL_2차가공_숫자만 남김
                condition = tourPlace.tel.contains(skey);

            } else if (sopt.equals("ADDRESS")) { // 여행지 주소
                condition = tourPlace.address.contains(skey);

            } else if (sopt.equals("DESCRIPTION")) { // 여행지 소개
                condition = tourPlace.description.contains(skey);

            }
            if (condition != null) andBuilder.and(condition);
        }

        // 시도 검색
        if (sido != null && StringUtils.hasText(sido.trim())) {
            andBuilder.and(tourPlace.sido.eq(sido.trim()));

            // 시군구 검색
            if (sigungu != null && StringUtils.hasText(sigungu.trim())) {
                andBuilder.and(tourPlace.sigungu.eq(sigungu.trim()));
            }
        } // endif - sido

         /* 검색 처리 E */

        // 페이징 및 정렬 처리
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));

        // 데이터 조회
        Page<TourPlace> data = repository.findAll(andBuilder, pageable);

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);

        List<TourPlace> items = data.getContent(); // limit 갯수에 맞춰 출력

        return new ListData<>(items, pagination); // 페이징 가능한 목록 생성!
    }

    /**
     * 상세 조회
     * @param seq
     * @return
     */
    public TourPlace get(Long seq){

        TourPlace item = repository.findById(seq).orElseThrow(TourPlaceNotFoundException::new);

        // 추가 데이터 처리 ( 수용 인원, 휴무일 ... )

        return item;
    }
}
