package com.joyfarm.wishlist.services;

import com.joyfarm.farmstival.member.MemberUtil;
import com.joyfarm.wishlist.entities.WishList;
import com.joyfarm.wishlist.repositories.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final MemberUtil memberUtil;
    private final WishListRepository wishListRepository;

    // 찜하기 추가
    public void add(Long seq, WishList type){

    }

    // 찜하기 제거
    public void remove(Long seq, WishList type){

    }

    // 찜하기 목록(등록번호) 가져오기
    public List<Long> getList(WishList type){
        return null;
    }
}
