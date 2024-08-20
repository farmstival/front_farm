package com.joyfarm.farmstival.Reservation.repositories;

import com.joyfarm.farmstival.Reservation.entities.ReservationTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ReservationTagRepository extends JpaRepository<ReservationTag, String>, QuerydslPredicateExecutor<ReservationTag> {

}
