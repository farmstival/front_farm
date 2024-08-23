package com.joyfarm.farmstival.board.services;

import com.joyfarm.farmstival.board.controllers.BoardDataSearch;
import com.joyfarm.farmstival.board.controllers.RequestBoard;
import com.joyfarm.farmstival.board.entities.Board;
import com.joyfarm.farmstival.board.entities.BoardData;
import com.joyfarm.farmstival.board.entities.QBoardData;
import com.joyfarm.farmstival.board.exceptions.BoardDataNotFoundException;
import com.joyfarm.farmstival.board.exceptions.BoardNotFoundException;
import com.joyfarm.farmstival.board.repositories.BoardDataRepository;
import com.joyfarm.farmstival.global.CommonSearch;
import com.joyfarm.farmstival.global.ListData;
import com.joyfarm.farmstival.global.Pagination;
import com.joyfarm.farmstival.global.Utils;
import com.joyfarm.farmstival.global.constants.DeleteStatus;
import com.joyfarm.farmstival.wishlist.constants.WishType;
import com.joyfarm.farmstival.wishlist.services.WishListService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardInfoService {

    private final JPAQueryFactory queryFactory;
    private final BoardDataRepository repository;
    private final HttpServletRequest request;
    private final BoardConfigInfoService configInfoService;
    private final Utils utils;
    private final WishListService wishListService;

    /**
     * 게시글 목록 조회
     * @return
     */
    public ListData<BoardData> getList(BoardDataSearch search, DeleteStatus status){

        String bid = search.getBid(); //단일 조회
        List<String> bids = search.getBids(); //게시판 여러개 조회

        //게시판 설정 조회
        Board board = bid != null && StringUtils.hasText(bid.trim()) ? configInfoService.get(bid.trim()).orElseThrow(BoardNotFoundException::new) : new Board();

        int page = Math.max(search.getPage(),1);//기본값은 1, 1 미만의 값 들어올 수 없다
        int limit = search.getLimit();
        limit = limit > 0 ? limit : board.getRowsPerPage();

        int offset = (page - 1) * limit;

        // 삭제가 되지 않은 게시글 목록이 기본 값
        status = Objects.requireNonNullElse(status, DeleteStatus.UNDELETED);

        String sopt = search.getSopt(); //검색 옵션
        String skey = search.getSkey(); //검색 키워드


        /* 검색 처리S */
        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();

        // 삭제, 미삭제 게시글 조회 처리
        if (status != DeleteStatus.ALL) {
            if (status == DeleteStatus.UNDELETED) {
                andBuilder.and(boardData.deletedAt.isNull()); // 미삭된 게시글
            } else {
                andBuilder.and(boardData.deletedAt.isNotNull()); // 삭제된 게시글
            }
        }

        //bid가 없을때는 bids로 조회
        if(bid != null && StringUtils.hasText(bid.trim())){ //게시판별 조회
            bids = List.of(bid);
        }
        if(bids != null && !bids.isEmpty()) { //게시판 여러개 조회
            andBuilder.and(boardData.board.bid.in(bids));
        }

        /**
         * 조건 검색 처리
         * sopt - ALL : 통합검색(제목 + 내용 + 글 작성자(작성자, 회원명))
         *      - SUBJECT : 제목 검색
         *      - CONTENT : 내용 검색
         *      - SUBJECT_CONTENT: 제목 + 내용 검색
         *      - NAME : 이름(작성자, 회원명)
         */

        sopt = sopt != null && StringUtils.hasText(sopt.trim()) ? sopt.trim() : "ALL"; //없을때 기본값 ALL
        if(skey != null && StringUtils.hasText(skey.trim())){
            skey = skey.trim();
            BooleanExpression condition = null;

            BooleanBuilder orBuilder = new BooleanBuilder();

            /* 이름 검색 S */
            BooleanBuilder nameCondition = new BooleanBuilder();
            nameCondition.or(boardData.poster.contains(skey));

            if(boardData.member != null){
                nameCondition.or(boardData.member.userName.contains(skey));
            }
            /* 이름 검색 E */


            if(sopt.equals("ALL")){ //통합 검색
               orBuilder.or(boardData.subject.concat(boardData.content)
                        .contains(skey))
                       .or(nameCondition);

            }else if(sopt.equals("SUBJECT")){ //제목 검색
                condition = boardData.subject.contains(skey);
            } else if (sopt.equals("CONTENT")) { //내용 검색
                condition = boardData.content.contains(skey);
            } else if (sopt.contains("SUBJET_CONTENT")) { //제목 + 내용 검색
                condition = boardData.subject.concat(boardData.content)
                        .contains(skey);
            } else if (sopt.equals("NAME")) {
                andBuilder.and(nameCondition);
            }

            if (condition != null) andBuilder.and(condition);
            andBuilder.and(orBuilder);
        }
        /* 검색 처리E */

        /* 정렬 처리 S */
        String sort = search.getSort();

        // boardData.viewCount.desc();//이런식으로 하면 특정 불가능
        PathBuilder<BoardData> pathBuilder = new PathBuilder<>(BoardData.class, "boardData"); //문구를 가지고 해당 항목을 찾아줌

        OrderSpecifier orderSpecifier = null;

        Order order = Order.DESC; //기본 방향 내림차순

        if(sort != null && StringUtils.hasText(sort.trim())){
            /* 정렬 항목(필드명)_방향 */
            /* ex) viewCount_DESC -> 조회수가 많은 순으로 정렬 */
            String[] _sort = sort.split("_");
            if(_sort[1].toUpperCase().equals("ASC")){ //오름차순
                order = Order.ASC;
            }

            orderSpecifier = new OrderSpecifier(order, pathBuilder.get(_sort[0]));
            //정렬 항목은 pathBuiler에서 조회해서 찾는다. ex) viewCount

        }

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        orderSpecifiers.add(boardData.notice.desc());

        if(orderSpecifier != null){
            orderSpecifiers.add(orderSpecifier);
        }

        orderSpecifiers.add(boardData.createdAt.desc()); //등록일 순
        /* 정렬 처리 E */


        /* 목록 조회 처리 S */
        List<BoardData> items = queryFactory
                .selectFrom(boardData)
                .leftJoin(boardData.board)
                .fetchJoin()
                .leftJoin(boardData.member)
                .fetchJoin()
                .where(andBuilder)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .offset(offset)
                .limit(limit)
                .fetch();

        //추가 정보 처리
        items.forEach(this::addInfo);

        /* 목록 조회 처리 E */

        // 전체 게시글 개수
        long total = repository.count(andBuilder);

        //페이징 처리
        int ranges = board.getPageCountPc();

        Pagination pagination = new Pagination(page,(int)total,ranges,limit,request); //현재 페이지, 전체 레코드 개수, 페이지 구간 개수,1페이지 당 레코드 개수

        return new ListData<>(items,pagination);
    }

    /**
     * 게시판 별 목록
     * bid 직접 수동으로 넣어줌
     * bid가 없을때는 bids로 조회
     * @param bid
     * @param search
     * @return
     */
    public ListData<BoardData> getList(String bid, BoardDataSearch search, DeleteStatus status){
        search.setBid(bid);//게시판 아이디

        return getList(search, status);
    }

    public ListData<BoardData> getList(String bid, BoardDataSearch search){
        return getList(bid, search, DeleteStatus.UNDELETED);
    }

    /**
     * 게시글 개별 조회
     * @param seq
     * @return
     */
    public BoardData get(Long seq, DeleteStatus status){

        BooleanBuilder andBuilder = new BooleanBuilder();
        QBoardData boardData = QBoardData.boardData;
        andBuilder.and(boardData.seq.eq(seq));

        // 삭제, 미삭제 게시글 조회 처리
        if (status != DeleteStatus.ALL) {
            if (status == DeleteStatus.UNDELETED) {
                andBuilder.and(boardData.deletedAt.isNull()); // 미삭된 게시글
            } else {
                andBuilder.and(boardData.deletedAt.isNotNull()); // 삭제된 게시글
            }
        }

        BoardData item = queryFactory.selectFrom(boardData)
                .leftJoin(boardData.board)
                .fetchJoin()
                .leftJoin(boardData.member)
                .fetchJoin()
                .where(andBuilder)
                .fetchFirst();

        if (item == null) {
            throw new BoardDataNotFoundException();
        }

        //추가 데이터 처리
        addInfo(item);

        return item;
    }

    public BoardData get(Long seq){
        return get(seq, DeleteStatus.UNDELETED);
    }

    /**
     * BoardData 엔티티 -> RequestBoard 커맨드 객체로 변환할 수 있는 편의 메서드
     * @param seq
     * @return
     */
    public RequestBoard getForm(Long seq, DeleteStatus status){
        BoardData item = get(seq, status);

        return getForm(item, status);
    }

    public RequestBoard getForm(BoardData item, DeleteStatus status){
        return new ModelMapper().map(item, RequestBoard.class); // 같은 getter,setter 메서드가 있으면 데이터 치환
    }

    public RequestBoard getForm(Long seq) {
        return getForm(seq, DeleteStatus.UNDELETED);
    }

    public RequestBoard getForm(BoardData item) {
        return getForm(item, DeleteStatus.UNDELETED);
    }

    /**
     * 내가 찜한 게시글 목록
     *
     * @param search
     * @return
     */
    public ListData<BoardData> getWishList(CommonSearch search) {

        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 10 : limit;
        int offset = (page - 1) * limit;


        List<Long> seqs = wishListService.getList(WishType.BOARD);
        if (seqs == null || seqs.isEmpty()) {
            return new ListData<>();
        }

        QBoardData boardData = QBoardData.boardData;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(boardData.seq.in(seqs));

        List<BoardData> items = queryFactory.selectFrom(boardData)
                .where(andBuilder)
                .leftJoin(boardData.board)
                .fetchJoin()
                .offset(offset)
                .limit(limit)
                .orderBy(boardData.createdAt.desc())
                .fetch();

        items.forEach(this::addInfo);

        long total = repository.count(andBuilder);
        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        return new ListData<>(items, pagination);
    }



    /**
     * 추가 데이터 처리
     *  - 업로드한 파일 목록
     *      에디터 이미지 목록, 첨부 파일 이미지 목록
     *  - 권한 : 글쓰기, 글 수정, 글 삭제, 글 조회 가능 여부
     *  - 댓글
     * @param item
     */
    public void addInfo(BoardData item){

    }

}
