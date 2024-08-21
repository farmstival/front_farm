package com.joyfarm.farmstival.activity.services;

import com.joyfarm.farmstival.activity.controllers.ActivitySearch;
import com.joyfarm.farmstival.activity.entities.Activity;
import com.joyfarm.farmstival.activity.entities.QActivity;
import com.joyfarm.farmstival.activity.exceptions.ActivityNotFoundException;
import com.joyfarm.farmstival.activity.repositories.ActivityRepository;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.Pagination;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@Transactional //엔티티 매니저를 사용하는 경우 필수!
@RequiredArgsConstructor
public class ActivityInfoService {

    private final HttpServletRequest request;
    private final ActivityRepository activityRepository;
    private final JPAQueryFactory queryFactory;

    /**
     * 액티비티 조회
     * @param seq
     * @return
     */
    public Activity get(Long seq) {
        Activity item = activityRepository.findById(seq).orElseThrow(ActivityNotFoundException::new);

        // 추가 정보 처리
        addInfo(item);
        return item;
    }

    /**
     * 액티비티 목록 조회
     * @param search
     * @return
     */
    public ListData<Activity> getList(ActivitySearch search) {
        int page =Math.max(search.getPage(), 1); //둘 중에 큰 것을 반환
        int limit = search.getLimit(); //한 페이지 당 보여줄 레코드 개수

        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit; //레코드 시작 위치, (현재 페이지-1)*limit

        //검색 처리 S
        String sopt = search.getSopt(); //검색 옵션 All - 통합 검색
        String skey = search.getSkey(); //검색 키워드

        /* 액티비티 검색 처리 S */
        QActivity activity = QActivity.activity;
        BooleanBuilder andBuilder = new BooleanBuilder();
        sopt = sopt != null && StringUtils.hasText(sopt.trim()) ? sopt.trim() : "ALL";
        if (skey != null && StringUtils.hasText(skey.trim())) {
            /**
             * sopt
             *      ALL - 통합 검색
             *            townName(체험 마을명), activityName (체험프로그램명)
             *            doroAddress(주소), ownerName(대표자명), ownerTel(대표자 전화번호)
             *      NAME - 예약자명, 대표자명
             *      MOBILE - 예약자 전화번호 + 대표자 전화번호
             *      ADDRESS - 도로명 주소
             *      ACTIVITY - 체험 마을명 + 체험프로그램명
             */

            skey = skey.trim();
            StringExpression expression = null;
            if (sopt.equals("ALL")) { // 통합 검색
                expression = activity.townName
                        .concat(activity.activityName)
                        .concat(activity.doroAddress)
                        .concat(activity.ownerName)
                        .concat(activity.ownerTel);
            } else if (sopt.equals("NAME")) {
                expression = activity.ownerName;
            } else if (sopt.equals("MOBILE")) {
                expression = activity.ownerTel;

            } else if (sopt.equals("ADDRESS")) {
                expression = activity.doroAddress;
            } else if (sopt.equals("ACTIVITY")) {
                expression = activity.activityName.concat(activity.townName);
            }

            if (expression != null) {
                andBuilder.and(expression.contains(skey));
            }
        }

        /* 액티비티 검색 처리 E */
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Activity> data = activityRepository.findAll(andBuilder, pageable);
        List<Activity> items = data.getContent();
        items.forEach(this::addInfo);

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);

        return new ListData<>(items, pagination);
    }

    private void addInfo(Activity item) {

    }
}