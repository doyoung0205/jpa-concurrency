package me.doyoung.jpaconcurrency.reservation.domain;

public class ReservationCapacityException extends RuntimeException {
    public ReservationCapacityException(String message) {
        super(message);
    }
}
