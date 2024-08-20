package com.joyfarm.farmstival.activity.services;

import com.joyfarm.farmstival.activity.controllers.ActivitySearch;
import com.joyfarm.farmstival.activity.entities.Activity;
import com.joyfarm.farmstival.activity.entities.QActivity;
import com.joyfarm.farmstival.activity.exceptions.ActivityNotFoundException;
import com.joyfarm.farmstival.activity.repositories.ActivityRepository;
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
@Transactional //엔티티 매니저를 사용하는 경우 필수!
@RequiredArgsConstructor
public class ActivityInfoService {

    private final HttpServletRequest request;
    private final ActivityRepository repository;
    private final JPAQueryFactory queryFactory;

    public ListData<Activity> getList(ActivitySearch search) {
        int page =Math.max(search.getPage(), 1); //둘 중에 큰 것을 반환
        int limit = search.getLimit(); //한 페이지 당 보여줄 레코드 개수

        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit; //레코드 시작 위치, (현재 페이지-1)*limit

        //검색 처리 S
        String sopt = search.getSopt(); //검색 옵션 All - 통합 검색
        String skey = search.getSkey(); //검색 키워드

        QActivity activity = QActivity.activity;
        BooleanBuilder andBuilder = new BooleanBuilder();
        //검색 처리 E

        List<Activity> items = queryFactory.selectFrom(activity)
                .leftJoin(activity.acTags)
                .fetchJoin()
                .where(andBuilder)
                .offset(offset)
                .limit(limit)
                .orderBy(activity.createdAt.desc())
                .fetch();


        long total = repository.count(andBuilder); //조회된 전체 개수
        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 체험활동 정보 조회
     * @param seq
     * @return
     */
    public Activity get(long seq) {
        Activity item = repository.findById(seq).orElseThrow(ActivityNotFoundException::new);

        //추가 데이터 처리
        return item;
    }

}
