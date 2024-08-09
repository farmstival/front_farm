package com.joyfarm.farmstival.farmfarm.repositories;

import com.joyfarm.farmstival.farmfarm.entities.TourPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TourPlaceRepository extends JpaRepository<TourPlace, Long>, QuerydslPredicateExecutor<TourPlace> {
}
