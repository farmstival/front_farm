package com.joyfarm.farmstival.farmfarm.services;

import com.joyfarm.farmstival.farmfarm.controllers.TourSearch;
import com.joyfarm.farmstival.farmfarm.entities.QTourPlace;
import com.joyfarm.farmstival.farmfarm.entities.TourPlace;
import com.joyfarm.farmstival.farmfarm.exceptions.TourPlaceNotFoundException;
import com.joyfarm.farmstival.farmfarm.repositories.TourPlaceRepository;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.Pagination;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional // 엔티티 매니저 사용 시 필수
public class TourInfoService {
    private final HttpServletRequest request;
    private final TourPlaceRepository repository;
    private final JPAQueryFactory queryFactory;

    public ListData<TourPlace> getList (TourSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit(); // 한 페이지 당 보여줄 레코드 갯수
        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit; // 레코드 시작 위치
        // 시작 위치에서 1을 빼야 DB 위치가 맞는다...?

        /* 검색 처리 S */
        String sopt = search.getSopt(); // 검색 옵션 ALL - 통합 검색
        String skey = search.getSkey(); // 검색 키워드

        QTourPlace tourPlace = QTourPlace.tourPlace;
        BooleanBuilder andBuilder = new BooleanBuilder();
        /* 검색 처리 E */

        List<TourPlace> items = queryFactory.selectFrom(tourPlace)
                .where(andBuilder) // 검색 조건
                .offset(offset)
                .limit(limit)
                .orderBy(tourPlace.createdAt.desc())
                .fetch();

        long total = repository.count(andBuilder); // 조회된 전체 갯수

        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 여행지 정보 조회
     * @param seq
     * @return
     */
    public TourPlace get(Long seq) {
        TourPlace item = repository.findById(seq).orElseThrow(TourPlaceNotFoundException::new);

        return item;
    }
}
