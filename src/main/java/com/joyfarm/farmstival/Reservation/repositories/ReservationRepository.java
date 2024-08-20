package com.joyfarm.farmstival.Reservation.repositories;

import com.joyfarm.farmstival.Reservation.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ReservationRepository  extends JpaRepository<Reservation, Long>, QuerydslPredicateExecutor<Reservation> {
}