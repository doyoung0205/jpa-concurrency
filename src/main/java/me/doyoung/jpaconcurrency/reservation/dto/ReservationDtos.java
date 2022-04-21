package me.doyoung.jpaconcurrency.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.doyoung.jpaconcurrency.reservation.domain.Reservation;

public class ReservationDtos {

    // 예약 요청 DTO
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private Long treatmentId;
        private String name;
    }

    // 예약 응답 DTO
    @Getter
    @NoArgsConstructor
    @ToString
    public static class Response {
        private Long id;

        public Response(Reservation reservation) {
            this.id = reservation.getId();
        }
    }
}
