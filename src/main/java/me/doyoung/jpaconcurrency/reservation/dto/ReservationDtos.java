package me.doyoung.jpaconcurrency.reservation.dto;

import lombok.Getter;
import me.doyoung.jpaconcurrency.reservation.domain.Reservation;

public class ReservationDtos {

    // 예약 요청 DTO
    @Getter
    public static class Request {

        private String name;

        public Request() {
        }

        public Request(String name) {
            this.name = name;
        }
    }

    // 예약 응답 DTO
    @Getter
    public static class Response {
        private Long id;

        public Response(Reservation reservation) {
            this.id = reservation.getId();
        }
    }
}
