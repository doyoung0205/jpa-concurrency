package me.doyoung.jpaconcurrency.reservation.application;

import me.doyoung.jpaconcurrency.reservation.domain.Reservation;
import me.doyoung.jpaconcurrency.reservation.dto.ReservationDtos;
import me.doyoung.jpaconcurrency.reservation.infra.ReservationRepository;
import me.doyoung.jpaconcurrency.treatment.domain.Treatment;
import me.doyoung.jpaconcurrency.treatment.infra.TreatmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationLifeCycleServiceTest {

    @Autowired
    ReservationLifeCycleService service;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TreatmentRepository treatmentRepository;

    Long treatmentId;

    @BeforeEach
    public void init() {
        treatmentRepository.deleteAllInBatch();
        reservationRepository.deleteAllInBatch();
        this.treatmentId = treatmentRepository.saveAndFlush(new Treatment("감기진료")).getId();
    }


    @DisplayName("예약이 성공하는 케이스")
    @Test
    void reserveSuccess() {
        // given - when
        final ReservationDtos.Response reserveResponse = service.saveReservation(new ReservationDtos.Request(treatmentId, "request1"));
        // then
        assertNotNull(reserveResponse.getId());
    }

    @DisplayName("예약이 실패하는 케이스")
    @Test
    void reserveFail() {
        // given
        final List<Reservation> reservations = IntStream.range(0, Treatment.DEFAULT_CAPACITY)
                .mapToObj(operand -> {
                    return Reservation.getFakeInstance(treatmentId, "fake" + operand);
                })
                .collect(Collectors.toList());
        reservationRepository.saveAll(reservations);

        // when - then
        assertThrows(Exception.class, () -> service.saveReservation(new ReservationDtos.Request(treatmentId, "face3")));
    }

}
