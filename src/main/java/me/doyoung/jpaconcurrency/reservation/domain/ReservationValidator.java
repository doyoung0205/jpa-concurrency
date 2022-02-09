package me.doyoung.jpaconcurrency.reservation.domain;

import me.doyoung.jpaconcurrency.reservation.domain.Reservation;

public interface ReservationValidator {
    void validate(Reservation reservation);
}
