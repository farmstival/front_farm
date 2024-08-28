package com.joyfarm.wishlist.repositories;

import com.joyfarm.wishlist.entities.WishList;
import com.joyfarm.wishlist.entities.WishListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WishListRepository extends JpaRepository<WishList, WishListId>, QuerydslPredicateExecutor<WishList> {
}
