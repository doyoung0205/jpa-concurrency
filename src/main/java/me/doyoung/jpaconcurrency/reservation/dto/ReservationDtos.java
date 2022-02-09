package me.doyoung.jpaconcurrency.reservation.dto;

import lombok.Getter;
import me.doyoung.jpaconcurrency.reservation.domain.Reservation;

public class ReservationDtos {

    // 예약 요청 DTO
    @Getter
    public static class Request {

        private final String name;

        public Request(String name) {
            this.name = name;
        }
    }

    @Getter
    public static class Response {
        private final Long id;

        public Response(Reservation reservation) {
            this.id = reservation.getId();
        }
    }
}
