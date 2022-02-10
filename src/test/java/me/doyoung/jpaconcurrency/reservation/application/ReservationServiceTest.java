package me.doyoung.jpaconcurrency.reservation.application;

import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationServiceTest {

    @Autowired
    ReservationService service;

    @Autowired
    ReservationRepository reservationRepository;

    @BeforeEach
    public void init() {
        reservationRepository.deleteAllInBatch();
    }

    @AfterEach
    public void afterEach() {
        reservationRepository.deleteAllInBatch();
    }

    @DisplayName("예약이 성공하는 케이스")
    @Test
    void reserveSuccess() {
        // given - when
        final ReservationDtos.Response reserveResponse = service.reserve(new ReservationDtos.Request("request1"));
        // then
        assertNotNull(reserveResponse.getId());
    }

    @DisplayName("예약이 실패하는 케이스")
    @Test
    void reserveFail() {
        // given
        final List<Reservation> reservations = Arrays.asList(Reservation.getFakeInstance("fake1"), Reservation.getFakeInstance("fake2"));
        reservationRepository.saveAll(reservations);

        // when - then
        assertThrows(Exception.class, () -> service.reserve(new ReservationDtos.Request("request1")));
    }

}
