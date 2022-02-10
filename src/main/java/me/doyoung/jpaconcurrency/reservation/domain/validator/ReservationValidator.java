package me.doyoung.jpaconcurrency.reservation.domain.validator;

import me.doyoung.jpaconcurrency.reservation.domain.Reservation;

public interface ReservationValidator {
    void validate(Reservation reservation);
}
