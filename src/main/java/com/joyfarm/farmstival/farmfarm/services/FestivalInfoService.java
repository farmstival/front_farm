package com.joyfarm.farmstival.farmfarm.services;

import com.joyfarm.farmstival.farmfarm.controllers.FestivalSearch;
import com.joyfarm.farmstival.farmfarm.entities.Festival;
import com.joyfarm.farmstival.farmfarm.entities.QFestival;
import com.joyfarm.farmstival.farmfarm.exceptions.FestivalNotFoundException;
import com.joyfarm.farmstival.farmfarm.repositories.FestivalRepository;
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
@Transactional // Querydsl 사용 (EntityManager 사용할 때는 필수)
public class FestivalInfoService {
    private final HttpServletRequest request;
    private final FestivalRepository repository;
    private final JPAQueryFactory queryFactory; //infoService 쓰게 되면 순환 참조 발생

    /* 축제 목록 조회 */
    public ListData<Festival> getList(FestivalSearch search){
        int page = Math.max(search.getPage(), 1); // max - 둘 중에 큰 수 반환 함수(0이 오면 1반환)
        int limit = search.getLimit(); // 한 페이지 당 보여줄 레코드 갯수
        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit; // 레코드 시작 위치 (1-20 / 21-40 / ...)

        /* 검색 처리 S */
        String sopt = search.getSopt(); // 검색 옵션 (ALL - 통합 검색)
        String skey = search.getSkey(); // 검색 키워드

        QFestival festival = QFestival.festival;
        BooleanBuilder andBuilder = new BooleanBuilder();
        /* 검색 처리 E */

        List<Festival> items = queryFactory.selectFrom(festival)
                .where(andBuilder)
                .offset(offset)
                .limit(limit)
                .orderBy(festival.createdAt.desc())
                .fetch();

        long total = repository.count(andBuilder);

        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        return new ListData<>(items, pagination);
    }

    /* 축제 정보 조회 */
    public Festival get(Long seq){
        Festival item = repository.findById(seq).orElseThrow(FestivalNotFoundException::new);

        // 추가 데이터 처리

        return item;
    }
}
