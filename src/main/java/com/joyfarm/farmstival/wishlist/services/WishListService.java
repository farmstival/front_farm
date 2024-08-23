package com.joyfarm.farmstival.wishlist.services;

import com.joyfarm.farmstival.member.MemberUtil;
import com.joyfarm.farmstival.wishlist.entities.WishList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final MemberUtil memberUtil;
    
    public void add(Long seq, WishList type) { // 추가
        
    }
    
    public void remove(Long seq, WishList type) { // 제거
        
    }

    public List<Long> getList(WishList type) { // 리스트
        return null;
    }
}
